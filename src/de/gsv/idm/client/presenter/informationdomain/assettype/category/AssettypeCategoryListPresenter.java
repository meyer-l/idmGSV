package de.gsv.idm.client.presenter.informationdomain.assettype.category;

import java.util.ArrayList;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.assettype.category.CreatedAssettypeCategoryEvent;
import de.gsv.idm.client.event.db.assettype.category.DeletedAssettypeCategoryEvent;
import de.gsv.idm.client.event.db.assettype.category.UpdatedAssettypeCategoryEvent;
import de.gsv.idm.client.presenter.general.GeneralListPresenter;
import de.gsv.idm.client.view.informationdomain.assettype.category.AssettypeCategoryEditor;
import de.gsv.idm.client.view.informationdomain.assettype.category.AssettypeCategoryListView;
import de.gsv.idm.client.view.properties.ObjectExchange;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;

public class AssettypeCategoryListPresenter extends GeneralListPresenter<AssettypeCategoryDTO> {

	public static final String LIST_HISTORY_STRING = "assetCategories";

	public interface AssettypeCategoryDisplay extends GeneralListDisplay<AssettypeCategoryDTO> {

	}

	interface ListDriver extends
	        SimpleBeanEditorDriver<ObjectExchange<AssettypeCategoryDTO>, AssettypeCategoryListView> {
	}

	private ListDriver driver = GWT.create(ListDriver.class);

	private final DBController dbConnector;
	private final AssettypeCategoryDisplay display;
	private final AssettypeCategoryEditor editorDisplay;
	private AssettypeCategoryEditorPresenter editorPresenter;

	public AssettypeCategoryListPresenter(AssettypeCategoryDisplay display) {
		super(display);
		this.dbConnector = DBController.getInstance();
		this.display = display;
		this.editorDisplay = new AssettypeCategoryEditor();
		editorPresenter = new AssettypeCategoryEditorPresenter(editorDisplay);
		setEditor(editorPresenter);
	}

	@Override
	public IsWidget go() {
		driver.initialize((AssettypeCategoryListView) display);
		driver.edit(objectExchange);
		display.setEditContainer(editorPresenter.go());
		bindGeneralView();
		bind();
		bindView();
		return display.asWidget();

	}

	private void bindView() {
		display.setAddText("Asset-Grundtyp-Kategorie hinzufügen");
		display.setListHeading("Verfügbare Asset-Grundtyp-Kategorien:");
		editorDisplay.setEditHeading("Asset-Grundtyp-Kategorie bearbeiten:");
	}

	private void bind() {
		eventBus.addHandler(CreatedAssettypeCategoryEvent.TYPE,
		        new GeneralEventHandler<CreatedAssettypeCategoryEvent>() {
			        public void onEvent(CreatedAssettypeCategoryEvent event) {
				        AssettypeCategoryDTO eventObject = event.getObject();
				        display.addListEntry(eventObject);
				        if ((editorPresenter.getEditObject() != null && editorPresenter
				                .getEditObject().getId() == eventObject.getId())
				                || eventObject.getId() <= 0) {
					        display.setSelected(eventObject.getId());
				        }
			        }
		        });

		eventBus.addHandler(UpdatedAssettypeCategoryEvent.TYPE,
		        new GeneralEventHandler<UpdatedAssettypeCategoryEvent>() {
			        public void onEvent(UpdatedAssettypeCategoryEvent event) {
				        AssettypeCategoryDTO eventObject = event.getObject();
				        display.updateListEntry(eventObject);
				        AssettypeCategoryDTO selected = display.getSelected();
				        if (selected != null
				                && display.getSelected().getId() == eventObject.getId()
				                && editorPresenter.getEditObject() != null
				                && editorPresenter.getEditObject().getId() == eventObject.getId()) {
					        editorPresenter.doEdit(eventObject);
				        }
			        }
		        });
		eventBus.addHandler(DeletedAssettypeCategoryEvent.TYPE,
		        new GeneralEventHandler<DeletedAssettypeCategoryEvent>() {
			        public void onEvent(DeletedAssettypeCategoryEvent event) {
				        AssettypeCategoryDTO eventObject = event.getObject();
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
		dbConnector.getAssettypeCategoryController().getAll(
		        new AsyncCallback<ArrayList<AssettypeCategoryDTO>>() {
			        public void onSuccess(ArrayList<AssettypeCategoryDTO> result) {
				        display.setList(result);
			        }

			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while calling AssettypesCategoryImpl.getAll");
			        }
		        });
	}

}
