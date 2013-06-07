package de.gsv.idm.client.view.widgets.cell;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.sencha.gxt.core.client.XTemplates;

public class ToolTipTextCell extends TextCell {

	interface ToolTipCellTemplates extends XTemplates {
		@XTemplate("<div qtip=\"{name}\" qtitle=\"\">{name}</div>")
		SafeHtml name(String name);
	}

	public ToolTipTextCell() {
		super(new SafeHtmlRenderer<String>() {

			@Override
			public SafeHtml render(String object) {
				final ToolTipCellTemplates comboBoxTemplates = GWT
				        .create(ToolTipCellTemplates.class);
				return comboBoxTemplates.name(object);
			}

			@Override
			public void render(String object, SafeHtmlBuilder appendable) {
				final ToolTipCellTemplates comboBoxTemplates = GWT
				        .create(ToolTipCellTemplates.class);
				appendable.append(comboBoxTemplates.name(object));
			}
		});
	}

}
