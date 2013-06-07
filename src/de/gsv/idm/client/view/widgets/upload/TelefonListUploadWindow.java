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

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.gui.change.ShowParsedChangeTabEvent;

public class TelefonListUploadWindow extends Window {
	public TelefonListUploadWindow(final Integer domainId, final String sessionId) {
		final Window window = this;
		setHeadingText("Telefonliste hochladen");
		final FormPanel form = new FormPanel();
		form.setAction(GWT.getModuleBaseURL() + "telefonListUpload");
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
						return "Telefonliste auswählen";
					}
				};
			}
		};
		file.setName("telefonList");
		file.setAllowBlank(false);

		FieldLabel label = new FieldLabel(file, "Telefonliste (.csv, UTF-8)");
		label.setLabelWidth(130);
		verticalLayoutContainer.add(label, new VerticalLayoutData(-18, -1));
		TextField domainIdField = new TextField();
		domainIdField.setName("domainId");
		domainIdField.setValue(domainId.toString(), false);
		verticalLayoutContainer.add(domainIdField);
		domainIdField.hide();
		TextField sessionIdField = new TextField();
		sessionIdField.setName("sessionId");
		sessionIdField.setValue(sessionId.toString(), false);
		verticalLayoutContainer.add(sessionIdField);
		sessionIdField.hide();
		TextButton btn = new TextButton("Telefonliste hochladen");
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
					MessageBox box = new MessageBox("Telefonliste einlesen:",
					        "Beim hochladen oder verarbeiten der Telefonliste trat ein Fehler auf");
					box.setIcon(MessageBox.ICONS.error());
					box.show();
				} else if (event.getResults().length() < 15 && event.getResults().contains("1")) {
					MessageBox box = new MessageBox("Telefonliste einlesen:",
					        "Die Telefonliste wurde erfolgreich hochgeladen und es wurden Änderungen gefunden");
					box.setIcon(MessageBox.ICONS.info());
					box.show();
					DBController.getInstance().getEventBus()
					        .fireEvent(new ShowParsedChangeTabEvent(domainId));
				} else if (event.getResults().length() < 15 && event.getResults().contains("0")) {
					MessageBox box = new MessageBox("Telefonliste einlesen:",
					        "Die Telefonliste wurde erfolgreich hochgeladen, aber es wurden keine Änderungen gefunden");
					box.setIcon(MessageBox.ICONS.info());
					box.show();
				} else {
					MessageBox box = new MessageBox("Telefonliste einlesen:",
					        "Beim hochladen oder verarbeiten der Telefonliste trat ein Fehler auf");
					box.setIcon(MessageBox.ICONS.error());
					box.show();
				}

				window.hide();

			}
		});

		addButton(btn);
		add(form);
		show();
	}
}
