package de.gsv.idm.client.view.informationdomain.assettype.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
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
import de.gsv.idm.client.view.properties.AssettypeMeasureLinkDTOProperties;
import de.gsv.idm.client.view.properties.EmployeeDTOProperties;
import de.gsv.idm.client.view.widgets.cell.ToolTipTextCell;
import de.gsv.idm.client.view.widgets.form.GridList;
import de.gsv.idm.client.view.widgets.form.MeasureStatusAssesmentBoxCell;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.comperator.GSKComperator;
import de.gsv.idm.shared.dto.AssettypeMeasureLinkDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.MeasureDTO;

public class AssettypeMeasureLinkGridList extends GridList<AssettypeMeasureLinkDTO> {

	ColumnConfig<AssettypeMeasureLinkDTO, Boolean> inherit;
	List<ColumnConfig<AssettypeMeasureLinkDTO, ?>> columns;

	public AssettypeMeasureLinkGridList(final ListStore<AssettypeMeasureLinkDTO> store,
	        ListStore<EmployeeDTO> employeeStore,
	        AssettypeMeasureLinkDTOProperties assetMeasureLinkProps) {
		super(store, assetMeasureLinkProps.valueProviderLabel());
		store.getSortInfo().clear();
		store.addSortInfo(new StoreSortInfo<AssettypeMeasureLinkDTO>(assetMeasureLinkProps.name(),
		        new GSKComperator(), SortDir.ASC));
		EmployeeDTOProperties employeeProps = GWT.create(EmployeeDTOProperties.class);
		columns = new ArrayList<ColumnConfig<AssettypeMeasureLinkDTO, ?>>();
		ColumnConfig<AssettypeMeasureLinkDTO, String> name = new ColumnConfig<AssettypeMeasureLinkDTO, String>(
		        assetMeasureLinkProps.name(), 100, "Name");
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		builder.appendEscaped("Name");
		name.setToolTip(builder.toSafeHtml());
		name.setCell(new ToolTipTextCell());
		columns.add(name);
		ColumnConfig<AssettypeMeasureLinkDTO, String> category = new ColumnConfig<AssettypeMeasureLinkDTO, String>(
		        assetMeasureLinkProps.category(), 10, "Kat.");
		builder = new SafeHtmlBuilder();
		builder.appendEscaped("Kategorie");
		category.setToolTip(builder.toSafeHtml());
		columns.add(category);
		ColumnConfig<AssettypeMeasureLinkDTO, EmployeeDTO> responsible = new ColumnConfig<AssettypeMeasureLinkDTO, EmployeeDTO>(
		        assetMeasureLinkProps.responsiblePerson(), 100, "Verantwortlicher");
		ComboBoxCell<EmployeeDTO> responsiplePersonCombo = new ComboBoxCell<EmployeeDTO>(
		        employeeStore, employeeProps.fullNameLabel());
		responsiplePersonCombo.setAllowBlank(true);
		responsiplePersonCombo.setTriggerAction(TriggerAction.ALL);
		responsible.setCell(responsiplePersonCombo);
		columns.add(responsible);

		ColumnConfig<AssettypeMeasureLinkDTO, String> status = new ColumnConfig<AssettypeMeasureLinkDTO, String>(
		        assetMeasureLinkProps.status(), 70, "Umsetzungsgrad");
		status.setCell(new MeasureStatusAssesmentBoxCell());
		columns.add(status);
		ColumnConfig<AssettypeMeasureLinkDTO, String> cost = new ColumnConfig<AssettypeMeasureLinkDTO, String>(
		        assetMeasureLinkProps.cost(), 70, "Kosten");
		TextInputCell costCell = new TextInputCell();
		cost.setCell(costCell);
		columns.add(cost);
		inherit = new ColumnConfig<AssettypeMeasureLinkDTO, Boolean>(
		        assetMeasureLinkProps.passOn(), 30, "Vererbung");
		CheckBoxCell markedAsDeletedCheckBock = new CheckBoxCell();
		inherit.setCell(markedAsDeletedCheckBock);
		columns.add(inherit);
		buildView(new ColumnModel<AssettypeMeasureLinkDTO>(columns));

		setText("Maßnahmen-Verknüpfungen");
	}

	public AssettypeMeasureLinkGridList(final ListStore<AssettypeMeasureLinkDTO> store,
	        ListStore<EmployeeDTO> employeeStore) {
		this(store, employeeStore, (AssettypeMeasureLinkDTOProperties) GWT
		        .create(AssettypeMeasureLinkDTOProperties.class));
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

	public void setInheritToolTip(String text) {
		SafeHtmlBuilder inheritBuilder = new SafeHtmlBuilder();
		inheritBuilder.appendEscaped(text);
		inherit.setToolTip(inheritBuilder.toSafeHtml());
		grid.reconfigure(store, new ColumnModel<AssettypeMeasureLinkDTO>(columns));
	}
}
