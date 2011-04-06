/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol;

/**
 * AT commands that can be sent to the Xbee module
 */
public enum XbeeAtCommands {
    A1("End Device Association"),
    A2("Coordinator Association"),
    AC("Apply Changes"),
    AI("Association Indication"),
    AP("API Enable"),
    AS("Active Scan"),
    BD("Interface Data Rate"),
    CA("CCA Threshold"),
    CC("Command Sequence Character"),
    CE("Coordinator Enable"),
    CH("Channel"),
    CN("Exit Command Mode"),
    CT("Command Mode Timeout"),
    D0("DIO0 Configuration"),
    D1("DIO1 Configuration"),
    D2("DIO2 Configuration"),
    D3("DIO3 Configuration"),
    D4("DIO4 Configuration"),
    D5("DIO5 Configuration"),
    D6("DIO6 Configuration"),
    D7("DIO7 Configuration"),
    D8("DIO8 Configuration"),
    DH("Destination High Address"),
    DL("Destination Low Address"),
    DN("Destination Node Adresss"),
    HV("Hardware Version"),
    IO("Digital Output Level"),
    IR("Sample Rate"),
    IS("Force Sample"),
    IT("Samples Before Transmit"),
    IU("I/O Output Enable"),
    MY("16-bit Adress"),
    ND("Node Discover"),
    NI("Node Identifier"),
    PR("Pullup Resistor"),
    SL("Serial Number Low"),
    SH("Serial Number High"),
    SM("Sleep Mode"),
    SO("Sleep Mode Options"),
    SP("Cyclic Sleep"),
    ST("Time Before Sleep"),
    VR("Firmware Version"),
    WR("Write");

    private String name;

    private XbeeAtCommands(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
