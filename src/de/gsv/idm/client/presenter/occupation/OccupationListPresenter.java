package de.gsv.idm.client.presenter.occupation;

import java.util.ArrayList;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.occupation.CreatedOccupationEvent;
import de.gsv.idm.client.event.db.occupation.DeletedOccupationEvent;
import de.gsv.idm.client.event.db.occupation.UpdatedOccupationEvent;
import de.gsv.idm.client.presenter.general.GeneralListPresenter;
import de.gsv.idm.client.presenter.occupation.OccupationEditorPresenter.OccupationEditorDisplay;
import de.gsv.idm.client.view.occupation.OccupationEditor;
import de.gsv.idm.client.view.occupation.OccupationListView;
import de.gsv.idm.client.view.properties.ObjectExchange;
import de.gsv.idm.shared.dto.OccupationDTO;

public class OccupationListPresenter extends GeneralListPresenter<OccupationDTO> {

	interface ListDriver extends
	        SimpleBeanEditorDriver<ObjectExchange<OccupationDTO>, OccupationListView> {
	}

	public interface OccupationListDisplay extends GeneralListDisplay<OccupationDTO> {

	}

	private ListDriver driver = GWT.create(ListDriver.class);

	private final HandlerManager eventBus;
	private final DBController dbConnector;
	private Integer domainId;
	private OccupationListDisplay display;
	private final OccupationEditorDisplay editorDisplay;
	private OccupationEditorPresenter editorPresenter;

	public OccupationListPresenter(OccupationListDisplay view, Integer domain_id) {
		super(view);
		this.dbConnector = DBController.getInstance();
		this.eventBus = dbConnector.getEventBus();

		this.display = view;
		this.domainId = domain_id;
		this.editorDisplay = new OccupationEditor();
		editorPresenter = new OccupationEditorPresenter(editorDisplay, domain_id);
		setEditor(editorPresenter);
	}

	@Override
	public IsWidget go() {
		driver.initialize((OccupationListView) display);
		driver.edit(objectExchange);
		display.setEditContainer(editorPresenter.go());
		bindGeneralView();
		bind();
		bindView();
		return display.asWidget();
	}

	private void bind() {
		eventBus.addHandler(CreatedOccupationEvent.TYPE,
		        new GeneralEventHandler<CreatedOccupationEvent>() {
			        public void onEvent(CreatedOccupationEvent event) {
				        OccupationDTO eventObject = event.getObject();
				        if (eventObject.getDomainId().equals(domainId)) {
					        display.addListEntry(eventObject);
					        if ((editorPresenter.getEditObject() != null && editorPresenter.getEditObject()
							        .getId() == eventObject.getId()) || eventObject.getId() <= 0) {
								display.setSelected(eventObject.getId());
							}
				        }
			        }
		        });

		eventBus.addHandler(UpdatedOccupationEvent.TYPE,
		        new GeneralEventHandler<UpdatedOccupationEvent>() {
			        public void onEvent(UpdatedOccupationEvent event) {
				        OccupationDTO eventObject = event.getObject();
				        if (eventObject.getDomainId().equals(domainId)) {
					        display.updateListEntry(eventObject);
					        OccupationDTO selected = display.getSelected();
					        if (selected != null && display.getSelected().getId() == eventObject.getId()
							        && editorPresenter.getEditObject() != null
							        && editorPresenter.getEditObject().getId() == eventObject.getId()) {
								editorPresenter.doEdit(eventObject);
							}
				        }
			        }
		        });
		eventBus.addHandler(DeletedOccupationEvent.TYPE,
		        new GeneralEventHandler<DeletedOccupationEvent>() {
			        public void onEvent(DeletedOccupationEvent event) {
				        OccupationDTO eventObject = event.getObject();
				        if (display.getStore().findModel(eventObject) != null) {
				        	if (display.getSelected() != null
							        && display.getSelected().getId() == eventObject.getId()
							        && editorPresenter.getEditObject() != null
							        && editorPresenter.getEditObject().getId() == eventObject.getId()) {
								display.setEditContainerVisible(false);
							}
					        display.removeListEntry(eventObject);
				        }

			        }
		        });
	}

	private void bindView() {
		display.setAddText("Dienstposten hinzufügen");
		display.setListHeading("Verfügbare Dienstposten:");
		editorDisplay.setEditHeading("Dienstposten bearbeiten:");
	}

	@Override
	protected void fillList() {
		dbConnector.getOccupationController().getAll(domainId,
		        new AsyncCallback<ArrayList<OccupationDTO>>() {
			        public void onSuccess(ArrayList<OccupationDTO> result) {
				        display.setList(result);
			        }

			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while calling OccupationsImpl.getAll");
			        }
		        });
	}

}
