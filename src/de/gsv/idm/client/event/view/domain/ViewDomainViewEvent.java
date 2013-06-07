package de.gsv.idm.client.event.view.domain;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.view.GeneralViewEvent;
import de.gsv.idm.client.presenter.domain.ViewDomainPresenter;


public class ViewDomainViewEvent extends GeneralViewEvent<ViewDomainViewEvent> {

	public static Type<GeneralEventHandler<ViewDomainViewEvent>> TYPE = new Type<GeneralEventHandler<ViewDomainViewEvent>>();
	private final int id;

	public ViewDomainViewEvent(int id) {
		super("");
		this.id = id;
	}

	@Override
	public Type<GeneralEventHandler<ViewDomainViewEvent>> getAssociatedType() {
		return TYPE;
	}

	public int getId() {
		return id; 
	}
	
	public String getHistoryString() {
		return getOldHistoryString() + ViewDomainPresenter.HISTORY_STRING + "/" + getId();
	}
}
