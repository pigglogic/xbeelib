/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Response from XBee to an AT command
 */
public class AtCommandResponse extends BaseDataFrame {

    private Integer frameId;
    private String command;
    private byte[] response;
    private AtCommandStatus status;

    public AtCommandResponse(int length, int apiId, Integer frameId, String command, byte[] response, AtCommandStatus status) {
        super(length, apiId);
        this.frameId = frameId;
        this.command = command;
        this.response = response;
        this.status = status;
    }

    public String getCommand() {
        return command;
    }

    public Integer getFrameId() {
        return frameId;
    }

    public byte[] getResponse() {
        return response;
    }

    public AtCommandStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
