package de.gsv.idm.shared.push.event.securitylevelchange;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.SecurityLevelChangeDTO;

@SuppressWarnings("serial")
public class SecurityLevelChangeDeletedPushEvent extends SecurityLevelChangePushEvent {
	
	public SecurityLevelChangeDeletedPushEvent() {

	}

	public SecurityLevelChangeDeletedPushEvent(SecurityLevelChangeDTO occupation) {
		super(occupation);
	}

	@Override
	public void call(CUDListener<SecurityLevelChangePushEvent> listener) {
		listener.onDeleted(this);
	}

}
