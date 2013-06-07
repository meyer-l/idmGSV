package de.gsv.idm.client.view.informationdomain.asset.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.cell.core.client.form.TextInputCell;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import de.gsv.idm.client.presenter.gsk.MeasureEditorPresenter;
import de.gsv.idm.client.view.gsk.MeasureEditor;
import de.gsv.idm.client.view.properties.AssetMeasureLinkDTOProperties;
import de.gsv.idm.client.view.properties.EmployeeDTOProperties;
import de.gsv.idm.client.view.widgets.cell.ToolTipTextCell;
import de.gsv.idm.client.view.widgets.form.GridList;
import de.gsv.idm.client.view.widgets.form.MeasureStatusAssesmentBoxCell;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.comperator.GSKComperator;
import de.gsv.idm.shared.dto.AssetMeasureLinkDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.MeasureDTO;

public class AssetMeasureLinkGridList extends GridList<AssetMeasureLinkDTO> {
	public AssetMeasureLinkGridList(ListStore<AssetMeasureLinkDTO> store,
	        ListStore<EmployeeDTO> employeeStore,
	        AssetMeasureLinkDTOProperties assetMeasureLinkProps) {
		super(store, assetMeasureLinkProps.valueProviderLabel());
		store.getSortInfo().clear();
		store.addSortInfo(new StoreSortInfo<AssetMeasureLinkDTO>(assetMeasureLinkProps.name(), new GSKComperator(),
		        SortDir.ASC));	
		EmployeeDTOProperties employeeProps = GWT.create(EmployeeDTOProperties.class);
		List<ColumnConfig<AssetMeasureLinkDTO, ?>> columns = new ArrayList<ColumnConfig<AssetMeasureLinkDTO, ?>>();
		ColumnConfig<AssetMeasureLinkDTO, String> name = new ColumnConfig<AssetMeasureLinkDTO, String>(
		        assetMeasureLinkProps.name(), 100, "Name");
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		builder.appendEscaped("Name");
		name.setToolTip(builder.toSafeHtml());
		name.setCell(new ToolTipTextCell());
		columns.add(name);
		ColumnConfig<AssetMeasureLinkDTO, String> category = new ColumnConfig<AssetMeasureLinkDTO, String>(
		        assetMeasureLinkProps.category(), 10, "Kat.");
		builder = new SafeHtmlBuilder();
		builder.appendEscaped("Kategorie");
		category.setToolTip(builder.toSafeHtml());
		columns.add(category);
		ColumnConfig<AssetMeasureLinkDTO, EmployeeDTO> responsible = new ColumnConfig<AssetMeasureLinkDTO, EmployeeDTO>(
		        assetMeasureLinkProps.responsiblePerson(), 100, "Verantwortlicher");
		ComboBoxCell<EmployeeDTO> responsiplePersonCombo = new ComboBoxCell<EmployeeDTO>(
		        employeeStore, employeeProps.fullNameLabel());
		responsiplePersonCombo.setAllowBlank(true);
		responsiplePersonCombo.setTriggerAction(TriggerAction.ALL);
		responsible.setCell(responsiplePersonCombo);
		columns.add(responsible);
		ColumnConfig<AssetMeasureLinkDTO, String> status = new ColumnConfig<AssetMeasureLinkDTO, String>(
		        assetMeasureLinkProps.status(), 100, "Umsetzungsgrad");
		status.setCell(new MeasureStatusAssesmentBoxCell());
		columns.add(status);
		ColumnConfig<AssetMeasureLinkDTO, String> cost = new ColumnConfig<AssetMeasureLinkDTO, String>(
		        assetMeasureLinkProps.cost(), 100, "Kosten");
		TextInputCell costCell = new TextInputCell();
		cost.setCell(costCell);
		columns.add(cost);
		buildView(new ColumnModel<AssetMeasureLinkDTO>(columns));
		setText("Maßnahmen-Verknüpfungen");
	}
	
	public AssetMeasureLinkGridList(final ListStore<AssetMeasureLinkDTO> store,
	        ListStore<EmployeeDTO> employeeStore) {
		this(store, employeeStore, (AssetMeasureLinkDTOProperties) GWT.create(AssetMeasureLinkDTOProperties.class));
	}
	
	@Override
	protected CellDoubleClickHandler getCellDoubleClickHandler() {
		return new CellDoubleClickHandler() {

			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				MeasureDTO selectedItem = grid.getSelectionModel().getSelectedItem().getMeasure();
				new EditorWindow<MeasureDTO>(new MeasureEditorPresenter(new MeasureEditor()),
				        selectedItem, "Maßnahme berarbeiten");
			}
		};
	}

}
