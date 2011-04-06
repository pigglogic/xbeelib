/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.impl;

import net.michaelpigg.xbeelib.XbeeHandlerCallback;
import net.michaelpigg.xbeelib.XbeeService;
import net.michaelpigg.xbeelib.protocol.AtCommand;
import net.michaelpigg.xbeelib.protocol.impl.XbeeHandler;
import net.michaelpigg.xbeelib.protocol.impl.XbeeHandlerCallbackDispatcher;
import net.michaelpigg.xbeelib.protocol.impl.XbeeProtocolFactory;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.serial.SerialAddress;
import org.apache.mina.transport.serial.SerialConnector;


/**
 *
 * @author michaelpigg
 */
public class DefaultXbeeService implements XbeeService {
        private SerialConnector connector;
        private SerialAddress address;
        private IoSession session;
        private XbeeHandler handler;
        private XbeeHandlerCallbackDispatcher dispatcher;

    public void connect(String serialPort) {
            connector = new SerialConnector();
            address = new SerialAddress(serialPort, 9600,
                    SerialAddress.DataBits.DATABITS_8, SerialAddress.StopBits.BITS_1, SerialAddress.Parity.NONE,
                    SerialAddress.FlowControl.RTSCTS_IN);
            dispatcher = new XbeeHandlerCallbackDispatcher();
            handler = new XbeeHandler(dispatcher);
            connector.setHandler(handler);
            final DefaultIoFilterChainBuilder fcb = connector.getFilterChain();
            fcb.addLast("codec", new ProtocolCodecFilter(new XbeeProtocolFactory()));

            ConnectFuture connectFuture = connector.connect(address);
            connectFuture.awaitUninterruptibly();
            session = connectFuture.getSession();
    }

    public void sendCommand(AtCommand atCommand) {
        WriteFuture future = session.write(atCommand);
    }

    public void addListener(XbeeHandlerCallback listener) {
        dispatcher.register(listener);
    }

    public void removeListener(XbeeHandlerCallback listener) {
        dispatcher.deregister(listener);
    }

    public void disconnect() {
        session.close(true);
    }

}
