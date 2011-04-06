/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */


package net.michaelpigg.xbeelib.impl;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import net.michaelpigg.xbeelib.XbeeService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Manages instances of XbeeService */
public class XbeeServiceFactory implements ManagedServiceFactory {
    private final Map<String, ServiceRegistration> serviceInstances = new HashMap<String, ServiceRegistration>();
    private final BundleContext bundleContext;

    private Logger logger = LoggerFactory.getLogger(XbeeServiceFactory.class.getName());

    public XbeeServiceFactory(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    public void deleted(String pid) {
        final ServiceRegistration registration = serviceInstances.get(pid);
        if (registration != null)
        {
            final DefaultXbeeService service = (DefaultXbeeService)bundleContext.getService(registration.getReference());
            registration.unregister();
            service.disconnect();
        }
    }

    public void stop() {
        for (String pid : serviceInstances.keySet())
        {
            deleted(pid);
        }
    }
    
    public String getName() {
        return "ManagedServiceFactory for XbeeService";
    }

    public void updated(String pid, Dictionary properties) throws ConfigurationException {
        final String xbeePort = (String) properties.get("xbee.port");
        if ( xbeePort == null || xbeePort.trim().length() < 1)
        {
            throw new ConfigurationException("xbee.port", "No value found");
        }

        deleted(pid);

        try {
            DefaultXbeeService xbeeService = new DefaultXbeeService();
            xbeeService.connect(xbeePort);
            final ServiceRegistration registration = bundleContext.registerService(XbeeService.class.getName(), xbeeService, null);
            serviceInstances.put(pid, registration);
        } catch (Exception e) {
            throw new ConfigurationException("xbee.port", "Unable to configure Xbee service on requested port.", e);
        }
    }


}
