/**
 * Copyright (c) 2009, Pigg Logic, LLC
 * All rights reserved.
 *
 * This code is provided under a modified BSD license as provided in the the file LICENSE.TXT
 */

/**
Copyright (c) 2009, Michael Pigg
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package net.michaelpigg.xbeelib.protocol;

/**
 * XBee constants.
 */
public class Constants {
    public static final int START_DELIMITER = 0x7E;

    public static final int API_AT_COMMAND = 0x08;
    public static final int API_AT_COMMAND_QUEUE_PARAM = 0x09;
    public static final int API_AT_COMMAND_RESPONSE = 0x88;
    public static final int API_MODEM_STATUS = 0x8A;
    
    public static final int API_REMOTE_AT_COMMAND = 0x17;
    public static final int API_REMOTE_AT_COMMAND_RESPONSE = 0x97;

    public static final int API_TRANSMIT_REQUEST_64 = 0x00;
    public static final int API_TRANSMIT_REQUEST_16 = 0x01;
    public static final int API_TRANSMIT_STATUS = 0x89;

    public static final int API_RECEIVE_16 = 0x81;
    public static final int API_RECEIVE_64 = 0x80;
    public static final int API_IO_RECEIVE_16 = 0x83;
    public static final int API_IO_RECEIVE_64 = 0x82;
}
