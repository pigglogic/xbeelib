/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.commands;

/** Monitor incoming data from XBee devices */
public interface XbeeMonitor {
    boolean start();
    boolean stop();
}
