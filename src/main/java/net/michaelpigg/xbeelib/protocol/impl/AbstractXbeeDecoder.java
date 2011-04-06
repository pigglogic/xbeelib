/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol.impl;

import net.michaelpigg.xbeelib.protocol.BaseDataFrame;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author MikePigg
 */
public abstract class AbstractXbeeDecoder implements MessageDecoder {
    private static final int CRC_LENGTH = 1;

    private int messageType;
    private Logger logger = LoggerFactory.getLogger(AbstractXbeeDecoder.class);
    
    public AbstractXbeeDecoder(int messageType)
    {
        this.messageType = messageType;
    }


    public MessageDecoderResult decodable(IoSession session, IoBuffer in)
    {
        while (in.hasRemaining())
        {
            if (in.getUnsigned() == 0x7E)
            {
                logger.trace("First byte is xbee delimiter");
                if (in.remaining() >= 2)
                {
                    logger.trace("There are {} bytes remaining", in.remaining());
                    // get length bytes
                    final int length = in.getUnsignedShort();
                    logger.trace("Length is read as {}", length);
                    if (in.remaining() >= length + CRC_LENGTH) {
                        logger.trace("Appears to be Xbee packet");
                        final short incomingMsgType = in.getUnsigned();
                        if (incomingMsgType == this.messageType) {
                            return MessageDecoderResult.OK;
                        } else {
                            logger.debug("Decoder asked if API {} is supported but only supports {}.", incomingMsgType, this.messageType);
                            return NOT_OK;
                        }
                    }
                }
                logger.debug("May be valid message, but need more data.");
                return MessageDecoderResult.NEED_DATA;
            }
        }

        logger.debug("Did not find Xbee start delimiter, but we may be starting in the middle of a packet");
        return MessageDecoderResult.NEED_DATA;
    }


    public MessageDecoderResult decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception
    {

        // look for start delimiter
        while (true)
        {
            if (in.getUnsigned() == 0x7E)
            {
                break;
            }
        }

        // need at least length bytes in order to proceed
        if (in.remaining() < 2)
        {
            return NEED_DATA;
        }

        // get data length
        final int length = in.getUnsignedShort();
        if (in.remaining() < length + CRC_LENGTH)
        {
            return NEED_DATA;
        }
        // get message type
        final int type = in.getUnsigned();
        if (type != messageType)
        {
            logger.debug("Unable to process data with api {}.", type);
            return MessageDecoderResult.NOT_OK;
        }
        final IoBuffer dataBytes = in.getSlice(length - 1);
        // Get checksum
        final int checksum = in.getUnsigned();
        // TODO: Is checksum valid?

        BaseDataFrame frame = new BaseDataFrame(length, type);
        return doDecode(frame, dataBytes, out);
    }

    public abstract MessageDecoderResult doDecode(final BaseDataFrame frame, final IoBuffer data, final ProtocolDecoderOutput out) throws Exception;

    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception
    {
    }

}
