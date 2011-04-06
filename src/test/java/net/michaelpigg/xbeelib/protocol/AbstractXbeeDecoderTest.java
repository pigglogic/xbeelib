/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol;

import static org.testng.Assert.assertEquals;
import net.michaelpigg.xbeelib.protocol.impl.AbstractXbeeDecoder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilter.NextFilter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.AbstractProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author michaelpigg
 */
@Test
public class AbstractXbeeDecoderTest {

    private byte[] oneioframe;
    private byte[] oneioframea;
    private byte[] oneioframeb;

    @BeforeTest
    public void beforeTest() throws IOException
    {
        oneioframe = FileUtils.readFileToByteArray(new File("xbee_oneioframe.dat"));
        oneioframea = FileUtils.readFileToByteArray(new File("xbee_oneioframeparta.dat"));
        oneioframeb = FileUtils.readFileToByteArray(new File("xbee_oneioframepartb.dat"));
    }


    public void testNoStartDelimiter() {
        TestXbeeDecoder decoder = new TestXbeeDecoder(Constants.API_IO_RECEIVE_16);
        MessageDecoderResult result = decoder.decodable(null, IoBuffer.wrap(new byte[] {0x01, 0x02, 0x03, 0x04}));
        assertEquals(result, MessageDecoderResult.NEED_DATA);
        
    }

    @Test(dataProvider="ExpectNeedsDataTests")
    public void testDecoderReturnsNeedData(byte[] input, String description) throws Exception {
        TestXbeeDecoder decoder = new TestXbeeDecoder(Constants.API_IO_RECEIVE_16);
        MessageDecoderResult result = decoder.decodable(null, IoBuffer.wrap(input));
        assertEquals(result, MessageDecoderResult.NEED_DATA, description);
        MessageDecoderResult decodeResult = decoder.decode(null, IoBuffer.wrap(input), new TestProtocolDecoderOutput());
        assertEquals(decodeResult, MessageDecoderResult.NEED_DATA, description);
    }

    @DataProvider(name="ExpectNeedsDataTests")
    public Iterator<Object[]> decoderNeedsData() {
        final List<Object[]> data = new ArrayList<Object[]>();
        data.add(new Object[]{ new byte[] {0x7E}, "Delimiter is single byte"});
        data.add(new Object[]{ new byte[] {0x7E, 0x01}, "Delimiter with one byte"});
        data.add(new Object[]{ new byte[] {0x01, 0x02, 0x7E}, "Delimiter in last byte"});
        data.add(new Object[]{ new byte[] {0x01, 0x7E, 0x0, 0x2}, "Delimiter to length bytes"});
        data.add(new Object[]{ new byte[] {0x01, 0x7E, 0x0, 0x2, (byte)Constants.API_IO_RECEIVE_16}, "Delimiter to API byte"});
        data.add(new Object[]{ new byte[] {0x01, 0x7E, 0x0, 0x2, (byte)Constants.API_IO_RECEIVE_16, 0x3}, "Delimiter to payload"});
        return data.iterator();
    }


    public void testDecoderSimpleIoFrame() throws Exception
    {
        TestXbeeDecoder decoder = new TestXbeeDecoder(Constants.API_IO_RECEIVE_16);
        MessageDecoderResult decodableResult = decoder.decodable(null, IoBuffer.wrap(oneioframe));
        Assert.assertEquals(decodableResult, MessageDecoderResult.OK);
        MessageDecoderResult decodeResult = decoder.decode(null, IoBuffer.wrap(oneioframe), new TestProtocolDecoderOutput());
        Assert.assertEquals(decodeResult, MessageDecoderResult.OK);
    }

    public void testDecoderSimpleIoFrameWrongApiReturnsNotOk() throws Exception
    {
        TestXbeeDecoder decoder = new TestXbeeDecoder(Constants.API_IO_RECEIVE_64);
        MessageDecoderResult decodableResult = decoder.decodable(null, IoBuffer.wrap(oneioframe));
        Assert.assertEquals(decodableResult, MessageDecoderResult.NOT_OK);
        MessageDecoderResult decodeResult = decoder.decode(null, IoBuffer.wrap(oneioframe), new TestProtocolDecoderOutput());
        Assert.assertEquals(decodeResult, MessageDecoderResult.NOT_OK);
    }

    public void testDecodeableFragmentedIoFrame()
    {
        TestXbeeDecoder decoder = new TestXbeeDecoder(Constants.API_IO_RECEIVE_16);
        IoBuffer buffer = IoBuffer.wrap(oneioframea);
        MessageDecoderResult decodableResult = decoder.decodable(null, buffer);
        Assert.assertEquals(decodableResult, MessageDecoderResult.NEED_DATA);
        buffer.position(buffer.limit());
        buffer.expand(oneioframeb.length);
        buffer.put(oneioframeb);
        buffer.position(0);
        decodableResult = decoder.decodable(null, buffer);
        Assert.assertEquals(decodableResult, MessageDecoderResult.OK);
    }

    public void testDecodeableIoFrameAfterPartialFrame()
    {
        TestXbeeDecoder decoder = new TestXbeeDecoder(Constants.API_IO_RECEIVE_16);
        IoBuffer buffer = IoBuffer.wrap(oneioframeb);
        MessageDecoderResult result = decoder.decodable(null, buffer);
        Assert.assertEquals(result, MessageDecoderResult.NEED_DATA);
        appendToBuffer(oneioframea, buffer);
        result = decoder.decodable(null, buffer);
        Assert.assertEquals(result, MessageDecoderResult.NEED_DATA);
        appendToBuffer(oneioframeb, buffer);
        result = decoder.decodable(null, buffer);
        Assert.assertEquals(result, MessageDecoderResult.OK);

    }

    private void appendToBuffer(byte[] newData, IoBuffer buffer) {
        buffer.position(buffer.limit());
        buffer.expand(newData.length);
        buffer.put(newData);
        buffer.position(0);

    }



    public class TestXbeeDecoder extends AbstractXbeeDecoder
    {

        public TestXbeeDecoder(int messageType) {
            super(messageType);
        }

        @Override
        public MessageDecoderResult doDecode(BaseDataFrame frame, IoBuffer data, ProtocolDecoderOutput out) throws Exception {
            return MessageDecoderResult.OK;
        }

    }

    private class TestProtocolDecoderOutput extends AbstractProtocolDecoderOutput {

        public TestProtocolDecoderOutput() {
        }

        public void flush(NextFilter nextFilter, IoSession session) {
            // ignore
        }

    }
}
