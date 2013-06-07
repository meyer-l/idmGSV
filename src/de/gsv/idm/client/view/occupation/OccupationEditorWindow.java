package de.gsv.idm.client.view.occupation;

import de.gsv.idm.client.presenter.occupation.OccupationEditorPresenter;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.dto.OccupationDTO;

public class OccupationEditorWindow extends EditorWindow<OccupationDTO> {

	public OccupationEditorWindow(Integer domain_id) {
		super(new OccupationEditorPresenter(new OccupationEditor(), domain_id));
		buildInformationView("Neuen Dienstposten anlegen");
	}

	public OccupationEditorWindow(Integer domain_id, OccupationDTO occupation) {
		super(new OccupationEditorPresenter(new OccupationEditor(), domain_id), occupation);
		buildInformationView("Dienstposten bearbeiten");
	}

	private void buildInformationView(String text) {
		setHeadingText(text);
	}

}
