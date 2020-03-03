/*
 * Copyright 2020 HPB Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hpb.bc.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseEntity implements Serializable{
	private static final long serialVersionUID = -5726027232895928043L;
	private Map<Object, Object> map;
	public Map<Object, Object> getMap() {
		if(this.map==null) {
			this.map=new HashMap<Object, Object>();
		}
		return map;
	}
	public void setMap(Map<Object, Object> map) {
		this.map = map;
	}
	public void put(Object key,Object value) {
		getMap().put(key, value);
	}
	public Object get(Object key) {
		return getMap().get(key);
	}
}