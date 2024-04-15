package com.syntifi.crypto.key.deterministic;

import com.syntifi.crypto.key.encdec.Hex;
import com.syntifi.crypto.key.mnemonic.Language;
import com.syntifi.crypto.key.mnemonic.MnemonicCode;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HierarchicalDeterministicKeyTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HierarchicalDeterministicKey.class);


    @Test
    void getMasterKeyFromSeed_seed_should_match() {
        byte[] init = "ed25519 seed".getBytes(StandardCharsets.UTF_8);
        assertEquals("a89c4655ab993cf786e4a8517899f0e5ddbdb35ebedc05da341b510bf0c5b8902eb38e1fb071b5a75f0e3b176ddea874ce81fafd6c44cd74c3c381ac5a7ed9fc",
                Hex.encode(HierarchicalDeterministicKey.getMasterKeyFromSeed(
                        "randomkey".getBytes(StandardCharsets.UTF_8), init)));
    }

    @Test
    void getKeyFromSeed_seed_should_match() throws IOException {
        MnemonicCode mnemonicCode = new MnemonicCode(Language.EN);
        String words = "shoot island position soft burden budget tooth cruel issue economy destroy above";
        byte[] seed = mnemonicCode.toSeed(Arrays.asList(words.split(" ")), "");
        assertEquals("577cd910aede2582668a741d476b45e7998e905a4286f701b87b25923501f9d4ea19513b460bcccbc069ebbe4327a59af3d6463045c4b6fa21a5e7004ccfcc3e",
                Hex.encode(seed));
        byte[] init = "ed25519 seed".getBytes(StandardCharsets.UTF_8);
        assertEquals("17ffa27bec941a557d88ef8d491171eab1dcb147b24b8c7f034766b5a2c425ab" +
                "6f1eef3d85f72c2500f3d42cad8632725e830d20ffa61d8dfda1c961c84302f0",
                Hex.encode(HierarchicalDeterministicKey.getMasterKeyFromSeed(seed, init)));
        int[] path = {44, 397, 0};
        assertEquals("88793a8eeec537c67ee8d459f1899a47a2f1b752d06a4c793c66fd751df8049838d300841c903867050c222b9f0b43893a5675f0a87756cfce4e3fd71c23334a",
                Hex.encode(HierarchicalDeterministicKey.getFromSeed(seed, init, path)));
        assertTrue(true);
    }
}
