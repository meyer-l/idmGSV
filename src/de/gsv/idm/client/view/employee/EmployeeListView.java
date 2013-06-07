package de.gsv.idm.client.view.employee;

import de.gsv.idm.client.presenter.employee.EmployeeListPresenter.EmployeeListDisplay;
import de.gsv.idm.client.view.general.GeneralListView;
import de.gsv.idm.shared.dto.EmployeeDTO;

public class EmployeeListView extends GeneralListView <EmployeeDTO, EmployeeListDisplay> implements EmployeeListDisplay {
	
	public EmployeeListView() {
		super(0.6);
	    addButtons();
	}


}
