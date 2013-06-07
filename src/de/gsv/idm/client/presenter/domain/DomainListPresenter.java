package de.gsv.idm.client.presenter.domain;

import java.util.ArrayList;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.domain.CreatedDomainEvent;
import de.gsv.idm.client.event.db.domain.DeletedDomainEvent;
import de.gsv.idm.client.event.db.domain.UpdatedDomainEvent;
import de.gsv.idm.client.event.view.domain.ViewDomainViewEvent;
import de.gsv.idm.client.presenter.domain.DomainEditorPresenter.DomainEditorDisplay;
import de.gsv.idm.client.presenter.general.GeneralListPresenter;
import de.gsv.idm.client.view.domain.DomainEditor;
import de.gsv.idm.client.view.domain.DomainListView;
import de.gsv.idm.client.view.properties.ObjectExchange;
import de.gsv.idm.shared.dto.DomainDTO;

public class DomainListPresenter extends GeneralListPresenter<DomainDTO> {

	public static String CHOOSE_HISTORY_STRING = "chooseDomain";

	public interface DomainDisplay extends GeneralListDisplay<DomainDTO> {
		void setCancelText(String text);

		HasSelectHandlers getCancelClick();

		void setCancelVisibility(boolean visibility);
	}

	interface ListDriver extends SimpleBeanEditorDriver<ObjectExchange<DomainDTO>, DomainListView> {
	}

	private ListDriver driver = GWT.create(ListDriver.class);

	private final DBController dbConnector;
	private final DomainDisplay display;
	private final int id;
	private final DomainEditorDisplay editorDisplay;
	private DomainEditorPresenter editorPresenter;

	public DomainListPresenter(DomainDisplay view) {
		this(view, -1);
	}

	public DomainListPresenter(DomainDisplay view, final int id) {
		super(view);
		this.dbConnector = DBController.getInstance();
		this.display = view;
		this.id = id;
		this.editorDisplay = new DomainEditor();
		editorPresenter = new DomainEditorPresenter(editorDisplay);
		setEditor(editorPresenter);
	}

	@Override
	public IsWidget go() {
		driver.initialize((DomainListView) display);
		driver.edit(objectExchange);
		display.setEditContainer(editorPresenter.go());
		bindGeneralView();
		bind();
		bindView();
		return display.asWidget();
	}

	private void bindView() {
		display.setAddText("Informationsverbund hinzufügen");
		display.setCancelText("Abbrechen");
		display.setListHeading("Verfügbare Informationsverbünde:");
		editorDisplay.setEditHeading("Informationsverbund bearbeiten:");
	}

	private void bind() {
		eventBus.addHandler(CreatedDomainEvent.TYPE, new GeneralEventHandler<CreatedDomainEvent>() {
			public void onEvent(CreatedDomainEvent event) {
				DomainDTO eventObject = event.getObject();
				display.addListEntry(eventObject);
				if ((editorPresenter.getEditObject() != null && editorPresenter.getEditObject()
				        .getId() == eventObject.getId()) || eventObject.getId() <= 0) {
					display.setSelected(eventObject.getId());
				}

			}
		});

		eventBus.addHandler(UpdatedDomainEvent.TYPE, new GeneralEventHandler<UpdatedDomainEvent>() {
			public void onEvent(UpdatedDomainEvent event) {
				DomainDTO eventObject = event.getObject();
				display.updateListEntry(eventObject);
				DomainDTO selected = display.getSelected();
				if (selected != null && display.getSelected().getId() == eventObject.getId()
				        && editorPresenter.getEditObject() != null
				        && editorPresenter.getEditObject().getId() == eventObject.getId()) {
					editorPresenter.doEdit(eventObject);
				}
			}
		});
		eventBus.addHandler(DeletedDomainEvent.TYPE, new GeneralEventHandler<DeletedDomainEvent>() {
			public void onEvent(DeletedDomainEvent event) {
				DomainDTO eventObject = event.getObject();
				if (display.getSelected() != null
				        && display.getSelected().getId() == eventObject.getId()
				        && editorPresenter.getEditObject() != null
				        && editorPresenter.getEditObject().getId() == eventObject.getId()) {
					display.setEditContainerVisible(false);
				}
				display.removeListEntry(eventObject);
			}
		});

		if (id != -1) {
			display.setCancelVisibility(true);

			display.getCancelClick().addSelectHandler(new SelectHandler() {
				public void onSelect(SelectEvent event) {
					eventBus.fireEvent(new ViewDomainViewEvent(id));
				}
			});
		}

	}

	@Override
	protected void bindSelectionModel() {
		display.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		display.getSelectionModel().addSelectionChangedHandler(
		        new SelectionChangedHandler<DomainDTO>() {
			        @Override
			        public void onSelectionChanged(SelectionChangedEvent<DomainDTO> event) {
				        if (event.getSelection().size() > 0) {
					        final DomainDTO domain = event.getSelection().get(0);
					        if (display.getEditContainerVisibility() && editorPresenter.openChanges()) {
						        final MessageBox messageBox = new MessageBox("Änderungen verwerfen?",
						                "Sie haben ungespeicherte Änderungen, wollen sie die Änderungen speichern?");
						        messageBox.getButtonBar().clear();
						        TextButton saveButton = new TextButton("Speichern");
						        saveButton.addSelectHandler(new SelectHandler() {

							        @Override
							        public void onSelect(SelectEvent event) {
							        	editorPresenter.doSave();
								        editorPresenter.edit(domain);
								        display.setEditContainerVisible(true);
								        messageBox.hide();
							        }
						        });
						        TextButton abortButton = new TextButton("Verwerfen");
						        abortButton.addSelectHandler(new SelectHandler() {

							        @Override
							        public void onSelect(SelectEvent event) {
								        editorPresenter.edit(domain);
								        display.setEditContainerVisible(true);
								        messageBox.hide();
							        }
						        });
						        messageBox.addButton(abortButton);
						        messageBox.addButton(saveButton);
						        messageBox.show();
					        } else {
						        editorPresenter.edit(domain);
						        editorDisplay.setEnabled(true);
						        display.setEditContainerVisible(true);
					        }

				        } else {
					        editorDisplay.setSaveEnabled(false);
					        editorDisplay.setChooseEnabled(false);
					        editorDisplay.setDeleteEnabled(false);
					        editorDisplay.setSaveAndNewEnabled(false);
					        display.setEditContainerVisible(false);
				        }
			        }
		        });
	}

	protected void fillList() {
		dbConnector.getDomainController().getAll(new AsyncCallback<ArrayList<DomainDTO>>() {
			public void onSuccess(ArrayList<DomainDTO> result) {
				display.setList(result);
				display.setSelected(id);
			}

			public void onFailure(Throwable caught) {
				DBController.getLogger()
				        .log(Level.SEVERE, "Error while calling DomainsImpl.getAll");
			}
		});
	}

}
