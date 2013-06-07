package de.gsv.idm.client.view.widgets.cell;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.XTemplates;

public class ToolTipCheckCell extends CheckBoxCell {
	interface ToolTipCellTemplates extends XTemplates {
		@XTemplate("<div qtip=\"Vererbung\" qtitle=\"\">{toolTip}</div>")
		SafeHtml inherit(String toolTip);
	}
	
	public ToolTipCheckCell(){
		
	}
}
