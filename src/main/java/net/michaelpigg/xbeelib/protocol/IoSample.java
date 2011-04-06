/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author MikePigg
 */
public class IoSample {
    private int channelId;
    private int data;

    public IoSample(int channelId, int data)
    {
        this.channelId = channelId;
        this.data = data;
    }

    public int getChannelId()
    {
        return channelId;
    }

    public int getData()
    {
        return data;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }


    
}
