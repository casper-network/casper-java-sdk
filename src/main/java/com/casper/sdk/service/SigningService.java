package com.casper.sdk.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.security.*;
import java.util.Base64;
import java.util.regex.Pattern;

public class SigningService {

    public  byte [] signWithKey(byte[] key, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        final PrivateKey privateKey = KeyPairGenerator.getInstance("Ed25519").generateKeyPair().getPrivate();
        final Signature signature = Signature.getInstance("Ed25519");

        signature.initSign(privateKey);
        signature.update(key);

        byte[] signed = signature.sign();

        return signed;

    }

    public  String signWithKey(final String privateKey)  {
        return generateEdDSAKey(privateKey);
    }

    public  String signWithPath(final String path)  {

        final File file;
        final String key;

        try{
            file = new File(path);
            if (!file.isFile() || !file.exists()){
                throw new FileNotFoundException(String.format("Path [%s] invalid", path));
            }

           key = Files.readString(file.toPath(), Charset.forName("ISO_8859_1"));

        }catch (Exception ex){
            throw new InvalidPathException(String.format("Path [%s] invalid", path), ex.getMessage());
        }

        final Pattern parse = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");

        return generateEdDSAKey(parse.matcher(key).replaceFirst("$1")
                .replace("\n", "").replace("\r", ""));
    }

    private  String generateEdDSAKey(final String key) {

        try{
            final PrivateKey privateKey = KeyPairGenerator.getInstance("Ed25519").generateKeyPair().getPrivate();
            final Signature signature = Signature.getInstance("Ed25519");

            signature.initSign(privateKey);
            signature.update(Base64.getDecoder().decode(key));

            return Base64.getEncoder().encodeToString(signature.sign());

        } catch (Exception e){
            throw new IllegalArgumentException("Failed to generate signature :", e.getCause());
        }

    }


}
