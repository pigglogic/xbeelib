/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib;
import net.michaelpigg.xbeelib.protocol.AtCommand;


/**
 *
 */
public interface XbeeService {

    public void sendCommand(AtCommand atCommand);

    public void addListener(XbeeHandlerCallback listener);

    public void removeListener(XbeeHandlerCallback listener);
}
