package org.apache.jcs.auxiliary.remote;

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

import java.util.HashSet;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.jcs.auxiliary.MockCacheEventLogger;
import org.apache.jcs.auxiliary.remote.behavior.IRemoteCacheAttributes;
import org.apache.jcs.engine.CacheElement;
import org.apache.jcs.engine.behavior.ICacheElement;
import org.apache.jcs.engine.behavior.ICacheElementSerialized;
import org.apache.jcs.utils.serialization.SerializationConversionUtil;

/**
 * Unit Tests for the Remote Cache.
 */
public class RemoteCacheUnitTest
    extends TestCase
{
    /**
     * Verify that the remote service update method is called. The remote cache serializes the obect
     * first.
     * <p>
     * @throws Exception
     */
    public void testUpdate()
        throws Exception
    {
        // SETUP
        long listenerId = 123;
        IRemoteCacheAttributes cattr = new RemoteCacheAttributes();
        MockRemoteCacheService<String, String> service = new MockRemoteCacheService<String, String>();
        MockRemoteCacheListener<String, String> listener = new MockRemoteCacheListener<String, String>();
        listener.setListenerId( listenerId );

        RemoteCache<String, String> remoteCache = new RemoteCache<String, String>( cattr, service, listener );

        String cacheName = "testUpdate";

        // DO WORK
        ICacheElement<String, String> element = new CacheElement<String, String>( cacheName, "key", "value" );
        remoteCache.update( element );

        // VERIFY
        assertTrue( "The element should be in the serialized wrapper.",
                    service.lastUpdate instanceof ICacheElementSerialized );
        ICacheElement<String, String> result = SerializationConversionUtil
            .getDeSerializedCacheElement( (ICacheElementSerialized<String, String>) service.lastUpdate, remoteCache
                .getElementSerializer() );
        assertEquals( "Wrong element updated.", element.getVal(), result.getVal() );
        assertEquals( "Wrong listener id.", Long.valueOf( listenerId ), service.updateRequestIdList.get( 0 ) );
    }

    /**
     * Verify that when we call fix events queued in the zombie are propagated to the new service.
     * <p>
     * @throws Exception
     */
    public void testUpdateZombieThenFix()
        throws Exception
    {
        // SETUP
        IRemoteCacheAttributes cattr = new RemoteCacheAttributes();
        ZombieRemoteCacheService<String, String> zombie = new ZombieRemoteCacheService<String, String>( 10 );
        MockRemoteCacheService<String, String> service = new MockRemoteCacheService<String, String>();
        MockRemoteCacheListener<String, String> listener = new MockRemoteCacheListener<String, String>();

        // set the zombie
        RemoteCache<String, String> remoteCache = new RemoteCache<String, String>( cattr, zombie, listener );

        String cacheName = "testUpdate";

        // DO WORK
        ICacheElement<String, String> element = new CacheElement<String, String>( cacheName, "key", "value" );
        remoteCache.update( element );
        // set the new service, this should call propagate
        remoteCache.fixCache( service );

        // VERIFY
        assertTrue( "The element should be in the serialized warapper.",
                    service.lastUpdate instanceof ICacheElementSerialized );
        ICacheElement<String, String> result = SerializationConversionUtil
            .getDeSerializedCacheElement( (ICacheElementSerialized<String, String>) service.lastUpdate, remoteCache
                .getElementSerializer() );
        assertEquals( "Wrong element updated.", element.getVal(), result.getVal() );
    }

    /**
     * Verify event log calls.
     * <p>
     * @throws Exception
     */
    public void testUpdate_simple()
        throws Exception
    {
        // SETUP
        IRemoteCacheAttributes cattr = new RemoteCacheAttributes();
        MockRemoteCacheService<String, String> service = new MockRemoteCacheService<String, String>();
        MockRemoteCacheListener<String, String> listener = new MockRemoteCacheListener<String, String>();

        RemoteCache<String, String> remoteCache = new RemoteCache<String, String>( cattr, service, listener );

        MockCacheEventLogger cacheEventLogger = new MockCacheEventLogger();
        remoteCache.setCacheEventLogger( cacheEventLogger );

        ICacheElement<String, String> item = new CacheElement<String, String>( "region", "key", "value" );

        // DO WORK
        remoteCache.update( item );

        // VERIFY
        assertEquals( "Start should have been called.", 1, cacheEventLogger.startICacheEventCalls );
        assertEquals( "End should have been called.", 1, cacheEventLogger.endICacheEventCalls );
    }

    /**
     * Verify event log calls.
     * <p>
     * @throws Exception
     */
    public void testGet_simple()
        throws Exception
    {
        // SETUP
        IRemoteCacheAttributes cattr = new RemoteCacheAttributes();
        MockRemoteCacheService<String, String> service = new MockRemoteCacheService<String, String>();
        MockRemoteCacheListener<String, String> listener = new MockRemoteCacheListener<String, String>();

        RemoteCache<String, String> remoteCache = new RemoteCache<String, String>( cattr, service, listener );

        MockCacheEventLogger cacheEventLogger = new MockCacheEventLogger();
        remoteCache.setCacheEventLogger( cacheEventLogger );

        // DO WORK
        remoteCache.get( "key" );

        // VERIFY
        assertEquals( "Start should have been called.", 1, cacheEventLogger.startICacheEventCalls );
        assertEquals( "End should have been called.", 1, cacheEventLogger.endICacheEventCalls );
    }

    /**
     * Verify event log calls.
     * <p>
     * @throws Exception
     */
    public void testGetMultiple_simple()
        throws Exception
    {
        // SETUP
        IRemoteCacheAttributes cattr = new RemoteCacheAttributes();
        MockRemoteCacheService<String, String> service = new MockRemoteCacheService<String, String>();
        MockRemoteCacheListener<String, String> listener = new MockRemoteCacheListener<String, String>();

        RemoteCache<String, String> remoteCache = new RemoteCache<String, String>( cattr, service, listener );

        MockCacheEventLogger cacheEventLogger = new MockCacheEventLogger();
        remoteCache.setCacheEventLogger( cacheEventLogger );

        // DO WORK
        remoteCache.getMultiple( new HashSet<String>() );

        // VERIFY
        assertEquals( "Start should have been called.", 1, cacheEventLogger.startICacheEventCalls );
        assertEquals( "End should have been called.", 1, cacheEventLogger.endICacheEventCalls );
    }

    /**
     * Verify event log calls.
     * <p>
     * @throws Exception
     */
    public void testRemove_simple()
        throws Exception
    {
        // SETUP
        IRemoteCacheAttributes cattr = new RemoteCacheAttributes();
        MockRemoteCacheService<String, String> service = new MockRemoteCacheService<String, String>();
        MockRemoteCacheListener<String, String> listener = new MockRemoteCacheListener<String, String>();

        RemoteCache<String, String> remoteCache = new RemoteCache<String, String>( cattr, service, listener );

        MockCacheEventLogger cacheEventLogger = new MockCacheEventLogger();
        remoteCache.setCacheEventLogger( cacheEventLogger );

        // DO WORK
        remoteCache.remove( "key" );

        // VERIFY
        assertEquals( "Start should have been called.", 1, cacheEventLogger.startICacheEventCalls );
        assertEquals( "End should have been called.", 1, cacheEventLogger.endICacheEventCalls );
    }

    /**
     * Verify event log calls.
     * <p>
     * @throws Exception
     */
    public void testRemoveAll_simple()
        throws Exception
    {
        // SETUP
        IRemoteCacheAttributes cattr = new RemoteCacheAttributes();
        MockRemoteCacheService<String, String> service = new MockRemoteCacheService<String, String>();
        MockRemoteCacheListener<String, String> listener = new MockRemoteCacheListener<String, String>();

        RemoteCache<String, String> remoteCache = new RemoteCache<String, String>( cattr, service, listener );

        MockCacheEventLogger cacheEventLogger = new MockCacheEventLogger();
        remoteCache.setCacheEventLogger( cacheEventLogger );

        // DO WORK
        remoteCache.remove( "key" );

        // VERIFY
        assertEquals( "Start should have been called.", 1, cacheEventLogger.startICacheEventCalls );
        assertEquals( "End should have been called.", 1, cacheEventLogger.endICacheEventCalls );
    }

    /**
     * Verify event log calls.
     * <p>
     * @throws Exception
     */
    public void testGetMatching_simple()
        throws Exception
    {
        // SETUP
        String pattern = "adsfasdfasd.?";
        IRemoteCacheAttributes cattr = new RemoteCacheAttributes();
        MockRemoteCacheService<String, String> service = new MockRemoteCacheService<String, String>();
        MockRemoteCacheListener<String, String> listener = new MockRemoteCacheListener<String, String>();

        RemoteCache<String, String> remoteCache = new RemoteCache<String, String>( cattr, service, listener );

        MockCacheEventLogger cacheEventLogger = new MockCacheEventLogger();
        remoteCache.setCacheEventLogger( cacheEventLogger );

        // DO WORK
        Map<String, ICacheElement<String, String>> result = remoteCache.getMatching( pattern );

        // VERIFY
        assertNotNull( "Should have a map", result );
        assertEquals( "Start should have been called.", 1, cacheEventLogger.startICacheEventCalls );
        assertEquals( "End should have been called.", 1, cacheEventLogger.endICacheEventCalls );
    }

    /**
     * Verify event log calls.
     * <p>
     * @throws Exception
     */
    public void testDispose_simple()
        throws Exception
    {
        // SETUP
        IRemoteCacheAttributes cattr = new RemoteCacheAttributes();
        MockRemoteCacheService<String, String> service = new MockRemoteCacheService<String, String>();
        MockRemoteCacheListener<String, String> listener = new MockRemoteCacheListener<String, String>();

        RemoteCache<String, String> remoteCache = new RemoteCache<String, String>( cattr, service, listener );

        MockCacheEventLogger cacheEventLogger = new MockCacheEventLogger();
        remoteCache.setCacheEventLogger( cacheEventLogger );

        // DO WORK
        remoteCache.dispose( );

        // VERIFY
        assertEquals( "Start should have been called.", 1, cacheEventLogger.startICacheEventCalls );
        assertEquals( "End should have been called.", 1, cacheEventLogger.endICacheEventCalls );
    }

    /**
     * Verify that there is no problem if there is no listener.
     * <p>
     * @throws Exception
     */
    public void testDispose_nullListener()
        throws Exception
    {
        // SETUP
        IRemoteCacheAttributes cattr = new RemoteCacheAttributes();
        MockRemoteCacheService<String, String> service = new MockRemoteCacheService<String, String>();
        MockRemoteCacheListener<String, String> listener = null;

        RemoteCache<String, String> remoteCache = new RemoteCache<String, String>( cattr, service, listener );

        MockCacheEventLogger cacheEventLogger = new MockCacheEventLogger();
        remoteCache.setCacheEventLogger( cacheEventLogger );

        // DO WORK
        remoteCache.dispose( );

        // VERIFY
        assertEquals( "Start should have been called.", 1, cacheEventLogger.startICacheEventCalls );
        assertEquals( "End should have been called.", 1, cacheEventLogger.endICacheEventCalls );
    }
}
