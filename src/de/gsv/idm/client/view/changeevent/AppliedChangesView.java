package de.gsv.idm.client.view.changeevent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import de.gsv.idm.client.presenter.changeevent.AppliedChangesPresenter.AppliedChangesDisplay;
import de.gsv.idm.client.view.general.GeneralGridView;
import de.gsv.idm.client.view.properties.ChangeEventDTOProperties;
import de.gsv.idm.shared.dto.ChangeEventDTO;

public class AppliedChangesView extends GeneralGridView<ChangeEventDTO> implements AppliedChangesDisplay {

	public AppliedChangesView() {		
		this((ChangeEventDTOProperties) GWT.create(ChangeEventDTOProperties.class));
	}

	public AppliedChangesView(ChangeEventDTOProperties create) {
		this(create, new StoreSortInfo<ChangeEventDTO>(create.date(),SortDir.DESC));
	}

	public AppliedChangesView(ChangeEventDTOProperties properties,
	        StoreSortInfo<ChangeEventDTO> sortInfo) {
		super(properties, sortInfo);
	}

	@Override
	protected ColumnModel<ChangeEventDTO> getColumnModel() {
		ChangeEventDTOProperties properties = GWT.create(ChangeEventDTOProperties.class);
		List<ColumnConfig<ChangeEventDTO, ?>> cfgs = new ArrayList<ColumnConfig<ChangeEventDTO, ?>>();
		
		ColumnConfig<ChangeEventDTO, String> name = new ColumnConfig<ChangeEventDTO, String>(
		        properties.name(), 150);
		name.setHeader("Änderungsart");
		cfgs.add(name);

		ColumnConfig<ChangeEventDTO, String> employee = new ColumnConfig<ChangeEventDTO, String>(
		        properties.employeeName(), 100);
		employee.setHeader("Mitarbeiter");
		cfgs.add(employee);

		ColumnConfig<ChangeEventDTO, String> oldValue = new ColumnConfig<ChangeEventDTO, String>(
		        properties.oldValue(), 300);
		oldValue.setHeader("Aktueller Stand");
		cfgs.add(oldValue);

		ColumnConfig<ChangeEventDTO, String> newValue = new ColumnConfig<ChangeEventDTO, String>(
		        properties.newValue(), 300);
		newValue.setHeader("Eingelesener Stand ");
		cfgs.add(newValue);

		ColumnConfig<ChangeEventDTO, Date> date = new ColumnConfig<ChangeEventDTO, Date>(
		        properties.date(), 70);
		date.setHeader("Datum");
		date.setCell(new DateCell(DateTimeFormat.getFormat("dd.MM.yyyy")));
		cfgs.add(date);
		return new ColumnModel<ChangeEventDTO>(cfgs);
	}

}
