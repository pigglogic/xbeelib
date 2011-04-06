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
import net.michaelpigg.xbeelib.protocol.AtCommandStatus;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.ArrayUtils;

/**
 *
 * Format results of ND command.
 */
public class NodeDiscoveryResponseFormatter implements Formattable {

    final AtCommandResponse response;

    public NodeDiscoveryResponseFormatter(AtCommandResponse response) {
        this.response = response;
    }

    public void formatTo(Formatter formatter, int flags, int width, int precision) {
        final StringBuilder sb = new StringBuilder("ND response: ");
        sb.append(format("Frame ID = %d;", response.getFrameId()))
          .append(format("Status = %s;", response.getStatus().toString()));

        final byte[] responseBytes = response.getResponse();
        if (response.getStatus() == AtCommandStatus.OK && responseBytes.length == 0)
        {
            sb.append("No more nodes found.");
        } else {
            sb.append(format("Address = %s;", Hex.encodeHexString(ArrayUtils.subarray(responseBytes, 2, 10))));
            sb.append(format("Signal strength = %d;", responseBytes[11]));
            final byte[] niBytes = ArrayUtils.subarray(responseBytes, 12, responseBytes.length);
            if (niBytes.length > 1) {
                sb.append(format("Node identifier = %s", niBytes));
            }
        }

        formatter.format(sb.toString());

    }


}
