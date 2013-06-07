package de.gsv.idm.client.gui.change;

import de.gsv.idm.client.event.GeneralEvent;
import de.gsv.idm.client.event.GeneralEventHandler;

public class ShowParsedChangeTabEvent extends GeneralEvent<ShowParsedChangeTabEvent> {

	public static Type<GeneralEventHandler<ShowParsedChangeTabEvent>> TYPE = new Type<GeneralEventHandler<ShowParsedChangeTabEvent>>();
	private final Integer domainId;

	public ShowParsedChangeTabEvent(Integer id) {
		this.domainId = id;
	}

	@Override
	public Type<GeneralEventHandler<ShowParsedChangeTabEvent>> getAssociatedType() {
		return TYPE;
	}

	public Integer getDomainId() {
		return domainId; 
	}

}
