/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents a command to read or set a module parameter on the XBee.
 */
public class AtCommand {

    private final XbeeAtCommands command;
    private Integer commandData;
    private XbeeAddress commandDestination;
    private final Integer frameId;
    /**
     * Construct an AT command to send to an XBee module
     * @param command The command to send - Required
     * @param commandData Value to set with the command. If null, then this is a read command.
     * @param commandDestination Remote destination to send the command to. If null, this is a local command.
     * @param frameId Id that will be used to correlate response messages. If null, no response is returned.
     */
    public AtCommand(XbeeAtCommands command, Integer commandData, Long commandDestination, Integer frameId) {
        this(command, commandData, commandDestination == null ? null : XbeeAddress.getAddress(commandDestination), frameId);
    }

    /**
     * Construct an AT command to send to an XBee module
     * @param command The command to send - Required
     * @param commandData Value to set with the command. If null, then this is a read command.
     * @param commandDestination Remote destination to send the command to. If null, this is a local command.
     * @param frameId Id that will be used to correlate response messages. If null, no response is returned.
     */
    public AtCommand(XbeeAtCommands command, Integer commandData, XbeeAddress commandDestination, Integer frameId) {
        this.command = command;
        this.commandData = commandData;
        this.commandDestination = commandDestination;
        this.frameId = frameId;
    }

    public XbeeAtCommands getCommand() {
        return command;
    }

    public Integer getCommandData() {
        return commandData;
    }

    public XbeeAddress getCommandDestination() {
        return commandDestination;
    }

    public Integer getFrameId() {
        return frameId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
