package com.casper.sdk;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Test;
import com.casper.sdk.service.SigningService;

import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Base64;

public class TestSigning {

    private static final String user1SecretKey = "MC4CAQAwBQYDK2VwBCIEILHUwAVnEJ9eYctg+GbOnxKcVrSeyX+OxlR4ardbtY9H";
    private static final byte[] data = "1234".getBytes(StandardCharsets.UTF_8);


    @Test
    public void testSigningWithKey() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        byte[] userSecretKey = Base64.getDecoder().decode(user1SecretKey);

        String signed = SigningService.signWithKey(userSecretKey, data);

        assert signed != null;

    }

    @Test
    public void testSigningWithPath()  {

        String signed = SigningService.signWithPath("/Users/carl/casper-node/utils/nctl/assets/net-1/users/user-1/secret_key.pem");

        assert signed != null;

    }

    @Test
    public void testSigningWithMalformedPath()  {
        assertThrows(InvalidPathException.class, () ->
                SigningService.signWithPath("/xyz/123/secret_key.pem")
        );
    }

  @Test
    public void testSigningWithInvalidFile()  {
        assertThrows(InvalidPathException.class, () ->
                SigningService.signWithPath("/Users/carl/casper-node/utils/nctl/assets/net-1/users/user-1/secret_key.xyz")
        );
    }




}
