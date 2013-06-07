package de.gsv.idm.client.presenter.securityzone;

import java.util.ArrayList;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.securityzone.CreatedSecurityzoneEvent;
import de.gsv.idm.client.event.db.securityzone.DeletedSecurityzoneEvent;
import de.gsv.idm.client.event.db.securityzone.UpdatedSecurityzoneEvent;
import de.gsv.idm.client.presenter.general.GeneralListPresenter;
import de.gsv.idm.client.view.properties.ObjectExchange;
import de.gsv.idm.client.view.securityzone.SecurityzoneEditor;
import de.gsv.idm.client.view.securityzone.SecurityzoneListView;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

public class SecurityzoneListPresenter extends GeneralListPresenter<SecurityzoneDTO> {

	public interface SecurityzoneDisplay extends GeneralListDisplay<SecurityzoneDTO> {

	}

	interface ListDriver extends
	        SimpleBeanEditorDriver<ObjectExchange<SecurityzoneDTO>, SecurityzoneListView> {
	}

	private ListDriver driver = GWT.create(ListDriver.class);

	private final DBController dbConnector;
	private final SecurityzoneDisplay display;
	private final SecurityzoneEditor editorDisplay;
	private SecurityzoneEditorPresenter editorPresenter;

	public SecurityzoneListPresenter(SecurityzoneDisplay display) {
		super(display);
		this.dbConnector = DBController.getInstance();
		this.display = display;
		this.editorDisplay = new SecurityzoneEditor();
		editorPresenter = new SecurityzoneEditorPresenter(editorDisplay);
		setEditor(editorPresenter);
	}

	@Override
	public IsWidget go() {
		driver.initialize((SecurityzoneListView) display);
		driver.edit(objectExchange);
		display.setEditContainer(editorPresenter.go());
		bindGeneralView();
		bind();
		bindView();
		return display.asWidget();

	}

	private void bindView() {
		display.setAddText("Schutzzone hinzufügen");
		display.setListHeading("Schutzzonen Kategorien:");
		editorDisplay.setEditHeading("Schutzzone bearbeiten:");
	}

	private void bind() {
		eventBus.addHandler(CreatedSecurityzoneEvent.TYPE,
		        new GeneralEventHandler<CreatedSecurityzoneEvent>() {
			        public void onEvent(CreatedSecurityzoneEvent event) {
				        SecurityzoneDTO eventObject = event.getObject();
				        display.addListEntry(eventObject);
				        if ((editorPresenter.getEditObject() != null && editorPresenter
				                .getEditObject().getId() == eventObject.getId())
				                || eventObject.getId() <= 0) {
					        display.setSelected(eventObject.getId());
				        }
			        }
		        });

		eventBus.addHandler(UpdatedSecurityzoneEvent.TYPE,
		        new GeneralEventHandler<UpdatedSecurityzoneEvent>() {
			        public void onEvent(UpdatedSecurityzoneEvent event) {
				        SecurityzoneDTO eventObject = event.getObject();
				        display.updateListEntry(eventObject);
				        SecurityzoneDTO selected = display.getSelected();
				        if (selected != null
				                && display.getSelected().getId() == eventObject.getId()
				                && editorPresenter.getEditObject() != null
				                && editorPresenter.getEditObject().getId() == eventObject.getId()) {
					        editorPresenter.doEdit(eventObject);
				        }
			        }
		        });
		eventBus.addHandler(DeletedSecurityzoneEvent.TYPE,
		        new GeneralEventHandler<DeletedSecurityzoneEvent>() {
			        public void onEvent(DeletedSecurityzoneEvent event) {
				        SecurityzoneDTO eventObject = event.getObject();
				        if (display.getSelected() != null
				                && display.getSelected().getId() == eventObject.getId()
				                && editorPresenter.getEditObject() != null
				                && editorPresenter.getEditObject().getId() == eventObject.getId()) {
					        display.setEditContainerVisible(false);
				        }
				        display.removeListEntry(eventObject);
			        }
		        });

	}

	protected void fillList() {
		dbConnector.getSecurityzoneController().getAll(
		        new AsyncCallback<ArrayList<SecurityzoneDTO>>() {
			        public void onSuccess(ArrayList<SecurityzoneDTO> result) {
				        display.setList(result);
			        }

			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while fetching Securityzones");
			        }
		        });
	}

}
