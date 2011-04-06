/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.commands;

import java.math.BigInteger;
import java.util.Formattable;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import net.michaelpigg.xbeelib.XbeeService;
import net.michaelpigg.xbeelib.protocol.AtCommand;
import net.michaelpigg.xbeelib.protocol.AtCommandResponse;
import net.michaelpigg.xbeelib.protocol.XbeeAtCommands;
import net.michaelpigg.xbeelib.XbeeHandlerCallbackAdapter;
import net.michaelpigg.xbeelib.protocol.XbeeAddress;
import net.michaelpigg.xbeelib.protocol.impl.DefaultAtCommandResponseFormatter;
import net.michaelpigg.xbeelib.protocol.impl.NodeDiscoveryResponseFormatter;
import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Provides a command to be used in Felix Shell that sends an Xbee command. */
public class ToXbeeCommand {

    private final ServiceTracker xbeeServiceTracker;
    private final Random random = new Random();

    private final Logger logger = LoggerFactory.getLogger(ToXbeeCommand.class);

    public ToXbeeCommand(BundleContext bundleContext) {
        this.xbeeServiceTracker = new ServiceTracker(bundleContext, XbeeService.class.getName(), null);
        xbeeServiceTracker.open();
    }

    @Override
    protected void finalize() throws Throwable {
        xbeeServiceTracker.close();
        super.finalize();
    }


    /** Generate a frame ID between 1 and 256. A value of zero
     would cause no response message to be generated */
    private int generateFrameId() {
        int frameId = 0;
        while (frameId == 0) {
            frameId = random.nextInt(256);
        }
        return frameId;
    }

    private XbeeAtCommands parseAtCommand(String cmdString)
    {
        try {
            return XbeeAtCommands.valueOf(cmdString.toUpperCase());
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }

    @Descriptor("Send command to local XBee module with data")
    public void xb(
            @Descriptor("XBee AT command") String command, @Descriptor("Data to send with command") String data)
    {
        execute(null, command, data);
    }

    @Descriptor("Send command to local XBee module without data")
    public void xb(@Descriptor("XBee AT command") String command)
    {
        execute(null, command, null);
    }

        @Descriptor("Send command to a remote XBee module with data")
    public void xbr(
            @Descriptor("Remote XBee address") String address,
            @Descriptor("XBee AT command") String command, @Descriptor("Data to send with command") String data)
    {
        execute(address, command, data);
    }

    @Descriptor("Send command to a remote XBee module without data")
    public void xbr(
            @Descriptor("Remote XBee address") String address,
            @Descriptor("XBee AT command") String command)
    {
        execute(address, command, null);
    }

    private void execute(String destAddressString, String atCommandString, String dataString) {
        final XbeeAtCommands atCommand = XbeeAtCommands.valueOf(atCommandString.toUpperCase());
        if (atCommand == null)
        {
            System.err.print("Unknown XBee command.");
            return;
        }

        XbeeAddress destAddress = null;
        if (destAddressString != null && destAddressString.length() > 1)
        {
            try {
                destAddress = XbeeAddress.getAddress(destAddressString);
            } catch (RuntimeException re) {
                System.err.printf("Unable to parse address %s", destAddressString);
                return;
            }
        }

        BigInteger data = null;
        if (dataString != null) {
            if (dataString.startsWith("0x") )
            {
                try {
                    data = new BigInteger(dataString.substring(2), 16);
                } catch (RuntimeException re) {
                    logger.error("Could not parse data {} as hex string", dataString);
                }
            } else {
                try {
                    data = new BigInteger(dataString, 10);
                } catch (RuntimeException re) {
                    logger.error("Could not parse data {} as decimal number", dataString);
                }
            }
            if (data == null) {
                System.err.println(String.format("Data %s can not be parsed.", dataString));
                return;
            }
        }
        final Integer commandData = (data == null) ? null : data.intValue();
        final Integer frameId = generateFrameId();
        final AtCommand command = new AtCommand(atCommand, commandData, destAddress, frameId);
        final XbeeService xbeeService = (XbeeService)xbeeServiceTracker.getService();
        if (xbeeService == null)
        {
            System.err.println("Cannot execute command because the xbee service can not be found.");
            return;
        }

        final XbeeResponseWaiter waiter = new XbeeResponseWaiter(command, xbeeService);
        logger.debug("About to send xbee command: {}", command);
        Future<AtCommandResponse> future = Executors.newSingleThreadExecutor().submit(waiter);
        try {
            final AtCommandResponse response = future.get(10, TimeUnit.SECONDS);
            final Formattable responseFormatter;
            if (XbeeAtCommands.ND.toString().equalsIgnoreCase(response.getCommand()) ) {
                responseFormatter = new NodeDiscoveryResponseFormatter(response);
            } else {
                responseFormatter = new DefaultAtCommandResponseFormatter(response);
            }

            System.out.println(String.format("%s", responseFormatter));

        } catch (InterruptedException ex) {
            logger.error("Interrupted waiting for xbee command response", ex);
        } catch (ExecutionException ex) {
            System.err.println(String.format("Exception executing command: %s", ex.toString()));
        } catch (TimeoutException tex) {
            System.err.println("No response recieved before timeout.");
        } finally {
            future.cancel(true);
        }
    }
    private class XbeeResponseWaiter extends XbeeHandlerCallbackAdapter implements Callable<AtCommandResponse>
    {
        private final AtCommand command;
        private final XbeeService xbeeService;
        private AtCommandResponse response;
        private final SynchronousQueue<AtCommandResponse> responseQueue = new SynchronousQueue<AtCommandResponse>();

        public XbeeResponseWaiter(AtCommand command, XbeeService xbeeService) {
            this.command = command;
            this.xbeeService = xbeeService;
        }

        public AtCommandResponse getResponse() {
            return response;
        }

        @Override
        public void CommandResponse(AtCommandResponse response) {
            logger.debug("Command response frame id {}; command {}; status {}", new Object[] {response.getFrameId(), response.getCommand(), response.getStatus().toString()});
            if ((int)response.getFrameId() == (int)command.getFrameId())
            {
                this.response = response;
                try {
                    responseQueue.put(response);
                } catch (InterruptedException ex) {
                    logger.debug("Interrupted putting response to queue", ex);
                }
            }
        }

        public AtCommandResponse call() throws Exception {
            try {
                xbeeService.addListener(this);
                xbeeService.sendCommand(command);
                return responseQueue.take();
            } finally {
                xbeeService.removeListener(this);
            }
        }
    }
}
