package de.gsv.idm.client.presenter.changeevent;

import java.util.List;

import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import de.gsv.idm.client.presenter.employee.EmployeeEditorPresenter;
import de.gsv.idm.client.presenter.general.GeneralGridPresenter;
import de.gsv.idm.client.presenter.informationdomain.asset.AssetEditorPresenter;
import de.gsv.idm.client.presenter.occupation.OccupationEditorPresenter;
import de.gsv.idm.client.view.employee.EmployeeEditor;
import de.gsv.idm.client.view.informationdomain.asset.AssetEditor;
import de.gsv.idm.client.view.occupation.OccupationEditor;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.ChangeAppliedDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.OccupationDTO;

public class ChangedAppliedPresenter extends GeneralGridPresenter<ChangeAppliedDTO> {

	public interface ChangedAppliedDisplay extends GeneralGridDisplay<ChangeAppliedDTO> {

		public void setEditObjectText(String text);

		public HasSelectHandlers getEditObjectClick();

		public void setEditObjectEnabled(Boolean bool);
	}

	private ChangedAppliedDisplay display;
	private List<ChangeAppliedDTO> storeList;

	public ChangedAppliedPresenter(ChangedAppliedDisplay view, List<ChangeAppliedDTO> storeList) {
		super(view);
		display = view;
		this.storeList = storeList;
	}

	@Override
	protected void bindBus() {

	}

	@Override
	protected void bindView() {
		display.setEditObjectEnabled(false);
		display.setStore(storeList);
		display.addDoubleClickHandler(new CellDoubleClickHandler() {

			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				openEditor();
			}

		});

		display.setEditObjectText("Geändertes Objekt einsehen");
		display.getEditObjectClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				openEditor();
			}
		});

		display.getGridSelectionModel().addSelectionChangedHandler(
		        new SelectionChangedHandler<ChangeAppliedDTO>() {

			        @Override
			        public void onSelectionChanged(SelectionChangedEvent<ChangeAppliedDTO> event) {
				        if (display.getGridSelectionModel().getSelectedItems().size() == 1) {
					        display.setEditObjectEnabled(true);
				        } else {
					        display.setEditObjectEnabled(false);
				        }
			        }
		        });
	}

	private void openEditor() {
		ChangeAppliedDTO object = display.getGridSelectionModel().getSelectedItem();
		if (object.getObject() instanceof AssetDTO) {
			AssetDTO asset = (AssetDTO) object.getObject();
			new EditorWindow<AssetDTO>(new AssetEditorPresenter(new AssetEditor(),
			        asset.getDomainId()), asset, "Asset bearbeiten");
		} else if (object.getObject() instanceof OccupationDTO) {
			OccupationDTO occupation = (OccupationDTO) object.getObject();
			new EditorWindow<OccupationDTO>(new OccupationEditorPresenter(new OccupationEditor(),
			        occupation.getDomainId()), occupation, "Dienstposten bearbeiten");
		} else if (object.getObject() instanceof EmployeeDTO) {
			EmployeeDTO employee = (EmployeeDTO) object.getObject();
			new EditorWindow<EmployeeDTO>(new EmployeeEditorPresenter(new EmployeeEditor(),
			        employee.getDomainId()), employee, "Mitarbeiter bearbeiten");
		}
	}
}
