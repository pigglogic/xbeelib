/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol;

import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author MikePigg
 */
public class ReceiveIoDataFrame extends ReceiveDataFrame {
    private int sampleCount;
    private short channelIndicators;
    private List<IoSample> digitalSamples;
    private List<IoSample> analogSamples;

    public ReceiveIoDataFrame(int length, int apiId, XbeeAddress sourceAddress, int signalStrength, boolean isBroadcastAddress, boolean isPanBroadcast, int sampleCount, short channelIndicators, List<IoSample> digitalSamples, List<IoSample> analogSamples)
    {
        super(length, apiId, sourceAddress, signalStrength, isBroadcastAddress, isPanBroadcast);
        this.sampleCount = sampleCount;
        this.channelIndicators = channelIndicators;
        this.digitalSamples = digitalSamples;
        this.analogSamples = analogSamples;
    }


    public List<IoSample> getAnalogSamples()
    {
        return analogSamples;
    }

    public short getChannelIndicators()
    {
        return channelIndicators;
    }

    public List<IoSample> getDigitalSamples()
    {
        return digitalSamples;
    }

    public int getSampleCount()
    {
        return sampleCount;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }


}
