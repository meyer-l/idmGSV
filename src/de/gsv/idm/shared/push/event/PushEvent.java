package de.gsv.idm.shared.push.event;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.novanic.eventservice.client.event.Event;

@SuppressWarnings("serial")
public abstract class PushEvent<T, V extends GeneralDTO<?>> implements Event {

	private V object;
	
	public PushEvent(){
	}
	
	public PushEvent(V object){
		this.object = object;
	}

	public abstract void call(CUDListener<T> listener);

	public V getObject() {
		return object;
	}

	public void setObject(V object) {
		this.object = object;
	}

}
