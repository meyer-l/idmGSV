package de.gsv.idm.client.presenter.gsk;

import java.util.ArrayList;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.presenter.general.GeneralListPresenter;
import de.gsv.idm.client.presenter.gsk.ThreatEditorPresenter.ThreatEditorDisplay;
import de.gsv.idm.client.view.gsk.ThreatEditor;
import de.gsv.idm.client.view.gsk.ThreatListView;
import de.gsv.idm.client.view.properties.ObjectExchange;
import de.gsv.idm.shared.dto.ThreatDTO;

public class ThreatListPresenter extends GeneralListPresenter<ThreatDTO> {

	public interface ThreatsListDisplay extends GeneralListDisplay<ThreatDTO> {

	}

	interface ListDriver extends SimpleBeanEditorDriver<ObjectExchange<ThreatDTO>, ThreatListView> {
	}

	private ListDriver driver = GWT.create(ListDriver.class);

	private final DBController dbController;
	private final ThreatsListDisplay display;
	private final ThreatEditorDisplay editorDisplay;
	private ThreatEditorPresenter editorPresenter;

	public ThreatListPresenter(ThreatsListDisplay display) {
		super(display);
		this.dbController = DBController.getInstance();

		this.display = display;
		editorDisplay = new ThreatEditor();
		editorPresenter = new ThreatEditorPresenter(editorDisplay);
		setEditor(editorPresenter);
	}

	@Override
	public IsWidget go() {
		driver.initialize((ThreatListView) display);
		driver.edit(objectExchange);
		display.setEditContainer(editorPresenter.go());
		display.addLoadEntry(new ThreatDTO(-1, "... Gefährdungen werden vom Server geladen ..."));

		bindGeneralView();
		bind();
		bindView();
		return display.asWidget();

	}

	private void bindView() {
		display.setListHeading("Verfügbare Gefährdungen:");
		editorDisplay.setEditHeading("Gefährdung anzeigen:");
	}

	private void bind() {

	}

	protected void fillList() {

	}

	public void fetchThreats() {
		if (display.getStore().getAll().size()<= 1) {
			dbController.getThreatController().getAll(new AsyncCallback<ArrayList<ThreatDTO>>() {
				public void onSuccess(ArrayList<ThreatDTO> result) {
					display.setList(result);
					if (result.size() > 0) {
						editorPresenter.edit(result.get(0));
						display.setSelected(result.get(0).getId());
					}
				}

				public void onFailure(Throwable caught) {
					DBController.getLogger().log(Level.SEVERE,
					        "Error while calling ThreatsImpl.getAll");
				}
			});

		}
	}

}