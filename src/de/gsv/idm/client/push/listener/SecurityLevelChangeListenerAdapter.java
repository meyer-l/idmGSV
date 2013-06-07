package de.gsv.idm.client.push.listener;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.db.securitylevelchange.CreatedSecurityLevelChangeEvent;
import de.gsv.idm.client.event.db.securitylevelchange.DeletedSecurityLevelChangeEvent;
import de.gsv.idm.client.event.db.securitylevelchange.UpdatedSecurityLevelChangeEvent;
import de.gsv.idm.client.push.CUDListenerAdapter;
import de.gsv.idm.shared.dto.SecurityLevelChangeDTO;
import de.gsv.idm.shared.push.event.securitylevelchange.SecurityLevelChangePushEvent;

public class SecurityLevelChangeListenerAdapter extends CUDListenerAdapter<SecurityLevelChangePushEvent, SecurityLevelChangeDTO> {

	private static SecurityLevelChangeListenerAdapter instance;

	static public SecurityLevelChangeListenerAdapter getInstance() {
		if (instance == null) {
			instance = new SecurityLevelChangeListenerAdapter();
		}
		return instance;
	}
	
	public SecurityLevelChangeListenerAdapter() {
	    super(DBController.getInstance().getSecurityLevelChangeController());
    }

	@Override
    protected void fireCreatedEvent(SecurityLevelChangePushEvent createdEvent) {
		eventBus.fireEvent(new CreatedSecurityLevelChangeEvent(createdEvent.getObject()));
	    
    }

	@Override
    protected void fireDeletedEvent(SecurityLevelChangePushEvent deletedEvent) {
		eventBus.fireEvent(new DeletedSecurityLevelChangeEvent(deletedEvent.getObject()));
    }

	@Override
	public void fireUpdatedEvent(SecurityLevelChangeDTO updatedEvent) {
		eventBus.fireEvent(new UpdatedSecurityLevelChangeEvent(updatedEvent));
    }

}
