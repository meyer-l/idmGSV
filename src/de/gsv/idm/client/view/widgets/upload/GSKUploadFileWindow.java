package de.gsv.idm.client.view.widgets.upload;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;
import com.sencha.gxt.widget.core.client.form.TextField;

public class GSKUploadFileWindow extends Window {

	public GSKUploadFileWindow(final String sessionId) {
		final FormPanel form = new FormPanel();
		final Window window = this;
		form.setAction(GWT.getModuleBaseURL() + "gskFileUpload");
		form.setEncoding(Encoding.MULTIPART);
		form.setMethod(Method.POST);
		form.setHeight(50);
		form.setWidth(500);
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		form.add(verticalLayoutContainer);

		final FileUploadField file = new FileUploadField() {
			public FileUploadFieldMessages getMessages() {
				return new FileUploadFieldMessages() {

					@Override
					public String browserText() {
						return "GSK-Katalog auswählen";
					}
				};
			}
		};
		file.setName("uploadedfile");
		file.setAllowBlank(false);
		FieldLabel label = new FieldLabel(file, "GSK-Katalog (.xml)");
		label.setLabelWidth(130);
		verticalLayoutContainer.add(label, new VerticalLayoutData(-18, -1));
		TextField sessionIdField = new TextField();
		sessionIdField.setName("sessionId");
		sessionIdField.setValue(sessionId, false);
		verticalLayoutContainer.add(sessionIdField);
		sessionIdField.hide();
		TextButton btn = new TextButton("Hochladen");
		btn.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				form.submit();
			}
		});
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				if (event.getResults().length() < 15 && event.getResults().contains("-1")) {
					MessageBox box = new MessageBox("GSK einlesen:",
					        "Beim hochladen oder verarbeiten des GSK trat ein Fehler auf");
					box.setIcon(MessageBox.ICONS.error());
					box.show();
				} else if (event.getResults().length() < 15 && event.getResults().contains("1")) {
					MessageBox box = new MessageBox("GSK einlesen:",
					        "Der GSK wurde erfolgreich hochgeladen und verarbeitet.");
					box.setIcon(MessageBox.ICONS.info());
					box.show();
				} else if (event.getResults().length() < 15 && event.getResults().contains("0")) {
					MessageBox box = new MessageBox("GSK einlesen:",
					        "Es wurde kein GSK angegeben oder die Benutzersession ist ungülitg.");
					box.setIcon(MessageBox.ICONS.info());
					box.show();
				} else {
					MessageBox box = new MessageBox("GSK einlesen:",
					        "Beim hochladen oder verarbeiten des GSK trat ein Fehler auf");
					box.setIcon(MessageBox.ICONS.error());
					box.show();
				}

				window.hide();
			}
		});
		addButton(btn);
		add(form);
	}
}
