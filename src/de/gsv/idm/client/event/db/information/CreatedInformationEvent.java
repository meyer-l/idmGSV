package de.gsv.idm.client.event.db.information;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.InformationDTO;

public class CreatedInformationEvent extends
		ObjectEvent<InformationDTO, CreatedInformationEvent> {
	public static Type<GeneralEventHandler<CreatedInformationEvent>> TYPE = new  Type<GeneralEventHandler<CreatedInformationEvent>>();
	
	public CreatedInformationEvent(InformationDTO information) {
		super(information);
	}
	
	@Override
	public Type<GeneralEventHandler<CreatedInformationEvent>> getAssociatedType() {
		return TYPE;
	}


}
