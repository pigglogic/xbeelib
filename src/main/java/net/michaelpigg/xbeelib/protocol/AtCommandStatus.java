/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol;

/**
 * AT command responses may be OK or ERROR.
 */
public enum AtCommandStatus {
    OK,
    ERROR,
    INVALID_COMMAND,
    INVALID_PARAMETER,
    NO_RESPONSE
}
