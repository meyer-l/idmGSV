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
import de.gsv.idm.client.view.properties.MeasureLinkDTOProperties;
import de.gsv.idm.client.view.widgets.form.GridList;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.comperator.GSKComperator;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.MeasureLinkDTO;

public class MeasureLinkGridList extends GridList<MeasureLinkDTO> {

	public MeasureLinkGridList(ListStore<MeasureLinkDTO> store,
	        MeasureLinkDTOProperties measureLinkProps) {
		super(store, measureLinkProps.valueProviderLabel());
		
		store.getSortInfo().clear();
		store.addSortInfo(new StoreSortInfo<MeasureLinkDTO>(measureLinkProps.name(), new GSKComperator(),
		        SortDir.ASC));		
		
		List<ColumnConfig<MeasureLinkDTO, ?>> measureLinkColumns = new ArrayList<ColumnConfig<MeasureLinkDTO, ?>>();
		ColumnConfig<MeasureLinkDTO, String> measureLinkName = new ColumnConfig<MeasureLinkDTO, String>(
		        measureLinkProps.name(), 100, "Name");
		measureLinkColumns.add(measureLinkName);
		ColumnConfig<MeasureLinkDTO, String> measureCategory = new ColumnConfig<MeasureLinkDTO, String>(
		        measureLinkProps.category(), 100, "Kategorie");
		measureLinkColumns.add(measureCategory);
		buildView(new ColumnModel<MeasureLinkDTO>(measureLinkColumns));
	}

	public MeasureLinkGridList(ListStore<MeasureLinkDTO> store) {
		this(store, (MeasureLinkDTOProperties) GWT.create(MeasureLinkDTOProperties.class));
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
