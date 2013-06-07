package de.gsv.idm.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.theme.blue.client.status.BlueBoxStatusAppearance;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.Status.StatusAppearance;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuBar;
import com.sencha.gxt.widget.core.client.menu.MenuBarItem;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.menu.SeparatorMenuItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.presenter.AppControllerPresenter.AppControllerDisplay;
import de.gsv.idm.client.view.general.GeneralView;
import de.gsv.idm.client.view.widgets.upload.GSKUploadFileWindow;

//TODO: create Domain aus dem menu
public class AppControllerView extends GeneralView implements AppControllerDisplay {

	private MenuBarItem domainMenu;
	private MenuItem rollbackTransaction;
	private MenuItem transactionBufferInfo;
	private MenuItem searchDomain;
	private MenuItem switchDomain;
	private MenuItem logoutMenuItem;
	private MenuItem iconMenuItem;
	private MenuItem appMenuItem;
	private MenuItem parseTelefonList;
	private MenuItem generateMeasureSummary;
	private ImageResources imageBundler;
	private final DockLayoutPanel startScreen;
	private final DockLayoutPanel appPanel;

	// Changeable Panel-Elements
	private Status lastEvent;

	public AppControllerView() {
		imageBundler = GWT.create(ImageResources.class);
		startScreen = new DockLayoutPanel(Unit.EM);

		startScreen.addNorth(buildTopBar(), 2);
		startScreen.addSouth(buildToolBar(), 2);
		appPanel = new DockLayoutPanel(Unit.EM);
		startScreen.add(appPanel);
	}

	@Override
	public Widget asWidget() {
		return startScreen;
	}

	@Override
	public void setRollbackTransactionEnabled(Boolean enabled) {
		rollbackTransaction.setEnabled(enabled);
	}

	@Override
	public void setTransactionBufferInfoText(String text) {
		transactionBufferInfo.setText(text);
	}

	@Override
	public void setLastEventText(String text) {
		lastEvent.setText(text);
	}

	@Override
	public HasWidgets getAppContainer() {
		return appPanel;
	}

	@Override
	public void setDomainMenuVisible(boolean visible) {
		domainMenu.setVisible(visible);

	}

	private Widget buildToolBar() {
		Status emptySpace = new Status();
		emptySpace.setWidth("59%");
		ToolBar toolBar = new ToolBar();
		lastEvent = new Status(GWT.<StatusAppearance> create(BlueBoxStatusAppearance.class));
		lastEvent.setWidth("40%");
		lastEvent.setText("Kein Event");
		toolBar.add(emptySpace);
		toolBar.add(lastEvent);
		toolBar.addStyleName(ThemeStyles.getStyle().borderTop());
		return toolBar;
	}

	private Widget buildTopBar() {
		// Build TopBar
		final BorderLayoutContainer topPanel = new BorderLayoutContainer();
		topPanel.setBorders(true);
		topPanel.addStyleName("topBar");
		FlowPanel titlePanel = new FlowPanel();
		titlePanel.addStyleName("titleBar");
		Label appTitle = new Label("idmGSV");
		appTitle.addStyleName("titleLabel");
		titlePanel.add(appTitle);

		// Menubar
		MenuBar menuBar = new MenuBar();
		menuBar.setBorders(false);
		Menu domainSubMenu = new Menu();
		
		searchDomain = new MenuItem("Informationsverbund durchsuchen");

		domainSubMenu.add(searchDomain);

		parseTelefonList = new MenuItem("Telefonliste einlesen");

		domainSubMenu.add(parseTelefonList);

		generateMeasureSummary = new MenuItem("Maßnahmenstatus-Zusammenfassung erstellen");

		domainSubMenu.add(generateMeasureSummary);
		domainSubMenu.add(new SeparatorMenuItem());
		switchDomain = new MenuItem("Informationsverbund wechseln");
		domainSubMenu.add(switchDomain);

		domainMenu = new MenuBarItem("Informationsverbund", domainSubMenu);
		menuBar.add(domainMenu);
		Menu transactionSubMenu = new Menu();
		transactionSubMenu.setWidth("40%");
		transactionBufferInfo = new MenuItem("Buffer Info");
		transactionBufferInfo.disable();
		transactionSubMenu.add(transactionBufferInfo);
		rollbackTransaction = new MenuItem("Datenobjektänderung zurücksetzen");
		rollbackTransaction.setIcon(imageBundler.arrowLeft());
		transactionSubMenu.add(rollbackTransaction);
		MenuBarItem transactionMenuBarItem = new MenuBarItem("Datenobjektänderung", transactionSubMenu);
		menuBar.add(transactionMenuBarItem);
		Menu gskSubMenu = new Menu();
		MenuItem parseGSK = new MenuItem("GSK-Katalog einlesen");
		parseGSK.addSelectionHandler(new SelectionHandler<Item>() {

			@Override
			public void onSelection(SelectionEvent<Item> event) {
				new GSKUploadFileWindow(DBController.getInstance().getUser().getSessionId()).show();
			}
		});
		gskSubMenu.add(parseGSK);
		MenuBarItem gskMenuBarItem = new MenuBarItem("GSK-Katalog", gskSubMenu);
		menuBar.add(gskMenuBarItem);
		Menu accountSubMenu = new Menu();
		logoutMenuItem = new MenuItem("Ausloggen");

		accountSubMenu.add(logoutMenuItem);
		MenuBarItem userMenuBarItem = new MenuBarItem("Account", accountSubMenu);
		menuBar.add(userMenuBarItem);

		Menu aboutSubMenu = new Menu();
		appMenuItem = new MenuItem("Über diese Anwendung");
		appMenuItem.addSelectionHandler(new SelectionHandler<Item>() {
			
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				final Dialog famFamFamDialog = new Dialog();
				famFamFamDialog.setHeadingText("Information zur Anwendung");
				famFamFamDialog.setPredefinedButtons(PredefinedButton.OK);
				famFamFamDialog.add(new Label(
				        "Die Anwendung wurde im Rahmen einer Masterarbeit"
				                + " zur besseren Unterstützung der"
				                + " IT-Grundschutz-Vorgehensweise von Lars Meyer entwickelt."));
				famFamFamDialog.setHideOnButtonClick(true);
				famFamFamDialog.setWidth(300);
				famFamFamDialog.show();
			}
		});
		aboutSubMenu.add(appMenuItem);
		
		iconMenuItem = new MenuItem("FamFamFam Icon-Archiv");
		iconMenuItem.addSelectionHandler(new SelectionHandler<Item>() {

			@Override
			public void onSelection(SelectionEvent<Item> event) {
				final Dialog famFamFamDialog = new Dialog();
				famFamFamDialog.setHeadingText("FamFamFam Icon-Archiv");
				famFamFamDialog.setPredefinedButtons(PredefinedButton.OK);
				famFamFamDialog.add(new Label(
				        "Die in dieser Anwendung verwendeten Icons entstammen"
				                + " dem FamFamFam: Silk-Archiv von Mark James:"
				                + " http://www.famfamfam.com/lab/icons/silk/"));
				famFamFamDialog.setHideOnButtonClick(true);
				famFamFamDialog.setWidth(300);
				famFamFamDialog.show();
			}
		});
		aboutSubMenu.add(iconMenuItem);

		MenuBarItem aboutMenuBarItem = new MenuBarItem("Über", aboutSubMenu);
		menuBar.add(aboutMenuBarItem);

		topPanel.setWestWidget(titlePanel, new BorderLayoutData(CLIENT_WIDTH * 0.1));
		topPanel.setCenterWidget(menuBar, new BorderLayoutData(CLIENT_WIDTH * 0.7));
		return topPanel;
	}

	@Override
	public HasSelectionHandlers<Item> getSwitchDomainClick() {
		return switchDomain;
	}

	@Override
	public HasSelectionHandlers<Item> getLogoutClick() {
		return logoutMenuItem;
	}

	@Override
	public HasSelectionHandlers<Item> getRollbackTransactionClick() {
		return rollbackTransaction;
	}

	@Override
	public HasSelectionHandlers<Item> getParseTelefonListClick() {
		return parseTelefonList;
	}

	@Override
	public HasSelectionHandlers<Item> getGenerateMeasureSummaryClick() {
		return generateMeasureSummary;
	}

	@Override
    public HasSelectionHandlers<Item> getSearchDomainClick() {
	    return searchDomain;
    }

}
