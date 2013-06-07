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

import de.gsv.idm.client.presenter.gsk.ModuleEditorPresenter;
import de.gsv.idm.client.view.gsk.ModuleEditor;
import de.gsv.idm.client.view.properties.ModuleDTOProperties;
import de.gsv.idm.client.view.widgets.cell.ToolTipTextCell;
import de.gsv.idm.client.view.widgets.form.GridList;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.comperator.GSKComperator;
import de.gsv.idm.shared.dto.ModuleDTO;

public class ModuleGridList extends GridList<ModuleDTO> {

	public ModuleGridList(ListStore<ModuleDTO> store, ModuleDTOProperties moduleProps) {
		super(store, moduleProps.valueProviderLabel());
		store.getSortInfo().clear();
		store.addSortInfo(new StoreSortInfo<ModuleDTO>(moduleProps.name(), new GSKComperator(),
		        SortDir.ASC));
		List<ColumnConfig<ModuleDTO, ?>> moduleAddColumns = new ArrayList<ColumnConfig<ModuleDTO, ?>>();
		ColumnConfig<ModuleDTO, String> moduleName = new ColumnConfig<ModuleDTO, String>(
		        moduleProps.name(), 100, "Name");
		moduleName.setCell(new ToolTipTextCell());
		moduleAddColumns.add(moduleName);
		buildView(new ColumnModel<ModuleDTO>(moduleAddColumns));
	}

	public ModuleGridList(ListStore<ModuleDTO> store) {
		this(store, (ModuleDTOProperties) GWT.create(ModuleDTOProperties.class));
	}
	
	@Override
	protected CellDoubleClickHandler getCellDoubleClickHandler() {
		return new CellDoubleClickHandler() {

			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				ModuleDTO selectedItem = grid.getSelectionModel().getSelectedItem();
				new EditorWindow<ModuleDTO>(new ModuleEditorPresenter(new ModuleEditor()),
				       selectedItem, "Modul berarbeiten");
			}
		};
	}
}
