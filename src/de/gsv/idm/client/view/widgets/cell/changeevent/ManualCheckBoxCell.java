package de.gsv.idm.client.view.widgets.cell.changeevent;

import de.gsv.idm.client.view.widgets.form.InstanceCheckBoxCell;
import de.gsv.idm.shared.dto.ChangeEventDTO;

public class ManualCheckBoxCell extends InstanceCheckBoxCell<ChangeEventDTO> {

	@Override
    public Boolean getBooleanValue(ChangeEventDTO value) {
	   return value.isManual();
    }

	@Override
    public void setBooleanValue(ChangeEventDTO value, Boolean bool) {
	    value.setManual(bool);
    }

	@Override
    public Boolean getDependentDisable(ChangeEventDTO value) {
	    return false;
    }

}
