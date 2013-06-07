package de.gsv.idm.client.view.informationdomain.assettype.category;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.presenter.informationdomain.assettype.category.AssettypeCategoryEditorPresenter.AssettypeCategoryEditorDisplay;
import de.gsv.idm.client.view.general.GeneralEditor;
import de.gsv.idm.client.view.validation.EmptyFieldValidator;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;

public class AssettypeCategoryEditor extends GeneralEditor<AssettypeCategoryDTO> implements
        AssettypeCategoryEditorDisplay {

	TextField name = new TextField();
	@Ignore
	TextButton chooseIconButton;

	@Ignore
	Image image;

	public AssettypeCategoryEditor() {
		container.add(new FieldLabel(name, "Name"), getFormData());
		name.addValidator(new EmptyFieldValidator());

		chooseIconButton = new TextButton();
		ImageResources bundler = GWT.create(ImageResources.class);
		chooseIconButton.setIcon(bundler.folderIcon());
		image = new Image();
		container.add(new FieldLabel(image, "Icon"), getHalfFormData());
		FieldLabel chooseIconButtonLabel = new FieldLabel(chooseIconButton, "");
		chooseIconButtonLabel.setLabelSeparator("");
		container.add(chooseIconButtonLabel, new VerticalLayoutData(-1, -1,
		        new Margins(5, 0, 5, 10)));
		addButtons();
	}

	@Override
	public void setChooseIconButtonText(String text) {
		chooseIconButton.setText(text);
	}

	@Override
	public HasSelectHandlers getChooseIconButtonClick() {
		return chooseIconButton;
	}

	@Override
	public Image getImage() {
		return image;
	}

	@Override
	public String getIconName() {
		return image.getTitle();
	}

	@Override
	public void setImageResource(ImageResource resource) {
		if (resource != null) {

			this.image.setResource(resource);
			this.image.setTitle(resource.getName());
			image.setVisible(true);
		} else {
			image.setVisible(false);
			image.setTitle("");
		}
	}

	@Override
    public void setChosseIconButtonToolTip(String toolTip) {
		chooseIconButton.setToolTip(toolTip);	    
    }

	@Override
    public void setEnabled(Boolean enabled) {
	    name.setEnabled(enabled);
	    chooseIconButton.setEnabled(enabled);
    }
}
