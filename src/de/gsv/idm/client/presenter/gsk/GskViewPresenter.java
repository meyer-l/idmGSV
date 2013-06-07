package de.gsv.idm.client.presenter.gsk;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import de.gsv.idm.client.presenter.ListPresenter;
import de.gsv.idm.client.view.gsk.MeasureListView;
import de.gsv.idm.client.view.gsk.ModuleListView;
import de.gsv.idm.client.view.gsk.ThreatListView;

public class GskViewPresenter implements ListPresenter {

	public interface GskDisplay {
		Widget asWidget();

		void setModulesTab(IsWidget widget);

		void setThreatsTab(IsWidget widget);

		void setMeasuresTab(IsWidget widget);
		
		IsWidget getThreatsTab();
		
		IsWidget getMeasuresTab();

		HasSelectionHandlers<Widget> getTabPanelSelection();

		void removeTestPanel();
	}

	private final GskDisplay display;
	private final ModuleListPresenter modulesListPresenter;
	private final ThreatListPresenter threatsListPresenter;
	private final MeasureListPresenter measuresListPresenter;

	public GskViewPresenter(GskDisplay display) {
		this.display = display;
		modulesListPresenter = new ModuleListPresenter(new ModuleListView());
		threatsListPresenter = new ThreatListPresenter(new ThreatListView());
		measuresListPresenter = new MeasureListPresenter(new MeasureListView());

	}

	@Override
	public IsWidget go() {
		display.setModulesTab(modulesListPresenter.go());
		display.setThreatsTab(threatsListPresenter.go());
		display.setMeasuresTab(measuresListPresenter.go());
		bindView();
		return display.asWidget();
	}

	private void bindView() {
		display.getTabPanelSelection().addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				if (event.getSelectedItem().equals(display.getThreatsTab().asWidget())) {
					threatsListPresenter.fetchThreats();
				}
			}
		});
		display.getTabPanelSelection().addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				if (event.getSelectedItem().equals(display.getMeasuresTab().asWidget())) {
					measuresListPresenter.fetchMeasures();
				}
			}
		});
	}

	public void fetchModules() {
		display.removeTestPanel();
	   modulesListPresenter.fetchModules();	    
    }

}
