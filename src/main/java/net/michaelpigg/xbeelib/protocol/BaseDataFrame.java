/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol;

/**
 * All XBee frames begin with the length and api fields.
 */
public class BaseDataFrame {
    private int length;
    private int apiId;

    public BaseDataFrame(int length, int apiId)
    {
        this.length = length;
        this.apiId = apiId;
    }

    public int getApiId()
    {
        return apiId;
    }

    public int getLength()
    {
        return length;
    }
    
    
}
