package de.gsv.idm.client.view.domain;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.presenter.domain.DomainEditorPresenter.DomainEditorDisplay;
import de.gsv.idm.client.view.general.GeneralEditor;
import de.gsv.idm.client.view.validation.EmptyFieldValidator;
import de.gsv.idm.shared.dto.DomainDTO;

public class DomainEditor extends GeneralEditor<DomainDTO> implements DomainEditorDisplay {

	private TextButton choose;

	TextField name = new TextField();
	TextField ident = new TextField();

	public DomainEditor() {
		choose = new TextButton();
		choose.setEnabled(false);
		ImageResources bundler = GWT.create(ImageResources.class);
		choose.setIcon(bundler.folderOpen());
		container.add(new FieldLabel(name, "Name"), getFormData());
		name.addValidator(new EmptyFieldValidator());
		name.setEnabled(false);
		container.add(new FieldLabel(ident, "Ident"), getFormData());
		ident.setEnabled(false);
		addButtons();
	}

	public void addButtons() {
		super.addButtons();
		addButton(choose);
	}

	@Override
	public void setSaveEnabled(boolean enabled) {
		super.setSaveEnabled(enabled);
	}

	public void setChooseEnabled(boolean enabled) {
		choose.setEnabled(enabled);		
	}

	public HasSelectHandlers getChooseClick() {
		return choose;
	}

	public void setChooseText(String text) {
		choose.setText(text);
	}

	@Override
    public void setEnabled(Boolean enabled) {
		name.setEnabled(enabled);
		ident.setEnabled(enabled);
    }

}
