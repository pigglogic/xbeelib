/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol.impl;

import net.michaelpigg.xbeelib.protocol.impl.AbstractXbeeDecoder;
import java.nio.charset.Charset;
import net.michaelpigg.xbeelib.protocol.AtCommandResponse;
import net.michaelpigg.xbeelib.protocol.AtCommandStatus;
import net.michaelpigg.xbeelib.protocol.BaseDataFrame;
import net.michaelpigg.xbeelib.protocol.Constants;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Decode Remote AT Command response messages.
 */
public class AtRemoteCommandResponseDecoder extends AbstractXbeeDecoder {

    public AtRemoteCommandResponseDecoder() {
        super(Constants.API_REMOTE_AT_COMMAND_RESPONSE);
    }


    @Override
    public MessageDecoderResult doDecode(BaseDataFrame frame, IoBuffer data, ProtocolDecoderOutput out) throws Exception {
        final int frameId = data.getUnsigned();
        final long remoteAddr64 = data.getLong();
        final int remoteAddr16 = data.getShort();
        final String cmdText = data.getString(2, Charset.forName("UTF-8").newDecoder());
        final AtCommandStatus status = data.getEnum(AtCommandStatus.class);
        final byte[] respData = new byte[data.remaining()];
        data.get(respData);
        out.write(new AtCommandResponse(frame.getLength(), frame.getApiId(), frameId, cmdText, respData, status));
        return OK;
    }

}
