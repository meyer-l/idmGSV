package de.gsv.idm.client.view.informationdomain.assettype.widgets;

import de.gsv.idm.client.presenter.informationdomain.assettype.AssettypeEditorPresenter;
import de.gsv.idm.client.view.informationdomain.assettype.AssettypeEditor;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.dto.AssettypeDTO;

public class AssettypeEditorWindow extends EditorWindow<AssettypeDTO> {

	public AssettypeEditorWindow(Integer domainId) {
		this(domainId, AssettypeDTO.getStaticClassName() + " erstellen");

	}

	public AssettypeEditorWindow(Integer domainId, String string) {
		super(new AssettypeEditorPresenter(new AssettypeEditor(), domainId), string);
		setWidth(1100);		
	}

	public AssettypeEditorWindow(AssettypeDTO object) {
		super(new AssettypeEditorPresenter(new AssettypeEditor(), object.getDomainId()), object,
		        AssettypeDTO.getStaticClassName() + " bearbeiten");
		setWidth(1100);
	}

}
