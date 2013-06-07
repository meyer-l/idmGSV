package de.gsv.idm.client.view.occupation.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import de.gsv.idm.client.view.occupation.OccupationEditorWindow;
import de.gsv.idm.client.view.properties.OccupationDTOProperties;
import de.gsv.idm.client.view.widgets.cell.ToolTipTextCell;
import de.gsv.idm.client.view.widgets.form.GridList;
import de.gsv.idm.shared.dto.OccupationDTO;

public class OccupationGridList extends GridList<OccupationDTO> {

	public OccupationGridList(ListStore<OccupationDTO> store,
	        OccupationDTOProperties occupationProps) {
		super(store, occupationProps.valueProviderLabel());

		List<ColumnConfig<OccupationDTO, ?>> occupationColumns = new ArrayList<ColumnConfig<OccupationDTO, ?>>();
		ColumnConfig<OccupationDTO, String> occupationName = new ColumnConfig<OccupationDTO, String>(
		        occupationProps.valueProviderLabel(), 100, "Name");
		occupationName.setCell(new ToolTipTextCell());
		occupationColumns.add(occupationName);
		ColumnConfig<OccupationDTO, String> occupationAvailabilityColumn = new ColumnConfig<OccupationDTO, String>(
		        occupationProps.calculatedAvailabilityName(), 100, "Verfügbarkeit");
		occupationColumns.add(occupationAvailabilityColumn);
		ColumnConfig<OccupationDTO, String> occupationConfidentialityColumn = new ColumnConfig<OccupationDTO, String>(
		        occupationProps.calculatedConfidentialityName(), 100, "Vertraulichkeit");
		occupationColumns.add(occupationConfidentialityColumn);
		ColumnConfig<OccupationDTO, String> occupationIntegrityColumn = new ColumnConfig<OccupationDTO, String>(
		        occupationProps.calculatedIntegrityName(), 100, "Integrität");
		occupationColumns.add(occupationIntegrityColumn);
		ColumnConfig<OccupationDTO, String> securityzoneColumn = new ColumnConfig<OccupationDTO, String>(
				occupationProps.assignedSecurityzoneName(), 100, "Schutzzone");
		occupationColumns.add(securityzoneColumn);
		buildView(new ColumnModel<OccupationDTO>(occupationColumns));
	}

	public OccupationGridList(ListStore<OccupationDTO> store) {
		this(store, (OccupationDTOProperties) GWT.create(OccupationDTOProperties.class));
	}
	
	@Override
	protected CellDoubleClickHandler getCellDoubleClickHandler() {
		return new CellDoubleClickHandler() {

			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				OccupationDTO selectedItem = grid.getSelectionModel().getSelectedItem();
				new OccupationEditorWindow(selectedItem.getDomainId(), selectedItem);
			}
		};
	}
}
