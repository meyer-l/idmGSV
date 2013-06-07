package de.gsv.idm.client.view.gsk;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;

import de.gsv.idm.client.presenter.gsk.GskViewPresenter.GskDisplay;

public class GskView implements GskDisplay {

	private final TabPanel tabPanel;
	private final BorderLayoutContainer mainPanel;
	private final BorderLayoutContainer testPanel;
	private final BorderLayoutContainer modulesPanel;
	private final BorderLayoutContainer threatsPanel;
	private final BorderLayoutContainer measuresPanel;

	static final int CLIENT_WIDTH = Window.getClientWidth();
	static final int CLIENT_HEIGHT = Window.getClientHeight();

	public GskView() {
		mainPanel = new BorderLayoutContainer();
		mainPanel.setWidth((int) (CLIENT_WIDTH));
		mainPanel.setBorders(false);

		BorderLayoutData parserLayout = new BorderLayoutData();
		parserLayout.setMinSize(0);

		// TabPanel
		tabPanel = new TabPanel();
		tabPanel.setBorders(false);
		tabPanel.setHeight((int) (CLIENT_HEIGHT * 0.884));
		tabPanel.setWidth((int) (CLIENT_WIDTH));
		
		testPanel = new BorderLayoutContainer();
		testPanel.setBorders(false);
		tabPanel.add(testPanel, new TabItemConfig("test"));

		modulesPanel = new BorderLayoutContainer();
		modulesPanel.setBorders(false);
		tabPanel.add(modulesPanel, new TabItemConfig("Bausteine"));

		threatsPanel = new BorderLayoutContainer();
		threatsPanel.setBorders(false);
		tabPanel.add(threatsPanel, new TabItemConfig("Gefährdungen"));

		measuresPanel = new BorderLayoutContainer();
		measuresPanel.setBorders(false);
		tabPanel.add(measuresPanel, new TabItemConfig("Maßnahmen"));

		mainPanel.setCenterWidget(tabPanel);
	}

	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public void setModulesTab(IsWidget widget) {
		modulesPanel.setCenterWidget(widget);
	}

	@Override
	public void setThreatsTab(IsWidget widget) {
		threatsPanel.setCenterWidget(widget);
	}

	@Override
	public void setMeasuresTab(IsWidget widget) {
		measuresPanel.setCenterWidget(widget);
	}

	@Override
    public IsWidget getThreatsTab() {
	    return threatsPanel;
    }

	@Override
    public IsWidget getMeasuresTab() {
	   return measuresPanel;
    }

	@Override
    public HasSelectionHandlers<Widget> getTabPanelSelection() {
	   return tabPanel;
    }

	@Override
    public void removeTestPanel() {
	    if(tabPanel.remove(testPanel));
    }

}
