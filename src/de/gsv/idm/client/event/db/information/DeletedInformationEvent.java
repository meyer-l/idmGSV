package de.gsv.idm.client.event.db.information;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.InformationDTO;

public class DeletedInformationEvent extends
		ObjectEvent<InformationDTO,DeletedInformationEvent> {
	public static Type<GeneralEventHandler<DeletedInformationEvent>> TYPE = new  Type<GeneralEventHandler<DeletedInformationEvent>>();
	
	public DeletedInformationEvent(InformationDTO information) {
		super(information);
	}

	
	@Override
	public Type<GeneralEventHandler<DeletedInformationEvent>> getAssociatedType() {
		return TYPE;
	}

}
