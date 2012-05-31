package org.apache.jcs.auxiliary.remote.server;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import org.apache.jcs.auxiliary.AbstractAuxiliaryCacheAttributes;
import org.apache.jcs.auxiliary.AuxiliaryCacheAttributes;
import org.apache.jcs.auxiliary.remote.behavior.IRemoteCacheConstants;
import org.apache.jcs.auxiliary.remote.server.behavior.IRemoteCacheServerAttributes;

/**
 * These attributes are used to configure the remote cache server.
 */
public class RemoteCacheServerAttributes
    extends AbstractAuxiliaryCacheAttributes
    implements IRemoteCacheServerAttributes
{
    /** Don't change */
    private static final long serialVersionUID = -2741662082869155365L;

    /** Name of the remote service. */
    private String remoteServiceName = IRemoteCacheConstants.REMOTE_CACHE_SERVICE_VAL;

    /** The remote host. */
    private String remoteHost;

    /** The registry remote port. */
    private int remotePort;

    /**
     * Failover servers will be used by local caches one at a time. Listeners will be registered
     * with all cluster servers. If we add a get from cluster attribute we will have the ability to
     * chain clusters and have them get from each other.
     */
    private String clusterServers = "";

    /** port the server will listen to */
    private int servicePort = 0;

    /** Cluster or local */
    private int remoteType = LOCAL;

    /** Remove item when put */
    private boolean removeUponRemotePut = true;

    /** Ony get, don't put */
    private boolean getOnly = false;

    /** Can a cluster remote put to other remotes */
    private boolean localClusterConsistency = true;

    /** Can a cluster remote get from other remotes */
    private boolean allowClusterGet = true;

    /** The config file, the initializationis multistage. Remote cache then composite cache. */
    private String configFileName = "";

    /** Factory socket time out. */
    public static final int DEFAULT_RMI_SOCKET_FACTORY_TIMEOUT_MS = 10000;

    /** Connect and read timeout. */
    private int rmiSocketFactoryTimeoutMillis = DEFAULT_RMI_SOCKET_FACTORY_TIMEOUT_MS;
    
    /** Should we start the registry */
    private boolean DEFAULT_START_REGISTRY = true;
    
    /** Should we start the registry */
    private boolean startRegistry = DEFAULT_START_REGISTRY;
    
    /** Should we try to keep the registry alive */
    private boolean DEFAULT_USE_REGISTRY_KEEP_ALIVE = true;
    
    /** Should we try to keep the registry alive */
    private boolean useRegistryKeepAlive = DEFAULT_USE_REGISTRY_KEEP_ALIVE;

    /** The delay between runs */
    private long registryKeepAliveDelayMillis = 15 * 1000;
    
    /** Default constructor for the RemoteCacheAttributes object */
    public RemoteCacheServerAttributes()
    {
        super();
    }

    /**
     * Gets the remoteTypeName attribute of the RemoteCacheAttributes object
     * <p>
     * @return The remoteTypeName value
     */
    public String getRemoteTypeName()
    {
        if ( remoteType == LOCAL )
        {
            return "LOCAL";
        }
        else if ( remoteType == CLUSTER )
        {
            return "CLUSTER";
        }
        return "LOCAL";
    }

    /**
     * Sets the remoteTypeName attribute of the RemoteCacheAttributes object
     * <p>
     * @param s The new remoteTypeName value
     */
    public void setRemoteTypeName( String s )
    {
        if ( s.equals( "LOCAL" ) )
        {
            remoteType = LOCAL;
        }
        else if ( s.equals( "CLUSTER" ) )
        {
            remoteType = CLUSTER;
        }
    }

    /**
     * Gets the remoteType attribute of the RemoteCacheAttributes object
     * <p>
     * @return The remoteType value
     */
    public int getRemoteType()
    {
        return remoteType;
    }

    /**
     * Sets the remoteType attribute of the RemoteCacheAttributes object
     * <p>
     * @param p The new remoteType value
     */
    public void setRemoteType( int p )
    {
        this.remoteType = p;
    }

    /**
     * clones
     * <p>
     * @return AuxiliaryCacheAttributes clone
     */
    public AuxiliaryCacheAttributes copy()
    {
        try
        {
            return (AuxiliaryCacheAttributes) this.clone();
        }
        catch ( Exception e )
        {
            // swallow
        }
        return this;
    }

    /**
     * Gets the remoteServiceName attribute of the RemoteCacheAttributes object
     * <p>
     * @return The remoteServiceName value
     */
    public String getRemoteServiceName()
    {
        return this.remoteServiceName;
    }

    /**
     * Sets the remoteServiceName attribute of the RemoteCacheAttributes object
     * <p>
     * @param s The new remoteServiceName value
     */
    public void setRemoteServiceName( String s )
    {
        this.remoteServiceName = s;
    }

    /**
     * Gets the remoteHost attribute of the RemoteCacheAttributes object
     * <p>
     * @return The remoteHost value
     */
    public String getRemoteHost()
    {
        return this.remoteHost;
    }

    /**
     * Sets the remoteHost attribute of the RemoteCacheAttributes object
     * <p>
     * @param s The new remoteHost value
     */
    public void setRemoteHost( String s )
    {
        this.remoteHost = s;
    }

    /**
     * Gets the remotePort attribute of the RemoteCacheAttributes object
     * <p>
     * @return The remotePort value
     */
    public int getRemotePort()
    {
        return this.remotePort;
    }

    /**
     * Sets the remotePort attribute of the RemoteCacheAttributes object
     * <p>
     * @param p The new remotePort value
     */
    public void setRemotePort( int p )
    {
        this.remotePort = p;
    }

    /**
     * Gets the clusterServers attribute of the RemoteCacheAttributes object
     * <p>
     * @return The clusterServers value
     */
    public String getClusterServers()
    {
        return this.clusterServers;
    }

    /**
     * Sets the clusterServers attribute of the RemoteCacheAttributes object
     * <p>
     * @param s The new clusterServers value
     */
    public void setClusterServers( String s )
    {
        this.clusterServers = s;
    }

    /**
     * Gets the localPort attribute of the RemoteCacheAttributes object
     * <p>
     * @return The localPort value
     */
    public int getServicePort()
    {
        return this.servicePort;
    }

    /**
     * Sets the localPort attribute of the RemoteCacheAttributes object
     * <p>
     * @param p The new localPort value
     */
    public void setServicePort( int p )
    {
        this.servicePort = p;
    }

    /**
     * Gets the removeUponRemotePut attribute of the RemoteCacheAttributes object
     * <p>
     * @return The removeUponRemotePut value
     */
    public boolean getRemoveUponRemotePut()
    {
        return this.removeUponRemotePut;
    }

    /**
     * Sets the removeUponRemotePut attribute of the RemoteCacheAttributes object
     * <p>
     * @param r The new removeUponRemotePut value
     */
    public void setRemoveUponRemotePut( boolean r )
    {
        this.removeUponRemotePut = r;
    }

    /**
     * Gets the getOnly attribute of the RemoteCacheAttributes object
     * <p>
     * @return The getOnly value
     */
    public boolean getGetOnly()
    {
        return this.getOnly;
    }

    /**
     * Sets the getOnly attribute of the RemoteCacheAttributes object
     * <p>
     * @param r The new getOnly value
     */
    public void setGetOnly( boolean r )
    {
        this.getOnly = r;
    }

    /**
     * Should cluster updates be propagated to the locals
     * <p>
     * @return The localClusterConsistency value
     */
    public boolean getLocalClusterConsistency()
    {
        return isLocalClusterConsistency();
    }

    /**
     * Should cluster updates be propagated to the locals
     * <p>
     * @return The localClusterConsistency value
     */
    public boolean isLocalClusterConsistency()
    {
        return localClusterConsistency;
    }
    
    /**
     * Should cluster updates be propagated to the locals
     * <p>
     * @param r The new localClusterConsistency value
     */
    public void setLocalClusterConsistency( boolean r )
    {
        this.localClusterConsistency = r;
    }

    /**
     * Should gets from non-cluster clients be allowed to get from other remote auxiliaries.
     * <p>
     * @return The localClusterConsistency value
     */
    public boolean getAllowClusterGet()
    {
        return isAllowClusterGet();
    }

    /**
     * Should gets from non-cluster clients be allowed to get from other remote auxiliaries.
     * <p>
     * @return The localClusterConsistency value
     */
    public boolean isAllowClusterGet()
    {
        return allowClusterGet;
    }
    
    /**
     * Should we try to get from other cluster servers if we don't find the items locally.
     * <p>
     * @param r The new localClusterConsistency value
     */
    public void setAllowClusterGet( boolean r )
    {
        allowClusterGet = r;
    }

    /**
     * Gets the ConfigFileName attribute of the IRemoteCacheAttributes object
     * <p>
     * @return The clusterServers value
     */
    public String getConfigFileName()
    {
        return configFileName;
    }

    /**
     * Sets the ConfigFileName attribute of the IRemoteCacheAttributes object
     * <p>
     * @param s The new clusterServers value
     */
    public void setConfigFileName( String s )
    {
        configFileName = s;
    }

    /**
     * @param rmiSocketFactoryTimeoutMillis The rmiSocketFactoryTimeoutMillis to set.
     */
    public void setRmiSocketFactoryTimeoutMillis( int rmiSocketFactoryTimeoutMillis )
    {
        this.rmiSocketFactoryTimeoutMillis = rmiSocketFactoryTimeoutMillis;
    }

    /**
     * @return Returns the rmiSocketFactoryTimeoutMillis.
     */
    public int getRmiSocketFactoryTimeoutMillis()
    {
        return rmiSocketFactoryTimeoutMillis;
    }

    /**
     * Should we try to keep the registry alive
     * <p>
     * @param useRegistryKeepAlive the useRegistryKeepAlive to set
     */
    public void setUseRegistryKeepAlive( boolean useRegistryKeepAlive )
    {
        this.useRegistryKeepAlive = useRegistryKeepAlive;
    }

    /**
     * Should we start the registry
     * <p>
     * @param startRegistry the startRegistry to set
     */
    public void setStartRegistry( boolean startRegistry )
    {
        this.startRegistry = startRegistry;
    }

    /**
     * Should we start the registry
     * <p>
     * @return the startRegistry
     */
    public boolean isStartRegistry()
    {
        return startRegistry;
    }

    /**
     * Should we try to keep the registry alive
     * <p>
     * @return the useRegistryKeepAlive
     */
    public boolean isUseRegistryKeepAlive()
    {
        return useRegistryKeepAlive;
    }

    /**
     * @param registryKeepAliveDelayMillis the registryKeepAliveDelayMillis to set
     */
    public void setRegistryKeepAliveDelayMillis( long registryKeepAliveDelayMillis )
    {
        this.registryKeepAliveDelayMillis = registryKeepAliveDelayMillis;
    }

    /**
     * @return the registryKeepAliveDelayMillis
     */
    public long getRegistryKeepAliveDelayMillis()
    {
        return registryKeepAliveDelayMillis;
    }

    /**
     * @return String details
     */
    @Override
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append( "\n remoteHost = [" + this.getRemoteHost() + "]" );
        buf.append( "\n remotePort = [" + this.getRemotePort() + "]" );
        buf.append( "\n servicePort = [" + this.getServicePort() + "]" );
        buf.append( "\n cacheName = [" + this.getCacheName() + "]" );
        buf.append( "\n removeUponRemotePut = [" + this.getRemoveUponRemotePut() + "]" );
        buf.append( "\n getOnly = [" + this.getGetOnly() + "]" );
        buf.append( "\n allowClusterGet = [" + this.getAllowClusterGet() + "]" );
        buf.append( "\n localClusterConsistency = [" + this.getLocalClusterConsistency() + "]" );
        buf.append( "\n configFileName = [" + this.getConfigFileName() + "]" );
        buf.append( "\n rmiSocketFactoryTimeoutMillis = [" + this.getRmiSocketFactoryTimeoutMillis() + "]" );
        buf.append( "\n startRegistry = [" + this.isStartRegistry() + "]" );
        buf.append( "\n useRegistryKeepAlive = [" + this.isUseRegistryKeepAlive() + "]" );
        buf.append( "\n registryKeepAliveDelayMillis = [" + this.getRegistryKeepAliveDelayMillis() + "]" );
        buf.append( "\n eventQueueType = [" + this.getEventQueueType() + "]" );
        buf.append( "\n eventQueuePoolName = [" + this.getEventQueuePoolName() + "]" );
        return buf.toString();
    }
}
