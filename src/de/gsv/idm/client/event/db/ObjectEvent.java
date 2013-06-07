package de.gsv.idm.client.event.db;

import de.gsv.idm.client.event.GeneralEvent;

public abstract class ObjectEvent<T,H> extends GeneralEvent<H> {
	private T object;
	
	public ObjectEvent(T dbObject) {
		this.object = dbObject;
	}
	
	public T getObject(){
		return object;
	}
	
	public void setObject(T dbObject) {
		this.object = dbObject;
	}
}
