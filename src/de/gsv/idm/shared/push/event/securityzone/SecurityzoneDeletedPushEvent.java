package de.gsv.idm.shared.push.event.securityzone;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

@SuppressWarnings("serial")
public class SecurityzoneDeletedPushEvent extends SecurityzonePushEvent {

	public SecurityzoneDeletedPushEvent() {
	}
	
	public SecurityzoneDeletedPushEvent(SecurityzoneDTO asset){
		super(asset);
	}
	
	@Override
	public void call(CUDListener<SecurityzonePushEvent> listener) {
		listener.onDeleted(this);
	}

}
