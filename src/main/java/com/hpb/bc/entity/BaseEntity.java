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