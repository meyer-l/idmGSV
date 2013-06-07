package de.gsv.idm.client.view.informationdomain.asset;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.presenter.informationdomain.asset.AssetEditorPresenter.AssetEditorDisplay;
import de.gsv.idm.client.view.employee.EmployeeGridList;
import de.gsv.idm.client.view.general.GeneralEditor;
import de.gsv.idm.client.view.informationdomain.asset.widgets.AssetMeasureLinkGridList;
import de.gsv.idm.client.view.informationdomain.asset.widgets.AssetModuleLinkGridList;
import de.gsv.idm.client.view.properties.AssetAssettypeModuleLinkDTOProperties;
import de.gsv.idm.client.view.properties.AssetMeasureLinkDTOProperties;
import de.gsv.idm.client.view.properties.EmployeeDTOProperties;
import de.gsv.idm.client.view.widgets.form.ModuleStatusAssesmentBox;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetMeasureLinkDTO;
import de.gsv.idm.shared.dto.AssetModuleLinkDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;

public class AssetEditor extends GeneralEditor<AssetDTO> implements AssetEditorDisplay {

	ListStore<EmployeeDTO> employeeStore;

	@Ignore
	TextButton editAssettypeButton = new TextButton();
	TextField assettypeName = new TextField();
	TextField identifier = new TextField();
	ComboBox<EmployeeDTO> assetResponsiblePerson;
	TextField assettypeResponsiblePerson = new TextField();

	ModuleStatusAssesmentBox assetStatus = new ModuleStatusAssesmentBox();
	TextField assettypeStatus = new TextField();

	TextField calculatedAvailabilityName = new TextField();
	TextField calculatedConfidentialityName = new TextField();
	TextField calculatedIntegrityName = new TextField();
	TextField calculatedSecurityAssesmentName = new TextField();

	AssetModuleLinkGridList assetModelAssetModuleLinkContainer;
	ListStore<AssetModuleLinkDTO> assetModelModuleStore;
	ListStoreEditor<AssetModuleLinkDTO> moduleLinks;

	TextField assignedSecurityzoneName = new TextField();
	@Ignore
	TextButton securityzoneMeasureDetailsButton = new TextButton();
	@Ignore
	TextButton editSecurityzoneButton = new TextButton();

	TextField securityAssesmentSourceName = new TextField();
	@Ignore
	TextButton editSecurityAssesmentSourceButton = new TextButton();
	
	TextField parentLabel = new TextField();
	@Ignore
	TextButton editParentButton = new TextButton();

	AssetMeasureLinkGridList assetMeasureLinkContainer;
	ListStore<AssetMeasureLinkDTO> assetMeasureStore;
	ListStoreEditor<AssetMeasureLinkDTO> measureLinksWithoutInherited;

	ListStore<EmployeeDTO> associatedPersonStore;
	ListStoreEditor<EmployeeDTO> associatedPersons;

	AssetMeasureLinkGridList inheritedMeasureContainer;
	ListStore<AssetMeasureLinkDTO> inheritedMeasureStore;
	ListStoreEditor<AssetMeasureLinkDTO> inheritedMeasures;

	EmployeeGridList associatedPersonContainer;
	
	@Ignore
	TextButton assetLinksEdit;

	public AssetEditor() {
		ImageResources imageBundler = GWT.create(ImageResources.class);
		container.add(new FieldLabel(identifier, "Asset-Bezeichner"), getFormData());
		container.add(new FieldLabel(assettypeName, "Asset-Typ"), getFormData());
		assettypeName.setReadOnly(true);
		FieldLabel editAssettypeButtonLabel = new FieldLabel(editAssettypeButton, "");
		editAssettypeButton.setIcon(imageBundler.edit());
		editAssettypeButtonLabel.setLabelSeparator("");
		container.add(editAssettypeButtonLabel,
		        new VerticalLayoutData(-1, -1, new Margins(5, 0, 5, 10)));
		
		container.add(new FieldLabel(parentLabel, "Eltern-Asset"), getFormData());
		parentLabel.setReadOnly(true);
		FieldLabel editParentButtonLabel = new FieldLabel(editParentButton, "");
		editParentButton.setIcon(imageBundler.edit());
		editParentButtonLabel.setLabelSeparator("");
		container.add(editParentButtonLabel,
		        new VerticalLayoutData(-1, -1, new Margins(5, 0, 5, 10)));

		container.add(
		        new FieldLabel(assettypeResponsiblePerson, "Asset-Grundtyp Verantwortlicher"),
		        getHalfFormData());
		assettypeResponsiblePerson.setReadOnly(true);
		EmployeeDTOProperties props = GWT.create(EmployeeDTOProperties.class);
		employeeStore = new ListStore<EmployeeDTO>(props.key());
		assetResponsiblePerson = new ComboBox<EmployeeDTO>(employeeStore, props.fullNameLabel());
		assetResponsiblePerson.setAllowBlank(true);
		assetResponsiblePerson.setTriggerAction(TriggerAction.ALL);
		container
		        .add(new FieldLabel(assetResponsiblePerson, "Verantwortlicher"), getHalfFormData());
		container.add(new FieldLabel(assettypeStatus, "Asset-Grundtyp Betriebstatus"),
		        getHalfFormData());
		assettypeStatus.setReadOnly(true);
		container.add(new FieldLabel(assetStatus, "Betriebsstatus"), getHalfFormData());

		calculatedSecurityAssesmentName.setReadOnly(true);
		container.add(new FieldLabel(calculatedSecurityAssesmentName, "Berechneter Schutzbedarf"),
		        getHalfFormData());
		container.add(new FieldLabel(securityAssesmentSourceName, "Quelle des Schutzbedarfes"),
		        getFormData());
		securityAssesmentSourceName.setReadOnly(true);
		FieldLabel editsecurityAssesmentSourceLabel = new FieldLabel(
		        editSecurityAssesmentSourceButton, "");
		editSecurityAssesmentSourceButton.setIcon(imageBundler.edit());
		editsecurityAssesmentSourceLabel.setLabelSeparator("");
		container.add(editsecurityAssesmentSourceLabel, new VerticalLayoutData(-1, -1, new Margins(
		        5, 0, 5, 10)));
		calculatedAvailabilityName.setReadOnly(true);
		container.add(new FieldLabel(calculatedAvailabilityName, "Berechneter Schutzbedarf Verfügbarkeit"), getHalfFormData());
		calculatedConfidentialityName.setReadOnly(true);
		container.add(new FieldLabel(calculatedConfidentialityName, "Berechneter Schutzbedarf Vertraulichkeit"),
		        getHalfFormData());
		calculatedIntegrityName.setReadOnly(true);
		container.add(new FieldLabel(calculatedIntegrityName, "Berechneter Schutzbedarf Integrität"), getHalfFormData());

		final EmployeeDTOProperties employeeProps = GWT.create(EmployeeDTOProperties.class);
		associatedPersonStore = new ListStore<EmployeeDTO>(employeeProps.key());

		associatedPersons = new ListStoreEditor<EmployeeDTO>(associatedPersonStore);
		associatedPersonContainer = new EmployeeGridList(associatedPersonStore);
		container.add(associatedPersonContainer, getFormData());

		assignedSecurityzoneName.setReadOnly(true);
		container.add(new FieldLabel(assignedSecurityzoneName, "Schutzzone der Mitarbeiter"),
		        getFormData());
		FlowPanel securityzoneButtons = new FlowPanel();
		securityzoneButtons.add(securityzoneMeasureDetailsButton);
		securityzoneMeasureDetailsButton.setIcon(imageBundler.eye());
		securityzoneButtons.add(editSecurityzoneButton);
		editSecurityzoneButton.setIcon(imageBundler.edit());
		FieldLabel securityzoneMeasureDetailsLabel = new FieldLabel(securityzoneButtons, "");
		securityzoneMeasureDetailsLabel.setLabelSeparator("");
		container.add(securityzoneMeasureDetailsLabel, new VerticalLayoutData(-1, -1, new Margins(
		        5, 0, 5, 10)));

		final AssetAssettypeModuleLinkDTOProperties assetModuleLinkProps = GWT
		        .create(AssetAssettypeModuleLinkDTOProperties.class);
		assetModelModuleStore = new ListStore<AssetModuleLinkDTO>(assetModuleLinkProps.key());
		moduleLinks = new ListStoreEditor<AssetModuleLinkDTO>(assetModelModuleStore);
		assetModelAssetModuleLinkContainer = new AssetModuleLinkGridList(assetModelModuleStore,
		        employeeStore);
		container.add(assetModelAssetModuleLinkContainer, getFormData());

		final AssetMeasureLinkDTOProperties assetMeasureLinkProps = GWT
		        .create(AssetMeasureLinkDTOProperties.class);
		assetMeasureStore = new ListStore<AssetMeasureLinkDTO>(assetMeasureLinkProps.key());
		measureLinksWithoutInherited = new ListStoreEditor<AssetMeasureLinkDTO>(assetMeasureStore);
		assetMeasureLinkContainer = new AssetMeasureLinkGridList(assetMeasureStore, employeeStore);
		container.add(assetMeasureLinkContainer, getFormData());
		inheritedMeasureStore = new ListStore<AssetMeasureLinkDTO>(assetMeasureLinkProps.key());
		inheritedMeasures = new ListStoreEditor<AssetMeasureLinkDTO>(inheritedMeasureStore);
		inheritedMeasureContainer = new AssetMeasureLinkGridList(inheritedMeasureStore,
		        employeeStore);
		inheritedMeasureContainer.setAddButtonVisibility(false);
		container.add(inheritedMeasureContainer, getFormData());
		inheritedMeasureContainer.setText("Vererbte Maßnahmen-Verknüpfungen");
		assetLinksEdit = new TextButton();
		buttonPanel.addButton(assetLinksEdit);
		assetLinksEdit.setIcon(imageBundler.addLink());
		buttonPanel.setHeaderVisible(false);
		addButtons();
	}
	
	@Override
	protected void addButtons(){
		buttonPanel.addButton(delete);
		buttonPanel.addButton(save);
	}

	public void setComboBoxList(ArrayList<EmployeeDTO> data) {
		employeeStore.replaceAll(data);
	}

	@Override
	public void setEditAssetButtonText(String text) {
		editAssettypeButton.setText(text);
	}

	@Override
	public HasSelectHandlers getEditAssetButtonClick() {
		return editAssettypeButton;
	}

	@Override
	public HasSelectHandlers getAddEmployeeButtonClick() {
		return associatedPersonContainer.getAddButtonClick();
	}

	@Override
	public void setAddEmployeeButtonText(String text) {
		associatedPersonContainer.setAddButtonText(text);
	}

	@Override
	public ListStore<EmployeeDTO> getEmployeeStore() {
		return associatedPersonStore;
	}

	@Override
	public void setEmployeeLabel(String text) {
		associatedPersonContainer.setText(text);
	}

	@Override
	public void setSecurityzoneMeasuresDetailsButtonText(String text) {
		securityzoneMeasureDetailsButton.setText(text);
	}

	@Override
	public HasSelectHandlers getSecurityzoneMeasuresDetailsButtonClick() {
		return securityzoneMeasureDetailsButton;
	}

	@Override
	public void setAddMeasureButtonText(String text) {
		assetMeasureLinkContainer.setAddButtonText(text);
	}

	@Override
	public HasSelectHandlers getAddMeasureButtonClick() {
		return assetMeasureLinkContainer.getAddButtonClick();
	}

	@Override
	public ListStore<AssetMeasureLinkDTO> getMeasuresStore() {
		return assetMeasureStore;
	}

	@Override
	public ListStore<AssetMeasureLinkDTO> getInheritedStore() {
		return inheritedMeasureStore;
	}

	@Override
	public void setSecurityzoneMeasuresDetailsButtonEnabled(Boolean enabled) {
		securityzoneMeasureDetailsButton.setEnabled(enabled);
		editSecurityzoneButton.setEnabled(enabled);
	}

	@Override
	public void setSecurityzoneMeasuresDetailsTooltip(String tooltip) {
		securityzoneMeasureDetailsButton.setToolTip(tooltip);
		assignedSecurityzoneName.setToolTip(tooltip);
	}

	@Override
	public void setCalcualtedSecurityAssesmentTooltip(String tooltip) {
		calculatedSecurityAssesmentName.setToolTip(tooltip);
	}

	@Override
	public void setEditSecurityzoneButtonText(String text) {
		editSecurityzoneButton.setText(text);
	}

	@Override
	public HasSelectHandlers getEditSecurityzoneButtonClick() {
		return editSecurityzoneButton;
	}

	@Override
	public void setIdentifierToolTip(String text) {
		identifier.setToolTip(text);
	}

	@Override
	public void setAssettypeResponsiblePersonToolTip(String text) {
		assettypeResponsiblePerson.setToolTip(text);
	}

	@Override
	public void setAvailabilityTooltip(String toolTip) {
		calculatedAvailabilityName.setToolTip(toolTip);
	}

	@Override
	public void setConfidentialityTip(String toolTip) {
		calculatedConfidentialityName.setToolTip(toolTip);
	}

	@Override
	public void setIntegrityTooltip(String toolTip) {
		calculatedIntegrityName.setToolTip(toolTip);
	}

	@Override
	public void setEmployeeListTooltip(String toolTip) {
		associatedPersonContainer.setToolTip(toolTip);
	}

	@Override
	public void setAssignedSecurityzoneToolTip(String toolTip) {
		assignedSecurityzoneName.setToolTip(toolTip);
	}

	@Override
	public void setModuleListTooltip(String toolTip) {
		assetModelAssetModuleLinkContainer.setToolTip(toolTip);
	}

	@Override
	public void setMeasureListTooltip(String toolTip) {
		assetMeasureLinkContainer.setToolTip(toolTip);
	}

	@Override
	public void setInheritedMeasureListTooltip(String toolTip) {
		inheritedMeasureContainer.setToolTip(toolTip);
	}

	@Override
	public void setEditSecurityAssesmentSourceButtonText(String text) {
		editSecurityAssesmentSourceButton.setText(text);
	}

	@Override
	public HasSelectHandlers getEditSecurityAssesmentSourceButtonClick() {
		return editSecurityAssesmentSourceButton;
	}

	@Override
	public void setEditSecurityAssesmentSourceButtonEnabled(Boolean enabled) {
		editSecurityAssesmentSourceButton.setEnabled(enabled);
	}

	@Override
    public void setEditParentButtonText(String text) {
		editParentButton.setText(text);
    }

	@Override
    public HasSelectHandlers getEditParentButtonClick() {
	    return editParentButton;
    }

	@Override
    public void setEditParentButtonEnabled(Boolean enabled) {
		editParentButton.setEnabled(enabled);
    }

	@Override
    public void setAssetLinkButtonText(String text) {
		assetLinksEdit.setText(text);
    }

	@Override
    public HasSelectHandlers getAssetLinkButtonClick() {
	    return assetLinksEdit;
    }

	@Override
    public void setEnabled(Boolean enabled) {
	   
    }

}
