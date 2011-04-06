/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol;

import net.michaelpigg.xbeelib.protocol.impl.AtCommandEncoder;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolCodecSession;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for {@link AbstractXbeeEncoder}.
 */
@Test
public class AtCommandEncoderTest {

    private AtCommandEncoder encoder;
    private ProtocolCodecSession ioSession;

    @BeforeMethod
    public void beforeMethod()
    {
        encoder = new AtCommandEncoder();
        ioSession = new ProtocolCodecSession();
    }

    public void encodeLocalRead() throws Exception
    {
        final IoBuffer expected = IoBuffer.allocate(8);
        expected.put(
                new byte[] {0x7E, 0x00, 0x04, 0x08, 0x52, 0x44, 0x4C, 0x15});
        expected.flip();
        AtCommand dlreadcmd = new AtCommand(XbeeAtCommands.DL, null, (Long)null, 0x52);
        encoder.encode(ioSession, dlreadcmd, ioSession.getEncoderOutput());
        assertThat((IoBuffer)ioSession.getEncoderOutputQueue().poll(), equalTo(expected));

    }

    public void encodeRemoteRead() throws Exception {
        final IoBuffer expected = IoBuffer.allocate(19);
        expected.put(new byte[] {0x7E, 0x00, 0x0F,
        0x17, 0x52,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xff, (byte)0xff,
        0x00, 0x01, 
        0x02,
        0x44, 0x4C, 0x05 });
        expected.flip();
        AtCommand dlremoteread = new AtCommand(XbeeAtCommands.DL, null, 0x1L, 0x52);
        encoder.encode(ioSession, dlremoteread, ioSession.getEncoderOutput());
        System.out.println("encode data " + ((IoBuffer)ioSession.getEncoderOutputQueue().peek()).getHexDump());
        assertThat((IoBuffer)ioSession.getEncoderOutputQueue().poll(), equalTo(expected));
    }
}
