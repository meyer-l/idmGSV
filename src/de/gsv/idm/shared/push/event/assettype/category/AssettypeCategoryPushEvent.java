package de.gsv.idm.shared.push.event.assettype.category;

import de.gsv.idm.shared.dto.AssettypeCategoryDTO;
import de.gsv.idm.shared.push.event.PushEvent;

@SuppressWarnings("serial")
public abstract class AssettypeCategoryPushEvent extends
        PushEvent<AssettypeCategoryPushEvent, AssettypeCategoryDTO> {
	public static final String CONVERSATION_DOMAIN = "assettypeCategory_pushDomain";

	AssettypeCategoryPushEvent() {

	}

	AssettypeCategoryPushEvent(AssettypeCategoryDTO object) {
		super(object);
	}

	public String getConservationDomain() {
		return CONVERSATION_DOMAIN;
	}
}
