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

import de.gsv.idm.client.presenter.gsk.ThreatEditorPresenter;
import de.gsv.idm.client.view.gsk.ThreatEditor;
import de.gsv.idm.client.view.properties.ThreatDTOProperties;
import de.gsv.idm.client.view.widgets.cell.ToolTipTextCell;
import de.gsv.idm.client.view.widgets.form.GridList;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.comperator.GSKComperator;
import de.gsv.idm.shared.dto.ThreatDTO;

public class ThreatGridList extends GridList<ThreatDTO> {

	public ThreatGridList(ListStore<ThreatDTO> store, ThreatDTOProperties threatProps) {
		super(store, threatProps.valueProviderLabel());
		store.getSortInfo().clear();
		store.addSortInfo(new StoreSortInfo<ThreatDTO>(threatProps.name(), new GSKComperator(),
		        SortDir.ASC));
		List<ColumnConfig<ThreatDTO, ?>> columns = new ArrayList<ColumnConfig<ThreatDTO, ?>>();
		ColumnConfig<ThreatDTO, String> name = new ColumnConfig<ThreatDTO, String>(
		        threatProps.name(), 100, "Name");
		name.setCell(new ToolTipTextCell());
		columns.add(name);
		buildView(new ColumnModel<ThreatDTO>(columns));
	}

	public ThreatGridList(ListStore<ThreatDTO> store) {
		this(store, (ThreatDTOProperties) GWT.create(ThreatDTOProperties.class));
	}
	
	@Override
	protected CellDoubleClickHandler getCellDoubleClickHandler() {
		return new CellDoubleClickHandler() {

			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				ThreatDTO selectedItem = grid.getSelectionModel().getSelectedItem();
				new EditorWindow<ThreatDTO>(new ThreatEditorPresenter(new ThreatEditor()),
				        selectedItem, "Maßnahme berarbeiten");
			}
		};
	}
}
