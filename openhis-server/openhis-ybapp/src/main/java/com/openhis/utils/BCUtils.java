package com.openhis.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

public class BCUtils {

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    public static Cipher getCipher(final String algorithm) {
        try {
            return Cipher.getInstance(algorithm, BouncyCastleProvider.PROVIDER_NAME);
        } catch (final NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static MessageDigest getMessageDigest(final String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm, BouncyCastleProvider.PROVIDER_NAME);
        } catch (final NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Signature getSignature(final String algorithm) {
        try {
            return Signature.getInstance(algorithm, BouncyCastleProvider.PROVIDER_NAME);
        } catch (final NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
