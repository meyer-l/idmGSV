package de.gsv.idm.client.view.securityzone.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import de.gsv.idm.client.view.properties.SecurityzoneDTOProperties;
import de.gsv.idm.client.view.securityzone.SecurityzoneEditorWindow;
import de.gsv.idm.client.view.widgets.cell.ToolTipTextCell;
import de.gsv.idm.client.view.widgets.form.GridList;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

public class SecurityzoneGridList extends GridList<SecurityzoneDTO> {

	public SecurityzoneGridList(ListStore<SecurityzoneDTO> store, SecurityzoneDTOProperties property) {
		super(store, property.name());
		List<ColumnConfig<SecurityzoneDTO, ?>> columns = new ArrayList<ColumnConfig<SecurityzoneDTO, ?>>();
		ColumnConfig<SecurityzoneDTO, String> name = new ColumnConfig<SecurityzoneDTO, String>(
		        property.valueProviderLabel(), 100, "Name");
		name.setCell(new ToolTipTextCell());
		columns.add(name);
		buildView(new ColumnModel<SecurityzoneDTO>(columns));
		
		setText("Schutzzonen-Verknüpfungen");
	}

	public SecurityzoneGridList(ListStore<SecurityzoneDTO> store) {
		this(store, (SecurityzoneDTOProperties) GWT.create(SecurityzoneDTOProperties.class));
	}
	
	@Override
	protected CellDoubleClickHandler getCellDoubleClickHandler() {
		return new CellDoubleClickHandler() {

			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				SecurityzoneDTO selectedItem = grid.getSelectionModel().getSelectedItem();
				new SecurityzoneEditorWindow(selectedItem, "Schutzzone berarbeiten");
			}
		};
	}

}
