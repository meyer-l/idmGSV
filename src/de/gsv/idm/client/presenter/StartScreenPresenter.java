package de.gsv.idm.client.presenter;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import de.gsv.idm.client.presenter.domain.DomainListPresenter;
import de.gsv.idm.client.presenter.gsk.GskViewPresenter;
import de.gsv.idm.client.presenter.informationdomain.assettype.category.AssettypeCategoryListPresenter;
import de.gsv.idm.client.presenter.securityzone.SecurityzoneListPresenter;
import de.gsv.idm.client.view.domain.DomainListView;
import de.gsv.idm.client.view.gsk.GskView;
import de.gsv.idm.client.view.informationdomain.assettype.category.AssettypeCategoryListView;
import de.gsv.idm.client.view.securityzone.SecurityzoneListView;

public class StartScreenPresenter implements ListPresenter {

	public static String HISTORY_STRING = "startScreen";

	public interface StartScreenDisplay {

		Widget asWidget();

		void setChooseDomainTab(IsWidget widget);

		void setGskTab(IsWidget widget);

		void setAssetCategoriesTab(IsWidget widget);

		void setSecurityzoneTab(IsWidget widget);

		IsWidget getGskTab();

		HasSelectionHandlers<Widget> getTabPanelSelection();

	}

	private Integer domainId;

	private final StartScreenDisplay display;
	private final DomainListPresenter domainListPresenter;
	private final GskViewPresenter gskPresenter;
	private final SecurityzoneListPresenter securityzonePresenter;
	private final AssettypeCategoryListPresenter assetCategoryListPresenter;

	public StartScreenPresenter(StartScreenDisplay view, int id) {
		this.display = view;
		this.domainId = id;
		this.domainListPresenter = new DomainListPresenter(new DomainListView(), id);
		this.gskPresenter = new GskViewPresenter(new GskView());
		securityzonePresenter = new SecurityzoneListPresenter(new SecurityzoneListView());
		assetCategoryListPresenter = new AssettypeCategoryListPresenter(
		        new AssettypeCategoryListView());
	}

	public StartScreenPresenter(StartScreenDisplay view) {
		this(view, -1);
	}

	@Override
	public IsWidget go() {
		bindView();
		display.setChooseDomainTab(domainListPresenter.go());
		display.setGskTab(gskPresenter.go());
		display.setSecurityzoneTab(securityzonePresenter.go());
		display.setAssetCategoriesTab(assetCategoryListPresenter.go());
		return display.asWidget();
	}

	private void bindView() {
		display.getTabPanelSelection().addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				if (event.getSelectedItem().equals(display.getGskTab().asWidget())) {
					gskPresenter.fetchModules();
				}
			}
		});
		
	}

	public String getHistoryString() {
		if (domainId == -1) {
			return HISTORY_STRING + '/';
		} else {
			return HISTORY_STRING + '/' + domainId;
		}
	}

}
