package de.gsv.idm.client.presenter.gsk;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.presenter.general.GeneralEditorPresenter;
import de.gsv.idm.client.view.gsk.MeasureEditor;
import de.gsv.idm.shared.dto.MeasureDTO;

public class MeasureEditorPresenter extends GeneralEditorPresenter<MeasureDTO> {
	
	public interface MeasureEditorDisplay extends GeneralEditorDisplay<MeasureDTO>{
		public void visibilityModulesContainer(boolean visible);
		public void visibilityThreatsContainer(boolean visible);
		public void visibilityMeasuresContainer(boolean visible);
	}
	interface ItemDriver extends SimpleBeanEditorDriver<MeasureDTO, MeasureEditor> {}
	
	private ItemDriver itemDriver = GWT.create(ItemDriver.class);
	private final MeasureEditorDisplay editorDisplay;
	
	public MeasureEditorPresenter(
            
            MeasureEditorDisplay editorDisplay) {
	    super(DBController.getInstance().getMeasureController(), editorDisplay);
	    this.editorDisplay = editorDisplay;
    }

	@Override
	public IsWidget go() {
		itemDriver.initialize((MeasureEditor) editorDisplay);
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

	@Override
    public void doEdit(MeasureDTO object) {
		clonedEditObject = object.clone();
		itemDriver.edit(object.clone());
		editorDisplay.setSaveEnabled(true);	  
		editorDisplay.visibilityModulesContainer(object.getModules().size() != 0);
		editorDisplay.visibilityThreatsContainer(object.getThreats().size() != 0);
		editorDisplay.visibilityMeasuresContainer(object.getMeasures().size() != 0);
    }

	@Override
    protected void fireDeletedEvent() {
	    
    }

	@Override
    protected MeasureDTO getFlushedObject() {
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
