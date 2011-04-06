/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

package net.michaelpigg.xbeelib.protocol.impl;

import net.michaelpigg.xbeelib.XbeeHandlerCallback;
import net.michaelpigg.xbeelib.protocol.AtCommandResponse;
import net.michaelpigg.xbeelib.protocol.ReceiveIoDataFrame;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author MikePigg
 */
public class XbeeHandler extends IoHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(XbeeHandler.class);

    private final XbeeHandlerCallback callback;
    public XbeeHandler(XbeeHandlerCallback callback) {
        this.callback = callback;
    }

    public void messageReceived(IoSession session, Object message) throws Exception
    {
        if (message instanceof ReceiveIoDataFrame)
        {
            callback.IoDataReceived((ReceiveIoDataFrame)message);

        } else if (message instanceof AtCommandResponse) {
            logger.debug("AT command response: {}", message.toString());
            callback.CommandResponse((AtCommandResponse)message);
        } else {
            logger.debug("Unknown message {}", message.toString());
        }
    }



    public void exceptionCaught(IoSession session, Throwable cause) throws Exception
    {
        logger.error("Exception in handler.", cause);
    }

    public void sessionIdle(IoSession session, IdleStatus status) throws Exception
    {
        logger.debug("Handler is IDLE " + session.getIdleCount(status));
    }

    
}
