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
package org.apache.commons.jcs3.jcache;

import org.apache.commons.jcs3.engine.control.event.behavior.IElementEvent;
import org.apache.commons.jcs3.engine.control.event.behavior.IElementEventHandler;

public class EvictionListener implements IElementEventHandler
{
    private final Statistics stats;

    public EvictionListener(final Statistics statistics)
    {
        this.stats = statistics;
    }

    @Override
    public void handleElementEvent(final IElementEvent event)
    {
        switch (event.getElementEvent())
        {
            case EXCEEDED_MAXLIFE_BACKGROUND:
            case EXCEEDED_MAXLIFE_ONREQUEST:
            case EXCEEDED_IDLETIME_ONREQUEST:
            case EXCEEDED_IDLETIME_BACKGROUND:
                stats.increaseEvictions(1);
                break;
        }
    }
}