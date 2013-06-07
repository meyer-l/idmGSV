package de.gsv.idm.client.view.gsk;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

import de.gsv.idm.client.presenter.gsk.ModuleEditorPresenter.ModuleEditorDisplay;
import de.gsv.idm.client.view.general.GeneralEditor;
import de.gsv.idm.client.view.gsk.widgets.MeasureGridList;
import de.gsv.idm.client.view.gsk.widgets.MeasureLinkGridList;
import de.gsv.idm.client.view.gsk.widgets.ModuleGridList;
import de.gsv.idm.client.view.gsk.widgets.ThreatGridList;
import de.gsv.idm.client.view.properties.MeasureDTOProperties;
import de.gsv.idm.client.view.properties.MeasureLinkDTOProperties;
import de.gsv.idm.client.view.properties.ModuleDTOProperties;
import de.gsv.idm.client.view.properties.ThreatDTOProperties;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.MeasureLinkDTO;
import de.gsv.idm.shared.dto.ModuleDTO;
import de.gsv.idm.shared.dto.ThreatDTO;

public class ModuleEditor extends GeneralEditor<ModuleDTO> implements ModuleEditorDisplay {

	TextField name = new TextField();
	TextField version = new TextField();
	TextArea moduleDescription = new TextArea();
	TextArea threatDescription = new TextArea();
	TextArea measureDescription = new TextArea();
	
	ListStore<ThreatDTO> threatStore;
	ListStoreEditor<ThreatDTO> threats;
	ThreatGridList threatsContainer;
	
	ListStore<MeasureLinkDTO> measureStore;
	ListStoreEditor<MeasureLinkDTO> measures;
	MeasureLinkGridList measuresContainer;
	
	ListStore<ModuleDTO> modulesAddStore;
	ListStoreEditor<ModuleDTO> modulesAdd;
	ModuleGridList modulesAddContainer;
	
	ListStore<ThreatDTO> threatAddStore;
	ListStoreEditor<ThreatDTO> threatsAdd;
	ThreatGridList threatsAddContainer;
	
	ListStore<MeasureDTO> measureAddStore;
	ListStoreEditor<MeasureDTO> measuresAdd;
	MeasureGridList measuresAddContainer;
	

	public ModuleEditor() {	
		container.add(new FieldLabel(name, "Name"), getFormData());
		name.setReadOnly(true);
		container.add(new FieldLabel(version, "Version"), getFormData());		
		version.setReadOnly(true);
		container.add(new FieldLabel(moduleDescription, "Beschreibung"), getFormData());	
		moduleDescription.setHeight(200);
		moduleDescription.setReadOnly(true);
		container.add(new FieldLabel(threatDescription, "Gefährdungslage"), getFormData());	
		threatDescription.setHeight(200);
		threatDescription.setReadOnly(true);
		container.add(new FieldLabel(measureDescription, "Maßnahmen- empfehlungen"), getFormData());	
		measureDescription.setHeight(200);
		measureDescription.setReadOnly(true);
		
		final ThreatDTOProperties threatProps = GWT
		        .create(ThreatDTOProperties.class);
		threatStore = new ListStore<ThreatDTO>(threatProps.key());
		threats = new ListStoreEditor<ThreatDTO>(threatStore);
		List<ColumnConfig<ThreatDTO, ?>> columns = new ArrayList<ColumnConfig<ThreatDTO, ?>>();
		ColumnConfig<ThreatDTO, String> name = new ColumnConfig<ThreatDTO, String>(
		        threatProps.name(), 100, "Name");
		columns.add(name);
		
		threatsContainer = new ThreatGridList(threatStore);
		threatsContainer.setAddButtonVisibility(false);
		threatsContainer.setText("Gefährdungen");
		container.add(threatsContainer, getFormData());
		
		final MeasureLinkDTOProperties measureLinkProps = GWT.create(MeasureLinkDTOProperties.class);
		measureStore = new ListStore<MeasureLinkDTO>(measureLinkProps.key());
		measures = new ListStoreEditor<MeasureLinkDTO>(measureStore);
		measuresContainer = new MeasureLinkGridList(measureStore);
		measuresContainer.setText("Maßnahmen");
		measuresContainer.setAddButtonVisibility(false);
		container.add(measuresContainer, getFormData());	
		
		final ModuleDTOProperties moduleProps = GWT
		        .create(ModuleDTOProperties.class);
		modulesAddStore = new ListStore<ModuleDTO>(moduleProps.key());
		modulesAdd = new ListStoreEditor<ModuleDTO>(modulesAddStore);
				
		modulesAddContainer = new ModuleGridList(modulesAddStore);
		modulesAddContainer.setAddButtonVisibility(false);
		modulesAddContainer.setText("Verlinkte Bausteine");
		container.add(modulesAddContainer, getFormData());
		
		threatAddStore = new ListStore<ThreatDTO>(threatProps.key());
		threatsAdd = new ListStoreEditor<ThreatDTO>(threatAddStore);		
		threatsAddContainer = new ThreatGridList(threatStore);
		threatsAddContainer.setAddButtonVisibility(false);
		threatsAddContainer.setText("Verlinkte Gefährdungen");
		container.add(threatsAddContainer, getFormData());
		
		final MeasureDTOProperties measureProps = GWT.create(MeasureDTOProperties.class);
		measureAddStore = new ListStore<MeasureDTO>(measureProps.key());
		measuresAdd = new ListStoreEditor<MeasureDTO>(measureAddStore);
		measuresAddContainer = new MeasureGridList(measureAddStore);
		measuresAddContainer.setText("Verlinkte Maßnahmen");
		measuresAddContainer.setAddButtonVisibility(false);
		container.add(measuresAddContainer,getFormData());
		
		
		FieldLabel seperationSpace = new FieldLabel(new FlowPanel(),"");
		seperationSpace.setLabelSeparator("");
		container.add(seperationSpace,getFormData());
		
	}
	
	public void visibilityModulesAddContainer(boolean visible){
		modulesAddContainer.setVisible(visible);
	}
	
	public void visibilityThreatsAddContainer(boolean visible){
		threatsAddContainer.setVisible(visible);
	}
	
	public void visibilityMeasuresAddContainer(boolean visible){
		measuresAddContainer.setVisible(visible);
	}

	@Override
    public void setEnabled(Boolean enabled) {
	    name.setEnabled(enabled);
	    version.setEnabled(enabled);
	    moduleDescription.setEnabled(enabled);
	    threatDescription.setEnabled(enabled);
	    measureDescription.setEnabled(enabled);
	    threatsContainer.setEnabled(enabled);
	    measuresContainer.setEnabled(enabled);
	    modulesAddContainer.setEnabled(enabled);
	    threatsAddContainer.setEnabled(enabled);
	    measuresAddContainer.setEnabled(enabled);
    }
}
