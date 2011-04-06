/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.commands;

import static java.lang.String.format;
import java.util.List;
import static org.apache.commons.codec.binary.Hex.encodeHexString;
import net.michaelpigg.xbeelib.XbeeHandlerCallback;
import net.michaelpigg.xbeelib.XbeeService;
import net.michaelpigg.xbeelib.protocol.AtCommandResponse;
import net.michaelpigg.xbeelib.protocol.IoSample;
import net.michaelpigg.xbeelib.protocol.ReceiveIoDataFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listen to incoming XBee traffic and dump to log.
 */
public class DefaultXbeeMonitor implements XbeeMonitor, XbeeHandlerCallback {

    private XbeeService xbeeService;

    private Logger logger = LoggerFactory.getLogger(DefaultXbeeMonitor.class);

    public DefaultXbeeMonitor(XbeeService xbeeService) {
        this.xbeeService = xbeeService;
    }

    public boolean start() {
        try {
            xbeeService.addListener(this);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean stop() {
        try {
            xbeeService.removeListener(this);        
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public void CommandResponse(AtCommandResponse response) {
        // ignoring for now
    }

    public void IoDataReceived(ReceiveIoDataFrame frame) {
        StringBuilder msg = new StringBuilder("IO Data:");
        msg.append(format(" Source = %s\n", encodeHexString(frame.getSourceAddress().getAddress())));
        if (frame.getDigitalSamples().size() > 0) {
            msg.append(format("Digital Samples = %d\n", frame.getDigitalSamples().size()));
            for (IoSample sample : frame.getDigitalSamples()) {
                msg.append(format(" %d : %d", sample.getChannelId(), sample.getData()));
            }
        }
        final List<IoSample> analogSamples = frame.getAnalogSamples();
        if (analogSamples.size() > 0) {
            msg.append(format("Analog Samples = %d\n", analogSamples.size()));
            for (IoSample sample : analogSamples) {
                msg.append(format(" %d : %d", sample.getChannelId(), sample.getData()));
            }
        }

        logger.info(msg.toString());
    }    
}
