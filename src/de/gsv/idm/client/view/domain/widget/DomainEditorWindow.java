package de.gsv.idm.client.view.domain.widget;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.presenter.domain.DomainEditorPresenter;
import de.gsv.idm.client.view.domain.DomainEditor;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.dto.DomainDTO;

public class DomainEditorWindow extends EditorWindow<DomainDTO> {

	public DomainEditorWindow(DomainEditorPresenter domainEditorPresenter) {
		super(domainEditorPresenter, "Domäne erstellen");
		final DomainEditorWindow window = this;
		domainEditorPresenter.getDomainEditorDisplay().getChooseClick().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				window.hide();
			}
		});
		
	}

	public DomainEditorWindow() {
		this(new DomainEditorPresenter(new DomainEditor()));

	}

	@Override
	protected void addSaveClick() {

	}

}
