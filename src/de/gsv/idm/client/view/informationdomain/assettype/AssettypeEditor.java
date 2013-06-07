package de.gsv.idm.client.view.informationdomain.assettype;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.ExpandMode;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.presenter.informationdomain.assettype.AssettypeEditorPresenter.AssettypeEditorDisplay;
import de.gsv.idm.client.view.general.GeneralEditor;
import de.gsv.idm.client.view.informationdomain.assettype.widgets.AssettypeMeasureLinkGridList;
import de.gsv.idm.client.view.informationdomain.assettype.widgets.AssettypeModuleLinkGridList;
import de.gsv.idm.client.view.properties.AssettypeCategoryDTOProperties;
import de.gsv.idm.client.view.properties.AssettypeMeasureLinkDTOProperties;
import de.gsv.idm.client.view.properties.AssettypeModuleLinkDTOProperties;
import de.gsv.idm.client.view.properties.EmployeeDTOProperties;
import de.gsv.idm.client.view.validation.EmptyClassValidator;
import de.gsv.idm.client.view.validation.EmptyFieldValidator;
import de.gsv.idm.client.view.validation.SecurityAssesmentValidator;
import de.gsv.idm.client.view.widgets.form.ModuleStatusAssesmentBox;
import de.gsv.idm.client.view.widgets.form.SecurityLevelAssesmentBox;
import de.gsv.idm.shared.comperator.LowerCaseStringComperator;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.AssettypeMeasureLinkDTO;
import de.gsv.idm.shared.dto.AssettypeModuleLinkDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;

public class AssettypeEditor extends GeneralEditor<AssettypeDTO> implements AssettypeEditorDisplay {

	ListStore<EmployeeDTO> employeeStore;

	AccordionLayoutContainer accordianContainer = new AccordionLayoutContainer();
	AccordionLayoutAppearance accordianAppearance = GWT
	        .<AccordionLayoutAppearance> create(AccordionLayoutAppearance.class);

	ContentPanel generalInformationContentPanel;

	TextField name = new TextField();
	ComboBox<AssettypeCategoryDTO> category;
	ListStore<AssettypeCategoryDTO> categoryStore;
	TextField location = new TextField();
	TextArea description = new TextArea();
	CheckBox itAsset = new CheckBox();
	TextField architecture = new TextField();
	ModuleStatusAssesmentBox status = new ModuleStatusAssesmentBox();
	ComboBox<EmployeeDTO> responsiblePerson;

	CheckBox propagateSecurityAssesment = new CheckBox();
	CheckBox manuelSecurityAssesment = new CheckBox();
	SecurityLevelAssesmentBox availability = new SecurityLevelAssesmentBox();
	TextArea availabilityExplanation = new TextArea();
	SecurityLevelAssesmentBox confidentiality = new SecurityLevelAssesmentBox();
	TextArea confidentialityExplanation = new TextArea();
	SecurityLevelAssesmentBox integrity = new SecurityLevelAssesmentBox();
	TextArea integrityExplanation = new TextArea();
	CheckBox personalData = new CheckBox();

	AssettypeModuleLinkGridList assetModuleLinkContainer;
	ListStore<AssettypeModuleLinkDTO> moduleStore;
	ListStoreEditor<AssettypeModuleLinkDTO> modules;

	AssettypeMeasureLinkGridList assetMeasureLinkContainer;
	ListStore<AssettypeMeasureLinkDTO> measureStore;
	ListStoreEditor<AssettypeMeasureLinkDTO> measures;

	@Ignore
	TextButton editCategoryButton;
	@Ignore
	TextButton chooseIconButton;
	@Ignore
	Image image;

	public AssettypeEditor() {
		accordianContainer.setExpandMode(ExpandMode.SINGLE_FILL);
		scrollPanel.setAlwaysShowScrollBars(true);
		formPanel.add(accordianContainer);
		accordianContainer.add(buildGeneralInformationPanel());
		accordianContainer.add(buildSecurityAssesmentPanel());
		accordianContainer.add(buildGskPanel());		

		addButtons();
		buttonPanel.setHeaderVisible(false);
	}

	@Override
	public HasChangeHandlers getItAssetsCheckBoxClick() {
		return itAsset;
	}

	@Override
	public void setItAssetsFieldsEnabled(Boolean bool) {
		architecture.setEnabled(bool);
	}

	@Override
	public HasChangeHandlers getManuelSecurityAssesmentCheckBoxClick() {
		return manuelSecurityAssesment;
	}

	@Override
	public void setSecurityAssesmentFieldsEnabled(Boolean bool) {
		availability.setEnabled(bool);
		availabilityExplanation.setEnabled(bool);
		confidentiality.setEnabled(bool);
		confidentialityExplanation.setEnabled(bool);
		integrity.setEnabled(bool);
		integrityExplanation.setEnabled(bool);
		personalData.setEnabled(bool);
	}

	public void setComboBoxList(ArrayList<EmployeeDTO> data) {
		employeeStore.replaceAll(data);
	}

	@Override
	public HasSelectHandlers getAddAssetModuleLinkClick() {
		return assetModuleLinkContainer.getAddButtonClick();
	}

	@Override
	public void setAddAssetModuleLinkText(String text) {
		assetModuleLinkContainer.setAddButtonText(text);
	}

	public ListStore<AssettypeModuleLinkDTO> getModulesListStore() {
		return moduleStore;
	}

	@Override
	public HasSelectHandlers getAddAssetMeasureLinkClick() {
		return assetMeasureLinkContainer.getAddButtonClick();
	}

	@Override
	public void setAddAssetMeasureLinkText(String text) {
		assetMeasureLinkContainer.setAddButtonText(text);
	}

	public ListStore<AssettypeMeasureLinkDTO> getMeasuresListStore() {
		return measureStore;
	}

	@Override
	public void setAssettypeCategoryList(List<AssettypeCategoryDTO> treeCategories) {
		categoryStore.addAll(treeCategories);
		if (category.getValue() == null) {
			category.setValue(categoryStore.get(0));
		}
	}

	@Override
	public void setChooseIconButtonText(String text) {
		chooseIconButton.setText(text);
	}

	@Override
	public HasSelectHandlers getChooseIconButtonClick() {
		return chooseIconButton;
	}

	@Override
	public Image getImage() {
		return image;
	}

	@Override
	public String getIconName() {
		return image.getTitle();
	}

	@Override
	public void setImageResource(ImageResource resource) {
		if (resource != null) {

			this.image.setResource(resource);
			this.image.setTitle(resource.getName());
			image.setVisible(true);
		} else {
			image.setVisible(false);
			image.setTitle("");
		}
	}

	private Widget buildGeneralInformationPanel() {
		generalInformationContentPanel = new ContentPanel(accordianAppearance);
		generalInformationContentPanel.setAnimCollapse(false);
		generalInformationContentPanel.setHeadingText("Allgemeine Informationen");

		VerticalLayoutContainer generalInformation = new VerticalLayoutContainer();
		generalInformation.setStyleName("blueBackground");
		generalInformationContentPanel.add(generalInformation);
		generalInformation.add(new FieldLabel(name, "Name"), getFormData());
		name.addValidator(new EmptyFieldValidator());

		AssettypeCategoryDTOProperties generalProps = GWT
		        .create(AssettypeCategoryDTOProperties.class);
		categoryStore = new ListStore<AssettypeCategoryDTO>(generalProps.comboBoxKey());
		categoryStore.addSortInfo(new StoreSortInfo<AssettypeCategoryDTO>(generalProps
		        .valueProviderLabel(), new LowerCaseStringComperator(), SortDir.ASC));
		category = new ComboBox<AssettypeCategoryDTO>(categoryStore, generalProps.label()) {
			@Override
			protected void onBlur(Event be) {
				String value = getCell().getText(getElement());
				super.onBlur(be);
				if (categoryStore.findModelWithKey(value) == null) {
					AssettypeCategoryDTO newCategory = new AssettypeCategoryDTO(-1, value, "");
					categoryStore.add(newCategory);
					setValue(newCategory);
				}
			}
		};
		category.setTriggerAction(TriggerAction.ALL);
		ImageResources imageBundler = GWT.create(ImageResources.class);
		HorizontalLayoutContainer categoryPanel = new HorizontalLayoutContainer();
		categoryPanel.add(category, new HorizontalLayoutData(420, 100, new Margins(0, 4, 4, 0)));
		Image tooltipImage = new Image(imageBundler.help());
		tooltipImage.setTitle("Durch die Combobox kann eine bestehende Kategorie ausgewählt"
		        + " werden. Außerdem können fehlende Kategorien durch das Eintragen"
		        + " des Kategorienamens in der Combobox automatisch erstellt werden.");
		categoryPanel.add(tooltipImage, new HorizontalLayoutData(100, 100, new Margins(0, 4, 4, 4)));
		FieldLabel categoryLabel = new FieldLabel(categoryPanel, "Asset-Grundtyp- Kategorie");
		generalInformation.add(categoryLabel, getFormData());
		category.addValidator(new EmptyClassValidator<AssettypeCategoryDTO>());
		editCategoryButton = new TextButton();
		imageBundler = GWT.create(ImageResources.class);
		editCategoryButton.setIcon(imageBundler.edit());
		FieldLabel editCategoryButtonLabel = new FieldLabel(editCategoryButton, "");
		editCategoryButtonLabel.setLabelSeparator("");
		generalInformation.add(editCategoryButtonLabel, new VerticalLayoutData(-1, -1, new Margins(
		        5, 0, 5, 10)));
		chooseIconButton = new TextButton();
		ImageResources bundler = GWT.create(ImageResources.class);
		chooseIconButton.setIcon(bundler.folderIcon());
		image = new Image();
		generalInformation.add(new FieldLabel(image, "Icon"), getHalfFormData());
		FieldLabel chooseIconButtonLabel = new FieldLabel(chooseIconButton, "");
		chooseIconButtonLabel.setLabelSeparator("");
		generalInformation.add(chooseIconButtonLabel, new VerticalLayoutData(-1, -1, new Margins(5,
		        0, 5, 10)));
		generalInformation.add(new FieldLabel(location, "Ort"), getFormData());
		EmployeeDTOProperties props = GWT.create(EmployeeDTOProperties.class);
		employeeStore = new ListStore<EmployeeDTO>(props.key());
		responsiblePerson = new ComboBox<EmployeeDTO>(employeeStore, props.fullNameLabel());
		responsiblePerson.setAllowBlank(true);
		responsiblePerson.setTriggerAction(TriggerAction.ALL);
		generalInformation.add(new FieldLabel(responsiblePerson, "Verantwortlicher"),
		        getHalfFormData());
		generalInformation.add(new FieldLabel(description, "Beschreibung"), getFormData());
		description.setHeight(200);
		HorizontalPanel itAssetCheckBoxPanel = new HorizontalPanel();
		itAssetCheckBoxPanel.add(itAsset);
		generalInformation.add(new FieldLabel(itAssetCheckBoxPanel, "IT-System"),
		        new VerticalLayoutData(-1, -1, new Margins(5, 0, 5, 10)));
		generalInformation.add(new FieldLabel(architecture, "Architektur/OS"), getFormData());
		generalInformation.add(new FieldLabel(status, "Betriebsstatus"), getHalfFormData());

		return generalInformationContentPanel;
	}

	private Widget buildSecurityAssesmentPanel() {
		ContentPanel securityAssesmentContentPanel = new ContentPanel(accordianAppearance);
		securityAssesmentContentPanel.setAnimCollapse(false);
		securityAssesmentContentPanel.setHeadingText("Schutzbedarf");
		accordianContainer.add(securityAssesmentContentPanel);
		VerticalLayoutContainer securityAssesment = new VerticalLayoutContainer();
		securityAssesment.setStyleName("blueBackground");
		securityAssesmentContentPanel.add(securityAssesment);

		HorizontalPanel propagateSecurityAssesmentCheckBoxPanel = new HorizontalPanel();
		propagateSecurityAssesmentCheckBoxPanel.add(propagateSecurityAssesment);
		securityAssesment.add(new FieldLabel(propagateSecurityAssesmentCheckBoxPanel,
		        "Schutzbedarf wird propagiert"), new VerticalLayoutData(-1, -1, new Margins(5, 0,
		        5, 10)));

		HorizontalPanel manuelSecurityAssesmentCheckBoxPanel = new HorizontalPanel();
		manuelSecurityAssesmentCheckBoxPanel.add(manuelSecurityAssesment);
		securityAssesment.add(new FieldLabel(manuelSecurityAssesmentCheckBoxPanel,
		        "Mindestschutz- bedarf für jede Instanz"), new VerticalLayoutData(-1, -1,
		        new Margins(5, 0, 5, 10)));
		securityAssesment.add(new FieldLabel(availability, "Schutzbedarf Verfügbarkeit"), getHalfFormData());
		availabilityExplanation.setHeight(100);
		availabilityExplanation.addValidator(new SecurityAssesmentValidator(availability));
		securityAssesment.add(
		        new FieldLabel(availabilityExplanation, "Verfügbarkeits- erläuterung"),
		        getFormData());

		securityAssesment
		        .add(new FieldLabel(confidentiality, "Schutzbedarf  Vertraulichkeit"), getHalfFormData());
		confidentialityExplanation.setHeight(100);
		confidentialityExplanation.addValidator(new SecurityAssesmentValidator(confidentiality));
		securityAssesment.add(new FieldLabel(confidentialityExplanation,
		        "Vertraulichkeits- erläuterung"), getFormData());

		securityAssesment.add(new FieldLabel(integrity, "Schutzbedarf Integrität"), getHalfFormData());
		integrityExplanation.addValidator(new SecurityAssesmentValidator(integrity));
		integrityExplanation.setHeight(100);
		securityAssesment.add(new FieldLabel(integrityExplanation, "Integritäts- erläuterung"),
		        getFormData());
		HorizontalPanel personalDataCheckBoxPanel = new HorizontalPanel();
		personalDataCheckBoxPanel.add(personalData);
		securityAssesment.add(new FieldLabel(personalDataCheckBoxPanel, "Personenbezug"),
		        new VerticalLayoutData(-1, -1, new Margins(5, 0, 5, 10)));

		return securityAssesmentContentPanel;
	}

	private Widget buildGskPanel() {
		ContentPanel gskContentPanel = new ContentPanel(accordianAppearance);
		gskContentPanel.setAnimCollapse(false);
		gskContentPanel.setHeadingText("IT-Grundschutz");

		VerticalLayoutContainer gskVerticalPanel = new VerticalLayoutContainer();
		gskVerticalPanel.setStyleName("blueBackground");
		gskContentPanel.add(gskVerticalPanel);
		final AssettypeModuleLinkDTOProperties assetModuleLinkProps = GWT
		        .create(AssettypeModuleLinkDTOProperties.class);
		moduleStore = new ListStore<AssettypeModuleLinkDTO>(assetModuleLinkProps.key());
		modules = new ListStoreEditor<AssettypeModuleLinkDTO>(moduleStore);
		assetModuleLinkContainer = new AssettypeModuleLinkGridList(moduleStore, employeeStore);
		gskVerticalPanel.add(assetModuleLinkContainer, getFormData());

		final AssettypeMeasureLinkDTOProperties assetMeasureLinkProps = GWT
		        .create(AssettypeMeasureLinkDTOProperties.class);
		measureStore = new ListStore<AssettypeMeasureLinkDTO>(assetMeasureLinkProps.key());
		measures = new ListStoreEditor<AssettypeMeasureLinkDTO>(measureStore);
		assetMeasureLinkContainer = new AssettypeMeasureLinkGridList(measureStore, employeeStore);
		gskVerticalPanel.add(assetMeasureLinkContainer, getFormData());
		FieldLabel seperationSpace = new FieldLabel(new FlowPanel(), "");
		seperationSpace.setLabelSeparator("");
		gskVerticalPanel.add(seperationSpace, getFormData());
		return gskContentPanel;
	}

	@Override
	public void setChooseIconButtonToolTip(String toolTip) {
		chooseIconButton.setToolTip(toolTip);
	}

	@Override
	public void setPropergateSecurityAssesmentToolTip(String toolTip) {
		propagateSecurityAssesment.setToolTip(toolTip);
	}

	@Override
	public void setManualSecurityAssesmentToolTip(String toolTip) {
		manuelSecurityAssesment.setToolTip(toolTip);
	}

	@Override
	public void setEditCategoryButtonText(String text) {
		editCategoryButton.setText(text);
	}

	@Override
	public HasSelectHandlers getEditCategoryButtonClick() {
		return editCategoryButton;
	}

	public void setAssetsMeasuresContainerToolTip(String text) {
		assetMeasureLinkContainer.setInheritToolTip(text);
	}

	public void setPersonalDataTooltip(String toolTip) {
		personalData.setToolTip(toolTip);
	}

	@Override
    public void resetEditor() {
		if (category.getValue() == null) {
			category.setValue(categoryStore.get(0));
		}
    }

	@Override
    public void setEnabled(Boolean enabled) {
	    
    }

}
