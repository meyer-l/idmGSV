package de.gsv.idm.client.view.securityzone.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

import de.gsv.idm.client.presenter.securityzone.SecurityzoneMeasuresDetailsPresenter.SecurityzoneMeasuresDetailsDisplay;
import de.gsv.idm.client.view.properties.SecurityzoneMeasureStatusObjectProperties;
import de.gsv.idm.client.view.securityzone.widgets.data.SecurityzoneMeasureStatusObject;

public class SecurityzoneMeasuresDetailsView implements SecurityzoneMeasuresDetailsDisplay {

	BorderLayoutContainer panel;
	
	public SecurityzoneMeasuresDetailsView(ListStore<SecurityzoneMeasureStatusObject> store){
		panel = new BorderLayoutContainer();
		SecurityzoneMeasureStatusObjectProperties property = GWT
		        .create(SecurityzoneMeasureStatusObjectProperties.class);
		List<ColumnConfig<SecurityzoneMeasureStatusObject, ?>> columnModel = new ArrayList<ColumnConfig<SecurityzoneMeasureStatusObject, ?>>();
		ColumnConfig<SecurityzoneMeasureStatusObject, String> name = new ColumnConfig<SecurityzoneMeasureStatusObject, String>(
		        property.name(), 100, "Name");
		columnModel.add(name);
		ColumnConfig<SecurityzoneMeasureStatusObject, String> status = new ColumnConfig<SecurityzoneMeasureStatusObject, String>(
		        property.status(), 100, "status");
		columnModel.add(status);
		ColumnConfig<SecurityzoneMeasureStatusObject, Boolean> inherited = new ColumnConfig<SecurityzoneMeasureStatusObject, Boolean>(
		        property.inherited(), 100, "Geerbt");
		CheckBoxCell inheritedCheckBox = new CheckBoxCell();
		inheritedCheckBox.disable(null);
		inherited.setCell(inheritedCheckBox);
		columnModel.add(inherited);
		Grid<SecurityzoneMeasureStatusObject> grid = new Grid<SecurityzoneMeasureStatusObject>(store, new ColumnModel<SecurityzoneMeasureStatusObject>(columnModel));
		grid.setBorders(true);
		grid.getView().setForceFit(true);
		panel.add(grid);
	}
	
	@Override
    public Widget asWidget() {
		
	    return panel;
    }

}
