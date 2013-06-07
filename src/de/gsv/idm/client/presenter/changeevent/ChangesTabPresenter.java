package de.gsv.idm.client.presenter.changeevent;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.gui.change.ShowParsedChangeTabEvent;
import de.gsv.idm.client.presenter.Presenter;
import de.gsv.idm.client.view.changeevent.AppliedChangesView;
import de.gsv.idm.client.view.changeevent.ManualChangesView;
import de.gsv.idm.client.view.changeevent.NotNeededChangesView;
import de.gsv.idm.client.view.changeevent.OpenChangesView;

public class ChangesTabPresenter implements Presenter {

	public interface ChangesTabDisplay {
		Widget asWidget();

		IsWidget getOpenChangesTab();

		IsWidget getManualChangesTab();

		IsWidget getAppliedChangesTab();

		IsWidget getNotNeededChangesTab();

		void setOpenChangesTab(IsWidget widget);

		void setManualChangesTab(IsWidget widget);

		void setAppliedChangesTab(IsWidget widget);

		void setNotNeededChangesTab(IsWidget widget);

		void setActiveTab(Widget tab);

		HasSelectionHandlers<Widget> getTabPanelSelection();

	}

	private final ChangesTabDisplay display;
	private Integer domainId;
	private final HandlerManager eventBus;
	private final OpenChangesPresenter openChangesPresenter;
	private final ManualChangesPresenter manualChangesPresenter;
	private final AppliedChangesPresenter appliedChangesPresenter;
	private final NotNeededChangesPresenter notNeededChangesPresenter;

	public ChangesTabPresenter(ChangesTabDisplay display, Integer domain_id) {
		this.display = display;
		this.domainId = domain_id;
		this.eventBus = DBController.getInstance().getEventBus();
		openChangesPresenter = new OpenChangesPresenter(new OpenChangesView(), domainId);
		manualChangesPresenter = new ManualChangesPresenter(new ManualChangesView(), domainId);
		appliedChangesPresenter = new AppliedChangesPresenter(new AppliedChangesView(), domainId);
		notNeededChangesPresenter = new NotNeededChangesPresenter(new NotNeededChangesView(),
		        domainId);

	}

	@Override
	public IsWidget go() {
		display.setOpenChangesTab(openChangesPresenter.go());
		display.setManualChangesTab(manualChangesPresenter.go());
		display.setAppliedChangesTab(appliedChangesPresenter.go());
		display.setNotNeededChangesTab(notNeededChangesPresenter.go());
		bindBus();
		bindView();

		return display.asWidget();
	}

	private void bindBus() {
		eventBus.addHandler(ShowParsedChangeTabEvent.TYPE,
		        new GeneralEventHandler<ShowParsedChangeTabEvent>() {

			        @Override
			        public void onEvent(ShowParsedChangeTabEvent event) {
				        if (event.getDomainId().equals(domainId)) {
					        display.setActiveTab((Widget) display.getOpenChangesTab());
				        }

			        }
		        });
	}

	private void bindView() {
		display.getTabPanelSelection().addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				if (event.getSelectedItem().equals(display.getOpenChangesTab().asWidget())) {
					openChangesPresenter.refreshView();
				}
			}
		});
		display.getTabPanelSelection().addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				if (event.getSelectedItem().equals(display.getManualChangesTab().asWidget())) {
					manualChangesPresenter.refreshView();
				}
			}
		});
		display.getTabPanelSelection().addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				if (event.getSelectedItem().equals(display.getAppliedChangesTab())) {
					appliedChangesPresenter.refreshView();
				}
			}
		});
		display.getTabPanelSelection().addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				if (event.getSelectedItem().equals(display.getNotNeededChangesTab().asWidget())) {
					notNeededChangesPresenter.refreshView();
				}
			}
		});
	}

	public void refreshOpenChangesView() {
		openChangesPresenter.refreshView();
	}

}
