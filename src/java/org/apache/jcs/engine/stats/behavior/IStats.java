package org.apache.jcs.engine.stats.behavior;

/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Serializable;

/**
 * This interface defines the common behavior for a stats holder. 
 * 
 * @author aaronsm
 *
 */
public interface IStats extends Serializable {

	/**
	 * Return generic statistical or historical data.
	 * 
	 * @return IStatElement[]
	 */
	public abstract IStatElement[] getStatElements( );
		
	/**
	 * Set the generic statistical or historical data.
	 * 
	 * @param stats
	 */
	public abstract void setStatElements( IStatElement[] stats );	

	/**
	 * Get the type name, such as "LRU Memory Cache."  No formal type is defined.
	 * 
	 * @return String
	 */
	public abstract String getTypeName();

	/**
	 * Set the type name, such as "LRU Memory Cache."  No formal type is defined.
	 * If we need formal types, we can use the cachetype param
	 * 
	 * @param name
	 */
	public abstract void setTypeName( String name );		
}
