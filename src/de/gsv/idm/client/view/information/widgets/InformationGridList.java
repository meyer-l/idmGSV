package de.gsv.idm.client.view.information.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import de.gsv.idm.client.view.information.InformationEditorWindow;
import de.gsv.idm.client.view.properties.InformationDTOProperties;
import de.gsv.idm.client.view.widgets.cell.ToolTipTextCell;
import de.gsv.idm.client.view.widgets.form.GridList;
import de.gsv.idm.shared.dto.InformationDTO;

public class InformationGridList extends GridList<InformationDTO> {

	public InformationGridList(ListStore<InformationDTO> store, InformationDTOProperties props) {
		super(store, props.valueProviderLabel());
		
		List<ColumnConfig<InformationDTO, ?>> columns = new ArrayList<ColumnConfig<InformationDTO, ?>>();
		ColumnConfig<InformationDTO, String> name = new ColumnConfig<InformationDTO, String>(
		        props.name(), 100, "Name");
		name.setCell(new ToolTipTextCell());
		columns.add(name);
		ColumnConfig<InformationDTO, String> availabilityColumn = new ColumnConfig<InformationDTO, String>(
		        props.availabilityName(), 100, "Verfügbarkeit");
		columns.add(availabilityColumn);
		ColumnConfig<InformationDTO, String> confidentialityColumn = new ColumnConfig<InformationDTO, String>(
		        props.confidentialityName(), 100, "Vertraulichkeit");
		columns.add(confidentialityColumn);
		ColumnConfig<InformationDTO, String> integrityColumn = new ColumnConfig<InformationDTO, String>(
		        props.integrityName(), 100, "Integrität");
		columns.add(integrityColumn);
		ColumnConfig<InformationDTO, String> securityzoneColumn = new ColumnConfig<InformationDTO, String>(
		        props.securityzoneName(), 100, "Schutzzone");
		columns.add(securityzoneColumn);
		buildView(new ColumnModel<InformationDTO>(columns));
	}

	public InformationGridList(ListStore<InformationDTO> store) {
		this(store, (InformationDTOProperties) GWT.create(InformationDTOProperties.class));
	}
	
	@Override
	protected CellDoubleClickHandler getCellDoubleClickHandler() {
		return new CellDoubleClickHandler() {

			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				InformationDTO selectedItem = grid.getSelectionModel().getSelectedItem();
				new InformationEditorWindow(selectedItem.getDomainId(), selectedItem);
			}
		};
	}

}
