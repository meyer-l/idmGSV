package de.gsv.idm.client.view.general;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.form.FormPanel;

import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.presenter.general.GeneralEditorPresenter.GeneralEditorDisplay;

public abstract class GeneralEditor<T> extends GeneralView implements IsWidget, Editor<T>, GeneralEditorDisplay<T>{
	
	protected ContentPanel buttonPanel;
	protected FormPanel formPanel;
	@Ignore
	protected TextButton save;
	@Ignore
	protected TextButton saveAndNew;
	@Ignore
	protected TextButton delete;
	protected VerticalLayoutContainer container;
	protected ScrollPanel scrollPanel;
	
	public GeneralEditor() {
		buttonPanel = new ContentPanel();		
		formPanel = new FormPanel();		
		scrollPanel = new ScrollPanel(formPanel);
		scrollPanel.setStyleName("blueBackground");		
		buttonPanel.setWidget(scrollPanel);
		formPanel.setLabelWidth(200);
		container = new VerticalLayoutContainer();
		formPanel.add(container);
		delete = new TextButton();
		delete.setEnabled(false);
		ImageResources bundler = GWT.create(ImageResources.class);
		delete.setIcon(bundler.remove());
		save = new TextButton();
		save.setEnabled(false);
		saveAndNew = new TextButton();
		saveAndNew.setEnabled(false);
		saveAndNew.setIcon(bundler.add());
		bundler = GWT.create(ImageResources.class);
		save.setIcon(bundler.disk());
	}
	
	protected void addButtons(){
		buttonPanel.addButton(delete);
		buttonPanel.addButton(save);
		buttonPanel.addButton(saveAndNew);
	}
	
	protected void addButton(Widget button){
		buttonPanel.addButton(button);
	}
	
	public Widget asWidget() {
		return buttonPanel;
	}
	
	public void setDeleteEnabled(boolean enabled) {
		delete.setEnabled(enabled);
	}

	public void setSaveEnabled(boolean enabled) {
		save.setEnabled(enabled);
	}
	
	public HasSelectHandlers getSaveClick() {
		return save;
	}
	
	public HasSelectHandlers getDeleteClick() {
		return delete;
	}
	
	public void setSaveText(String text) {
		save.setText(text);
	}
	
	public void setDeleteText(String text) {
		delete.setText(text);
	}
	
	public Integer getEditorHeight(){
		return buttonPanel.getOffsetHeight();
	}
	
	public boolean isFormValid(){
		return formPanel.isValid();
	}

	@Override
    public void setEditHeading(String text) {
	    buttonPanel.setHeadingText(text);
    }

	@Override
    public void setSaveAndNewText(String text) {
		saveAndNew.setText(text);
    }

	@Override
    public HasSelectHandlers getSaveAndNewClick() {
	    return saveAndNew;
    }

	@Override
    public void setSaveAndNewEnabled(boolean enabled) {
		saveAndNew.setEnabled(enabled);
    }

	@Override
    public void setSaveAndNewVisibility(boolean visible) {
	    saveAndNew.setVisible(visible);
    }

	@Override
    public void resetScrollBar() {
	   scrollPanel.scrollToTop();
    }

	@Override
   abstract public void setEnabled(Boolean enabled);
	
}
