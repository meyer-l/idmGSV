package de.gsv.idm.shared.push.event.assettype.category;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;

@SuppressWarnings("serial")
public class AssettypeCategoryUpdatedPushEvent extends AssettypeCategoryPushEvent {
	public AssettypeCategoryUpdatedPushEvent() {
	}

	public AssettypeCategoryUpdatedPushEvent(AssettypeCategoryDTO asset) {
		super(asset);
	}

	public void call(CUDListener<AssettypeCategoryPushEvent> listener) {
		listener.onUpdated(this);
	}

}
