package de.gsv.idm.client.view.changeevent;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import de.gsv.idm.client.presenter.changeevent.ChangedAppliedPresenter.ChangedAppliedDisplay;
import de.gsv.idm.client.view.general.GeneralGridView;
import de.gsv.idm.client.view.properties.ChangeAppliedDTOProperties;
import de.gsv.idm.shared.comperator.LowerCaseStringComperator;
import de.gsv.idm.shared.dto.ChangeAppliedDTO;

public class ChangedAppliedView extends GeneralGridView<ChangeAppliedDTO> implements
        ChangedAppliedDisplay {

	TextButton editObject;

	public ChangedAppliedView() {
		this((ChangeAppliedDTOProperties) GWT.create(ChangeAppliedDTOProperties.class));
	}

	public ChangedAppliedView(ChangeAppliedDTOProperties create) {
		this(create, new StoreSortInfo<ChangeAppliedDTO>(create.name(),
		        new LowerCaseStringComperator(), SortDir.DESC));
	}

	public ChangedAppliedView(ChangeAppliedDTOProperties properties,
	        StoreSortInfo<ChangeAppliedDTO> sortInfo) {
		super(properties, sortInfo);
		editObject = new TextButton();
		mainPanel.addButton(editObject);
	}

	@Override
	protected ColumnModel<ChangeAppliedDTO> getColumnModel() {
		ChangeAppliedDTOProperties properties = GWT.create(ChangeAppliedDTOProperties.class);
		List<ColumnConfig<ChangeAppliedDTO, ?>> cfgs = new ArrayList<ColumnConfig<ChangeAppliedDTO, ?>>();

		ColumnConfig<ChangeAppliedDTO, String> object = new ColumnConfig<ChangeAppliedDTO, String>(
		        properties.objectClassName(), 150);
		object.setHeader("Objekt");
		cfgs.add(object);

		ColumnConfig<ChangeAppliedDTO, String> name = new ColumnConfig<ChangeAppliedDTO, String>(
		        properties.valueProviderLabel(), 150);
		name.setHeader("Name: ");
		cfgs.add(name);

		ColumnConfig<ChangeAppliedDTO, String> status = new ColumnConfig<ChangeAppliedDTO, String>(
		        properties.statusLabel(), 150);
		status.setHeader("Status: ");
		cfgs.add(status);

		return new ColumnModel<ChangeAppliedDTO>(cfgs);
	}

	@Override
	public void setEditObjectText(String text) {
		editObject.setText(text);
	}

	@Override
	public HasSelectHandlers getEditObjectClick() {
		return editObject;
	}

	@Override
	public void setEditObjectEnabled(Boolean bool) {
		editObject.setEnabled(bool);
	}

}
