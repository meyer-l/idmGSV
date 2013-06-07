package de.gsv.idm.client.presenter.changeevent;

import java.util.List;
import java.util.logging.Level;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.info.Info;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.presenter.general.GeneralEditGridPresenter;
import de.gsv.idm.client.view.changeevent.ChangedAppliedView;
import de.gsv.idm.client.view.widgets.GeneralWindow;
import de.gsv.idm.shared.dto.ChangeAppliedDTO;
import de.gsv.idm.shared.dto.ChangeEventDTO;
import de.gsv.idm.shared.dto.GeneralDTO;

public abstract class EditableChangesPresenter extends GeneralEditGridPresenter<ChangeEventDTO> {

	public interface EditableChangesDisplay extends GeneralEditGridDisplay<ChangeEventDTO> {

		void setAutomaticTooltip(String tooltip);

		void setManualTooltip(String tooltip);

		void setNotNeededTooltip(String tooltip);
	}

	private EditableChangesDisplay display;
	private Integer domainId;

	public EditableChangesPresenter(EditableChangesDisplay view, Integer domainId) {
		super(view);
		display = view;
		this.domainId = domainId;
	}

	@Override
	protected void bindView() {
		super.bindView();
		display.setAutomaticTooltip("Die Änderung wird automatisch in den Informationsverbund"
		        + " eingearbeitet. Wenn die Änderungen neue Assets beinhalten, steht die"
		        + " Option nicht zur verfügung.");
		display.setManualTooltip("Die Änderung wird manuell in den Informationsverbund"
		        + " eingearbeitet und der Umsetzungsstatus kann im"
				+" 'Manuelle Änderungen'-Tab dokumentiert werden.");
		display.setNotNeededTooltip("Die Änderung muss nicht in den Informationsverbund"
		        + " eingearbeitet werden.");
	}
	
	public void refreshView(){
		display.refreshGrid();
	}

	protected abstract void setStore();

	protected abstract void individualBindView();

	@Override
    protected void doSaveButtonAction() {
		dbConnector.getChangeEventController().saveEditableList(domainId,
		        display.getStoredItems(), new AsyncCallback<List<ChangeAppliedDTO>>() {

			        @Override
			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while saveing ChangeEventList");
			        }

			        @Override
			        public void onSuccess(List<ChangeAppliedDTO> result) {
			        	for(ChangeAppliedDTO appliedChange : result) {
			        		GeneralDTO<?> changedObject = appliedChange.getObject();
			        		String text = changedObject.getClassName()+ " " +changedObject.getLabel()+ " wurde aktualisiert. (ID: "+changedObject.getId()+")";
							Info.display("RPC:", text);
			        	}
				        if (result.size() > 0) {
					        new GeneralWindow(new ChangedAppliedPresenter(
					                new ChangedAppliedView(), result),
					                "Ergebnis der automatisch verarbeiteten Änderungen");
				        }

			        }
		        });
    }

}
