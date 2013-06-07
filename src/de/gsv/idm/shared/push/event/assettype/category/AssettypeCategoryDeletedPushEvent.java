package de.gsv.idm.shared.push.event.assettype.category;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;

@SuppressWarnings("serial")
public class AssettypeCategoryDeletedPushEvent extends AssettypeCategoryPushEvent {

	public AssettypeCategoryDeletedPushEvent() {
	}
	
	public AssettypeCategoryDeletedPushEvent(AssettypeCategoryDTO asset){
		super(asset);
	}

	@Override
	public void call(CUDListener<AssettypeCategoryPushEvent> listener) {
		listener.onDeleted(this);		
	}

}
