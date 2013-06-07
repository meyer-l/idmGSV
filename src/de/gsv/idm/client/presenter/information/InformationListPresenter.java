package de.gsv.idm.client.presenter.information;

import java.util.ArrayList;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.information.CreatedInformationEvent;
import de.gsv.idm.client.event.db.information.DeletedInformationEvent;
import de.gsv.idm.client.event.db.information.UpdatedInformationEvent;
import de.gsv.idm.client.presenter.general.GeneralListPresenter;
import de.gsv.idm.client.presenter.information.InformationEditorPresenter.InformationEditorDisplay;
import de.gsv.idm.client.view.information.InformationEditor;
import de.gsv.idm.client.view.information.InformationListView;
import de.gsv.idm.client.view.properties.ObjectExchange;
import de.gsv.idm.shared.dto.InformationDTO;

public class InformationListPresenter extends GeneralListPresenter<InformationDTO> {

	interface ListDriver extends
	        SimpleBeanEditorDriver<ObjectExchange<InformationDTO>, InformationListView> {
	}

	public interface InformationListDisplay extends GeneralListDisplay<InformationDTO> {
	}

	private ListDriver driver = GWT.create(ListDriver.class);

	private final HandlerManager eventBus;
	private final DBController dbConnector;
	private final InformationEditorDisplay editorDisplay;

	private Integer domainId;
	private final InformationListDisplay display;
	private InformationEditorPresenter editorPresenter;

	public InformationListPresenter(InformationListDisplay display, Integer domain_id) {
		super(display);
		this.dbConnector = DBController.getInstance();
		this.eventBus = dbConnector.getEventBus();

		this.display = display;
		this.domainId = domain_id;
		this.editorDisplay = new InformationEditor();
		editorPresenter = new InformationEditorPresenter(editorDisplay, domain_id);
		setEditor(editorPresenter);
	}

	protected void fillList() {
		dbConnector.getInformationController().getAll(domainId,
		        new AsyncCallback<ArrayList<InformationDTO>>() {
			        public void onSuccess(ArrayList<InformationDTO> result) {
				        display.setList(result);
			        }

			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while calling InformationsImpl.getAll");
			        }
		        });
	}

	@Override
	public IsWidget go() {
		driver.initialize((InformationListView) display);
		driver.edit(objectExchange);
		display.setEditContainer(editorPresenter.go());
		bindGeneralView();
		bind();
		bindView();
		return display.asWidget();
	}

	private void bind() {
		eventBus.addHandler(CreatedInformationEvent.TYPE,
		        new GeneralEventHandler<CreatedInformationEvent>() {
			        public void onEvent(CreatedInformationEvent event) {
				        InformationDTO eventObject = event.getObject();
				        if (eventObject.getDomainId().equals(domainId)) {
					        display.addListEntry(eventObject);
					        if ((editorPresenter.getEditObject() != null && editorPresenter.getEditObject()
							        .getId() == eventObject.getId()) || eventObject.getId() <= 0) {
								display.setSelected(eventObject.getId());
							}
				        }
			        }
		        });

		eventBus.addHandler(UpdatedInformationEvent.TYPE,
		        new GeneralEventHandler<UpdatedInformationEvent>() {
			        public void onEvent(UpdatedInformationEvent event) {
				        InformationDTO eventObject = event.getObject();
				        if (eventObject.getDomainId().equals(domainId)) {
					        display.updateListEntry(eventObject);
					        InformationDTO selected = display.getSelected();
					        if (selected != null && display.getSelected().getId() == eventObject.getId()
							        && editorPresenter.getEditObject() != null
							        && editorPresenter.getEditObject().getId() == eventObject.getId()) {
								editorPresenter.doEdit(eventObject);
							}
				        }
			        }
		        });
		eventBus.addHandler(DeletedInformationEvent.TYPE,
		        new GeneralEventHandler<DeletedInformationEvent>() {
			        public void onEvent(DeletedInformationEvent event) {
			        	InformationDTO eventObject = event.getObject();
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
		display.setAddText("Information hinzufügen");
		display.setListHeading("Verfügbare Informationen:");
		editorDisplay.setEditHeading("Information berarbeiten:");
	}

}
