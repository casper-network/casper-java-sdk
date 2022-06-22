package com.casper.sdk.service.signing;

import com.casper.sdk.exceptions.SignatureException;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.Algorithm;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for issues/108.
 *
 * @see <a href="https://github.com/casper-network/casper-java-sdk/issues/108">issues/108</a>
 */
class Secp256k1PrivateKeyTest {

    public static final X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1");
    public static final byte[] MESSAGE = "The quick brown fox jumped over the lazy dog".getBytes(StandardCharsets.UTF_8);
    public static final byte[] EXPECTED_SIGNED = ByteUtils.decodeHex("898aca19a91a770078ca62dd14627305718a49c8fd2f59782cfe067a60ae3d7b2fa92d6ac504ce68c6d8b492325aaa32907c5eeab33a21262717d7ed1c5b932e");
    private final SigningService signingService = new SigningService();

    static final ECDomainParameters CURVE =
            new ECDomainParameters(
                    CURVE_PARAMS.getCurve(),
                    CURVE_PARAMS.getG(),
                    CURVE_PARAMS.getN(),
                    CURVE_PARAMS.getH());

    private static final String SECP256K1_SECRET_KEY = "/SECP256K1/key1_secret_key.pem";
    private static final String SECP256K1_PUBLIC_KEY = "/SECP256K1/key1_public_key.pem";



   /* @Test
    void name2() throws java.security.SignatureException {

        ECKeyGenerationParameters keyParams = new ECKeyGenerationParameters(CURVE, new SecureRandom());

        ECKeyPairGenerator generator = new ECKeyPairGenerator();
        generator.init(keyParams);
        AsymmetricCipherKeyPair pair = generator.generateKeyPair();

        assertThat(pair, is(notNullValue()));

        final ECKeyPair ecKeyPair = ECKeyPair.create(((ECPrivateKeyParameters) pair.getPrivate()).getD());

        Sign.SignatureData signature = Sign.signMessage(Hash.sha256(MESSAGE), ecKeyPair, false);

        //return Hex.toHexString(signature.getR()) + Hex.toHexString(signature.getS());
        ByteBuffer bb = ByteBuffer.allocate(signature.getR().length + signature.getS().length);
        bb.put(signature.getR());
        bb.put(signature.getS());
        byte[] signed = bb.array();


        try {
            final byte[] x = ((ECPublicKeyParameters) pair.getPublic()).getQ().getXCoord().getEncoded();
            final byte[] y = ((ECPublicKeyParameters) pair.getPublic()).getQ().getYCoord().getEncoded();
            byte[] concat = ByteUtils.concat(x, y);
            byte[] bytes = ecKeyPair.getPublicKey().toByteArray();
            final byte[] shortPublicKey = getShortKey(bytes);

            final Sign.SignatureData signatureData = new Sign.SignatureData(
                    (byte) 27,
                    Arrays.copyOfRange(signed, 0, 32),
                    Arrays.copyOfRange(signed, 32, 64)
            );
            final BigInteger derivedKey = Sign.signedMessageHashToKey(Hash.sha256(MESSAGE), signatureData);
            byte[] shortDerivedKey = getShortKey(derivedKey.toByteArray());
            boolean verified = Arrays.equals(shortDerivedKey, shortPublicKey);
            assertThat(verified, is(true));
        } catch (Exception e) {
            throw new SignatureException(e);
        }

    } */

    /**
     * Gets a short key
     *
     * @param key the key as a byte array
     * @return short key as byte array
     */
    public static byte[] getShortKey(byte[] key) {
        BigInteger pubKey = new BigInteger(key);
        String pubKeyPrefix = pubKey.testBit(0) ? "03" : "02";
        byte[] pubKeyBytes = Arrays.copyOf(key, 32);
        return Hex.decode(pubKeyPrefix + Hex.toHexString(pubKeyBytes));
    }


    /*
    @Test
    void name() {

        ECKeyGenerationParameters generationParameters = new ECKeyGenerationParameters(CURVE, new SecureRandom());

        ECKeyPairGenerator gen = new ECKeyPairGenerator();
        SecureRandom secureRandom = new SecureRandom();
        ECKeyGenerationParameters keyGenParam = new ECKeyGenerationParameters(CURVE, secureRandom);
        gen.init(keyGenParam);
        AsymmetricCipherKeyPair kp = gen.generateKeyPair();

        final ECKeyPair ecKeyPair = ECKeyPair.create(((ECPrivateKeyParameters) kp.getPrivate()).getD());

        Sign.SignatureData signature = Sign.signMessage(Hash.sha256(MESSAGE), ecKeyPair, false);

        ByteBuffer bb = ByteBuffer.allocate(signature.getR().length + signature.getS().length);
        bb.put(signature.getR());
        bb.put(signature.getS());
        byte[] signed = bb.array();
        assertThat(signed, is(EXPECTED_SIGNED));


        assertThat(((ECPrivateKeyParameters) kp.getPrivate()).getD(), is(notNullValue()));



    } */

    @Test
    void testJavaSecToW3j() {


        final PrivateKey privateKey = signingService.loadKey(Secp256k1PrivateKeyTest.class.getResourceAsStream(SECP256K1_SECRET_KEY));
        final PublicKey publicKey = signingService.loadKey(Secp256k1PrivateKeyTest.class.getResourceAsStream(SECP256K1_PUBLIC_KEY));
        final KeyPair keyPair = new KeyPair(publicKey, privateKey);

        final ECKeyPair ecKeyPair = ECKeyPair.create(keyPair);
        Sign.SignatureData signature = Sign.signMessage(Hash.sha256(MESSAGE), ecKeyPair, false);

        ByteBuffer bb = ByteBuffer.allocate(signature.getR().length + signature.getS().length);
        bb.put(signature.getR());
        bb.put(signature.getS());
        byte[] signed = bb.array();
        assertThat(signed, is(EXPECTED_SIGNED));

    }

    @Test
    void generatePrivateKey() throws Exception {

        try {
            KeyPair keyPair = signingService.generateKeyPair(Algorithm.SECP256K1);

            fail("secp256k1 KeyPair generation is not yet supported.");

            ECKeyPair ecKeyPair = ECKeyPair.create(keyPair);
            BigInteger publicKey = ecKeyPair.getPublicKey();

            Sign.SignatureData signature = Sign.signMessage(Hash.sha256(MESSAGE), ecKeyPair, false);

            ByteBuffer bb = ByteBuffer.allocate(signature.getR().length + signature.getS().length);
            bb.put(signature.getR());
            bb.put(signature.getS());
            byte[] signed = bb.array();

            assertThat(signed, is(notNullValue()));

            Sign.SignatureData signatureData = new Sign.SignatureData(
                    (byte) 27,
                    Arrays.copyOfRange(signed, 0, 32),
                    Arrays.copyOfRange(signed, 32, 64));
            BigInteger derivedKey = Sign.signedMessageHashToKey(Hash.sha256(MESSAGE), signatureData);

            BigInteger publicKeyFromPrivate = Sign.publicKeyFromPrivate(ecKeyPair.getPrivateKey());

            byte[] key = getKey(ecKeyPair);
            boolean equals = Arrays.equals(getShortKey(derivedKey.toByteArray()), key);
            assertThat(equals, is(true));

        } catch (SignatureException e) {
            assertThat(e.getMessage(), containsString("secp256k1 KeyPair generation is not yet supported"));
        }


    }


    private byte[] getKey(final ECKeyPair keyPair) {
        final BigInteger pubKey = keyPair.getPublicKey();
        String pubKeyPrefix = pubKey.testBit(0) ? "03" : "02";
        byte[] pubKeyBytes = Arrays.copyOf(pubKey.toByteArray(), 32);
        return Hex.decode(pubKeyPrefix + Hex.toHexString(pubKeyBytes));
    }
}