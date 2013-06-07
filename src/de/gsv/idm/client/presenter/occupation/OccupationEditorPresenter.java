package de.gsv.idm.client.presenter.occupation;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent.StoreDataChangeHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.information.DeletedInformationEvent;
import de.gsv.idm.client.event.db.information.UpdatedInformationEvent;
import de.gsv.idm.client.event.db.occupation.CreatedOccupationEvent;
import de.gsv.idm.client.event.db.occupation.DeletedOccupationEvent;
import de.gsv.idm.client.event.db.occupation.UpdatedOccupationEvent;
import de.gsv.idm.client.presenter.general.GeneralAssetCheckPresenter;
import de.gsv.idm.client.presenter.securitylevelchange.data.SecurityLevelBundle;
import de.gsv.idm.client.view.information.widgets.InformationGridToGridChooser;
import de.gsv.idm.client.view.occupation.OccupationEditor;
import de.gsv.idm.client.view.securityzone.SecurityzoneEditorWindow;
import de.gsv.idm.shared.dto.InformationDTO;
import de.gsv.idm.shared.dto.OccupationDTO;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

//TODO: In der InformationGridList sollten auch die Informationen aus den dienstposten angezeigt werden.

public class OccupationEditorPresenter extends GeneralAssetCheckPresenter<OccupationDTO> {
	public interface OccupationEditorDisplay extends GeneralEditorDisplay<OccupationDTO> {
		HasSelectHandlers getAddInformationButtonClick();

		ListStore<InformationDTO> getInformationStore();

		void setInformationLabel(String text);

		void setAddInformationButtonText(String text);

		public HasChangeHandlers getManuelSecurityAssesmentCheckBoxClick();

		public void setSecurityAssesmentFieldsEnabled(Boolean bool);

		void setPersonalDataToolTip(String toolTip);

		void setCalculatedAvailabilityTooltip(String toolTip);

		void setCalculatedConfidentialityTip(String toolTip);

		void setCalculatedIntegrityTooltip(String toolTip);

		void setManualAssesmentTooltip(String toolTip);

		void setAssignedSecurityzoneTooltip(String toolTip);

		HasSelectHandlers getEditSecurityzoneButtonClick();

		void setEditSecurityzoneButtonText(String tex);

		void setEditSecurityzoneButtonEnabled(Boolean b);
	}

	interface ItemDriver extends SimpleBeanEditorDriver<OccupationDTO, OccupationEditor> {
	}

	private ItemDriver itemDriver = GWT.create(ItemDriver.class);

	private Integer domain_id;
	private final OccupationEditorDisplay editorDisplay;

	public OccupationEditorPresenter(OccupationEditorDisplay occupationEditor, Integer domain_id) {
		super(DBController.getInstance().getOccupationController(), occupationEditor);
		this.editorDisplay = occupationEditor;
		this.domain_id = domain_id;
	}

	@Override
	public IsWidget go() {
		itemDriver.initialize((OccupationEditor) editorDisplay);
		editorDisplay.setEnabled(false);
		bindBus();
		bindView();
		return editorDisplay.asWidget();

	}

	private void bindBus() {
		eventBus.addHandler(UpdatedInformationEvent.TYPE,
		        new GeneralEventHandler<UpdatedInformationEvent>() {
			        public void onEvent(UpdatedInformationEvent event) {
				        if (editorDisplay.getInformationStore().findModel(event.getObject()) != null) {
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
	}

	public void bindView() {
		editorDisplay.setInformationLabel("Informationen");
		editorDisplay.setAddInformationButtonText("Information hinzufügen");
		editorDisplay.getAddInformationButtonClick().addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				new InformationGridToGridChooser(editorDisplay.getInformationStore(), domain_id);
			}
		});

		editorDisplay.getManuelSecurityAssesmentCheckBoxClick().addChangeHandler(
		        new ChangeHandler() {
			        public void onChange(ChangeEvent event) {
				        CheckBox check = (CheckBox) event.getSource();
				        if (check.getValue()) {
					        editorDisplay.setSecurityAssesmentFieldsEnabled(true);
				        } else {
					        editorDisplay.setSecurityAssesmentFieldsEnabled(false);
				        }
			        }
		        });

		editorDisplay.setPersonalDataToolTip("Bei Personbezug wird für das Schutzziel"
		        + " Vertraulichkeit immer der Wert 'Sehr Hoch' propagiert.");

		editorDisplay.setCalculatedAvailabilityTooltip("Aus den verknüpften Informationen"
		        + "berechneter Schutzbedarf für die Verfügbarkeit.");

		editorDisplay.setCalculatedConfidentialityTip("Aus den verknüpften Informationen"
		        + " berechneter Schutzbedarf für die Vertraulichkeit.");

		editorDisplay.setCalculatedIntegrityTooltip("Aus den verknüpften Informationen berechneter"
		        + " Schutzbedarf für die Integrität.");

		editorDisplay.setManualAssesmentTooltip("Wenn Verknüpfungen mit Informationen nicht"
		        + " zur Schutzbedarfsermittlung geeignet sind, kann der Schutzbedarf"
		        + " auch manuell festgelegt werden.");

		editorDisplay.setAssignedSecurityzoneTooltip("Aus den verknüpften Informationen"
		        + " abgeleitete Schutzzone");

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
				        OccupationDTO flushed = itemDriver.flush();
				        itemDriver.edit(flushed);
				        editorDisplay.setEditSecurityzoneButtonEnabled(flushed
				                .getAssignedSecurityzone() != null);
			        }
		        });
	}

	@Override
	public void doNew() {
		OccupationDTO newObject = new OccupationDTO(domain_id);
		newObject.setName("Neuer Dienstposten");
		newObject.setId(createTempId());
		eventBus.fireEvent(new CreatedOccupationEvent(newObject));
		doEdit(newObject);
	}

	@Override
	public void doSave() {
		OccupationDTO flushObject = itemDriver.flush();
		if (clonedEditObject.getId() == null || flushObject.getId()< 0) {
			createObject(flushObject);
		} else {
			updateObject(flushObject, clonedEditObject.clone());
		}
	}

	@Override
	public void doDelete() {
		final OccupationDTO toDelete = itemDriver.flush();
		if(toDelete != null && toDelete.getId() != null && toDelete.getId() > 0){
			deleteObject(toDelete);
		} else {
			eventBus.fireEvent(new DeletedOccupationEvent(toDelete));
		}

	}

	public void doEdit(OccupationDTO object) {
		resetScrollBar();
		referenceObject = object;
		clonedEditObject = object.clone();
		itemDriver.edit(object.clone());
		editorDisplay.setSaveEnabled(true);
		editorDisplay.setSaveAndNewEnabled(true);
		editorDisplay.setSecurityAssesmentFieldsEnabled(object.isManuelSecurityAssesment());
		editorDisplay.setEditSecurityzoneButtonEnabled(clonedEditObject
		        .getAssignedSecurityzone() != null);
		editorDisplay.setDeleteEnabled(true);
	}

	@Override
	protected void addUpdateEventHandler(final HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels) {
		eventBus.addHandler(UpdatedOccupationEvent.TYPE,
		        new GeneralEventHandler<UpdatedOccupationEvent>() {

			        @Override
			        public void onEvent(UpdatedOccupationEvent event) {
				        checkForChanges(oldSecuriytLevels);
				        eventBus.removeHandler(UpdatedOccupationEvent.TYPE, this);
			        }
		        });
	}

	@Override
	protected void addDeleteEventHandler(
	        final HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels, final Integer oldId) {
		eventBus.addHandler(DeletedOccupationEvent.TYPE,
		        new GeneralEventHandler<DeletedOccupationEvent>() {

			        @Override
			        public void onEvent(DeletedOccupationEvent event) {
				        if (event.getObject().getId().equals(oldId)) {
					        checkForChanges(oldSecuriytLevels);
					        eventBus.removeHandler(DeletedOccupationEvent.TYPE, this);
				        }
			        }
		        });
	}

	@Override
    protected void fireDeletedEvent() {
		eventBus.fireEvent(new DeletedOccupationEvent(referenceObject));
    }

	@Override
    protected OccupationDTO getFlushedObject() {
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
