package de.gsv.idm.client.view.securityzone;

import de.gsv.idm.client.presenter.securityzone.SecurityzoneEditorPresenter;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

public class SecurityzoneEditorWindow extends EditorWindow<SecurityzoneDTO> {

	public SecurityzoneEditorWindow() {
		super(new SecurityzoneEditorPresenter(new SecurityzoneEditor()), "Neue Schutzzone anlegen");
	}
	
	public SecurityzoneEditorWindow(SecurityzoneDTO securityzone) {
		this(securityzone, "Neue Schutzzone anlegen");
	}

	public SecurityzoneEditorWindow(SecurityzoneDTO securityzone, String title) {
		super(new SecurityzoneEditorPresenter(new SecurityzoneEditor()), securityzone, title);
	}
}
