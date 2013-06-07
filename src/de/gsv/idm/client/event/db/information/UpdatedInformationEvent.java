package de.gsv.idm.client.event.db.information;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.InformationDTO;

public class UpdatedInformationEvent extends
		ObjectEvent<InformationDTO, UpdatedInformationEvent> {
	
	public static Type<GeneralEventHandler<UpdatedInformationEvent>> TYPE = new  Type<GeneralEventHandler<UpdatedInformationEvent>>();
	
	public UpdatedInformationEvent(InformationDTO information) {
		super(information);
	}
	
	@Override
	public Type<GeneralEventHandler<UpdatedInformationEvent>> getAssociatedType() {
		return TYPE;
	}


}
