package de.gsv.idm.client.event.view;

import com.google.gwt.user.client.ui.HasWidgets;


public abstract class GeneralContainerEvent<T> extends GeneralViewEvent<T> {
	private HasWidgets container;
	
	public GeneralContainerEvent(String historyString, HasWidgets container) {
		super(historyString);
		this.container = container;
	}
	
	public HasWidgets getContainer(){
		return container;
	}
}
