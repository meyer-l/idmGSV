package de.gsv.idm.client.view;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;

import de.gsv.idm.client.presenter.StartScreenPresenter.StartScreenDisplay;

public class StartScreenView extends Composite implements StartScreenDisplay {

	private TabPanel tabPanel;
	private BorderLayoutContainer domainPanel;
	private BorderLayoutContainer gskPanel;
	private BorderLayoutContainer securityzonesPanel;
	private BorderLayoutContainer assetCategoriesPanel;
	
	static final int CLIENT_WIDTH = Window.getClientWidth();
	static final int CLIENT_HEIGHT = Window.getClientHeight();
	
	public StartScreenView() {
		final BorderLayoutContainer mainPanel = new BorderLayoutContainer();
		mainPanel.setWidth((int) (CLIENT_WIDTH));
		initWidget(mainPanel);		

		mainPanel.setCenterWidget(buildTabPanel());
	}

	private IsWidget buildTabPanel() {
		tabPanel = new TabPanel();
		tabPanel.setHeight((int) (CLIENT_HEIGHT * 0.921));
		tabPanel.setWidth((int) (CLIENT_WIDTH));

		domainPanel = new BorderLayoutContainer();
		domainPanel.setBorders(false);
		tabPanel.add(domainPanel, new TabItemConfig("Informationsverbünde"));
		
		gskPanel = new BorderLayoutContainer();
		gskPanel.setBorders(false);
		tabPanel.add(gskPanel, new TabItemConfig("Grundschutz-Katalog"));
		
		securityzonesPanel = new BorderLayoutContainer();
		securityzonesPanel.setBorders(false);
		tabPanel.add(securityzonesPanel, new TabItemConfig("Schutzzonen"));
		
		assetCategoriesPanel = new BorderLayoutContainer();
		assetCategoriesPanel.setBorders(false);
		tabPanel.add(assetCategoriesPanel, new TabItemConfig("Asset-Grundtyp-Kategorien"));
	    return tabPanel;
    }

	@Override
    public void setChooseDomainTab(IsWidget widget) {
	     domainPanel.setCenterWidget(widget);
    }

	@Override
    public void setGskTab(IsWidget widget) {
	     gskPanel.setCenterWidget(widget);
    }
	
	@Override
    public void setAssetCategoriesTab(IsWidget widget) {
	     assetCategoriesPanel.setCenterWidget(widget);
    }
	
	public void setSecurityzoneTab(IsWidget widget){
		securityzonesPanel.setCenterWidget(widget);
	}
	
	public Widget asWidget(){
		return this;
	}

	@Override
    public IsWidget getGskTab() {
	    return gskPanel;
    }

	@Override
    public HasSelectionHandlers<Widget> getTabPanelSelection() {
	    return tabPanel;
    }
}
