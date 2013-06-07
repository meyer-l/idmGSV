package de.gsv.idm.client.view.informationdomain.assettype.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import de.gsv.idm.client.presenter.gsk.ModuleEditorPresenter;
import de.gsv.idm.client.view.gsk.ModuleEditor;
import de.gsv.idm.client.view.properties.AssettypeModuleLinkDTOProperties;
import de.gsv.idm.client.view.properties.EmployeeDTOProperties;
import de.gsv.idm.client.view.widgets.cell.ToolTipTextCell;
import de.gsv.idm.client.view.widgets.form.GridList;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.comperator.GSKComperator;
import de.gsv.idm.shared.dto.AssettypeModuleLinkDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.ModuleDTO;

public class AssettypeModuleLinkGridList extends GridList<AssettypeModuleLinkDTO> {

	public AssettypeModuleLinkGridList(ListStore<AssettypeModuleLinkDTO> store,
	        ListStore<EmployeeDTO> employeeStore, AssettypeModuleLinkDTOProperties assetModuleLinkProps) {
		super(store, assetModuleLinkProps.valueProviderLabel());
		
		store.getSortInfo().clear();
		store.addSortInfo(new StoreSortInfo<AssettypeModuleLinkDTO>(assetModuleLinkProps.name(), new GSKComperator(),
		        SortDir.ASC));		
		
		EmployeeDTOProperties employeeProps = GWT.create(EmployeeDTOProperties.class);
		List<ColumnConfig<AssettypeModuleLinkDTO, ?>> columns = new ArrayList<ColumnConfig<AssettypeModuleLinkDTO, ?>>();
		ColumnConfig<AssettypeModuleLinkDTO, String> name = new ColumnConfig<AssettypeModuleLinkDTO, String>(
		        assetModuleLinkProps.name(), 100, "Name");
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		builder.appendEscaped("Name");
		name.setToolTip(builder.toSafeHtml());
		name.setCell(new ToolTipTextCell());
		columns.add(name);
		ColumnConfig<AssettypeModuleLinkDTO, EmployeeDTO> responsible = new ColumnConfig<AssettypeModuleLinkDTO, EmployeeDTO>(
		        assetModuleLinkProps.responsiblePerson(), 100, "Verantwortlicher");
		ComboBoxCell<EmployeeDTO> responsiplePersonCombo = new ComboBoxCell<EmployeeDTO>(
		        employeeStore, employeeProps.fullNameLabel());
		responsiplePersonCombo.setAllowBlank(true);
		responsiplePersonCombo.setTriggerAction(TriggerAction.ALL);
		responsible.setCell(responsiplePersonCombo);
		columns.add(responsible);
		buildView(new ColumnModel<AssettypeModuleLinkDTO>(columns));
		setText("Baustein-Verknüpfungen");
	}

	public AssettypeModuleLinkGridList(ListStore<AssettypeModuleLinkDTO> store,
	        ListStore<EmployeeDTO> employeeStore) {
		this(store, employeeStore, (AssettypeModuleLinkDTOProperties) GWT
		        .create(AssettypeModuleLinkDTOProperties.class));
	}
	
	@Override
	protected CellDoubleClickHandler getCellDoubleClickHandler() {
		return new CellDoubleClickHandler() {

			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				ModuleDTO selectedItem = grid.getSelectionModel().getSelectedItem().getModule();
				new EditorWindow<ModuleDTO>(new ModuleEditorPresenter(new ModuleEditor()),
				       selectedItem, "Modul berarbeiten");
			}
		};
	}

}
