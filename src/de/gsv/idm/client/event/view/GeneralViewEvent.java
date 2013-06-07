package de.gsv.idm.client.event.view;

import de.gsv.idm.client.event.GeneralEvent;


public abstract class GeneralViewEvent<T> extends GeneralEvent<T> {

protected String oldHistoryString;
	
	public GeneralViewEvent(String historyString) {
		this.oldHistoryString = historyString;
	}
	
	public String getOldHistoryString(){
		return oldHistoryString;
	}
	
	public void setOldHistoryString(String historyString){
		this.oldHistoryString = historyString;
	}
}
