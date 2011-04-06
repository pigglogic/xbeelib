/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol.impl;

import java.math.BigInteger;
import net.michaelpigg.xbeelib.protocol.AtCommand;
import net.michaelpigg.xbeelib.protocol.Constants;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;


/**
 *
 * Encode commands to send to XBee module
 */
public class AtCommandEncoder implements MessageEncoder<AtCommand> {

    public void encode(IoSession session, AtCommand message, ProtocolEncoderOutput out) throws Exception {

        IoBuffer buf = IoBuffer.allocate(4);
        final int apiValue;
        if (message.getCommandDestination() == null)
        {
            buf.put((byte)Constants.API_AT_COMMAND);
        } else {
            buf.expand(15);
            buf.put((byte)Constants.API_REMOTE_AT_COMMAND);
            
        }

        buf.put((byte)message.getFrameId().byteValue());
        if (message.getCommandDestination() != null) {
            buf.put(message.getCommandDestination().getCombinedAddress());
            
            buf.put((byte)0x2); // TODO: Supprot command option
        }

        buf.put(message.getCommand().toString().getBytes("UTF-8"));

        if (message.getCommandData() != null) {
            final byte[] cmdData = BigInteger.valueOf(message.getCommandData()).toByteArray();
            buf.expand(cmdData.length).put(cmdData);
        }
        
        buf.flip();
        final byte[] body = buf.array();
        IoBuffer buf2 = IoBuffer.allocate(buf.capacity() + 4);
        buf2.put((byte)Constants.START_DELIMITER).putShort((short)body.length)
                .put(body).put((byte)computeChecksum(body));
        buf2.flip();
        out.write(buf2);
    }

    private int computeChecksum(byte[] buffer)
    {
        int sum = 0;
        for (int x = 0; x < buffer.length; x++)
        {
            sum += buffer[x];
        }

        sum = sum & 0xFF;
        return 0xFF - sum;
    }
}
