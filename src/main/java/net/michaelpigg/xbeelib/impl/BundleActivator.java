/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import net.michaelpigg.xbeelib.commands.ConfigureXbeeServiceCommand;
import net.michaelpigg.xbeelib.commands.ToXbeeCommand;
import net.michaelpigg.xbeelib.commands.XbeeMonitorCommand;
import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Converter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Create and register instances of XbeeService. */
public class BundleActivator implements org.osgi.framework.BundleActivator{

    private ServiceRegistration serviceFactoryRegistration;
    private XbeeServiceFactory serviceFactory;
    private ServiceTracker configAdminTracker;

    private Logger logger = LoggerFactory.getLogger(BundleActivator.class);

    public void start(BundleContext context) throws Exception {
        final Map<String, Object> props = new HashMap<String, Object>();
        props.put(Constants.SERVICE_PID, "net.michaelpigg.xbeelib.xbeeservice");
        serviceFactory = new XbeeServiceFactory(context);
        serviceFactoryRegistration = context.registerService(ManagedServiceFactory.class.getName(), serviceFactory, new Hashtable(props));
        logger.debug("Registering ToXbeeCommand");
        context.registerService(ToXbeeCommand.class.getName(), new ToXbeeCommand(context), commandRegistrationProperties("xbee", "xb", "xbr"));
        context.registerService(XbeeMonitorCommand.class.getName(), new XbeeMonitorCommand(context), commandRegistrationProperties("xbee", "xmon"));
        context.registerService(ConfigureXbeeServiceCommand.class.getName(), new ConfigureXbeeServiceCommand(context), commandRegistrationProperties("xbee", "xbconfig", "listports"));
        configAdminTracker = new ServiceTracker(context, ConfigurationAdmin.class.getName(), new ConfigAdminTrackerCustomizer(context));
        configAdminTracker.open();
    }

    private Hashtable commandRegistrationProperties(final String scope, final String... functions) {
        final Hashtable cmdProps = new Hashtable();
        cmdProps.put(CommandProcessor.COMMAND_SCOPE, scope);
        cmdProps.put(CommandProcessor.COMMAND_FUNCTION, functions);
        return cmdProps;
    }

    public void stop(BundleContext context) throws Exception {
        serviceFactory.stop();
        if (serviceFactoryRegistration != null) {
            serviceFactoryRegistration.unregister();
        }
    }

    private class ConfigAdminTrackerCustomizer implements ServiceTrackerCustomizer {
        final BundleContext context;

        public ConfigAdminTrackerCustomizer(BundleContext context) {
            this.context = context;
        }


        public Object addingService(ServiceReference reference) {
            try {
                final ConfigurationAdmin ca = (ConfigurationAdmin) context.getService(reference);
                Configuration config = ca.getConfiguration("org.ops4j.pax.logging", null);
                final Map<String, String> logProps = new HashMap<String, String>();
                logProps.put("log4j.rootLogger", "INFO");
                config.update(new Hashtable(logProps));
                return ca;
            } catch (IOException ex) {
                logger.error("Unable to configure logging", ex);
                return null;
            }
        }

        public void modifiedService(ServiceReference reference, Object service) {
            
        }

        public void removedService(ServiceReference reference, Object service) {
            context.ungetService(reference);
        }

    }

}
