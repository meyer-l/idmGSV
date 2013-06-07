package de.gsv.idm.client.event;

import com.google.gwt.event.shared.GwtEvent;

public abstract class GeneralEvent<T> extends
GwtEvent<GeneralEventHandler<T>>{
	
	@SuppressWarnings("unchecked")
	@Override
	protected void dispatch(GeneralEventHandler<T> handler) {
		handler.onEvent((T) this);		
	}
	
	
}
