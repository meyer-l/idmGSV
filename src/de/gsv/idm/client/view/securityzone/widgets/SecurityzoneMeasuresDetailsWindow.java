package de.gsv.idm.client.view.securityzone.widgets;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.presenter.securityzone.SecurityzoneMeasuresDetailsPresenter;
import de.gsv.idm.client.view.properties.SecurityzoneMeasureStatusObjectProperties;
import de.gsv.idm.client.view.securityzone.widgets.data.SecurityzoneMeasureStatusObject;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetMeasureLinkDTO;
import de.gsv.idm.shared.dto.MeasureDTO;

public class SecurityzoneMeasuresDetailsWindow extends Window {

	public SecurityzoneMeasuresDetailsWindow(AssetDTO asset) {
		SecurityzoneMeasureStatusObjectProperties property = GWT
		        .create(SecurityzoneMeasureStatusObjectProperties.class);
		ListStore<SecurityzoneMeasureStatusObject> store = new ListStore<SecurityzoneMeasureStatusObject>(
		        property.key());
		for (MeasureDTO measure : asset.getAssignedSecurityzone().getAllMeasures()) {
			SecurityzoneMeasureStatusObject statusObject = null;

			for (AssetMeasureLinkDTO assetMeasure : asset.getInheritedMeasures()) {
				if (assetMeasure.getMeasure().equals(measure)) {
					statusObject = new SecurityzoneMeasureStatusObject(measure,
					        assetMeasure.getStatus(), true);
				}
			}

			if (statusObject != null) {
				for (AssetMeasureLinkDTO assetMeasure : asset.getMeasureLinksWithoutInherited()) {
					if (assetMeasure.getMeasure().equals(measure)) {
						statusObject = new SecurityzoneMeasureStatusObject(measure,
						        assetMeasure.getStatus(), false);
					}
				}
			}

			if (statusObject != null) {
				store.add(statusObject);
			}

		}
		SecurityzoneMeasuresDetailsPresenter presenter = new SecurityzoneMeasuresDetailsPresenter(
		        new SecurityzoneMeasuresDetailsView(store));
		setBodyBorder(false);
		setWidth(610);
		setHeight(230);
		ContentPanel buttonPanel = new ContentPanel();
		buttonPanel.setHeaderVisible(false);
		add(buttonPanel);
		buttonPanel.add(presenter.go());
		TextButton okButton = new TextButton("Ok");
		final SecurityzoneMeasuresDetailsWindow view = this;
		okButton.addSelectHandler(new SelectHandler() {
			
			public void onSelect(SelectEvent event) {
				view.hide();				
			}
		});
		buttonPanel.addButton(okButton);
		setHeadingText("Umsetzungsstatus der Maßnahmen aus " + asset.getAssignedSecurityzoneName()
		        + " für Asset " + asset.getName());
		show();
	}

}
