/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */


package net.michaelpigg.xbeelib.protocol.impl;

import static java.lang.String.format;
import java.util.Formattable;
import java.util.Formatter;
import net.michaelpigg.xbeelib.protocol.AtCommandResponse;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * Format results of a generic AT command.
 */
public class DefaultAtCommandResponseFormatter implements Formattable {

    final AtCommandResponse response;

    public DefaultAtCommandResponseFormatter(AtCommandResponse response) {
        this.response = response;
    }

    public void formatTo(Formatter formatter, int flags, int width, int precision) {
        final StringBuilder sb = new StringBuilder(format("%s response: ", response.getCommand()));
        sb.append(format("Frame ID = %d;", response.getFrameId()))
          .append(format("Status = %s;", response.getStatus().toString()));

        final byte[] responseBytes = response.getResponse();
        sb.append(format("Data: %s\n", Hex.encodeHexString(responseBytes)));
        sb.append("\n");
        formatter.format(sb.toString());

    }


}
