package de.gsv.idm.client.view.information;

import de.gsv.idm.client.presenter.information.InformationEditorPresenter;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.dto.InformationDTO;

public class InformationEditorWindow extends EditorWindow<InformationDTO> {

	public InformationEditorWindow(Integer domain_id) {
		super(new InformationEditorPresenter(new InformationEditor(),
		        domain_id));
		buildInformationView("Neue Information anlegen");
	}

	public InformationEditorWindow(Integer domain_id,
	        InformationDTO information) {
		super(new InformationEditorPresenter(new InformationEditor(),
		        domain_id), information);
		buildInformationView("Information berarbeiten");
	}

	private void buildInformationView(String text) {
		setHeadingText(text);
	}

}
