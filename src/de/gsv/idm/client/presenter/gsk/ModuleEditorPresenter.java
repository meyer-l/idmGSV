package de.gsv.idm.client.presenter.gsk;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.presenter.general.GeneralEditorPresenter;
import de.gsv.idm.client.view.gsk.ModuleEditor;
import de.gsv.idm.shared.dto.ModuleDTO;

public class ModuleEditorPresenter extends GeneralEditorPresenter<ModuleDTO> {

	public interface ModuleEditorDisplay extends GeneralEditorDisplay<ModuleDTO> {
		public void visibilityModulesAddContainer(boolean visible);

		public void visibilityThreatsAddContainer(boolean visible);

		public void visibilityMeasuresAddContainer(boolean visible);
	}

	interface ItemDriver extends SimpleBeanEditorDriver<ModuleDTO, ModuleEditor> {
	}

	private ItemDriver itemDriver = GWT.create(ItemDriver.class);
	private final ModuleEditorDisplay editorDisplay;

	public ModuleEditorPresenter(ModuleEditorDisplay editorDisplay) {
		super(DBController.getInstance().getModuleController(), editorDisplay);
		this.editorDisplay = editorDisplay;
	}

	@Override
	public IsWidget go() {
		itemDriver.initialize((ModuleEditor) editorDisplay);
		return editorDisplay.asWidget();
	}

	@Override
	public void doNew() {
	}

	@Override
	public void doSave() {
	}

	@Override
	public void doDelete() {
	}

	public void doEdit(ModuleDTO module) {
		clonedEditObject = module.clone();
		itemDriver.edit(module.clone());
		editorDisplay.setSaveEnabled(true);
		editorDisplay.visibilityModulesAddContainer(module.getModulesAdd().size() != 0);
		editorDisplay.visibilityThreatsAddContainer(module.getThreatsAdd().size() != 0);
		editorDisplay.visibilityMeasuresAddContainer(module.getMeasuresAdd().size() != 0);
	}

	@Override
    protected void fireDeletedEvent() {
	    
    }

	@Override
    protected ModuleDTO getFlushedObject() {
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
