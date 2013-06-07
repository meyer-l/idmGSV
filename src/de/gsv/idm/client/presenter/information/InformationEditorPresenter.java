package de.gsv.idm.client.presenter.information;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.information.CreatedInformationEvent;
import de.gsv.idm.client.event.db.information.DeletedInformationEvent;
import de.gsv.idm.client.event.db.information.UpdatedInformationEvent;
import de.gsv.idm.client.event.db.securityzone.CreatedSecurityzoneEvent;
import de.gsv.idm.client.event.db.securityzone.DeletedSecurityzoneEvent;
import de.gsv.idm.client.presenter.general.GeneralAssetCheckPresenter;
import de.gsv.idm.client.presenter.securitylevelchange.data.SecurityLevelBundle;
import de.gsv.idm.client.view.information.InformationEditor;
import de.gsv.idm.client.view.securityzone.SecurityzoneListWindow;
import de.gsv.idm.shared.dto.InformationDTO;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

public class InformationEditorPresenter extends GeneralAssetCheckPresenter<InformationDTO> {

	public interface InformationEditorDisplay extends GeneralEditorDisplay<InformationDTO> {
		ListStore<SecurityzoneDTO> getSecurityzoneStore();

		void setMeasuresLabel(String text);

		SecurityzoneDTO getPreviousSecurityzone();

		void addSecurityzoneComboBoxSelectionHandler(
		        SelectionHandler<SecurityzoneDTO> selectionHandler);

		ListStore<MeasureDTO> getMeasuresStore();

		void setPersonalDataTooltip(String toolTip);

		void setChooseSecurityzoneTooltip(String toolTip);

		HasSelectHandlers getEditSecurityzonesButtonClick();

		void setEditSecurityzonesButtonText(String text);

		void clearSecurityzoneBox(SecurityzoneDTO object);
	}

	interface ItemDriver extends SimpleBeanEditorDriver<InformationDTO, InformationEditor> {
	}

	private final InformationEditorDisplay editorDisplay;
	private ItemDriver itemDriver = GWT.create(ItemDriver.class);
	private Integer domainId;

	public InformationEditorPresenter(InformationEditorDisplay informationEditor, Integer domain_id) {
		super(DBController.getInstance().getInformationController(), informationEditor);
		this.editorDisplay = informationEditor;
		this.domainId = domain_id;
	}

	@Override
	public IsWidget go() {
		itemDriver.initialize((InformationEditor) editorDisplay);
		editorDisplay.setEnabled(false);
		bindView();
		bindBus();
		return editorDisplay.asWidget();
	}

	private void bindBus() {
		eventBus.addHandler(CreatedSecurityzoneEvent.TYPE,
		        new GeneralEventHandler<CreatedSecurityzoneEvent>() {

			        @Override
			        public void onEvent(CreatedSecurityzoneEvent event) {
				        SecurityzoneDTO object = event.getObject();

				        if (object != null && object.getId() > 0
				                && editorDisplay.getSecurityzoneStore().findModel(object) == null) {
					        editorDisplay.getSecurityzoneStore().add(object);
				        }
			        }
		        });

		eventBus.addHandler(DeletedSecurityzoneEvent.TYPE,
		        new GeneralEventHandler<DeletedSecurityzoneEvent>() {

			        @Override
			        public void onEvent(DeletedSecurityzoneEvent event) {
				        SecurityzoneDTO object = event.getObject();

				        if (editorDisplay.getSecurityzoneStore().findModel(object) != null) {
					        editorDisplay.clearSecurityzoneBox(object);
					        editorDisplay.getSecurityzoneStore().remove(object);
				        }
			        }
		        });
	}

	private void bindView() {
		DBController.getInstance().getSecurityzoneController()
		        .getAll(new AsyncCallback<ArrayList<SecurityzoneDTO>>() {

			        @Override
			        public void onSuccess(ArrayList<SecurityzoneDTO> result) {
				        editorDisplay.getSecurityzoneStore().replaceAll(result);
			        }

			        @Override
			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while calling SecurityzoneImpl.getAll");
			        }
		        });
		editorDisplay.setMeasuresLabel("Umzusetzende Maßnahmen");

		editorDisplay
		        .addSecurityzoneComboBoxSelectionHandler(new SelectionHandler<SecurityzoneDTO>() {

			        @Override
			        public void onSelection(SelectionEvent<SecurityzoneDTO> event) {
				        ListStore<MeasureDTO> store = editorDisplay.getMeasuresStore();
				        if (editorDisplay.getPreviousSecurityzone() != null) {
					        for (MeasureDTO measure : editorDisplay.getPreviousSecurityzone()
					                .getAllMeasures()) {
						        store.remove(measure);
					        }
				        }
				        store.addAll(event.getSelectedItem().getAllMeasures());
			        }
		        });
		editorDisplay.setPersonalDataTooltip("Bei Personbezug wird für das Schutzziel"
		        + " Vertraulichkeit immer der Wert 'Sehr Hoch' propagiert.");
		editorDisplay.setChooseSecurityzoneTooltip("Die Schutzzone legt die Maßnahmen fest,"
		        + " die in Assets zum Schutz dieser Information umgesetzt werden müssen.");
		editorDisplay.setEditSecurityzonesButtonText("Schutzzonen berarbeiten");
		editorDisplay.getEditSecurityzonesButtonClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				new SecurityzoneListWindow();
			}
		});
	}

	public void doSave() {
		InformationDTO flushObject = itemDriver.flush();
		if (clonedEditObject.getId() == null || flushObject.getId() < 0) {
			createObject(flushObject);
		} else {
			updateObject(flushObject, clonedEditObject.clone());
		}
	}

	public void doDelete() {
		final InformationDTO toDelete = itemDriver.flush();
		if (toDelete != null && toDelete.getId() != null && toDelete.getId() > 0) {
			deleteObject(toDelete);
		} else {
			eventBus.fireEvent(new DeletedInformationEvent(toDelete));
		}
	}

	public void doEdit(InformationDTO object) {
		resetScrollBar();
		referenceObject = object;
		clonedEditObject = object.clone();
		itemDriver.edit(object.clone());
		editorDisplay.setSaveEnabled(true);
		editorDisplay.setSaveAndNewEnabled(true);
		editorDisplay.setDeleteEnabled(true);
	}

	@Override
	public void doNew() {
		InformationDTO newObject = new InformationDTO(domainId);
		newObject.setName("Neue Information");
		newObject.setId(createTempId());
		eventBus.fireEvent(new CreatedInformationEvent(newObject));
		doEdit(newObject);
	}

	@Override
	protected void addUpdateEventHandler(
	        final HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels) {
		eventBus.addHandler(UpdatedInformationEvent.TYPE,
		        new GeneralEventHandler<UpdatedInformationEvent>() {

			        @Override
			        public void onEvent(UpdatedInformationEvent event) {
				        checkForChanges(oldSecuriytLevels);
				        eventBus.removeHandler(UpdatedInformationEvent.TYPE, this);
			        }
		        });
	}

	@Override
	protected void addDeleteEventHandler(
	        final HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels, final Integer oldId) {
		eventBus.addHandler(DeletedInformationEvent.TYPE,
		        new GeneralEventHandler<DeletedInformationEvent>() {

			        @Override
			        public void onEvent(DeletedInformationEvent event) {
				        if (event.getObject().getId().equals(oldId)) {
					        checkForChanges(oldSecuriytLevels);
					        eventBus.removeHandler(DeletedInformationEvent.TYPE, this);
				        }
			        }
		        });
	}

	@Override
	protected void fireDeletedEvent() {
		eventBus.fireEvent(new DeletedInformationEvent(referenceObject));
	}

	@Override
	protected InformationDTO getFlushedObject() {
		return itemDriver.flush();
	}

	public boolean openChanges() {
		if (clonedEditObject != null) {
			return itemDriver.isDirty();
		} else {
			return false;
		}

	}
}
