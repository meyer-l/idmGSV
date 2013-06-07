package de.gsv.idm.client.view.informationdomain.asset.widgets;

import de.gsv.idm.client.presenter.informationdomain.asset.AssetEditorPresenter;
import de.gsv.idm.client.view.informationdomain.asset.AssetEditor;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.dto.AssetDTO;

public class AssetEditorWindow extends EditorWindow<AssetDTO> {
	
	public AssetEditorWindow(Integer domainId) {
		this(domainId, AssetDTO.getStaticClassName() + " erstellen");

	}

	public AssetEditorWindow(Integer domainId, String string) {
		super(new AssetEditorPresenter(new AssetEditor(), domainId), string);
	}

	public AssetEditorWindow(AssetDTO object) {
		super(new AssetEditorPresenter(new AssetEditor(), object.getDomainId()), object,
				AssetDTO.getStaticClassName() + " " + object.getLabel() + " bearbeiten");
	}

}
