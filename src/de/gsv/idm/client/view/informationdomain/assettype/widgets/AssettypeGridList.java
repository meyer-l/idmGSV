package de.gsv.idm.client.view.informationdomain.assettype.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import de.gsv.idm.client.presenter.informationdomain.assettype.AssettypeEditorPresenter;
import de.gsv.idm.client.view.informationdomain.assettype.AssettypeEditor;
import de.gsv.idm.client.view.properties.AssettypeDTOProperties;
import de.gsv.idm.client.view.widgets.form.GridList;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.dto.AssettypeDTO;

public class AssettypeGridList extends GridList<AssettypeDTO> {

	public AssettypeGridList(ListStore<AssettypeDTO> store) {
		this(store, (AssettypeDTOProperties) GWT.create(AssettypeDTOProperties.class));
	}

	public AssettypeGridList(ListStore<AssettypeDTO> store, AssettypeDTOProperties property) {
		super(store, property.valueProviderLabel());

		List<ColumnConfig<AssettypeDTO, ?>> columns = new ArrayList<ColumnConfig<AssettypeDTO, ?>>();
		ColumnConfig<AssettypeDTO, String> name = new ColumnConfig<AssettypeDTO, String>(
		        property.name(), 100, "Name");
		columns.add(name);
		buildView(new ColumnModel<AssettypeDTO>(columns));
		grid.setHideHeaders(true);
	}

	@Override
	protected CellDoubleClickHandler getCellDoubleClickHandler() {
		return new CellDoubleClickHandler() {

			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				AssettypeDTO selectedItem = grid.getSelectionModel().getSelectedItem();
				new EditorWindow<AssettypeDTO>(new AssettypeEditorPresenter(new AssettypeEditor(),
				        selectedItem.getDomainId()), selectedItem, "Asset-Grundtyp berarbeiten");
			}
		};
	}

}
