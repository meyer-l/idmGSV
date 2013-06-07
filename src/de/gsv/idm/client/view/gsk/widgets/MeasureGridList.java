package de.gsv.idm.client.view.gsk.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import de.gsv.idm.client.presenter.gsk.MeasureEditorPresenter;
import de.gsv.idm.client.view.gsk.MeasureEditor;
import de.gsv.idm.client.view.properties.MeasureDTOProperties;
import de.gsv.idm.client.view.widgets.cell.ToolTipTextCell;
import de.gsv.idm.client.view.widgets.form.GridList;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.comperator.GSKComperator;
import de.gsv.idm.shared.dto.MeasureDTO;

public class MeasureGridList extends GridList<MeasureDTO> {

	public MeasureGridList(ListStore<MeasureDTO> store, MeasureDTOProperties measureProps) {
		super(store, measureProps.valueProviderLabel());
		store.getSortInfo().clear();
		store.addSortInfo(new StoreSortInfo<MeasureDTO>(measureProps.name(), new GSKComperator(),
		        SortDir.ASC));
		List<ColumnConfig<MeasureDTO, ?>> measureColumns = new ArrayList<ColumnConfig<MeasureDTO, ?>>();
		ColumnConfig<MeasureDTO, String> measureName = new ColumnConfig<MeasureDTO, String>(
		        measureProps.name(), 100, "Name");
		measureName.setCell(new ToolTipTextCell());
		measureColumns.add(measureName);
		buildView(new ColumnModel<MeasureDTO>(measureColumns));
	}

	public MeasureGridList(ListStore<MeasureDTO> store) {
		this(store, (MeasureDTOProperties) GWT.create(MeasureDTOProperties.class));
	}

	@Override
	protected CellDoubleClickHandler getCellDoubleClickHandler() {
		return new CellDoubleClickHandler() {

			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				MeasureDTO selectedItem = grid.getSelectionModel().getSelectedItem();
				new EditorWindow<MeasureDTO>(new MeasureEditorPresenter(new MeasureEditor()),
				        selectedItem, "Maßnahme berarbeiten");
			}
		};
	}

}
