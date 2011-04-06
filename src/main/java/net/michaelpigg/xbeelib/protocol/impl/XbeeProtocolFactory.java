/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol.impl;

import net.michaelpigg.xbeelib.protocol.AtCommand;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 *
 * @author MikePigg
 */
public class XbeeProtocolFactory extends DemuxingProtocolCodecFactory {

    public XbeeProtocolFactory()
    {
        super.addMessageDecoder(IoDataReceiveDecoder.new16BitAddressDecoder());
        super.addMessageDecoder(IoDataReceiveDecoder.new64BitAddressDecoder());
        super.addMessageDecoder(new AtCommandResponseDecoder());
        super.addMessageDecoder(new AtRemoteCommandResponseDecoder());
        super.addMessageEncoder(AtCommand.class, AtCommandEncoder.class);
    }


}
