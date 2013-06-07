package de.gsv.idm.client.view.informationdomain.asset.widgets;

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
import de.gsv.idm.client.view.properties.AssetAssettypeModuleLinkDTOProperties;
import de.gsv.idm.client.view.properties.EmployeeDTOProperties;
import de.gsv.idm.client.view.widgets.cell.ToolTipTextCell;
import de.gsv.idm.client.view.widgets.form.GridList;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.comperator.GSKComperator;
import de.gsv.idm.shared.dto.AssetModuleLinkDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.ModuleDTO;

public class AssetModuleLinkGridList extends GridList<AssetModuleLinkDTO> {

	public AssetModuleLinkGridList(ListStore<AssetModuleLinkDTO> store,
	        ListStore<EmployeeDTO> employeeStore,
	        AssetAssettypeModuleLinkDTOProperties assetModuleLinkProps) {
		super(store, assetModuleLinkProps.valueProviderLabel());
		store.getSortInfo().clear();
		store.addSortInfo(new StoreSortInfo<AssetModuleLinkDTO>(assetModuleLinkProps.name(), new GSKComperator(),
		        SortDir.ASC));
		EmployeeDTOProperties employeeProps = GWT.create(EmployeeDTOProperties.class);
		List<ColumnConfig<AssetModuleLinkDTO, ?>> columns = new ArrayList<ColumnConfig<AssetModuleLinkDTO, ?>>();
		ColumnConfig<AssetModuleLinkDTO, String> name = new ColumnConfig<AssetModuleLinkDTO, String>(
		        assetModuleLinkProps.name(), 100, "Name");
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		builder.appendEscaped("Name");
		name.setToolTip(builder.toSafeHtml());
		name.setCell(new ToolTipTextCell());
		columns.add(name);
		ColumnConfig<AssetModuleLinkDTO, EmployeeDTO> responsible = new ColumnConfig<AssetModuleLinkDTO, EmployeeDTO>(
		        assetModuleLinkProps.responsiblePerson(), 100, "Verantwortlicher");
		ComboBoxCell<EmployeeDTO> responsiplePersonCombo = new ComboBoxCell<EmployeeDTO>(
		        employeeStore, employeeProps.fullNameLabel());
		responsiplePersonCombo.setAllowBlank(true);
		responsiplePersonCombo.setTriggerAction(TriggerAction.ALL);
		responsible.setCell(responsiplePersonCombo);
		columns.add(responsible);
		buildView(new ColumnModel<AssetModuleLinkDTO>(columns));
		setText("Baustein-Verknüpfungen");
		setAddButtonVisibility(false);
	}

	public AssetModuleLinkGridList(ListStore<AssetModuleLinkDTO> store,
	        ListStore<EmployeeDTO> employeeStore) {
		this(store, employeeStore, (AssetAssettypeModuleLinkDTOProperties) GWT
		        .create(AssetAssettypeModuleLinkDTOProperties.class));
	}
	
	@Override
	protected CellDoubleClickHandler getCellDoubleClickHandler() {
		return new CellDoubleClickHandler() {

			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				ModuleDTO selectedItem = grid.getSelectionModel().getSelectedItem().getAssettypeModuleLink().getModule();
				new EditorWindow<ModuleDTO>(new ModuleEditorPresenter(new ModuleEditor()),
				       selectedItem, "Modul berarbeiten");
			}
		};
	}

}
