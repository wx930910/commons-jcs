package org.apache.commons.jcs3.auxiliary.lateral.socket.tcp;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.jcs3.auxiliary.lateral.LateralElementDescriptor;
import org.apache.commons.jcs3.auxiliary.lateral.socket.tcp.behavior.ITCPLateralCacheAttributes;
import org.apache.commons.jcs3.io.ObjectInputStreamClassLoaderAware;
import org.apache.commons.jcs3.log.Log;
import org.apache.commons.jcs3.log.LogManager;

/**
 * This class is based on the log4j SocketAppender class. I'm using a different repair structure, so
 * it is significantly different.
 */
public class LateralTCPSender
{
    /** The logger */
    private static final Log log = LogManager.getLog( LateralTCPSender.class );

    /** Config */
    private final int socketOpenTimeOut;
    private final int socketSoTimeOut;

    /** The stream from the server connection. */
    private ObjectOutputStream oos;

    /** The socket connection with the server. */
    private Socket socket;

    /** how many messages sent */
    private int sendCnt;

    /** Use to synchronize multiple threads that may be trying to get. */
    private final Object getLock = new int[0];

    /**
     * Constructor for the LateralTCPSender object.
     * <p>
     * @param lca
     * @throws IOException
     */
    public LateralTCPSender( final ITCPLateralCacheAttributes lca )
        throws IOException
    {
        this.socketOpenTimeOut = lca.getOpenTimeOut();
        this.socketSoTimeOut = lca.getSocketTimeOut();

        final String p1 = lca.getTcpServer();
        if ( p1 == null )
        {
            throw new IOException( "Invalid server (null)" );
        }

        final String h2 = p1.substring( 0, p1.indexOf( ":" ) );
        final int po = Integer.parseInt( p1.substring( p1.indexOf( ":" ) + 1 ) );
        log.debug( "h2 = {0}, po = {1}", h2, po );

        if ( h2.isEmpty() )
        {
            throw new IOException( "Cannot connect to invalid address [" + h2 + ":" + po + "]" );
        }

        init( h2, po );
    }

    /**
     * Creates a connection to a TCP server.
     * <p>
     * @param host
     * @param port
     * @throws IOException
     */
    protected void init( final String host, final int port )
        throws IOException
    {
        try
        {
            log.info( "Attempting connection to [{0}]", host );

            // have time out socket open do this for us
            try
            {
                socket = new Socket();
                socket.connect( new InetSocketAddress( host, port ), this.socketOpenTimeOut );
            }
            catch ( final IOException ioe )
            {
                if (socket != null)
                {
                    socket.close();
                }

                throw new IOException( "Cannot connect to " + host + ":" + port, ioe );
            }

            socket.setSoTimeout( socketSoTimeOut );
            synchronized ( this )
            {
                oos = new ObjectOutputStream( socket.getOutputStream() );
            }
        }
        catch ( final java.net.ConnectException e )
        {
            log.debug( "Remote host [{0}] refused connection.", host );
            throw e;
        }
        catch ( final IOException e )
        {
            log.debug( "Could not connect to [{0}]", host, e );
            throw e;
        }
    }

    /**
     * Sends commands to the lateral cache listener.
     * <p>
     * @param led
     * @throws IOException
     */
    public <K, V> void send( final LateralElementDescriptor<K, V> led )
        throws IOException
    {
        sendCnt++;
        if ( log.isInfoEnabled() && sendCnt % 100 == 0 )
        {
            log.info( "Send Count (port {0}) = {1}", socket.getPort(), sendCnt );
        }

        log.debug( "sending LateralElementDescriptor" );

        if ( led == null )
        {
            return;
        }

        if ( oos == null )
        {
            throw new IOException( "No remote connection is available for LateralTCPSender." );
        }

        synchronized ( this.getLock )
        {
            oos.writeUnshared( led );
            oos.flush();
        }
    }

    /**
     * Sends commands to the lateral cache listener and gets a response. I'm afraid that we could
     * get into a pretty bad blocking situation here. This needs work. I just wanted to get some
     * form of get working. However, get is not recommended for performance reasons. If you have 10
     * laterals, then you have to make 10 failed gets to find out none of the caches have the item.
     * <p>
     * @param led
     * @return ICacheElement
     * @throws IOException
     */
    public <K, V> Object sendAndReceive( final LateralElementDescriptor<K, V> led )
        throws IOException
    {
        if ( led == null )
        {
            return null;
        }

        if ( oos == null )
        {
            throw new IOException( "No remote connection is available for LateralTCPSender." );
        }

        Object response = null;

        // Synchronized to insure that the get requests to server from this
        // sender and the responses are processed in order, else you could
        // return the wrong item from the cache.
        // This is a big block of code. May need to re-think this strategy.
        // This may not be necessary.
        // Normal puts, etc to laterals do not have to be synchronized.
        synchronized ( this.getLock )
        {
            try
            {
                // clean up input stream, nothing should be there yet.
                if ( socket.getInputStream().available() > 0 )
                {
                    socket.getInputStream().read( new byte[socket.getInputStream().available()] );
                }
            }
            catch ( final IOException ioe )
            {
                log.error( "Problem cleaning socket before send {0}", socket, ioe );
                throw ioe;
            }

            // write object to listener
            oos.writeUnshared( led );
            oos.flush();

            try (ObjectInputStream ois = new ObjectInputStreamClassLoaderAware( socket.getInputStream(), null ))
            {
                socket.setSoTimeout( socketSoTimeOut );
                response = ois.readObject();
            }
            catch ( final IOException ioe )
            {
                final String message = "Could not open ObjectInputStream to " + socket +
                    " SoTimeout [" + socket.getSoTimeout() +
                    "] Connected [" + socket.isConnected() + "]";
                log.error( message, ioe );
                throw ioe;
            }
            catch ( final Exception e )
            {
                log.error( e );
            }
        }

        return response;
    }

    /**
     * Closes connection used by all LateralTCPSenders for this lateral connection. Dispose request
     * should come into the facade and be sent to all lateral cache services. The lateral cache
     * service will then call this method.
     * <p>
     * @throws IOException
     */
    public void dispose()
        throws IOException
    {
        log.info( "Dispose called" );
        // WILL CLOSE CONNECTION USED BY ALL
        oos.close();
        socket.close();
    }
}
