/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol.impl;

import net.michaelpigg.xbeelib.protocol.BaseDataFrame;
import net.michaelpigg.xbeelib.protocol.ReceiveDataFrame;
import net.michaelpigg.xbeelib.protocol.XbeeAddress;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Decoder for receive data frames.
 */
public abstract class XbeeReceiveDataDecoder extends AbstractXbeeDecoder {

    private static final int BYTES_16_BIT_ADDRESS = 2;
    private static final int BYTES_64_BIT_ADDRESS = 8;

    private final int addressSize;

    public XbeeReceiveDataDecoder(int messageType, int addressSize) {
        super(messageType);
        switch (addressSize)
        {
            case 16:
                this.addressSize = BYTES_16_BIT_ADDRESS;
                break;
            case 64:
                this.addressSize = BYTES_64_BIT_ADDRESS;
                break;
            default:
                throw new IllegalArgumentException("Address size must be 16 or 64 bits");
        }
    }

    protected ReceiveDataFrame decodeDataFrame(BaseDataFrame frame, IoBuffer data) {
        final byte[] addressBytes = new byte[addressSize];
        // Get source address
        data.get(addressBytes);
        final XbeeAddress srcAddr = XbeeAddress.getAddress(addressBytes);
        // Get RSSI
        final int rssi = data.getUnsigned();
        // Get options
        final int opts = data.getUnsigned();

        final boolean isBroadcast = (opts & 0x2) == 0x2;
        final boolean isPan = (opts & 0x4) == 0x4;
        return new ReceiveDataFrame(frame.getLength(),
                frame.getApiId(), srcAddr, rssi, isBroadcast, isPan);

    }

    @Override
    public MessageDecoderResult doDecode(BaseDataFrame frame, IoBuffer data, ProtocolDecoderOutput out) throws Exception {
        out.write(decodeDataFrame(frame, data));
        return OK;
    }



}
