/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */
package net.michaelpigg.xbeelib.protocol;

import static org.apache.commons.codec.binary.Hex.encodeHexString;
import java.math.BigInteger;
import java.util.Arrays;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.ArrayUtils;

/** Address of an XBee module.*/
public abstract class XbeeAddress {

    private static final Hex hexCodec = new Hex("UTF-8");
    public static final byte[] BROADCAST_64_BIT = new byte[]{0x0, 0x0, 0x0, 0x0, 0x0, 0x0, (byte) 0xff, (byte) 0xff};
    public static final byte[] BROADCAST_16_BIT = new byte[]{(byte) 0xff, (byte) 0xfe};
    protected final byte[] address_64;
    protected final byte[] address_16;

    public static XbeeAddress getAddress(byte[] address) {
        final byte[] addrBytes;
        if (address.length == 1) {
            addrBytes = new byte[] {0x0, address[0]};
        } else if (address.length == 2) {
            addrBytes = Arrays.copyOf(address, 2);
        } else if (address.length == 8) {
            addrBytes = Arrays.copyOf(address, 8);
        } else {
            throw new IllegalArgumentException("Xbee addresses must either be 64 bit or 16 bit addresses.");
        }

        return (addrBytes.length == 8) ? new XbeeAddress64(addrBytes) : new XbeeAddress16(addrBytes);

    }

    public static XbeeAddress getAddress(String address) {
        try {
            return getAddress(hexCodec.decode(address.getBytes()));
        } catch (DecoderException dex) {
            throw new RuntimeException(dex);
        }
    }

    @Deprecated
    public static XbeeAddress getAddress(Long address) {
        if (address == null || address < 0 || address > 65535) {
            throw new RuntimeException(String.format("Address %l is not valid", address));
        }
        return getAddress(BigInteger.valueOf(address).toByteArray());
    }

    private XbeeAddress(byte[] address16, byte[] address64) {
        this.address_16 = Arrays.copyOf(address16, 2);
        this.address_64 = Arrays.copyOf(address64, 8);
    }

    /** Returns main part of address */
    public abstract byte[] getAddress();

    /** Returns complete address byte array as used in XBee remote commands */
    public byte[] getCombinedAddress()
    {
        return ArrayUtils.addAll(address_64, address_16);
    }
    
    public static class XbeeAddress16 extends XbeeAddress {

        private XbeeAddress16(byte[] address) {
            super(address, BROADCAST_64_BIT);
        }

        @Override
        public byte[] getAddress() {
            return address_16;
        }

        @Override
        public String toString() {
            return encodeHexString(address_16);
        }
    }

    public static class XbeeAddress64 extends XbeeAddress {

        private XbeeAddress64(byte[] address) {
            super(BROADCAST_16_BIT, address);
        }

        @Override
        public byte[] getAddress() {
            return address_64;
        }

        @Override
        public String toString() {
            return encodeHexString(address_64);
        }
    }
}
