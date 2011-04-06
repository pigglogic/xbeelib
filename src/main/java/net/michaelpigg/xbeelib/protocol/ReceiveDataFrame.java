/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol;

/**
 *
 * @author MikePigg
 */
public class ReceiveDataFrame extends BaseDataFrame {
    private XbeeAddress sourceAddress;
    private int signalStrength;
    private boolean isBroadcastAddress;
    private boolean isPanBroadcast;

    public ReceiveDataFrame(int length, int apiId, XbeeAddress sourceAddress, int signalStrength, boolean isBroadcastAddress, boolean isPanBroadcast)
    {
        super(length, apiId);
        this.sourceAddress = sourceAddress;
        this.signalStrength = signalStrength;
        this.isBroadcastAddress = isBroadcastAddress;
        this.isPanBroadcast = isPanBroadcast;
    }

    public boolean isIsBroadcastAddress()
    {
        return isBroadcastAddress;
    }

    public boolean isIsPanBroadcast()
    {
        return isPanBroadcast;
    }

    public int getSignalStrength()
    {
        return signalStrength;
    }

    public XbeeAddress getSourceAddress()
    {
        return sourceAddress;
    }
    
}
