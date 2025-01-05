package test.core;

import org.junit.Before;
import org.unit.Test;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;
import main.core.Transaction;
import static org.junit.Assert.*;

public class CryptoUtilTest {
    private KeyPair keyPair;
    private String testString;

    @Before
    public void setUp() {
        keyPair = CryptoUtil.generateKeyPair();
        testString = "Hello, Blockchain!";
    }

    @Test
    public void testSHA256() {
        String hash = CryptoUtil.sha256(testString);
        assertNotNull("Hash should not be null", hash);
        assertEquals("Hash should be 64 characters long", 64, hash.length());
        assertEquals("Same input should produce same hash",
                hash, CryptoUtil.sha256(testString));
    }

    @Test
    public void testKeyPairGeneration() {
        assertNotNull("KeyPair should not be null", keyPair);
        assertNotNull("Public key should not be null", keyPair.getPublic());
        assertNotNull("Private key should not be null", keyPair.getPrivate());
    }

    @Test
    public void testSignatureVerification() {
        byte[] signature = CryptoUtil.applyECDSASig(keyPair.getPrivate(), testString);
        assertTrue("Signature should be valid",
                CryptoUtil.verifyECDSASig(keyPair.getPublic(), testString, signature));
    }

    @Test
    public void testInvalidSignature() {
        byte[] signature = CryptoUtil.applyECDSASig(keyPair.getPrivate(), testString);
        String differentString = "Different string";
        assertFalse("Signature should be invalid for different string",
                CryptoUtil.verifyECDSASig(keyPair.getPublic(), differentString, signature));
    }

    @Test
    public void testGetStringFromKey() {
        String publicKeyString = CryptoUtil.getStringFromKey(keyPair.getPublic());
        String privateKeyString = CryptoUtil.getStringFromKey(keyPair.getPrivate());

        assertNotNull("Public key string should not be null", publicKeyString);
        assertNotNull("Private key string should not be null", privateKeyString);
        assertNotEquals("Public and private key strings should be different",
                publicKeyString, privateKeyString);
    }

    @Test
    public void testMerkleRoot() {
        List<Transaction> transactions = new ArrayList<>();
        KeyPair sender = CryptoUtil.generateKeyPair();
        KeyPair recipient = CryptoUtil.generateKeyPair();

        // Create some test transactions
        for (int i = 0; i < 4; i++) {
            Transaction transaction = new Transaction(
                    sender.getPublic(),
                    recipient.getPublic(),
                    100.0 + i);
            transaction.generateSignature(sender.getPrivate());
            transactions.add(transaction);
        }

        String merkleRoot = CryptoUtil.getMerkleRoot(transactions);
        assertNotNull("Merkle root should not be null", merkleRoot);
        assertEquals("Merkle root should be 64 characters long",
                64, merkleRoot.length());

        // Test with same transactions in different order
        List<Transaction> shuffledTransactions = new ArrayList<>(transactions);
        java.util.Collections.shuffle(shuffledTransactions);
        String shuffledMerkleRoot = CryptoUtil.getMerkleRoot(shuffledTransactions);

        assertEquals("Merkle root should be same regardless of transaction order",
                merkleRoot, shuffledMerkleRoot);
    }

    @Test
    public void testEmptyMerkleRoot() {
        String merkleRoot = CryptoUtil.getMerkleRoot(new ArrayList<>());
        assertEquals("Empty transaction list should return empty string",
                "", merkleRoot);
    }
}