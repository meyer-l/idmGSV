package de.gsv.idm.client.view.informationdomain.assettype;

import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;

import de.gsv.idm.client.presenter.informationdomain.assettype.AssettypeFromAssetEditorPresenter.AssettypeFromAssetEditorDisplay;

public class AssettypeFromModelEditor extends AssettypeEditor implements AssettypeFromAssetEditorDisplay {
	
	@Ignore
	TextButton createNewAsset;

	@Override
	protected void addButtons(){
		buttonPanel.addButton(delete);
		createNewAsset = new TextButton();		
		//buttonPanel.addButton(createNewAsset);
		buttonPanel.addButton(save);
	}	
	
	@Override
	public HasSelectHandlers getSaveNewAssetClick() {
		return createNewAsset;
	}

	@Override
	public void setSaveNewAssetText(String text) {
		createNewAsset.setText(text);

	}
	
	public void setSaveNewAssetToolTip(String text){
		createNewAsset.setToolTip(text);
		save.setToolTip(text);
	}

}
