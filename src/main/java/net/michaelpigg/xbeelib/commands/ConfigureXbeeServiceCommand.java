/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */


package net.michaelpigg.xbeelib.commands;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

/** List potential ports and configure port for XbeeService */
public class ConfigureXbeeServiceCommand {

    final BundleContext context;
    
    public ConfigureXbeeServiceCommand(BundleContext context) {
        this.context = context;
    }
   

    @Descriptor("List possible ports")
    public void listports() {
        listPorts(System.out, System.err);
    }

    @Descriptor("Add instance for port")
    public void xbconfig(@Descriptor("Port index") Integer port)
    {
        configurePort(port, System.out, System.err);
    }

    private void listPorts(PrintStream out, PrintStream err) {
        final List<String> ports = enumeratePorts();
        if (ports.size() < 1) {
            out.println("No matching ports found.");
        }
        for (int x = 0; x < ports.size(); x++) {
            out.printf("%d : %s\n", x, ports.get(x));
        }
    }

    private List<String> enumeratePorts() {
        final List<String> ports = new ArrayList<String>();
        final File devFile = new File("/dev");
        if (!devFile.exists() || !devFile.isDirectory())
        {
            return ports;
        }
        final File[] devFiles = devFile.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.startsWith("USB") || name.startsWith("tty.usbserial");
            }
        });
        for (File file : devFiles) {
            ports.add(file.getAbsolutePath());
        }
        return ports;

    }
    private void configurePort(Integer portIndex, PrintStream out, PrintStream err) {
        List<String> portList = enumeratePorts();
        if (portIndex < 0 || portIndex > portList.size() -1) {
            err.println("Port argument does not correspond to a known port");
            listPorts(out, err);
            return;
        }
        final String port = portList.get(portIndex);

        ServiceReference sr = context.getServiceReference(ConfigurationAdmin.class.getName());
        if (sr == null) {
            err.println("Could not find configuration admin service");
            return;
        }
        ConfigurationAdmin configAdmin = (ConfigurationAdmin) context.getService(sr);
        try {
            Configuration config = configAdmin.createFactoryConfiguration("net.michaelpigg.xbeelib.xbeeservice");
            Dictionary props = new Hashtable();
            props.put("xbee.port", port);
            config.update(props);
            out.printf("Port was configured with pid %s", config.getPid());
        } catch (IOException ex) {
            err.println("Error creating configuration for port");
            return;
        }

    }


}
