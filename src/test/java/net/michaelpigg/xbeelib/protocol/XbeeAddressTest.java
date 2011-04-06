/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol;

import org.apache.commons.codec.DecoderException;
import static org.testng.Assert.assertEquals;
import static java.util.Arrays.copyOf;
import java.util.Arrays;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class XbeeAddressTest {

    private final Logger logger = LoggerFactory.getLogger(XbeeAddressTest.class);

    final static byte[] TEST_LOW = {(byte)0x40, (byte)0x31, (byte)0x08, (byte)0x05};
    final static byte[] TEST_HIGH = {(byte)0x00, (byte)0x13, (byte)0xA2, (byte)0x00};
    final static byte[] TEST_ADDR = {(byte)0x01, (byte)0x13, (byte)0xA2, (byte)0x00, (byte)0x40, (byte)0x31, (byte)0x08, (byte)0x05};
    final static String TEST_ADDR_STR = "0113A20040310805";

    public void test64BitAddress() {
        XbeeAddress xaddr = XbeeAddress.getAddress(TEST_ADDR);
        Assert.assertEquals(xaddr.getAddress(), TEST_ADDR);
        Assert.assertEquals(xaddr.toString().toLowerCase(), TEST_ADDR_STR.toLowerCase());
    }

    public void test16BitAddress() {
        XbeeAddress xaddr = XbeeAddress.getAddress(new byte[] {0x03});
        Assert.assertEquals(xaddr.getAddress(), new byte[] {0x00, 0x03});
        Assert.assertEquals(xaddr.toString(), "0003");
    }

    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testInvalidAddress() {
        XbeeAddress.getAddress(new byte[] {0x1, 0x2, 0x3});
    }

    private final Hex hex = new Hex("UTF-8");

    public void test16BitCombinedAddress() throws DecoderException {
        logger.info("test16BitCombinedAddress");
        XbeeAddress address = XbeeAddress.getAddress(new byte[]{0x0, 0x1});
        byte[] combinedAddress = address.getCombinedAddress();

        final byte[] expected = ArrayUtils.addAll(XbeeAddress.BROADCAST_64_BIT, new byte[] {0x0, 0x1});
        logger.info("actual value {}", Hex.encodeHexString(combinedAddress));
        logger.info("expected value {}", Hex.encodeHexString(expected));
        assertEquals(combinedAddress, expected);
    }

    public void test64BitCombinedAddress() throws DecoderException {
        logger.info("test64BitCombinedAddress");
        XbeeAddress address = XbeeAddress.getAddress(TEST_ADDR);
        byte[] combinedAddress = address.getCombinedAddress();

        final byte[] expected = ArrayUtils.addAll(TEST_ADDR, XbeeAddress.BROADCAST_16_BIT);
        logger.info("actual value {}", Hex.encodeHexString(combinedAddress));
        logger.info("expected value {}", Hex.encodeHexString(expected));
        assertEquals(combinedAddress, expected);
    }

}
