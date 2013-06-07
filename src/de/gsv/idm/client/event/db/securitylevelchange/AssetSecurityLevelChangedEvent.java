package de.gsv.idm.client.event.db.securitylevelchange;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.client.presenter.securitylevelchange.data.SecurityLevelBundle;
import de.gsv.idm.shared.dto.AssetDTO;

public class AssetSecurityLevelChangedEvent extends
        ObjectEvent<AssetDTO, AssetSecurityLevelChangedEvent> {

	public static Type<GeneralEventHandler<AssetSecurityLevelChangedEvent>> TYPE = new Type<GeneralEventHandler<AssetSecurityLevelChangedEvent>>();

	private SecurityLevelBundle oldSecurityLevels;

	public AssetSecurityLevelChangedEvent(AssetDTO object, SecurityLevelBundle oldSecurityLevels) {
		super(object);
		this.setOldSecurityLevel(oldSecurityLevels);
	}

	public SecurityLevelBundle getOldSecurityLevels() {
		return oldSecurityLevels;
	}

	public void setOldSecurityLevel(SecurityLevelBundle oldSecurityLevels) {
		this.oldSecurityLevels = oldSecurityLevels;
	}

	@Override
	public Type<GeneralEventHandler<AssetSecurityLevelChangedEvent>> getAssociatedType() {
		return TYPE;
	}

}
