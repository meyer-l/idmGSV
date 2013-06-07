package de.gsv.idm.client.presenter;

import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.menu.Item;

import de.gsv.idm.client.IdmGsv;
import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ReceivedPushEvent;
import de.gsv.idm.client.event.db.TransactionBufferEvent;
import de.gsv.idm.client.event.view.domain.ViewDomainViewEvent;
import de.gsv.idm.client.event.view.startscreen.StartScreenViewEvent;
import de.gsv.idm.client.presenter.domain.ViewDomainPresenter;
import de.gsv.idm.client.presenter.search.SearchPresenter;
import de.gsv.idm.client.push.listener.AssetLinkListenerAdapter;
import de.gsv.idm.client.push.listener.AssetListenerAdapter;
import de.gsv.idm.client.push.listener.AssettypeCategoryListenerAdapter;
import de.gsv.idm.client.push.listener.AssettypeListenerAdapter;
import de.gsv.idm.client.push.listener.ChangeEventListenerAdapter;
import de.gsv.idm.client.push.listener.DomainListenerAdapter;
import de.gsv.idm.client.push.listener.EmployeeListenerAdapter;
import de.gsv.idm.client.push.listener.InformationListenerAdapter;
import de.gsv.idm.client.push.listener.MeasureListenerAdapter;
import de.gsv.idm.client.push.listener.ModuleListenerAdapter;
import de.gsv.idm.client.push.listener.OccupationListenerAdapter;
import de.gsv.idm.client.push.listener.SecurityLevelChangeListenerAdapter;
import de.gsv.idm.client.push.listener.SecurityzoneListenerAdapter;
import de.gsv.idm.client.push.listener.ThreatListenerAdapter;
import de.gsv.idm.client.transaction.DBTransactionEvent;
import de.gsv.idm.client.transaction.TransactionBuffer;
import de.gsv.idm.client.view.StartScreenView;
import de.gsv.idm.client.view.domain.ViewDomainView;
import de.gsv.idm.client.view.search.SearchView;
import de.gsv.idm.client.view.search.SearchWindow;
import de.gsv.idm.client.view.widgets.upload.TelefonListUploadWindow;
import de.gsv.idm.shared.push.event.asset.AssetPushEvent;
import de.gsv.idm.shared.push.event.asset.link.AssetLinkPushEvent;
import de.gsv.idm.shared.push.event.assettype.AssettypePushEvent;
import de.gsv.idm.shared.push.event.assettype.category.AssettypeCategoryPushEvent;
import de.gsv.idm.shared.push.event.change.event.ChangeEventPushEvent;
import de.gsv.idm.shared.push.event.domain.DomainPushEvent;
import de.gsv.idm.shared.push.event.employee.EmployeePushEvent;
import de.gsv.idm.shared.push.event.information.InformationPushEvent;
import de.gsv.idm.shared.push.event.measure.MeasurePushEvent;
import de.gsv.idm.shared.push.event.module.ModulePushEvent;
import de.gsv.idm.shared.push.event.occupation.OccupationPushEvent;
import de.gsv.idm.shared.push.event.securitylevelchange.SecurityLevelChangePushEvent;
import de.gsv.idm.shared.push.event.securityzone.SecurityzonePushEvent;
import de.gsv.idm.shared.push.event.threat.ThreatPushEvent;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.domain.DomainFactory;

//GWT-HTTPS-Infos: http://stackoverflow.com/questions/7161121/secure-gwt-application-with-https
//Standard-Login-Daten root//admin
public class AppControllerPresenter implements Presenter, ValueChangeHandler<String> {

	public interface AppControllerDisplay extends IsWidget {
		void setRollbackTransactionEnabled(Boolean enabled);

		HasSelectionHandlers<Item> getRollbackTransactionClick();

		void setTransactionBufferInfoText(String text);

		void setLastEventText(String text);

		HasWidgets getAppContainer();

		void setDomainMenuVisible(boolean b);

		HasSelectionHandlers<Item> getSwitchDomainClick();

		HasSelectionHandlers<Item> getLogoutClick();

		HasSelectionHandlers<Item> getParseTelefonListClick();

		HasSelectionHandlers<Item> getGenerateMeasureSummaryClick();

		HasSelectionHandlers<Item> getSearchDomainClick();
	}

	private final DBController dbController;
	private final HandlerManager eventBus;
	private final RemoteEventService pushService;
	private TransactionBuffer transactionBuffer;
	private Integer lastDomainId;
	private ListPresenter presenter = null;

	AppControllerDisplay display;

	public AppControllerPresenter(RemoteEventService pushService, AppControllerDisplay view) {
		this.dbController = DBController.getInstance();
		this.eventBus = dbController.getEventBus();
		this.pushService = pushService;
		transactionBuffer = new TransactionBuffer(50);
		bindEventBus();
		bindPushService();
		display = view;
	}

	private void bindPushService() {
		pushService.addListener(DomainFactory.getDomain(DomainPushEvent.CONVERSATION_DOMAIN),
		        DomainListenerAdapter.getInstance());
		pushService.addListener(DomainFactory.getDomain(InformationPushEvent.CONVERSATION_DOMAIN),
		        InformationListenerAdapter.getInstance());
		pushService.addListener(DomainFactory.getDomain(OccupationPushEvent.CONVERSATION_DOMAIN),
		        OccupationListenerAdapter.getInstance());
		pushService.addListener(DomainFactory.getDomain(EmployeePushEvent.CONVERSATION_DOMAIN),
		        EmployeeListenerAdapter.getInstance());
		pushService.addListener(DomainFactory.getDomain(ModulePushEvent.CONVERSATION_DOMAIN),
		        ModuleListenerAdapter.getInstance());
		pushService.addListener(DomainFactory.getDomain(ThreatPushEvent.CONVERSATION_DOMAIN),
		        ThreatListenerAdapter.getInstance());
		pushService.addListener(DomainFactory.getDomain(MeasurePushEvent.CONVERSATION_DOMAIN),
		        MeasureListenerAdapter.getInstance());
		pushService.addListener(DomainFactory.getDomain(AssettypePushEvent.CONVERSATION_DOMAIN),
		        AssettypeListenerAdapter.getInstance());
		pushService.addListener(DomainFactory.getDomain(AssetPushEvent.CONVERSATION_DOMAIN),
		        AssetListenerAdapter.getInstance());
		pushService.addListener(
		        DomainFactory.getDomain(AssettypeCategoryPushEvent.CONVERSATION_DOMAIN),
		        AssettypeCategoryListenerAdapter.getInstance());
		pushService.addListener(DomainFactory.getDomain(SecurityzonePushEvent.CONVERSATION_DOMAIN),
		        SecurityzoneListenerAdapter.getInstance());
		pushService.addListener(DomainFactory.getDomain(AssetLinkPushEvent.CONVERSATION_DOMAIN),
		        AssetLinkListenerAdapter.getInstance());
		pushService.addListener(DomainFactory.getDomain(ChangeEventPushEvent.CONVERSATION_DOMAIN),
		        ChangeEventListenerAdapter.getInstance());
		pushService.addListener(
		        DomainFactory.getDomain(SecurityLevelChangePushEvent.CONVERSATION_DOMAIN),
		        SecurityLevelChangeListenerAdapter.getInstance());
	}

	private void bindEventBus() {
		// Bind Domains
		History.addValueChangeHandler(this);
		eventBus.addHandler(StartScreenViewEvent.TYPE,
		        new GeneralEventHandler<StartScreenViewEvent>() {
			        public void onEvent(StartScreenViewEvent event) {
				        History.newItem(event.getHistoryString());
			        }
		        });

		eventBus.addHandler(ViewDomainViewEvent.TYPE,
		        new GeneralEventHandler<ViewDomainViewEvent>() {
			        public void onEvent(ViewDomainViewEvent event) {
				        History.newItem(ViewDomainPresenter.HISTORY_STRING + "/" + event.getId());
			        }
		        });

		eventBus.addHandler(TransactionBufferEvent.TYPE,
		        new GeneralEventHandler<TransactionBufferEvent>() {
			        public void onEvent(TransactionBufferEvent event) {
				        transactionBuffer.push(event.getObject());
				        refreshTransactionBuffer();
			        }
		        });
		eventBus.addHandler(ReceivedPushEvent.TYPE, new GeneralEventHandler<ReceivedPushEvent>() {
			public void onEvent(ReceivedPushEvent event) {
				display.setLastEventText(event.getObject().getText());
			}
		});
	}

	private void refreshTransactionBuffer() {
		Integer currentTransactionBufferSize = transactionBuffer.size();
		if (currentTransactionBufferSize > 0) {
			display.setRollbackTransactionEnabled(true);
			display.setTransactionBufferInfoText(currentTransactionBufferSize + " Element(e): "
			        + transactionBuffer.peekHead().getString());

		} else {
			display.setRollbackTransactionEnabled(false);
			display.setTransactionBufferInfoText(currentTransactionBufferSize + " Element(e)");
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		int id = -1;
		String tokens = event.getValue();
		HasWidgets nextContainer = display.getAppContainer();
		if (tokens != null) {
			String[] substrings = tokens.split("/");
			for (int i = 0; i < substrings.length; i++) {
				String token = substrings[i];
				int idIndex = i + 1;
				if (idIndex < substrings.length && substrings[idIndex].matches("\\d+")) {
					id = Integer.parseInt(substrings[idIndex]);
					i++;
				} else {
					id = -1;
				}
				if (token.equals(ViewDomainPresenter.HISTORY_STRING)) {
					if (id != -1) {
						display.setDomainMenuVisible(true);
						lastDomainId = id;
						presenter = new ViewDomainPresenter(new ViewDomainView(), id);
					}
				}
				if (token.equals(StartScreenPresenter.HISTORY_STRING)) {
					display.setDomainMenuVisible(false);
					presenter = new StartScreenPresenter(new StartScreenView(), id);
				}

				if (presenter != null) {
					nextContainer.clear();
					nextContainer.add((Widget) presenter.go());
				}
			}
		}
	}

	public IsWidget go() {
		bindView();
		if ("".equals(History.getToken())) {
			History.newItem(StartScreenPresenter.HISTORY_STRING);
		} else {
			History.fireCurrentHistoryState();
		}

		return display.asWidget();
	}

	private void bindView() {
		display.getSwitchDomainClick().addSelectionHandler(new SelectionHandler<Item>() {

			@Override
			public void onSelection(SelectionEvent<Item> event) {
				eventBus.fireEvent(new StartScreenViewEvent(lastDomainId));
			}
		});

		display.getLogoutClick().addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				final Boolean rememberMe = dbController.getUser().getRememberMe();
				dbController.getUserController().logout(dbController.getUser(),
				        new AsyncCallback<Boolean>() {

					        @Override
					        public void onSuccess(Boolean result) {
						        if (result) {
							        dbController.setUser(null);
							        pushService.removeListeners();
							        RootLayoutPanel.get().clear();
							        IdmGsv.displayLoginBox(rememberMe);
						        }
					        }

					        @Override
					        public void onFailure(Throwable caught) {
						        DBController.getLogger().log(Level.SEVERE, "Failure while Logout.");
					        }
				        });

			}
		});

		display.getGenerateMeasureSummaryClick().addSelectionHandler(new SelectionHandler<Item>() {

			@Override
			public void onSelection(SelectionEvent<Item> event) {
				RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, GWT
				        .getModuleBaseURL()
				        + "assetsMeasuresSummaryProvider?domainId="
				        + lastDomainId
				        + "&sessionId="
				        + DBController.getInstance().getUser().getSessionId());
				Window.open(builder.getUrl(), "_blank", "");
			}
		});

		display.getParseTelefonListClick().addSelectionHandler(new SelectionHandler<Item>() {

			@Override
			public void onSelection(SelectionEvent<Item> event) {
				new TelefonListUploadWindow(lastDomainId, DBController.getInstance().getUser()
				        .getSessionId());
			}
		});

		display.getRollbackTransactionClick().addSelectionHandler(new SelectionHandler<Item>() {
			public void onSelection(SelectionEvent<Item> event) {
				DBTransactionEvent element = transactionBuffer.pop();
				element.rollback();
				refreshTransactionBuffer();
			}
		});
		refreshTransactionBuffer();

		display.getSearchDomainClick().addSelectionHandler(new SelectionHandler<Item>() {

			@Override
			public void onSelection(SelectionEvent<Item> event) {
				new SearchWindow(new SearchPresenter(new SearchView(), lastDomainId));
			}
		});
	}
	
}
