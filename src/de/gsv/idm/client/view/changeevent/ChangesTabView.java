package de.gsv.idm.client.view.changeevent;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;

import de.gsv.idm.client.presenter.changeevent.ChangesTabPresenter.ChangesTabDisplay;
import de.gsv.idm.client.view.general.GeneralView;

public class ChangesTabView extends GeneralView implements ChangesTabDisplay {

	private final TabPanel tabPanel;
	private final BorderLayoutContainer openChangesTab;
	private final BorderLayoutContainer manualChangesTab;
	private final BorderLayoutContainer appliedChangesTab;
	private final BorderLayoutContainer notNeededChangesTab;
	final BorderLayoutContainer mainPanel;

	public ChangesTabView() {
		mainPanel = new BorderLayoutContainer();
		mainPanel.setBorders(false);
		mainPanel.setWidth((int) (CLIENT_WIDTH));

		// TabPanel
		tabPanel = new TabPanel();
		tabPanel.setWidth((int) (CLIENT_WIDTH));
		tabPanel.setBorders(false);

		// Open Changes
		openChangesTab = new BorderLayoutContainer();
		openChangesTab.setBorders(false);
		tabPanel.add(openChangesTab, new TabItemConfig("Offene Änderungen"));

		// Manual Changes
		manualChangesTab = new BorderLayoutContainer();
		manualChangesTab.setBorders(false);
		tabPanel.add(manualChangesTab, new TabItemConfig("Manuell umzusetzende Änderungen"));

		// Applied Changes
		appliedChangesTab = new BorderLayoutContainer();
		appliedChangesTab.setBorders(false);
		tabPanel.add(appliedChangesTab, new TabItemConfig("Verarbeitete Änderungen"));

		// Not Needed Changes
		notNeededChangesTab = new BorderLayoutContainer();
		notNeededChangesTab.setBorders(false);
		tabPanel.add(notNeededChangesTab, new TabItemConfig("Nicht benötigte Änderungen"));

		mainPanel.add(tabPanel);
	}

	@Override
	public IsWidget getOpenChangesTab() {
		return openChangesTab;
	}

	@Override
	public void setOpenChangesTab(IsWidget widget) {
		openChangesTab.setCenterWidget(widget);
	}

	public void setActiveTab(Widget tab) {
		tabPanel.setActiveWidget(tab);
	}

	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public void setManualChangesTab(IsWidget widget) {
		manualChangesTab.setCenterWidget(widget);
	}

	@Override
	public void setAppliedChangesTab(IsWidget widget) {
		appliedChangesTab.setCenterWidget(widget);
	}

	@Override
	public void setNotNeededChangesTab(IsWidget widget) {
		notNeededChangesTab.setCenterWidget(widget);
	}

	@Override
    public IsWidget getManualChangesTab() {
	   return manualChangesTab;
    }

	@Override
    public IsWidget getAppliedChangesTab() {
	    return appliedChangesTab;
    }

	@Override
    public IsWidget getNotNeededChangesTab() {
	   return notNeededChangesTab;
    }

	@Override
    public HasSelectionHandlers<Widget> getTabPanelSelection() {
	    return tabPanel;
    }

}
