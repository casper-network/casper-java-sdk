package com.syntifi.crypto.key.mnemonic;

import com.syntifi.crypto.key.AbstractCryptoTests;
import com.syntifi.crypto.key.encdec.Hex;
import com.syntifi.crypto.key.mnemonic.exception.MnemonicException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * examples at
 * https://github.com/bitcoinbook/bitcoinbook/blob/develop/ch05.asciidoc#creating-an-hd-wallet-from-the-seed
 *
 * comparing with the output from
 * https://iancoleman.io/bip39/
 */
public class MnemonicCodeTest extends AbstractCryptoTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(MnemonicCodeTest.class);

    @Test
    void getSeedFromWordlist_seed_should_match() throws IOException, MnemonicException.MnemonicWordException, MnemonicException.MnemonicChecksumException, MnemonicException.MnemonicLengthException {
        String words = "army van defense carry jealous true garbage claim echo media make crunch";
        List<String> wordList = Arrays.asList(words.split(" "));
        MnemonicCode mnemonicCode = new MnemonicCode(Language.EN);
        byte[] seed = mnemonicCode.toSeed(wordList, "");
        assertEquals("5b56c417303faa3fcba7e57400e120a0ca83ec5a4fc9ffba757fbe63fbd77a89a1a3be4c67196f57c39a88b76373733891bfaba16ed27a813ceed498804c0570", Hex.encode(seed));
    }

    @Test
    void getEntropyFromWordlist_entropy_should_match() throws IOException, MnemonicException.MnemonicWordException, MnemonicException.MnemonicChecksumException, MnemonicException.MnemonicLengthException {
        String words = "army van defense carry jealous true garbage claim echo media make crunch";
        List<String> wordList = Arrays.asList(words.split(" "));
        MnemonicCode mnemonicCode = new MnemonicCode(Language.EN);
        byte[] entropy = mnemonicCode.toEntropy(wordList);
        assertEquals("0c1e24e5917779d297e14d45f14e1a1a", Hex.encode(entropy));
    }

    @Test
    void getWordListFromEntropy_words_should_match() throws IOException, MnemonicException.MnemonicWordException, MnemonicException.MnemonicChecksumException, MnemonicException.MnemonicLengthException {
        MnemonicCode mnemonicCode = new MnemonicCode(Language.EN);
        List<String> w = mnemonicCode.toMnemonic(Hex.decode("0c1e24e5917779d297e14d45f14e1a1a"));
        String words = "army van defense carry jealous true garbage claim echo media make crunch";
        List<String> wordList = Arrays.asList(words.split(" "));
        assertEquals(w, wordList);
    }

    @Test
    void generateRandomList_should_generate_12_words() throws IOException, MnemonicException.MnemonicWordException, MnemonicException.MnemonicChecksumException, MnemonicException.MnemonicLengthException {
        MnemonicCode mnemonicCode = new MnemonicCode(Language.EN);
        List<String> words = mnemonicCode.generateSecureRandomWords();
        assertEquals(12, words.size());
    }

    @Test
    void getEntropyAndSeedFromWordlistPT_entropy_should_match() throws IOException, MnemonicException.MnemonicWordException, MnemonicException.MnemonicChecksumException, MnemonicException.MnemonicLengthException {
        String words = "alfinete trilogia citar berro graveto teimar evacuar broa debitar jurista irritado cerrado";
        List<String> wordList = Arrays.asList(words.split(" "));
        MnemonicCode mnemonicCode = new MnemonicCode(Language.PT);
        byte[] entropy = mnemonicCode.toEntropy(wordList);
        assertEquals("0c1e24e5917779d297e14d45f14e1a1a", Hex.encode(entropy));
        byte[] seed = mnemonicCode.toSeed(wordList, "");
        assertEquals("0d2d2982b58e3faf152b5e3276198829bea74db45fdbce7fa8f2e2e5c2ac8cd0f634bc35e33960456d09299af65763b7c094ea52c88f60cbb37251c067c6a9f4", Hex.encode(seed));
    }

    @Test
    void getEntropyAndSeedFromWordlistFR_entropy_should_match() throws IOException, MnemonicException.MnemonicWordException, MnemonicException.MnemonicChecksumException, MnemonicException.MnemonicLengthException {
        String words = "amour troupeau couteau brèche gustatif tenaille exécuter capuche dicter lagune jaune cogner";
        List<String> wordList = Arrays.asList(words.split(" "));
        MnemonicCode mnemonicCode = new MnemonicCode(Language.FR);
        byte[] entropy = mnemonicCode.toEntropy(wordList);
        assertEquals("0c1e24e5917779d297e14d45f14e1a1a", Hex.encode(entropy));
        byte[] seed = mnemonicCode.toSeed(wordList, "");
        assertEquals("895debca7a86928a0c4cb5712aefd6d4cf4c7cfd23448ccd5418932e4f00c940089a3501f4f33eaf115f1a689d6c1e54b6bb6d5f40e4791234cb2ac87d62df68", Hex.encode(seed));
    }

    @Test
    void getEntropyAndSeedFromWordlistES_entropy_should_match() throws IOException, MnemonicException.MnemonicWordException, MnemonicException.MnemonicChecksumException, MnemonicException.MnemonicLengthException {
        String words = "amistad túnica costa broma juicio toalla furgón caña domingo masivo maldad código";
        List<String> wordList = Arrays.asList(words.split(" "));
        MnemonicCode mnemonicCode = new MnemonicCode(Language.ES);
        byte[] entropy = mnemonicCode.toEntropy(wordList);
        assertEquals("0c1e24e5917779d297e14d45f14e1a1a", Hex.encode(entropy));
        byte[] seed = mnemonicCode.toSeed(wordList, "");
        assertEquals("847ebfae3823c7ebf1cb2e8313774784751f554bd6c772c4966a860920852f4e96a058f191b0140a3190ca2d47e7766cfa69aae9a2a44b457ef86df1336e6847", Hex.encode(seed));
    }

/*TODO: Chinese
    @Test
    void getEntropyAndSeedFromWordlistCNSimplified_entropy_should_match() throws IOException, MnemonicException.MnemonicWordException, MnemonicException.MnemonicChecksumException, MnemonicException.MnemonicLengthException {
        String words = "点 挡 眼 器 哥 舒 久 示 止 累 夏 便";
        List<String> wordList = Arrays.asList(words.split(" "));
        MnemonicCode mnemonicCode = new MnemonicCode(Language.CNS);
        byte[] entropy = mnemonicCode.toEntropy(wordList);
        assertEquals("0c1e24e5917779d297e14d45f14e1a1a", Hex.encode(entropy));
        byte[] seed = mnemonicCode.toSeed(wordList, "");
        assertEquals("ca43a6850c4dc61c05d487224e827cc9b5bcab49fd28a206bcf19f11c39d9dd4329e55e33940ae551b9fa295c9b73770c77d878fd63dc000b3995f4dc62f3f63", Hex.encode(seed));
    }
*/
}
