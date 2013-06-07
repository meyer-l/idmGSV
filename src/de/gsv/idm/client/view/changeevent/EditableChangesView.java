package de.gsv.idm.client.view.changeevent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import de.gsv.idm.client.presenter.changeevent.EditableChangesPresenter.EditableChangesDisplay;
import de.gsv.idm.client.view.general.GeneralEditGridView;
import de.gsv.idm.client.view.properties.ChangeEventDTOProperties;
import de.gsv.idm.client.view.properties.HasKeyAndName;
import de.gsv.idm.client.view.widgets.cell.changeevent.AutomaticCheckBoxCell;
import de.gsv.idm.client.view.widgets.cell.changeevent.ManualCheckBoxCell;
import de.gsv.idm.client.view.widgets.cell.changeevent.NotNeededCheckBoxCell;
import de.gsv.idm.shared.dto.ChangeEventDTO;

public abstract class EditableChangesView extends GeneralEditGridView<ChangeEventDTO> implements
        EditableChangesDisplay {

	protected ColumnConfig<ChangeEventDTO, ChangeEventDTO> automatic;
	protected ColumnConfig<ChangeEventDTO, ChangeEventDTO> manual;
	protected ColumnConfig<ChangeEventDTO, ChangeEventDTO> notNeeded;

	public EditableChangesView(HasKeyAndName<ChangeEventDTO> properties,
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

		automatic = new ColumnConfig<ChangeEventDTO, ChangeEventDTO>(properties.instance(), 80);
		automatic.setHeader("Automatisch");
		AutomaticCheckBoxCell automaticCell = new AutomaticCheckBoxCell();
		automatic.setCell(automaticCell);
		cfgs.add(automatic);
		manual = new ColumnConfig<ChangeEventDTO, ChangeEventDTO>(properties.instance(), 80);
		manual.setHeader("Manuell");
		ManualCheckBoxCell manualCell = new ManualCheckBoxCell();
		manual.setCell(manualCell);
		cfgs.add(manual);

		notNeeded = new ColumnConfig<ChangeEventDTO, ChangeEventDTO>(properties.instance(), 80);
		notNeeded.setHeader("Nicht benötigt");
		NotNeededCheckBoxCell notNeededCell = new NotNeededCheckBoxCell();
		notNeeded.setCell(notNeededCell);
		cfgs.add(notNeeded);

		ColumnConfig<ChangeEventDTO, Date> date = new ColumnConfig<ChangeEventDTO, Date>(
		        properties.date(), 70);
		date.setHeader("Datum");
		date.setCell(new DateCell(DateTimeFormat.getFormat("dd.MM.yyyy")));
		cfgs.add(date);
		return new ColumnModel<ChangeEventDTO>(cfgs);
	}

	@Override
	public void setAutomaticTooltip(String tooltip) {
		addTooltip(automatic, tooltip);

	}

	@Override
	public void setManualTooltip(String tooltip) {
		addTooltip(manual, tooltip);
	}

	@Override
	public void setNotNeededTooltip(String tooltip) {
		addTooltip(notNeeded, tooltip);
	}

	protected void addTooltip(ColumnConfig<ChangeEventDTO, ChangeEventDTO> columnConfig,
	        String tooltip) {
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		builder.appendEscaped(tooltip);
		columnConfig.setToolTip(builder.toSafeHtml());
	}

}
