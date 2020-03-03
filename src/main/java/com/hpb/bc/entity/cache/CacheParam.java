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

package com.hpb.bc.entity.cache;

import com.hpb.bc.entity.BaseEntity;

public class CacheParam extends BaseEntity{
	private static final long serialVersionUID = -890388505919955051L;
	private String proccessId;

	public String getProccessId() {
		return proccessId;
	}

	public void setProccessId(String proccessId) {
		this.proccessId = proccessId;
	}
	
}
