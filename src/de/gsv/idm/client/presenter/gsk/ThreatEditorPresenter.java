package de.gsv.idm.client.presenter.gsk;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.presenter.general.GeneralEditorPresenter;
import de.gsv.idm.client.view.gsk.ThreatEditor;
import de.gsv.idm.shared.dto.ThreatDTO;

public class ThreatEditorPresenter extends GeneralEditorPresenter<ThreatDTO> {
	public interface ThreatEditorDisplay extends GeneralEditorDisplay<ThreatDTO>{
		public void visibilityModulesContainer(boolean visible);
		public void visibilityThreatsContainer(boolean visible);
		public void visibilityMeasuresContainer(boolean visible);
	}
	interface ItemDriver extends SimpleBeanEditorDriver<ThreatDTO, ThreatEditor> {}
	
	private ItemDriver itemDriver = GWT.create(ItemDriver.class);
	private final ThreatEditorDisplay editorDisplay;
	
	public ThreatEditorPresenter(ThreatEditorDisplay editorDisplay) {
	    super(DBController.getInstance().getThreatController(), editorDisplay);
	    this.editorDisplay = editorDisplay;
    }

	@Override
    public IsWidget go() {
		itemDriver.initialize((ThreatEditor) editorDisplay);
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
    public void doEdit(ThreatDTO object) {
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
    protected ThreatDTO getFlushedObject() {
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
