package de.gsv.idm.client.view.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.employee.CreatedEmployeeEvent;
import de.gsv.idm.client.event.db.employee.DeletedEmployeeEvent;
import de.gsv.idm.client.event.db.employee.UpdatedEmployeeEvent;
import de.gsv.idm.client.view.properties.EmployeeDTOProperties;
import de.gsv.idm.client.view.properties.HasKeyAndName;
import de.gsv.idm.client.view.widgets.window.GridToGridRPCChooser;
import de.gsv.idm.shared.dto.EmployeeDTO;

public class EmployeeGridToGridChooser extends GridToGridRPCChooser<EmployeeDTO> {

	@SuppressWarnings("unchecked")
	public EmployeeGridToGridChooser(ListStore<EmployeeDTO> employeeStore, Integer domain_id) {
		super(DBController.getInstance().getEmployeeController(), employeeStore,
		        (HasKeyAndName<EmployeeDTO>) GWT.create(EmployeeDTOProperties.class), domain_id);
		setEastPanelHeadingText("Ausgewählte Mitarbeiter");
		setWestPanelHeadingText("Verfügbare Mitarbeiter");
		setHeadingText("Mitarbeiter hinzufügen");
		bindEventBus();
		bindView();
	}

	public void callInitStores() {
		DBController.getInstance().getEmployeeController()
		        .getAll(domainId, new AsyncCallback<ArrayList<EmployeeDTO>>() {
			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while calling EmployeeImpl.getAll");
			        }

			        public void onSuccess(ArrayList<EmployeeDTO> result) {
				        initStores(result);
			        }
		        });
	}

	private void bindEventBus() {
		eventBus.addHandler(CreatedEmployeeEvent.TYPE,
		        new GeneralEventHandler<CreatedEmployeeEvent>() {
			        public void onEvent(CreatedEmployeeEvent event) {
				        if (event.getObject().getDomainId().equals(domainId)) {
					        toChoose.getStore().add(event.getObject());
				        }

			        }
		        });

		eventBus.addHandler(UpdatedEmployeeEvent.TYPE,
		        new GeneralEventHandler<UpdatedEmployeeEvent>() {
			        public void onEvent(UpdatedEmployeeEvent event) {
				        if (toChoose.getStore().findModel(event.getObject()) != null) {
					        toChoose.getStore().update(event.getObject());
				        }
				        if (selected.getStore().findModel(event.getObject()) != null) {
					        selected.getStore().update(event.getObject());
				        }

			        }
		        });
		eventBus.addHandler(DeletedEmployeeEvent.TYPE,
		        new GeneralEventHandler<DeletedEmployeeEvent>() {
			        public void onEvent(DeletedEmployeeEvent event) {
				        if (toChoose.getStore().findModel(event.getObject()) != null) {
					        toChoose.getStore().remove(event.getObject());
				        }
				        if (selected.getStore().findModel(event.getObject()) != null) {
					        selected.getStore().remove(event.getObject());
				        }
			        }
		        });
	}

	private void bindView() {
		editObject.setText("Mitarbeiter bearbeiten");
		newObject.setText("Neuen Mitarbeiter anlegen");
		verticalButtonBuffer.setWidth("245px");
		newObject.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				new EmployeeEditorWindow(domainId);
			}
		});

		editObject.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				List<EmployeeDTO> selectList = new ArrayList<EmployeeDTO>();
				selectList.addAll(toChoose.getSelectionModel().getSelectedItems());
				selectList.addAll(selected.getSelectionModel().getSelectedItems());
				if (selectList.size() == 1) {
					new EmployeeEditorWindow(selectList.get(0));
				}

			}
		});
	}

}
