/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.commands;

import java.io.PrintStream;
import net.michaelpigg.xbeelib.XbeeService;
import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/** Monitor incoming XBee packets and log */
public class XbeeMonitorCommand {

    private final ServiceTracker xbeeServiceTracker;
    private XbeeMonitor xbeeMonitor;

    public XbeeMonitorCommand(BundleContext bundleContext) {
        this.xbeeServiceTracker = new ServiceTracker(bundleContext, XbeeService.class.getName(), null);
        xbeeServiceTracker.open();
    }

    @Descriptor("Start or stop XBee monitor")
    public void xmon(@Descriptor("Command [start|stop]") String command) {
        if (command.equalsIgnoreCase("stop"))
        {
            stopMonitor(System.out, System.err);
        } else if (command.equalsIgnoreCase("start")) {
            startMonitor(System.out, System.err);
        }
    }

    private void startMonitor(PrintStream out, PrintStream err) {
        if (xbeeMonitor != null)
        {
            err.println("Monitor is already running.");
            return;
        }
        XbeeService xbeeService = (XbeeService)xbeeServiceTracker.getService();
        if (xbeeService == null) {
            out.println("No XBee service is available to monitor.");
            return;
        }
        xbeeMonitor = new DefaultXbeeMonitor(xbeeService);
        final boolean started = xbeeMonitor.start();
        if (!started) {
            err.println("Unable to start XBee monitor");
            xbeeMonitor = null;
            return;
        }
        out.println("XBee service monitor started.");
    }

    private void stopMonitor(PrintStream out, PrintStream err) {
        if (xbeeMonitor == null) {
            err.println("No XBee service monitor is running");
            return;
        }
        final boolean stopped = xbeeMonitor.stop();
        if (stopped) {
            out.println("XBee monitor stopped.");
        } else {
            err.println("XBee monitor did not stop.");
        }
        xbeeMonitor = null;
    }

}
