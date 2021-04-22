package org.apache.commons.jcs3.utils.discovery;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jcs3.utils.discovery.behavior.IDiscoveryListener;

/** Mock listener, for testing. */
public class MockDiscoveryListener {
	public static IDiscoveryListener mockIDiscoveryListener1() {
		List<DiscoveredService> mockFieldVariableDiscoveredServices = new ArrayList<>();
		IDiscoveryListener mockInstance = mock(IDiscoveryListener.class);
		doAnswer((stubInvo) -> {
			DiscoveredService service = stubInvo.getArgument(0);
			mockFieldVariableDiscoveredServices.remove(service);
			return null;
		}).when(mockInstance).removeDiscoveredService(any(DiscoveredService.class));
		doAnswer((stubInvo) -> {
			DiscoveredService service = stubInvo.getArgument(0);
			mockFieldVariableDiscoveredServices.add(service);
			return null;
		}).when(mockInstance).addDiscoveredService(any(DiscoveredService.class));
		return mockInstance;
	}

}
