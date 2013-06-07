package de.gsv.idm.client.view.gsk;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.gsv.idm.client.presenter.gsk.ThreatEditorPresenter.ThreatEditorDisplay;
import de.gsv.idm.client.view.general.GeneralEditor;
import de.gsv.idm.client.view.gsk.widgets.MeasureGridList;
import de.gsv.idm.client.view.gsk.widgets.ModuleGridList;
import de.gsv.idm.client.view.gsk.widgets.ThreatGridList;
import de.gsv.idm.client.view.properties.MeasureDTOProperties;
import de.gsv.idm.client.view.properties.ModuleDTOProperties;
import de.gsv.idm.client.view.properties.ThreatDTOProperties;
import de.gsv.idm.client.view.widgets.form.GridList;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.ModuleDTO;
import de.gsv.idm.shared.dto.ThreatDTO;

public class ThreatEditor extends GeneralEditor<ThreatDTO> implements ThreatEditorDisplay {
	TextField name = new TextField();
	TextField version = new TextField();
	FieldLabel versionLabel;
	TextArea description = new TextArea();
	
	ListStore<ModuleDTO> modulesStore;
	ListStoreEditor<ModuleDTO> modules;
	GridList<ModuleDTO> modulesContainer;
	
	ListStore<ThreatDTO> threatStore;
	ListStoreEditor<ThreatDTO> threats;
	GridList<ThreatDTO> threatsContainer;
	
	ListStore<MeasureDTO> measureStore;
	ListStoreEditor<MeasureDTO> measures;
	GridList<MeasureDTO> measuresContainer;

	public ThreatEditor() {
		container.add(new FieldLabel(name, "Name"), getFormData());
		name.setReadOnly(true);
		versionLabel = new FieldLabel(version, "Version"); 
		container.add(versionLabel, getFormData());		
		version.setReadOnly(true);
		container.add(new FieldLabel(description, "Beschreibung"), getFormData());		
		description.setReadOnly(true);
		description.setHeight(200);
		
		final ModuleDTOProperties moduleProps = GWT
		        .create(ModuleDTOProperties.class);
		modulesStore = new ListStore<ModuleDTO>(moduleProps.key());
		modules = new ListStoreEditor<ModuleDTO>(modulesStore);
		
		modulesContainer = new ModuleGridList(modulesStore);
		modulesContainer.setAddButtonVisibility(false);
		modulesContainer.setText("Verlinkte Bausteine");
		container.add(modulesContainer, getFormData());
		
		final ThreatDTOProperties threatProps = GWT
		        .create(ThreatDTOProperties.class);
		threatStore = new ListStore<ThreatDTO>(threatProps.key());
		threats = new ListStoreEditor<ThreatDTO>(threatStore);	
		threatsContainer = new ThreatGridList(threatStore);
		threatsContainer.setAddButtonVisibility(false);
		threatsContainer.setText("Verlinkte Gefährdungen");
		container.add(threatsContainer, getFormData());
		
		final MeasureDTOProperties measureProps = GWT.create(MeasureDTOProperties.class);
		measureStore = new ListStore<MeasureDTO>(measureProps.key());
		measures = new ListStoreEditor<MeasureDTO>(measureStore);
		measuresContainer = new MeasureGridList(measureStore);
		measuresContainer.setText("Verlinkte Maßnahmen");
		measuresContainer.setAddButtonVisibility(false);
		container.add(measuresContainer, getFormData());
		FieldLabel seperationSpace = new FieldLabel(new FlowPanel(),"");
		seperationSpace.setLabelSeparator("");
		container.add(seperationSpace,getFormData());
	}
	
	public void visibilityModulesContainer(boolean visible){
		modulesContainer.setVisible(visible);
	}
	
	public void visibilityThreatsContainer(boolean visible){
		threatsContainer.setVisible(visible);
	}
	
	public void visibilityMeasuresContainer(boolean visible){
		measuresContainer.setVisible(visible);
	}

	@Override
    public void setEnabled(Boolean enabled) {
		name.setEnabled(enabled);
	    version.setEnabled(enabled);
	    description.setEnabled(enabled);
	    threatsContainer.setEnabled(enabled);
	    measuresContainer.setEnabled(enabled);
	    modulesContainer.setEnabled(enabled);
    }
}
