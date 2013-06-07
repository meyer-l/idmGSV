package de.gsv.idm.client.presenter.securitylevelchange;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.securitylevelchange.AssetSecurityLevelChangedEvent;
import de.gsv.idm.client.event.db.securitylevelchange.CreatedSecurityLevelChangeEvent;
import de.gsv.idm.client.event.db.securitylevelchange.DeletedSecurityLevelChangeEvent;
import de.gsv.idm.client.presenter.general.GeneralEditGridPresenter;
import de.gsv.idm.client.presenter.informationdomain.asset.AssetEditorPresenter;
import de.gsv.idm.client.view.informationdomain.asset.AssetEditor;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.SecurityLevelChangeDTO;

public class SecurityLevelChangePresenter extends GeneralEditGridPresenter<SecurityLevelChangeDTO> {

	public interface SecurityLevelChangeDisplay extends
	        GeneralEditGridDisplay<SecurityLevelChangeDTO> {
		void setReviewAllButtonText(String text);

		HasSelectHandlers getReviewAllButtonClick();
	}

	private final SecurityLevelChangeDisplay display;
	private Integer domainId;

	public SecurityLevelChangePresenter(SecurityLevelChangeDisplay view, Integer domainId) {
		super(view);
		display = view;
		this.domainId = domainId;
	}

	@Override
	protected void bindBus() {
		eventBus.addHandler(AssetSecurityLevelChangedEvent.TYPE,
		        new GeneralEventHandler<AssetSecurityLevelChangedEvent>() {

			        @Override
			        public void onEvent(final AssetSecurityLevelChangedEvent event) {
				        dbConnector.getSecurityLevelChangeController().create(
				                new SecurityLevelChangeDTO(event.getObject(), event
				                        .getOldSecurityLevels()),
				                new AsyncCallback<SecurityLevelChangeDTO>() {

					                @Override
					                public void onSuccess(SecurityLevelChangeDTO result) {

					                }

					                @Override
					                public void onFailure(Throwable caught) {
						                DBController.getLogger().log(
						                        Level.SEVERE,
						                        "Error while create SecurityLevelChange for Asset "
						                                + event.getObject().getName());
					                }
				                });
			        }
		        });

		eventBus.addHandler(DeletedSecurityLevelChangeEvent.TYPE,
		        new GeneralEventHandler<DeletedSecurityLevelChangeEvent>() {

			        @Override
			        public void onEvent(DeletedSecurityLevelChangeEvent event) {
				        display.removeItem(event.getObject());
			        }
		        });

		eventBus.addHandler(CreatedSecurityLevelChangeEvent.TYPE,
		        new GeneralEventHandler<CreatedSecurityLevelChangeEvent>() {

			        @Override
			        public void onEvent(CreatedSecurityLevelChangeEvent event) {
				        display.addItem(event.getObject());
			        }
		        });
	}

	@Override
	protected void setStore() {
		dbConnector.getSecurityLevelChangeController().getAll(domainId,
		        new AsyncCallback<ArrayList<SecurityLevelChangeDTO>>() {

			        @Override
			        public void onSuccess(ArrayList<SecurityLevelChangeDTO> result) {
				        display.setStore(result);
			        }

			        @Override
			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while calling SecurityLevelChangeImpl.getAll");
			        }
		        });
	}

	@Override
	protected void individualBindView() {
		display.setHeaderText("Schutzbedarf-Änderungen");

		display.addDoubleClickHandler(new CellDoubleClickHandler() {

			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				SecurityLevelChangeDTO change = display.getGridSelectionModel().getSelectedItem();
				new EditorWindow<AssetDTO>(new AssetEditorPresenter(new AssetEditor(), domainId),
				        change.getAsset(), "Asset bearbeiten");
			}
		});

		display.setReviewAllButtonText("Alle Schutzbedarf-Änderungen als kontrolliert speichern");
		display.getReviewAllButtonClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				List<SecurityLevelChangeDTO> list = new ArrayList<SecurityLevelChangeDTO>(display
				        .getStoredItems());

				for (SecurityLevelChangeDTO changeItem : list) {
					changeItem.setReviewed(true);
				}

				dbConnector.getSecurityLevelChangeController().saveEditableList(domainId, list,
				        new AsyncCallback<Boolean>() {

					        @Override
					        public void onFailure(Throwable caught) {
						        DBController.getLogger().log(Level.SEVERE,
						                "Error while saveing SecurityLevelChange-List");
					        }

					        @Override
					        public void onSuccess(Boolean result) {
					        }
				        });
			}
		});

	}

	@Override
	protected void doSaveButtonAction() {
		dbConnector.getSecurityLevelChangeController().saveEditableList(domainId,
		        display.getStoredItems(), new AsyncCallback<Boolean>() {

			        @Override
			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while saveing SecurityLevelChange-List");
			        }

			        @Override
			        public void onSuccess(Boolean result) {
			        }
		        });
	}

}
