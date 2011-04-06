/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib;

import net.michaelpigg.xbeelib.protocol.AtCommandResponse;
import net.michaelpigg.xbeelib.protocol.ReceiveIoDataFrame;

/**
 * Adapter class that does nothing by default with callbacks. Classes may implement
 * only the callbacks they are interested in.
 */
public class XbeeHandlerCallbackAdapter implements XbeeHandlerCallback {

    public void CommandResponse(AtCommandResponse response) {
        // do nothing
    }

    public void IoDataReceived(ReceiveIoDataFrame frame) {
        // do nothing
    }

}
