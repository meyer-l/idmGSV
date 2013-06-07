package de.gsv.idm.client.presenter.informationdomain.assettype.category;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.db.assettype.category.CreatedAssettypeCategoryEvent;
import de.gsv.idm.client.event.db.assettype.category.DeletedAssettypeCategoryEvent;
import de.gsv.idm.client.presenter.general.GeneralEditorPresenter;
import de.gsv.idm.client.presenter.helper.ImageHelper;
import de.gsv.idm.client.view.informationdomain.assettype.category.AssettypeCategoryEditor;
import de.gsv.idm.client.view.widgets.window.ChooseImageWindow;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;

public class AssettypeCategoryEditorPresenter extends GeneralEditorPresenter<AssettypeCategoryDTO> {

	public interface AssettypeCategoryEditorDisplay extends
	        GeneralEditorDisplay<AssettypeCategoryDTO> {
		void setChooseIconButtonText(String text);

		HasSelectHandlers getChooseIconButtonClick();

		void setChosseIconButtonToolTip(String toolTip);

		Image getImage();

		String getIconName();

		void setImageResource(ImageResource image);
	}

	interface ItemDriver extends
	        SimpleBeanEditorDriver<AssettypeCategoryDTO, AssettypeCategoryEditor> {
	}

	private ItemDriver itemDriver = GWT.create(ItemDriver.class);

	private final AssettypeCategoryEditorDisplay editorDisplay;

	public AssettypeCategoryEditorPresenter(AssettypeCategoryEditorDisplay editorDisplay) {
		super(DBController.getInstance().getAssettypeCategoryController(), editorDisplay);
		this.editorDisplay = editorDisplay;
	}

	@Override
	public IsWidget go() {
		itemDriver.initialize((AssettypeCategoryEditor) editorDisplay);
		editorDisplay.setEnabled(false);
		bindView();
		return editorDisplay.asWidget();
	}

	private void bindView() {
		editorDisplay.setChooseIconButtonText("Icon auswählen");
		editorDisplay.setChosseIconButtonToolTip("Das Icon wird zur Darstellung von"
		        + " Asset-Grundtypen im Informationsverbund verwendet, wenn dem"
		        + " Asset-Grundtyp kein eigenes Icon zugewiesen wurde.");
		editorDisplay.getChooseIconButtonClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				new ChooseImageWindow(editorDisplay.getImage());
			}
		});
	}

	public void doEdit(AssettypeCategoryDTO object) {
		referenceObject = object;
		clonedEditObject = object.clone();
		itemDriver.edit(object.clone());
		editorDisplay.setImageResource(ImageHelper.getImageResourceFromIconName(object
		        .getIconName()));
		editorDisplay.setSaveEnabled(true);
		editorDisplay.setSaveAndNewEnabled(true);
		editorDisplay.setDeleteEnabled(true);
	}

	@Override
	public void doSave() {
		AssettypeCategoryDTO flushed = itemDriver.flush();
		flushed.setIconName(editorDisplay.getIconName());
		if (clonedEditObject.getId() == null || flushed.getId()< 0) {
			createObject(flushed);
		} else {
			updateObject(flushed, clonedEditObject.clone());
		}
	}

	@Override
	public void doDelete() {
		final AssettypeCategoryDTO toDelete = itemDriver.flush();
		if(toDelete != null && toDelete.getId() != null && toDelete.getId() > 0){
			deleteObject(toDelete);
		} else {
			eventBus.fireEvent(new DeletedAssettypeCategoryEvent(toDelete));
		}
	}

	@Override
	public void doNew() {
		AssettypeCategoryDTO newObject = new AssettypeCategoryDTO();
		newObject.setName("Neue Asset-Grundtyp-Kategorie");
		newObject.setId(createTempId());
		eventBus.fireEvent(new CreatedAssettypeCategoryEvent(newObject));
		doEdit(newObject);
	}

	@Override
    protected void fireDeletedEvent() {
		eventBus.fireEvent(new DeletedAssettypeCategoryEvent(referenceObject));
    }

	@Override
    protected AssettypeCategoryDTO getFlushedObject() {
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
