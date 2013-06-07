package de.gsv.idm.client.presenter.employee;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent.StoreDataChangeHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.employee.CreatedEmployeeEvent;
import de.gsv.idm.client.event.db.employee.DeletedEmployeeEvent;
import de.gsv.idm.client.event.db.employee.UpdatedEmployeeEvent;
import de.gsv.idm.client.event.db.information.DeletedInformationEvent;
import de.gsv.idm.client.event.db.information.UpdatedInformationEvent;
import de.gsv.idm.client.event.db.occupation.DeletedOccupationEvent;
import de.gsv.idm.client.event.db.occupation.UpdatedOccupationEvent;
import de.gsv.idm.client.presenter.general.GeneralAssetCheckPresenter;
import de.gsv.idm.client.presenter.securitylevelchange.data.SecurityLevelBundle;
import de.gsv.idm.client.view.employee.EmployeeEditor;
import de.gsv.idm.client.view.information.widgets.InformationGridToGridChooser;
import de.gsv.idm.client.view.occupation.widgets.OccupationGridToGridChooser;
import de.gsv.idm.client.view.securityzone.SecurityzoneEditorWindow;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.InformationDTO;
import de.gsv.idm.shared.dto.OccupationDTO;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

public class EmployeeEditorPresenter extends GeneralAssetCheckPresenter<EmployeeDTO> {

	public interface EmployeeEditorDisplay extends GeneralEditorDisplay<EmployeeDTO> {
		HasSelectHandlers getAddInformationButtonClick();

		HasSelectHandlers getAddOccupationButtonClick();

		ListStore<InformationDTO> getInformationStore();

		void setInformationLabel(String text);

		void setAddInformationButtonText(String text);

		ListStore<OccupationDTO> getOccupationStore();

		void setOccupationLabel(String text);

		void setAddOccupationButtonText(String text);

		void setAssetModelTree(List<AssetDTO> result);

		public void setAssetmodelTreePanelVisible(Boolean visibile);

		void setCalculatedAvailabilityTooltip(String toolTip);

		void setCalculatedConfidentialityTip(String toolTip);

		void setCalculatedIntegrityTooltip(String toolTip);

		void setAssignedSecurityzoneTooltip(String toolTip);
		
		HasSelectHandlers getEditSecurityzoneButtonClick();
		
		void setEditSecurityzoneButtonText(String tex);
		
		void setEditSecurityzoneButtonEnabled(Boolean enabled);
	}

	interface ItemDriver extends SimpleBeanEditorDriver<EmployeeDTO, EmployeeEditor> {
	}

	private ItemDriver itemDriver = GWT.create(ItemDriver.class);

	private final EmployeeEditorDisplay editorDisplay;
	private Integer domain_id;

	public EmployeeEditorPresenter(EmployeeEditorDisplay employeeEditor, Integer domain_id) {
		super(DBController.getInstance().getEmployeeController(), employeeEditor);
		this.editorDisplay = employeeEditor;
		this.domain_id = domain_id;
	}

	@Override
	public IsWidget go() {
		itemDriver.initialize((EmployeeEditor) editorDisplay);
		editorDisplay.setEnabled(false);
		bindBus();
		bindView();
		return editorDisplay.asWidget();
	}

	private void bindBus() {
		eventBus.addHandler(UpdatedInformationEvent.TYPE,
		        new GeneralEventHandler<UpdatedInformationEvent>() {
			        public void onEvent(UpdatedInformationEvent event) {
				        if (editorDisplay.getInformationStore().hasRecord(event.getObject())) {
					        editorDisplay.getInformationStore().update(event.getObject());
				        }
			        }
		        });
		eventBus.addHandler(DeletedInformationEvent.TYPE,
		        new GeneralEventHandler<DeletedInformationEvent>() {
			        public void onEvent(DeletedInformationEvent event) {
				        if (editorDisplay.getInformationStore().findModel(event.getObject()) != null) {
					        editorDisplay.getInformationStore().remove(event.getObject());
				        }
			        }
		        });
		eventBus.addHandler(UpdatedOccupationEvent.TYPE,
		        new GeneralEventHandler<UpdatedOccupationEvent>() {
			        public void onEvent(UpdatedOccupationEvent event) {
				        if (editorDisplay.getOccupationStore().findModel(event.getObject()) != null)
					        editorDisplay.getOccupationStore().update(event.getObject());
			        }
		        });
		eventBus.addHandler(DeletedOccupationEvent.TYPE,
		        new GeneralEventHandler<DeletedOccupationEvent>() {
			        public void onEvent(DeletedOccupationEvent event) {
				        if (editorDisplay.getOccupationStore().hasRecord(event.getObject()))
					        editorDisplay.getOccupationStore().remove(event.getObject());
			        }
		        });

	}

	public void bindView() {
		editorDisplay.setInformationLabel("Zusätzliche Informationen");
		editorDisplay.setAddInformationButtonText("Information hinzufügen");
		editorDisplay.setOccupationLabel("Dienstposten");
		editorDisplay.setAddOccupationButtonText("Dienstposten hinzufügen");

		editorDisplay.getAddInformationButtonClick().addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				new InformationGridToGridChooser(editorDisplay.getInformationStore(), domain_id);
			}
		});
		editorDisplay.getAddOccupationButtonClick().addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				new OccupationGridToGridChooser(editorDisplay.getOccupationStore(), domain_id);
			}
		});

		editorDisplay.setCalculatedAvailabilityTooltip("Aus den verknüpften Informationen und"
		        + " Dienstposten berechneter Schutzbedarf für die Verfügbarkeit.");

		editorDisplay.setCalculatedConfidentialityTip("Aus den verknüpften Informationen und"
		        + " Dienstposten berechneter Schutzbedarf für die Vertraulichkeit.");

		editorDisplay.setCalculatedIntegrityTooltip("Aus den verknüpften Informationen und"
		        + " Dienstposten berechneter Schutzbedarf für die Integrität.");
		editorDisplay.setAssignedSecurityzoneTooltip("Aus den verknüpften Informationen und"
		        + " Dienstposten abgeleitete Schutzzone");
		
		editorDisplay.getEditSecurityzoneButtonClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				SecurityzoneDTO securityzone = itemDriver.flush().getAssignedSecurityzone();
				if(securityzone != null){
					new SecurityzoneEditorWindow(securityzone);
				}
			}
		});

		editorDisplay.setEditSecurityzoneButtonText("Schutzzone berarbeiten");
		
		editorDisplay.getInformationStore().addStoreDataChangeHandler(
		        new StoreDataChangeHandler<InformationDTO>() {

			        @Override
			        public void onDataChange(StoreDataChangeEvent<InformationDTO> event) {
				        EmployeeDTO flushed = itemDriver.flush();
				        itemDriver.edit(flushed);
				        editorDisplay.setEditSecurityzoneButtonEnabled(flushed
				                .getAssignedSecurityzone() != null);
			        }
		        });
		
		editorDisplay.getOccupationStore().addStoreDataChangeHandler(
		        new StoreDataChangeHandler<OccupationDTO>() {

			        @Override
			        public void onDataChange(StoreDataChangeEvent<OccupationDTO> event) {
				        EmployeeDTO flushed = itemDriver.flush();
				        itemDriver.edit(flushed);
				        editorDisplay.setEditSecurityzoneButtonEnabled(flushed
				                .getAssignedSecurityzone() != null);
			        }
		        });
	}

	@Override
	public void doNew() {
		EmployeeDTO newObject = new EmployeeDTO(domain_id);
		newObject.setSurname("Neuer Mitarbeiter");
		newObject.setId(createTempId());
		eventBus.fireEvent(new CreatedEmployeeEvent(newObject));
		doEdit(newObject);
	}

	@Override
	public void doSave() {
		EmployeeDTO flushObject = itemDriver.flush();
		if (clonedEditObject.getId() == null || flushObject.getId()< 0) {
			createObject(flushObject);
		} else {
			updateObject(flushObject, clonedEditObject.clone());
		}
	}

	@Override
	public void doDelete() {
		final EmployeeDTO toDelete = itemDriver.flush();
		if(toDelete != null && toDelete.getId() != null && toDelete.getId() > 0){
			deleteObject(toDelete);
		} else {
			eventBus.fireEvent(new DeletedEmployeeEvent(toDelete));
		}
	}

	@Override
	public void doEdit(EmployeeDTO object) {
		resetScrollBar();
		referenceObject = object;
		clonedEditObject = object.clone();
		itemDriver.edit(object.clone());
		editorDisplay.setSaveEnabled(true);
		editorDisplay.setSaveAndNewEnabled(true);
		if (object.getAssetmodels().size() > 0) {
			editorDisplay.setAssetModelTree(object.getAssetmodels());
			editorDisplay.setAssetmodelTreePanelVisible(true);
		}
		editorDisplay.setEditSecurityzoneButtonEnabled(clonedEditObject
		        .getAssignedSecurityzone() != null);
		editorDisplay.setDeleteEnabled(true);
	}

	@Override
	protected void addUpdateEventHandler(final HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels) {
		eventBus.addHandler(UpdatedEmployeeEvent.TYPE,
		        new GeneralEventHandler<UpdatedEmployeeEvent>() {

			        @Override
			        public void onEvent(UpdatedEmployeeEvent event) {
				        checkForChanges(oldSecuriytLevels);
				        eventBus.removeHandler(UpdatedEmployeeEvent.TYPE, this);
			        }
		        });
	}

	@Override
	protected void addDeleteEventHandler(
	        final HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels, final Integer oldId) {
		eventBus.addHandler(DeletedEmployeeEvent.TYPE,
		        new GeneralEventHandler<DeletedEmployeeEvent>() {

			        @Override
			        public void onEvent(DeletedEmployeeEvent event) {
				        if (event.getObject().getId().equals(oldId)) {
					        checkForChanges(oldSecuriytLevels);
					        eventBus.removeHandler(DeletedEmployeeEvent.TYPE, this);
				        }
			        }
		        });
	}

	@Override
    protected void fireDeletedEvent() {
		eventBus.fireEvent(new DeletedEmployeeEvent(referenceObject));
    }

	@Override
    protected EmployeeDTO getFlushedObject() {
		return itemDriver.flush();
    }
	
	public boolean openChanges() {
		if(clonedEditObject != null){
			return itemDriver.isDirty();
		} else {
			return false;
		}
	    
    }

}
