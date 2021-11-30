package com.syntifi.casper.sdk.crypto;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ASN1Identifiers{

    public static final ASN1ObjectIdentifier Secp256k1OIDCurve = new ASN1ObjectIdentifier("1.3.132.0.10");
    public static final ASN1ObjectIdentifier Secp256k1OIDkey = new ASN1ObjectIdentifier("1.2.840.10045.2.1");
    public static final ASN1ObjectIdentifier Ed25519OID = new ASN1ObjectIdentifier("1.3.101.112");
    public static final String PUBLIC_KEY_DER_HEADER = "PUBLIC KEY";
    public static final String EC_PRIVATE_KEY_DER_HEADER = "EC PRIVATE KEY";
}