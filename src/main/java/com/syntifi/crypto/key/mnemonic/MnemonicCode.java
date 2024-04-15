package com.syntifi.crypto.key.mnemonic;

/*
 * Copyright 2013 Ken Sedgwick
 * Copyright 2014 Andreas Schildbach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * CHANGES:
 * - Removing static initializer
 * - Using own Hex and Sha256 implementations
 * - Removing logs, watches, ...
 * - Removing internal dependencies to helper/utils
 * - Adding method to secure random derive the key
 * - Adding support for multiple languages
 *
 */

import com.syntifi.crypto.key.encdec.Hex;
import com.syntifi.crypto.key.hash.Sha256;
import com.syntifi.crypto.key.mnemonic.exception.MnemonicException;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A MnemonicCode object may be used to convert between binary seed values and
 * lists of words per <a href="https://github.com/bitcoin/bips/blob/master/bip-0039.mediawiki">the BIP 39
 * specification</a>
 * Original implementation at:
 * https://github.com/bitcoinj/bitcoinj/blob/master/core/src/main/java/org/bitcoinj/crypto/MnemonicCode.java
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.3.0
 */
public class MnemonicCode {
    private static final int PBKDF2_ROUNDS = 2048;
    private final List<String> wordList;
    private final Language language;

    /**
     * Creates an MnemonicCode object, initializing with words read from the supplied input stream.
     * If a wordListDigest is supplied the digest of the words will be checked.
     *
     * @param language      words languages
     * @throws IOException  if an error ocrurs when processing the file buffers
     */
    public MnemonicCode(Language language) throws IOException {
        this.language = language;
        InputStream wordStream = getClass().getResourceAsStream("/" + language.getFileName());
        if (wordStream == null)
            throw new FileNotFoundException(language.getFileName());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(wordStream, language.getCharset()))) {
            this.wordList = br.lines()
                    .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        }

        if (this.wordList.size() != 2048)
            throw new IllegalArgumentException("input stream did not contain 2048 bytes");

        // If a wordListDigest is supplied check to make sure it matches.
        if (language.getCheckSum()!= null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : this.getWordList()) {
                stringBuilder.append(s);
            }
            byte[] digest = Sha256.digest(String.valueOf(stringBuilder).getBytes(language.getCharset()));
            String hexDigest = Hex.encode(digest);
            if (!hexDigest.equals(language.getCheckSum()))
                throw new IllegalArgumentException("wordlist checksum mismatch");
        }
    }

    /**
     * Convert mnemonic word list to seed.
     *
     * @param words list of words
     * @return derived seed in byte array
     */
    public byte[] toSeed(List<String> words) {
        return toSeed(words, "");
    }

    /**
     * Convert mnemonic word list to seed.
     *
     * @param words list of words
     * @param passphrase password, use {@link #toSeed(List)} if not required
     * @return derived seed in byte array
     */
    public byte[] toSeed(List<String> words, String passphrase) {
        if (passphrase == null)
            throw new RuntimeException("A null passphrase is not allowed.");

        // To create binary seed from mnemonic, we use PBKDF2 function
        // with mnemonic sentence (in UTF-8) used as a password and
        // string "mnemonic" + passphrase (again in UTF-8) used as a
        // salt. Iteration count is set to 2048 and HMAC-SHA512 is
        // used as a pseudo-random function. Desired length of the
        // derived key is 512 bits (= 64 bytes).
        //
        String pass = String.join(" ", words);
        String salt = "mnemonic" + passphrase;

        final PKCS5S2ParametersGenerator pbkdf2 = new PKCS5S2ParametersGenerator(new SHA512Digest());
        pbkdf2.init(
                pass.getBytes(StandardCharsets.UTF_8),
                salt.getBytes(StandardCharsets.UTF_8),
                PBKDF2_ROUNDS);

        final KeyParameter key = (KeyParameter) pbkdf2.generateDerivedParameters(512);
        return key.getKey();
    }

    private boolean[] bytesToBits(byte[] data) {
        boolean[] bits = new boolean[data.length * 8];
        for (int i = 0; i < data.length; ++i)
            for (int j = 0; j < 8; ++j)
                bits[(i * 8) + j] = (data[i] & (1 << (7 - j))) != 0;
        return bits;
    }


    /**
     * Gets the word list this code uses.
     *
     * @return unmodifiable word list
     */
    public List<String> getWordList() {
        return wordList;
    }

    /**
     * Convert mnemonic word list to original entropy value.
     *
     * @param words list of words
     * @return entropy byte array
     * @throws MnemonicException.MnemonicLengthException if the number of words in the list is not multiple of 3 or empty
     * @throws MnemonicException.MnemonicWordException   if a word in the list is not part of the dictionary
     * @throws MnemonicException.MnemonicChecksumException if the checksum of the file does not match the specified one
     */
    public byte[] toEntropy(List<String> words) throws MnemonicException.MnemonicLengthException,
            MnemonicException.MnemonicWordException, MnemonicException.MnemonicChecksumException {
        if (words.size() % 3 > 0)
            throw new MnemonicException.MnemonicLengthException("Word list size must be multiple of three words.");

        if (words.size() == 0)
            throw new MnemonicException.MnemonicLengthException("Word list is empty.");

        // Look up all the words in the list and construct the
        // concatenation of the original entropy and the checksum.
        //
        int concatLenBits = words.size() * 11;
        boolean[] concatBits = new boolean[concatLenBits];
        int wordindex = 0;
        Collator collator = Collator.getInstance(language.getLocale());
        //collator.setDecomposition(Collator.FULL_DECOMPOSITION);
        //collator.setStrength(Collator.PRIMARY);
        for (String word : words) {
            // Find the words index in the wordlist.
            int ndx = Collections.binarySearch(this.wordList, word, collator);
            if (ndx < 0)
                throw new MnemonicException.MnemonicWordException(word);

            // Set the next 11 bits to the value of the index.
            for (int ii = 0; ii < 11; ++ii)
                concatBits[(wordindex * 11) + ii] = (ndx & (1 << (10 - ii))) != 0;
            ++wordindex;
        }

        int checksumLengthBits = concatLenBits / 33;
        int entropyLengthBits = concatLenBits - checksumLengthBits;

        // Extract original entropy as bytes.
        byte[] entropy = new byte[entropyLengthBits / 8];
        for (int ii = 0; ii < entropy.length; ++ii)
            for (int jj = 0; jj < 8; ++jj)
                if (concatBits[(ii * 8) + jj])
                    entropy[ii] |= 1 << (7 - jj);

        // Take the digest of the entropy.
        byte[] hash = Sha256.digest(entropy);
        boolean[] hashBits = bytesToBits(hash);

        // Check all the checksum bits.
        for (int i = 0; i < checksumLengthBits; ++i)
            if (concatBits[entropyLengthBits + i] != hashBits[i])
                throw new MnemonicException.MnemonicChecksumException();

        return entropy;
    }

    /**
     * Convert entropy data to mnemonic word list.
     *
     * @param entropy byte array
     * @return list of words
     * @throws MnemonicException.MnemonicLengthException if the number of words in the list is not multiple of 3 or empty
     */
    public List<String> toMnemonic(byte[] entropy) throws MnemonicException.MnemonicLengthException {
        if (entropy.length % 4 > 0)
            throw new MnemonicException.MnemonicLengthException("Entropy length not multiple of 32 bits.");

        if (entropy.length == 0)
            throw new MnemonicException.MnemonicLengthException("Entropy is empty.");

        // We take initial entropy of ENT bits and compute its
        // checksum by taking first ENT / 32 bits of its SHA256 hash.

        byte[] hash = Sha256.digest(entropy);
        boolean[] hashBits = bytesToBits(hash);

        boolean[] entropyBits = bytesToBits(entropy);
        int checksumLengthBits = entropyBits.length / 32;

        // We append these bits to the end of the initial entropy.
        boolean[] concatBits = new boolean[entropyBits.length + checksumLengthBits];
        System.arraycopy(entropyBits, 0, concatBits, 0, entropyBits.length);
        System.arraycopy(hashBits, 0, concatBits, entropyBits.length, checksumLengthBits);

        // Next we take these concatenated bits and split them into
        // groups of 11 bits. Each group encodes number from 0-2047
        // which is a position in a wordlist.  We convert numbers into
        // words and use joined words as mnemonic sentence.

        ArrayList<String> words = new ArrayList<>();
        int nWords = concatBits.length / 11;
        for (int i = 0; i < nWords; ++i) {
            int index = 0;
            for (int j = 0; j < 11; ++j) {
                index <<= 1;
                if (concatBits[(i * 11) + j])
                    index |= 0x1;
            }
            words.add(this.wordList.get(index));
        }

        return words;
    }

    /**
     * Check to see if a mnemonic word list is valid.
     *
     * @param words list of words
     * @throws MnemonicException if an errors occurs on reading the dictionary of on the given words
     */
    public void check(List<String> words) throws MnemonicException {
        toEntropy(words);
    }

    /**
     * Method to generate words from securerandom entropy
     *
     * @return list of mnemonic words
     * @throws IOException if an error occurs in reading the dictionary file
     * @throws MnemonicException.MnemonicLengthException if the number of words in the list is not multiple of 3 or empty
     */
    public List<String> generateSecureRandomWords() throws IOException, MnemonicException.MnemonicLengthException {
        MnemonicCode mnemonicCode = new MnemonicCode(this.language);
        SecureRandom rnd = new SecureRandom();
        byte[] entropy = new byte[16];
        rnd.nextBytes(entropy);
        return mnemonicCode.toMnemonic(entropy);
    }
}
