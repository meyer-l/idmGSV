package de.gsv.idm.client.view.domain;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;

import de.gsv.idm.client.presenter.domain.ViewDomainPresenter.ViewDomainDisplay;

public class ViewDomainView extends Composite implements ViewDomainDisplay {

	private final TabPanel tabPanel;
	private ContentPanel domainPanel;
	private final BorderLayoutContainer informationPanel;
	private final BorderLayoutContainer occupationPanel;
	private final BorderLayoutContainer employeePanel;
	private final BorderLayoutContainer itAssetsPanel;
	private final BorderLayoutContainer riskAnalysisPanel;
	private final BorderLayoutContainer changesPanel;
	private final BorderLayoutContainer securityLevelChangePanel;

	static final int CLIENT_WIDTH = Window.getClientWidth();
	static final int CLIENT_HEIGHT = Window.getClientHeight();

	public ViewDomainView() {
		final BorderLayoutContainer mainPanel = new BorderLayoutContainer();
		mainPanel.setBorders(false);
		mainPanel.setWidth((int) (CLIENT_WIDTH));
		initWidget(mainPanel);

		// Domain-Header
		domainPanel = new ContentPanel();
		domainPanel.setBorders(false);

		// TabPanel
		tabPanel = new TabPanel();
		tabPanel.setHeight((int) (CLIENT_HEIGHT * 0.884));
		tabPanel.setWidth((int) (CLIENT_WIDTH));
		tabPanel.setBorders(false);

		// Informationsverbund
		itAssetsPanel = new BorderLayoutContainer();
		tabPanel.add(itAssetsPanel, new TabItemConfig("Informationsverbund"));

		// EmployeeTab
		employeePanel = new BorderLayoutContainer();
		tabPanel.add(employeePanel, new TabItemConfig("Mitarbeiter"));

		// OccupationTab
		occupationPanel = new BorderLayoutContainer();
		tabPanel.add(occupationPanel, new TabItemConfig("Dienstposten"));

		// InformationTab
		informationPanel = new BorderLayoutContainer();
		tabPanel.add(informationPanel, new TabItemConfig("Informationen"));

		// Schutzbedarf Änderungen
		securityLevelChangePanel = new BorderLayoutContainer();
		tabPanel.add(securityLevelChangePanel, new TabItemConfig("Schutzbedarf-Änderungen"));

		// Informationsverbund Änderungen
		changesPanel = new BorderLayoutContainer();
		tabPanel.add(changesPanel, new TabItemConfig("Informationsverbund-Änderungen"));

		// Risikoanalyse
		riskAnalysisPanel = new BorderLayoutContainer();
		tabPanel.add(riskAnalysisPanel, new TabItemConfig("Risikoanalyse"));

		domainPanel.add(tabPanel);
		mainPanel.setCenterWidget(domainPanel);
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void setPanelTitle(String text) {
		domainPanel.setHeadingText(text);
	}

	public void setInformationTab(IsWidget widget) {
		informationPanel.setCenterWidget(widget);
	}

	public void setOccupationTab(IsWidget widget) {
		occupationPanel.setCenterWidget(widget);
	}

	@Override
	public void setEmployeeTab(IsWidget widget) {
		employeePanel.setCenterWidget(widget);
	}

	@Override
	public void setInformationDomainTab(IsWidget widget) {
		itAssetsPanel.setCenterWidget(widget);
	}

	@Override
	public void setRiskAnalysisTab(IsWidget widget) {
		riskAnalysisPanel.setCenterWidget(widget);
	}

	public void setChangesTab(IsWidget widget) {
		changesPanel.setCenterWidget(widget);
	}

	public IsWidget getParsedChangesTab() {
		return changesPanel;
	}

	public void setActiveTab(IsWidget tab) {
		tabPanel.setActiveWidget(tab);
	}

	@Override
	public HasSelectionHandlers<Widget> getTabPanelSelection() {
		return tabPanel;
	}

	@Override
	public IsWidget getRiskAnalysisTab() {
		return riskAnalysisPanel;
	}

	@Override
	public void setSecurityLevelChangeTab(IsWidget widget) {
		securityLevelChangePanel.setCenterWidget(widget);
	}

	@Override
	public IsWidget getSecurityLevelChangeTab() {
		return securityLevelChangePanel;
	}

}
