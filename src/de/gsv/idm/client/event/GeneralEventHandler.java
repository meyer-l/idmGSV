package de.gsv.idm.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface GeneralEventHandler<T> extends EventHandler {
	void onEvent(T event);
}
