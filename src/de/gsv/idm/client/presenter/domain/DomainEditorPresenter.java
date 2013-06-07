package de.gsv.idm.client.presenter.domain;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.db.domain.CreatedDomainEvent;
import de.gsv.idm.client.event.db.domain.DeletedDomainEvent;
import de.gsv.idm.client.event.view.domain.ViewDomainViewEvent;
import de.gsv.idm.client.presenter.general.GeneralEditorPresenter;
import de.gsv.idm.client.view.domain.DomainEditor;
import de.gsv.idm.shared.dto.DomainDTO;

public class DomainEditorPresenter extends GeneralEditorPresenter<DomainDTO> {

	public interface DomainEditorDisplay extends GeneralEditorDisplay<DomainDTO> {
		void setChooseText(String text);

		HasSelectHandlers getChooseClick();

		void setChooseEnabled(boolean enabled);

	}

	interface ItemDriver extends SimpleBeanEditorDriver<DomainDTO, DomainEditor> {
	}

	private ItemDriver itemDriver = GWT.create(ItemDriver.class);

	private final DomainEditorDisplay editorDisplay;

	public DomainEditorPresenter(DomainEditorDisplay domainEditor) {
		super(DBController.getInstance().getDomainController(), domainEditor);
		this.editorDisplay = domainEditor;
	}

	@Override
	public IsWidget go() {
		itemDriver.initialize((DomainEditor) editorDisplay);
		bindView();
		return editorDisplay.asWidget();
	}

	private void bindView() {
		editorDisplay.setChooseText("Auswählen");
		editorDisplay.getChooseClick().addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				eventBus.fireEvent(new ViewDomainViewEvent(itemDriver.flush().getId()));
			}
		});
	}

	public void doEdit(DomainDTO domain) {
		referenceObject = domain;
		clonedEditObject = domain.clone();
		itemDriver.edit(domain.clone());
		editorDisplay.setSaveEnabled(true);
		editorDisplay.setSaveAndNewEnabled(true);
		editorDisplay.setChooseEnabled(domain.getId() != null && domain.getId() > 0);
		editorDisplay.setDeleteEnabled(true);
	}

	@Override
	public void doSave() {
		DomainDTO flushedDomain = itemDriver.flush();
		if (flushedDomain.getId() == null || flushedDomain.getId()< 0) {
			createObject(flushedDomain);
		} else {
			updateObject(flushedDomain, clonedEditObject.clone());
		}
	}

	@Override
	public void doDelete() {
		final DomainDTO toDelete = itemDriver.flush();
		if(toDelete != null && toDelete.getId() != null && toDelete.getId() > 0){
			deleteObject(toDelete);
		} else {
			eventBus.fireEvent(new DeletedDomainEvent(toDelete));
		}
		
	}

	@Override
	public void doNew() {
		DomainDTO newObject = new DomainDTO();
		newObject.setName("Neue Domäne");
		newObject.setId(createTempId());
		eventBus.fireEvent(new CreatedDomainEvent(newObject));
		doEdit(newObject);
	}

	public DomainEditorDisplay getDomainEditorDisplay() {
		return editorDisplay;
	}

	@Override
    protected void fireDeletedEvent() {
		eventBus.fireEvent(new DeletedDomainEvent(referenceObject));
    }

	@Override
    protected DomainDTO getFlushedObject() {
		return itemDriver.flush();
    }

	public boolean openChanges() {
		if(clonedEditObject != null){
			return itemDriver.isDirty();
		} else {
			return false;
		}
	    
    }

}
