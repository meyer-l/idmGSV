package de.gsv.idm.client.view.widgets.securitylevelchange;

import de.gsv.idm.client.view.widgets.form.InstanceCheckBoxCell;
import de.gsv.idm.shared.dto.SecurityLevelChangeDTO;

public class SecurityLevelChangeCheckBoxCell extends InstanceCheckBoxCell<SecurityLevelChangeDTO>{

	@Override
    public Boolean getBooleanValue(SecurityLevelChangeDTO value) {
	    return value.getReviewed();
    }

	@Override
    public void setBooleanValue(SecurityLevelChangeDTO value, Boolean bool) {
	    value.setReviewed(bool);
    }

	@Override
    public Boolean getDependentDisable(SecurityLevelChangeDTO value) {
	    return false;
    }

}
