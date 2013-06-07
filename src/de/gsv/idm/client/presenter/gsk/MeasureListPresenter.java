package de.gsv.idm.client.presenter.gsk;

import java.util.ArrayList;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.presenter.general.GeneralListPresenter;
import de.gsv.idm.client.presenter.gsk.MeasureEditorPresenter.MeasureEditorDisplay;
import de.gsv.idm.client.view.gsk.MeasureEditor;
import de.gsv.idm.client.view.gsk.MeasureListView;
import de.gsv.idm.client.view.properties.ObjectExchange;
import de.gsv.idm.shared.dto.MeasureDTO;

public class MeasureListPresenter extends GeneralListPresenter<MeasureDTO> {

	public interface MeasuresListDisplay extends GeneralListDisplay<MeasureDTO> {

	}

	interface ListDriver extends
	        SimpleBeanEditorDriver<ObjectExchange<MeasureDTO>, MeasureListView> {
	}

	private ListDriver driver = GWT.create(ListDriver.class);

	private final DBController dbConnector;
	private final MeasuresListDisplay display;
	private final MeasureEditorPresenter editorPresenter;
	private final MeasureEditorDisplay editorDisplay;

	public MeasureListPresenter(MeasuresListDisplay display) {
		super(display);
		this.dbConnector = DBController.getInstance();
		this.display = display;
		editorDisplay = new MeasureEditor();
		editorPresenter = new MeasureEditorPresenter(editorDisplay);
		setEditor(editorPresenter);
	}

	@Override
	public IsWidget go() {
		driver.initialize((MeasureListView) display);
		driver.edit(objectExchange);
		display.setEditContainer(editorPresenter.go());
		display.addLoadEntry(new MeasureDTO(-1, "... Maßnahmen werden vom Server geladen ..."));
		bindGeneralView();
		bind();
		bindView();
		return display.asWidget();
	}

	private void bind() {

	}

	private void bindView() {
		display.setListHeading("Verfügbare Maßnahmen:");
		editorDisplay.setEditHeading("Maßnahme anzeigen:");
	}

	protected void fillList() {

	}

	public void fetchMeasures() {
		if (display.getStore().getAll().size() <= 1) {
			dbConnector.getMeasureController().getAll(new AsyncCallback<ArrayList<MeasureDTO>>() {
				public void onSuccess(ArrayList<MeasureDTO> result) {
					display.setList(result);
					if (result.size() > 0) {
						editorPresenter.edit(result.get(0));
						display.setSelected(result.get(0).getId());
					}
				}

				public void onFailure(Throwable caught) {
					DBController.getLogger().log(Level.SEVERE,
					        "Error while calling MeasuresImpl.getAll");
				}
			});
		}
	}
}