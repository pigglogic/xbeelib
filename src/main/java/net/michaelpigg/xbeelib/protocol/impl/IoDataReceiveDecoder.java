/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol.impl;

import java.util.ArrayList;
import java.util.List;
import net.michaelpigg.xbeelib.protocol.BaseDataFrame;
import net.michaelpigg.xbeelib.protocol.Constants;
import net.michaelpigg.xbeelib.protocol.IoSample;
import net.michaelpigg.xbeelib.protocol.ReceiveDataFrame;
import net.michaelpigg.xbeelib.protocol.ReceiveIoDataFrame;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 *
 * @author MikePigg
 */
public class IoDataReceiveDecoder extends XbeeReceiveDataDecoder {

    public static IoDataReceiveDecoder new16BitAddressDecoder() {
        return new IoDataReceiveDecoder(Constants.API_IO_RECEIVE_16, 16);
    }

    public static IoDataReceiveDecoder new64BitAddressDecoder() {
        return new IoDataReceiveDecoder(Constants.API_IO_RECEIVE_64, 64);
    }

    public IoDataReceiveDecoder(final int messageType, final int addressSize)
    {
        super(messageType, addressSize);
    }

    public MessageDecoderResult doDecode(BaseDataFrame baseFrame, IoBuffer payload, ProtocolDecoderOutput out) throws Exception
    {
        final ReceiveDataFrame frame = decodeDataFrame(baseFrame, payload);
        // get IO header data
        final int sampleCount = payload.getUnsigned();
        final int channelIndicator = payload.getUnsignedShort();
        final List<IoSample> digitalSamples = new ArrayList<IoSample>();
        final List<IoSample> analogSamples = new ArrayList<IoSample>();
        if (payload.remaining() < sampleCount * 2)
        {
            
            return NOT_OK;
        }
        for (int x = 0; x < sampleCount; x++)
        {
            // if any digital channel is enabled, then read DIO data bytes
            if ((channelIndicator & 0x1F) != 0x0) {
                IoSample dsample = new IoSample(0, payload.getUnsignedShort());
                digitalSamples.add(dsample);
            }

            int adcMask = 0x200;
            // read bytes for each ADC that is enabled
            for (int y = 0; y < 5; y++) {
                if ((adcMask & channelIndicator) == adcMask) {
                    IoSample asample = new IoSample(y, payload.getUnsignedShort());
                    analogSamples.add(asample);
                }
                adcMask = adcMask<<1;
            }
        }
        final ReceiveIoDataFrame dataFrame = new ReceiveIoDataFrame(frame.getLength(), frame.getApiId(), frame.getSourceAddress(), frame.getSignalStrength(), frame.isIsBroadcastAddress(), frame.isIsPanBroadcast(),
                sampleCount, (short)channelIndicator, digitalSamples, analogSamples);
        out.write(dataFrame);
        return OK;
    }
}
