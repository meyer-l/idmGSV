package de.gsv.idm.shared.push.event.securitylevelchange;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.SecurityLevelChangeDTO;

@SuppressWarnings("serial")
public class SecurityLevelChangeUpdatedPushEvent extends SecurityLevelChangePushEvent {

	public SecurityLevelChangeUpdatedPushEvent() {

	}

	public SecurityLevelChangeUpdatedPushEvent(SecurityLevelChangeDTO occupation) {
		super(occupation);
	}

	@Override
	public void call(CUDListener<SecurityLevelChangePushEvent> listener) {
		listener.onUpdated(this);
	}

}
