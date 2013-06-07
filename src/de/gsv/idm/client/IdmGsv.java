package de.gsv.idm.client;

import java.util.Date;
import java.util.logging.Level;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.presenter.AppControllerPresenter;
import de.gsv.idm.client.view.AppControllerView;
import de.gsv.idm.client.view.widgets.window.LoginWindow;
import de.gsv.idm.shared.dto.UserDTO;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;

public class IdmGsv implements EntryPoint {

	final static long DURATION = 1000 * 60 * 60 * 24 * 3;

	public void onModuleLoad() {		
		String cookieID = Cookies.getCookie("cid");
		if (cookieID != null)
			checkCookieId(cookieID);
		else
			displayLoginBox(false);

	}

	public static void displayLoginBox(Boolean cookieFound) {
		new LoginWindow("Login", cookieFound);
	}

	private void checkCookieId(String cookieId) {
		DBController.getInstance().getUserController()
		        .checkCookieId(cookieId, new AsyncCallback<UserDTO>() {

			        @Override
			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE, "Failure at checking sessionId");
				        displayLoginBox(true);
			        }

			        @Override
			        public void onSuccess(UserDTO result) {
				        if (result != null && result.getSessionId() != null
				                && !result.getSessionId().equals("")) {
					        DBController.getLogger().log(Level.INFO,
					                "Cookie for " + result.getUsername() + " still valid.");
					        DBController.getInstance().setUser(result);
					        loadApp();
				        } else {
					        displayLoginBox(true);
				        }
			        }
		        });
	}

	private static void loadApp() {
		RemoteEventServiceFactory eventServiceFactory = RemoteEventServiceFactory.getInstance();
		final RemoteEventService pushService = eventServiceFactory.getRemoteEventService();
		AppControllerPresenter appViewer = new AppControllerPresenter(pushService,
		        new AppControllerView());
		RootLayoutPanel.get().add((Widget) appViewer.go());	
	}

	public static void handleLogin(String username, String password, final Boolean rememberMe) {
		DBController.getInstance().getUserController()
		        .login(username, password, rememberMe, new AsyncCallback<UserDTO>() {

			        @Override
			        public void onSuccess(UserDTO result) {
				        if (result != null && result.getSessionId() != null
				                && !result.getSessionId().equals("")) {
					        DBController.getLogger().log(Level.INFO,
					                result.getUsername() + " logged in.");
					        DBController.getInstance().setUser(result);
					        if (rememberMe) {
						        Date expires = new Date(System.currentTimeMillis() + DURATION);
						        Cookies.setCookie("cid", result.getCookieId(), expires, null, "/",
						                false);
					        } else {
						        Cookies.removeCookie("cid");
					        }

					        loadApp();
				        } else {
					        displayLoginBox(rememberMe);
					        new AlertMessageBox("Fehler",
					                "Benutzername oder Passwort stimmen nicht überein.").show();
				        }
			        }

			        @Override
			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE, "Failure at Login");
				        new AlertMessageBox("Fehler",
				                "Beim einloggen ist ein Fehler aufgetreten.").show();
				        displayLoginBox(rememberMe);
			        }
		        });

	}
}
