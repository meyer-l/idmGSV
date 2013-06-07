package de.gsv.idm.client.presenter.gsk;

import java.util.ArrayList;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.presenter.general.GeneralListPresenter;
import de.gsv.idm.client.presenter.gsk.ModuleEditorPresenter.ModuleEditorDisplay;
import de.gsv.idm.client.view.gsk.ModuleEditor;
import de.gsv.idm.client.view.gsk.ModuleListView;
import de.gsv.idm.client.view.properties.ObjectExchange;
import de.gsv.idm.shared.dto.ModuleDTO;

public class ModuleListPresenter extends GeneralListPresenter<ModuleDTO> {

	public interface ModulesListDisplay extends GeneralListDisplay<ModuleDTO> {

	}

	interface ListDriver extends SimpleBeanEditorDriver<ObjectExchange<ModuleDTO>, ModuleListView> {
	}

	private ListDriver driver = GWT.create(ListDriver.class);

	private final DBController dbConnector;
	private final ModulesListDisplay display;
	private final ModuleEditorPresenter editorPresenter;
	private final ModuleEditorDisplay editorDisplay;

	public ModuleListPresenter(ModulesListDisplay display) {
		super(display);
		this.dbConnector = DBController.getInstance();
		this.display = display;
		editorDisplay = new ModuleEditor();
		editorPresenter = new ModuleEditorPresenter(editorDisplay);
		setEditor(editorPresenter);
	}

	@Override
	public IsWidget go() {
		driver.initialize((ModuleListView) display);
		driver.edit(objectExchange);
		display.setEditContainer(editorPresenter.go());
		display.addLoadEntry(new ModuleDTO(-1, "... Bausteine werden vom Server geladen ..."));
		bindGeneralView();
		bind();
		bindView();
		return display.asWidget();
	}

	private void bindView() {
		display.setListHeading("Verfügbare Bausteine:");
		editorDisplay.setEditHeading("Baustein anzeigen:");
	}

	private void bind() {

	}

	protected void fillList() {

	}

	public void fetchModules() {
		if (display.getStore().getAll().size() <= 1) {
			dbConnector.getModuleController().getAll(new AsyncCallback<ArrayList<ModuleDTO>>() {
				public void onSuccess(ArrayList<ModuleDTO> result) {
					display.setList(result);
					if (result.size() > 0) {
						editorPresenter.edit(result.get(0));
						display.setSelected(result.get(0).getId());
					}

				}

				public void onFailure(Throwable caught) {
					DBController.getLogger().log(Level.SEVERE,
					        "Error while calling ModulesImpl.getAll");
				}
			});

		}
	}

}
