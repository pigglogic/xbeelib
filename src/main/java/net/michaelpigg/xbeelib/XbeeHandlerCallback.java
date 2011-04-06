/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */


package net.michaelpigg.xbeelib;

import net.michaelpigg.xbeelib.protocol.AtCommandResponse;
import net.michaelpigg.xbeelib.protocol.ReceiveIoDataFrame;

    public interface XbeeHandlerCallback {
        public void CommandResponse(AtCommandResponse response);

        public void IoDataReceived(ReceiveIoDataFrame frame);

    }