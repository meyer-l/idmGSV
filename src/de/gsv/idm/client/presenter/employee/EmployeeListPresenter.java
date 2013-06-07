package de.gsv.idm.client.presenter.employee;

import java.util.ArrayList;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.employee.CreatedEmployeeEvent;
import de.gsv.idm.client.event.db.employee.DeletedEmployeeEvent;
import de.gsv.idm.client.event.db.employee.UpdatedEmployeeEvent;
import de.gsv.idm.client.presenter.employee.EmployeeEditorPresenter.EmployeeEditorDisplay;
import de.gsv.idm.client.presenter.general.GeneralListPresenter;
import de.gsv.idm.client.view.employee.EmployeeEditor;
import de.gsv.idm.client.view.employee.EmployeeListView;
import de.gsv.idm.client.view.properties.ObjectExchange;
import de.gsv.idm.shared.dto.EmployeeDTO;

public class EmployeeListPresenter extends GeneralListPresenter<EmployeeDTO> {

	interface ListDriver extends
	        SimpleBeanEditorDriver<ObjectExchange<EmployeeDTO>, EmployeeListView> {
	}

	public interface EmployeeListDisplay extends GeneralListDisplay<EmployeeDTO> {
	}

	private ListDriver driver = GWT.create(ListDriver.class);

	private final DBController dbConnector;;
	private EmployeeEditorPresenter editorPresenter;

	private Integer domainId;
	private final EmployeeListDisplay display;
	private final EmployeeEditorDisplay editorDisplay;

	public EmployeeListPresenter(EmployeeListDisplay display, Integer domain_id) {
		super(display);
		this.dbConnector = DBController.getInstance();
		this.display = display;
		this.domainId = domain_id;
		editorDisplay = new EmployeeEditor();
		editorPresenter = new EmployeeEditorPresenter(editorDisplay, domain_id);
		setEditor(editorPresenter);
	}

	protected void fillList() {
		dbConnector.getEmployeeController().getAll(domainId,
		        new AsyncCallback<ArrayList<EmployeeDTO>>() {
			        public void onSuccess(ArrayList<EmployeeDTO> result) {
				        display.setList(result);
			        }

			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while calling EmployeesImpl.getAll");
			        }
		        });
	}

	@Override
	public IsWidget go() {
		driver.initialize((EmployeeListView) display);
		driver.edit(objectExchange);
		display.setEditContainer(editorPresenter.go());
		bindGeneralView();
		bind();
		bindView();
		return display.asWidget();
	}

	private void bind() {
		eventBus.addHandler(CreatedEmployeeEvent.TYPE,
		        new GeneralEventHandler<CreatedEmployeeEvent>() {
			        public void onEvent(CreatedEmployeeEvent event) {
				        if (event.getObject().getDomainId().equals(domainId)) {
					        EmployeeDTO eventObject = event.getObject();
					        display.addListEntry(eventObject);
					        if ((editorPresenter.getEditObject() != null && editorPresenter.getEditObject()
							        .getId() == eventObject.getId()) || eventObject.getId() <= 0) {
								display.setSelected(eventObject.getId());
							}
				        }
			        }
		        });

		eventBus.addHandler(UpdatedEmployeeEvent.TYPE,
		        new GeneralEventHandler<UpdatedEmployeeEvent>() {
			        public void onEvent(UpdatedEmployeeEvent event) {
				        if (event.getObject().getDomainId().equals(domainId)) {
					        EmployeeDTO eventObject = event.getObject();
					        display.updateListEntry(eventObject);
					        EmployeeDTO selected = display.getSelected();
					        if (selected != null && display.getSelected().getId() == eventObject.getId()
							        && editorPresenter.getEditObject() != null
							        && editorPresenter.getEditObject().getId() == eventObject.getId()) {
								editorPresenter.doEdit(eventObject);
							}
				        }

			        }
		        });
		eventBus.addHandler(DeletedEmployeeEvent.TYPE,
		        new GeneralEventHandler<DeletedEmployeeEvent>() {
			        public void onEvent(DeletedEmployeeEvent event) {
				        EmployeeDTO eventObject = event.getObject();
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
		display.setAddText("Mitarbeiter hinzufügen");
		display.setListHeading("Verfügbare Mitarbeiter:");
		editorDisplay.setEditHeading("Mitarbeiter berarbeiten:");
	}

}
