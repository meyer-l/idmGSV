package de.gsv.idm.client.view.employee;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.tree.Tree;

import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.presenter.employee.EmployeeEditorPresenter.EmployeeEditorDisplay;
import de.gsv.idm.client.presenter.helper.ImageHelper;
import de.gsv.idm.client.presenter.informationdomain.asset.AssetEditorPresenter;
import de.gsv.idm.client.view.general.GeneralEditor;
import de.gsv.idm.client.view.information.widgets.InformationGridList;
import de.gsv.idm.client.view.informationdomain.asset.AssetEditor;
import de.gsv.idm.client.view.occupation.widgets.OccupationGridList;
import de.gsv.idm.client.view.properties.AssetDTOProperties;
import de.gsv.idm.client.view.properties.InformationDTOProperties;
import de.gsv.idm.client.view.properties.OccupationDTOProperties;
import de.gsv.idm.client.view.validation.EmptyFieldValidator;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.InformationDTO;
import de.gsv.idm.shared.dto.OccupationDTO;

public class EmployeeEditor extends GeneralEditor<EmployeeDTO> implements EmployeeEditorDisplay {
	TextField surname = new TextField();
	TextField name = new TextField();
	TextField telefon = new TextField();

	TextField assignedSecurityzoneName = new TextField();
	@Ignore
	TextButton editSecurityzoneButton = new TextButton();

	TextField calculatedAvailabilityName = new TextField();
	TextField calculatedConfidentialityName = new TextField();
	TextField calculatedIntegrityName = new TextField();

	ListStore<InformationDTO> informationStore;
	ListStoreEditor<InformationDTO> informations;
	InformationGridList informationsContainer;

	ListStore<OccupationDTO> occupationStore;
	ListStoreEditor<OccupationDTO> occupations;
	OccupationGridList occupationsContainer;

	Tree<AssetDTO, String> assetTree;
	TreeStore<AssetDTO> assetStore;
	FieldLabel assetmodelTreePanelLabel;

	public EmployeeEditor() {
		container.add(new FieldLabel(surname, "Nachname"), getFormData());
		surname.addValidator(new EmptyFieldValidator());
		container.add(new FieldLabel(name, "Vorname"), getFormData());
		name.addValidator(new EmptyFieldValidator());
		container.add(new FieldLabel(telefon, "Telefonnr."), getFormData());

		final OccupationDTOProperties occupationProps = GWT.create(OccupationDTOProperties.class);
		occupationStore = new ListStore<OccupationDTO>(occupationProps.key());
		occupations = new ListStoreEditor<OccupationDTO>(occupationStore);

		occupationsContainer = new OccupationGridList(occupationStore);
		container.add(occupationsContainer, getFormData());

		final InformationDTOProperties informationProps = GWT
		        .create(InformationDTOProperties.class);
		informationStore = new ListStore<InformationDTO>(informationProps.key());
		informations = new ListStoreEditor<InformationDTO>(informationStore);
		informationsContainer = new InformationGridList(informationStore);
		container.add(informationsContainer, getFormData());
		container.add(new FieldLabel(assignedSecurityzoneName, "Zugewiesene Schutzzone"),
		        getHalfFormData());
		FieldLabel securityzoneEditButtonLabel = new FieldLabel(editSecurityzoneButton, "");
		ImageResources imageBundler = GWT.create(ImageResources.class);
		editSecurityzoneButton.setIcon(imageBundler.edit());
		securityzoneEditButtonLabel.setLabelSeparator("");
		container.add(securityzoneEditButtonLabel, new VerticalLayoutData(-1, -1, new Margins(5, 0,
		        5, 10)));
		assignedSecurityzoneName.setReadOnly(true);
		calculatedAvailabilityName.setReadOnly(true);
		container.add(new FieldLabel(calculatedAvailabilityName,
		        "Berechneter Schutzbedarf  Verfügbarkeit"), getHalfFormData());
		calculatedConfidentialityName.setReadOnly(true);
		container.add(new FieldLabel(calculatedConfidentialityName,
		        "Berechneter Schutzbedarf  Vertraulichkeit"), getHalfFormData());
		calculatedIntegrityName.setReadOnly(true);
		container.add(new FieldLabel(calculatedIntegrityName,
		        "Berechneter Schutzbedarf  Integrität"), getHalfFormData());

		AssetDTOProperties assetProps = GWT.create(AssetDTOProperties.class);
		assetStore = new TreeStore<AssetDTO>(assetProps.key());
		assetStore.addSortInfo(new StoreSortInfo<AssetDTO>(assetProps.valueProviderLabel(),
		        SortDir.ASC));
		assetTree = new Tree<AssetDTO, String>(assetStore, assetProps.valueProviderLabel()) {
			@Override
			protected void onDoubleClick(Event event) {
				AssetDTO node = getSelectionModel().getSelectedItem();
				if (node != null) {
					new EditorWindow<AssetDTO>(new AssetEditorPresenter(new AssetEditor(),
					        node.getDomainId()), node, "Asset-Beziehung bearbeiten");
				}
				super.onDoubleClick(event);
			}
		};
		assetTree.setIconProvider(new IconProvider<AssetDTO>() {
			@Override
			public ImageResource getIcon(AssetDTO model) {
				if (model != null) {
					return ImageHelper.getImageResourceFromIconName((model).getTreeIconName());
				}

				return ImageHelper.getImageResourceFromIconName("");
			}

		});

		assetTree.setHeight(200);
		assetTree.setBorders(true);
		assetmodelTreePanelLabel = new FieldLabel(assetTree, "Verknüpfte Asset-Beziehungen");
		assetmodelTreePanelLabel.hide();
		container.add(assetmodelTreePanelLabel, getFormData());

		addButtons();
	}

	public HasSelectHandlers getAddInformationButtonClick() {
		return informationsContainer.getAddButtonClick();
	}

	public HasSelectHandlers getAddOccupationButtonClick() {
		return occupationsContainer.getAddButtonClick();
	}

	@Override
	public ListStore<InformationDTO> getInformationStore() {
		return informationStore;
	}

	@Override
	public void setInformationLabel(String text) {
		informationsContainer.setText(text);
	}

	@Override
	public void setAddInformationButtonText(String text) {
		informationsContainer.setAddButtonText(text);
	}

	@Override
	public ListStore<OccupationDTO> getOccupationStore() {
		return occupationStore;
	}

	@Override
	public void setOccupationLabel(String text) {
		occupationsContainer.setText(text);
	}

	@Override
	public void setAddOccupationButtonText(String text) {
		occupationsContainer.setAddButtonText(text);
	}

	@Override
	public void setAssetModelTree(List<AssetDTO> result) {
		assetStore.clear();
		for (AssetDTO model : result) {
			if (assetStore.findModel(model) == null) {
				assetStore.add(model);
			}
			processAssetModelDTOChildren(model);
		}
		// XXX: Remove when treeStore add-bug while fitering is fixed. Maybe
		// that is fixed with gxt 3.03 ...
		assetStore.fireEvent(new StoreFilterEvent<GeneralDTO<?>>());
	}

	private void processAssetModelDTOChildren(AssetDTO parent) {
		for (AssetDTO child : parent.getChildren()) {
			if (assetStore.findModel(child) == null) {
				assetStore.add(parent, child);
			}
			processAssetModelDTOChildren(child);
		}
	}

	@Override
	public void setAssetmodelTreePanelVisible(Boolean visible) {
		assetmodelTreePanelLabel.setVisible(visible);
	}

	@Override
	public void setCalculatedAvailabilityTooltip(String toolTip) {
		calculatedAvailabilityName.setToolTip(toolTip);
	}

	@Override
	public void setCalculatedConfidentialityTip(String toolTip) {
		calculatedConfidentialityName.setToolTip(toolTip);
	}

	@Override
	public void setCalculatedIntegrityTooltip(String toolTip) {
		calculatedIntegrityName.setToolTip(toolTip);
	}

	@Override
	public void setAssignedSecurityzoneTooltip(String toolTip) {
		assignedSecurityzoneName.setToolTip(toolTip);
	}

	@Override
	public HasSelectHandlers getEditSecurityzoneButtonClick() {
		return editSecurityzoneButton;
	}

	@Override
	public void setEditSecurityzoneButtonText(String text) {
		editSecurityzoneButton.setText(text);
	}

	@Override
	public void setEditSecurityzoneButtonEnabled(Boolean enabled) {
		editSecurityzoneButton.setEnabled(enabled);
	}

	@Override
    public void setEnabled(Boolean enabled) {
	   surname.setEnabled(enabled);
	   name.setEnabled(enabled);
	   telefon.setEnabled(enabled);
	   assignedSecurityzoneName.setEnabled(enabled);
	   editSecurityzoneButton .setEnabled(enabled);
	   calculatedAvailabilityName.setEnabled(enabled);
	   calculatedConfidentialityName.setEnabled(enabled);
	   calculatedIntegrityName.setEnabled(enabled);
       informationsContainer.setEnabled(enabled);
       occupationsContainer.setEnabled(enabled);
       assetmodelTreePanelLabel.setEnabled(enabled);
    }
}
