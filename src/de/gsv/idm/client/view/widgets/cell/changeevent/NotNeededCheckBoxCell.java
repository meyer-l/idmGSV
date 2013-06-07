package de.gsv.idm.client.view.widgets.cell.changeevent;

import de.gsv.idm.client.view.widgets.form.InstanceCheckBoxCell;
import de.gsv.idm.shared.dto.ChangeEventDTO;

public class NotNeededCheckBoxCell extends InstanceCheckBoxCell<ChangeEventDTO> {

	@Override
	public Boolean getBooleanValue(ChangeEventDTO value) {
		return value.isNotNeeded();
	}

	@Override
	public void setBooleanValue(ChangeEventDTO value, Boolean bool) {
		value.setNotNeeded(bool);
	}

	@Override
	public Boolean getDependentDisable(ChangeEventDTO value) {
		return false;
	}

}
