package de.gsv.idm.client.event.view.startscreen;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.view.GeneralViewEvent;
import de.gsv.idm.client.presenter.StartScreenPresenter;

public class StartScreenViewEvent extends GeneralViewEvent<StartScreenViewEvent> {

	public static Type<GeneralEventHandler<StartScreenViewEvent>> TYPE = new Type<GeneralEventHandler<StartScreenViewEvent>>();
	private int id;

	public StartScreenViewEvent(int id) {
		super("");
		this.id = id;
	}

	@Override
	public Type<GeneralEventHandler<StartScreenViewEvent>> getAssociatedType() {
		return TYPE;
	}

	public int getId() {
		return id;
	}

	public String getHistoryString() {
		if(id > 0){
			return getOldHistoryString() + StartScreenPresenter.HISTORY_STRING + "/" + getId() + "/";
		} else {
			return getOldHistoryString() + StartScreenPresenter.HISTORY_STRING + "/";
		}
		
	}
}