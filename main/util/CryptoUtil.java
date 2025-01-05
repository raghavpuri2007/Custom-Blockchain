package main.util;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import main.core.Transaction;

public class CryptoUtil {

    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
        try {
            Signature dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            return dsa.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static String getMerkleRoot(List<Transaction> transactions) {
        List<String> treeLayer = new ArrayList<>();
        for (Transaction transaction : transactions) {
            treeLayer.add(transaction.getTransactionId());
        }

        while (treeLayer.size() > 1) {
            List<String> newLayer = new ArrayList<>();
            for (int i = 0; i < treeLayer.size() - 1; i += 2) {
                newLayer.add(sha256(treeLayer.get(i) + treeLayer.get(i + 1)));
            }
            if (treeLayer.size() % 2 == 1) {
                newLayer.add(sha256(treeLayer.get(treeLayer.size() - 1) +
                        treeLayer.get(treeLayer.size() - 1)));
            }
            treeLayer = newLayer;
        }

        return treeLayer.size() > 0 ? treeLayer.get(0) : "";
    }

    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

            keyGen.initialize(ecSpec, random);
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}