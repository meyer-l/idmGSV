package de.gsv.idm.client.view.employee;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import de.gsv.idm.client.view.properties.EmployeeDTOProperties;
import de.gsv.idm.client.view.widgets.cell.ToolTipTextCell;
import de.gsv.idm.client.view.widgets.form.GridList;
import de.gsv.idm.shared.dto.EmployeeDTO;

public class EmployeeGridList extends GridList<EmployeeDTO> {

	public EmployeeGridList(ListStore<EmployeeDTO> store, EmployeeDTOProperties employeeProps) {
	    super(store, employeeProps.valueProviderLabel());
	    
		List<ColumnConfig<EmployeeDTO, ?>> employeeColumns = new ArrayList<ColumnConfig<EmployeeDTO, ?>>();
		ColumnConfig<EmployeeDTO, String> employeeName = new ColumnConfig<EmployeeDTO, String>(
		        employeeProps.valueProviderLabel(), 100, "Name");
		employeeName.setCell(new ToolTipTextCell());
		employeeColumns.add(employeeName);
		ColumnConfig<EmployeeDTO, String> securityAssesment = new ColumnConfig<EmployeeDTO, String>(
		        employeeProps.calculatedSecurityAssesmentName(), 100, "Schutzbedarf");
		employeeColumns.add(securityAssesment);
		ColumnConfig<EmployeeDTO, String> assignedSecurityZone = new ColumnConfig<EmployeeDTO, String>(
		        employeeProps.assignedSecurityzoneName(), 100, "Zugewiesene Schutzzone");
		employeeColumns.add(assignedSecurityZone);
		buildView(new ColumnModel<EmployeeDTO>(employeeColumns));
    }
	public EmployeeGridList(ListStore<EmployeeDTO> store) {
		this(store,(EmployeeDTOProperties) GWT.create(EmployeeDTOProperties.class));
	}
	
	@Override
	protected CellDoubleClickHandler getCellDoubleClickHandler() {
		return new CellDoubleClickHandler() {

			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				EmployeeDTO selectedItem = grid.getSelectionModel().getSelectedItem();
				new EmployeeEditorWindow(selectedItem);
			}
		};
	}
	

}
