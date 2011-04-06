/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol.impl;

import java.util.ArrayList;
import java.util.List;
import net.michaelpigg.xbeelib.protocol.AtCommandResponse;
import net.michaelpigg.xbeelib.protocol.ReceiveIoDataFrame;
import net.michaelpigg.xbeelib.XbeeHandlerCallback;

/**
 * Maintains a list of callback handlers and dispatches callbacks to all of them.
 *
 */
public class XbeeHandlerCallbackDispatcher implements XbeeHandlerCallback {

    private List<XbeeHandlerCallback> callbackHandlers;

    public XbeeHandlerCallbackDispatcher() {
        this.callbackHandlers = new ArrayList<XbeeHandlerCallback>();
    }

    public void register(XbeeHandlerCallback handler)
    {
        callbackHandlers.add(handler);
    }

    public void deregister(XbeeHandlerCallback handler)
    {
        callbackHandlers.remove(handler);
    }

    public void CommandResponse(AtCommandResponse response) {
        for (XbeeHandlerCallback handler : callbackHandlers) {
            handler.CommandResponse(response);
        }

    }

    public void IoDataReceived(ReceiveIoDataFrame frame) {
        for (XbeeHandlerCallback handler : callbackHandlers) {
            handler.IoDataReceived(frame);
        }
    }

}
