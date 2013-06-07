package de.gsv.idm.client.presenter.domain;

import java.util.logging.Level;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.view.startscreen.StartScreenViewEvent;
import de.gsv.idm.client.gui.change.ShowParsedChangeTabEvent;
import de.gsv.idm.client.presenter.ListPresenter;
import de.gsv.idm.client.presenter.changeevent.ChangesTabPresenter;
import de.gsv.idm.client.presenter.employee.EmployeeListPresenter;
import de.gsv.idm.client.presenter.information.InformationListPresenter;
import de.gsv.idm.client.presenter.informationdomain.InformationDomainTreePresenter;
import de.gsv.idm.client.presenter.occupation.OccupationListPresenter;
import de.gsv.idm.client.presenter.risk.RiskAnalysisPresenter;
import de.gsv.idm.client.presenter.securitylevelchange.SecurityLevelChangePresenter;
import de.gsv.idm.client.view.changeevent.ChangesTabView;
import de.gsv.idm.client.view.employee.EmployeeListView;
import de.gsv.idm.client.view.information.InformationListView;
import de.gsv.idm.client.view.informationdomain.InformationDomainTreeView;
import de.gsv.idm.client.view.occupation.OccupationListView;
import de.gsv.idm.client.view.risk.RiskAnalysisView;
import de.gsv.idm.client.view.securitylevelchange.SecurityLevelChangeView;
import de.gsv.idm.shared.dto.DomainDTO;

public class ViewDomainPresenter implements ListPresenter {

	public static String HISTORY_STRING = "viewDomain";

	public interface ViewDomainDisplay {
		Widget asWidget();

		void setPanelTitle(String text);

		void setInformationTab(IsWidget widget);

		void setOccupationTab(IsWidget widget);

		void setEmployeeTab(IsWidget widget);

		void setInformationDomainTab(IsWidget widget);

		void setRiskAnalysisTab(IsWidget widget);

		void setSecurityLevelChangeTab(IsWidget widget);

		IsWidget getSecurityLevelChangeTab();

		IsWidget getRiskAnalysisTab();

		void setChangesTab(IsWidget widget);

		IsWidget getParsedChangesTab();

		void setActiveTab(IsWidget tab);

		HasSelectionHandlers<Widget> getTabPanelSelection();
	}

	private final DBController dbConnector;

	private final ViewDomainDisplay display;
	private final InformationListPresenter informationEditPresenter;
	private final OccupationListPresenter occupationEditPresenter;
	private final EmployeeListPresenter employeeEditPresenter;
	private final InformationDomainTreePresenter itAssetsPresenter;
	private final RiskAnalysisPresenter riskAnalysisPresenter;
	private final ChangesTabPresenter changeEventPresenter;
	private final SecurityLevelChangePresenter securityLevelChangePresenter;

	private final HandlerManager eventBus;

	private DomainDTO domain;

	public ViewDomainPresenter(ViewDomainDisplay view, int id) {
		dbConnector = DBController.getInstance();
		eventBus = dbConnector.getEventBus();

		display = view;
		informationEditPresenter = new InformationListPresenter(new InformationListView(), id);
		occupationEditPresenter = new OccupationListPresenter(new OccupationListView(), id);
		employeeEditPresenter = new EmployeeListPresenter(new EmployeeListView(), id);
		itAssetsPresenter = new InformationDomainTreePresenter(new InformationDomainTreeView(), id);
		riskAnalysisPresenter = new RiskAnalysisPresenter(new RiskAnalysisView(), id);
		changeEventPresenter = new ChangesTabPresenter(new ChangesTabView(), id);
		securityLevelChangePresenter = new SecurityLevelChangePresenter(
		        new SecurityLevelChangeView(), id);
		fetchDomain(id);
	}

	@Override
	public IsWidget go() {
		display.setInformationTab(informationEditPresenter.go());
		display.setOccupationTab(occupationEditPresenter.go());
		display.setEmployeeTab(employeeEditPresenter.go());
		display.setInformationDomainTab(itAssetsPresenter.go());
		display.setRiskAnalysisTab(riskAnalysisPresenter.go());
		display.setChangesTab(changeEventPresenter.go());
		display.setSecurityLevelChangeTab(securityLevelChangePresenter.go());

		bindBus();
		bindView();
		return display.asWidget();
	}

	private void bindView() {
		display.getTabPanelSelection().addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				if (event.getSelectedItem().equals(display.getParsedChangesTab().asWidget())) {
					changeEventPresenter.refreshOpenChangesView();
				}
			}
		});

		display.getTabPanelSelection().addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				if (event.getSelectedItem().equals(display.getRiskAnalysisTab().asWidget())) {
					riskAnalysisPresenter.refreshView();
				}
			}
		});

		display.getTabPanelSelection().addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				if (event.getSelectedItem().equals(display.getSecurityLevelChangeTab().asWidget())) {
					securityLevelChangePresenter.refreshView();
				}
			}
		});
	}

	private void bindBus() {
		eventBus.addHandler(ShowParsedChangeTabEvent.TYPE,
		        new GeneralEventHandler<ShowParsedChangeTabEvent>() {

			        @Override
			        public void onEvent(ShowParsedChangeTabEvent event) {
				        if (event.getDomainId() != null
				                && event.getDomainId().equals(domain.getId())) {
					        display.setActiveTab(display.getParsedChangesTab().asWidget());
				        }

			        }
		        });

	}

	private void fetchDomain(final int id) {
		dbConnector.getDomainController().getObject(id, new AsyncCallback<DomainDTO>() {

			@Override
			public void onSuccess(DomainDTO result) {
				domain = result;
				display.setPanelTitle("Informationsverbund: " + domain.getName());
			}

			@Override
			public void onFailure(Throwable caught) {
				dbConnector.getEventBus().fireEvent(new StartScreenViewEvent(-1));
				DBController.getLogger().log(Level.SEVERE,
				        "Error while calling DomainImpl.getObject, Id:" + id);
			}

		});
	}
	
}
