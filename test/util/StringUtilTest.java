package com.blockchain.util;

import org.junit.Test;

import main.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testGetHexString() {
        byte[] bytes = new byte[] { (byte) 0xFF, (byte) 0x00, (byte) 0xAB };
        String hexString = StringUtil.getHexString(bytes);
        assertEquals("ff00ab", hexString.toLowerCase());
    }

    @Test
    public void testJoin() {
        List<String> strings = Arrays.asList("Hello", "World", "!");
        String joined = StringUtil.join(strings, " ");
        assertEquals("Hello World !", joined);
    }

    @Test
    public void testPadLeftZeros() {
        String result = StringUtil.padLeftZeros("123", 5);
        assertEquals("00123", result);

        // Test when input is longer than length
        result = StringUtil.padLeftZeros("123456", 5);
        assertEquals("123456", result);
    }

    @Test
    public void testGetBytesUtf8() {
        String input = "Hello World";
        byte[] bytes = StringUtil.getBytesUtf8(input);
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);

        // Convert back to string to verify
        String result = new String(bytes);
        assertEquals(input, result);
    }

    @Test
    public void testRepeat() {
        String result = StringUtil.repeat("ab", 3);
        assertEquals("ababab", result);

        // Test empty string
        result = StringUtil.repeat("", 5);
        assertEquals("", result);

        // Test zero repeats
        result = StringUtil.repeat("test", 0);
        assertEquals("", result);
    }

    @Test
    public void testTruncate() {
        String input = "Hello World";
        String result = StringUtil.truncate(input, 8);
        assertEquals("Hel...", result);

        // Test when input is shorter than maxLength
        result = StringUtil.truncate(input, 15);
        assertEquals(input, result);
    }

    @Test
    public void testIsValidHexString() {
        assertTrue(StringUtil.isValidHexString("1234567890abcdefABCDEF"));
        assertFalse(StringUtil.isValidHexString("123G")); // 'G' is not valid hex
        assertFalse(StringUtil.isValidHexString("Hello"));
        assertTrue(StringUtil.isValidHexString("")); // Empty string is valid
    }

    @Test
    public void testReverseHexString() {
        String result = StringUtil.reverseHexString("1234");
        assertEquals("3412", result);

        // Test longer string
        result = StringUtil.reverseHexString("0123456789abcdef");
        assertEquals("efcdab8967452301", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReverseHexStringOddLength() {
        // Should throw exception for odd length
        StringUtil.reverseHexString("123");
    }
}