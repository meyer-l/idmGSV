package de.gsv.idm.client.view.widgets.window;

import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.presenter.general.GeneralEditorPresenter;
import de.gsv.idm.client.presenter.general.GeneralEditorPresenter.GeneralEditorDisplay;
import de.gsv.idm.client.presenter.informationdomain.assettype.AssettypeFromAssetEditorPresenter.AssettypeFromAssetEditorDisplay;
import de.gsv.idm.shared.dto.GeneralDTO;

public class EditorWindow<T extends GeneralDTO<T>> extends Window {

	protected GeneralEditorPresenter<T> presenter;
	protected GeneralEditorDisplay<T> display;
	public EditorWindow(GeneralEditorPresenter<T> presenter) {
		this.presenter = presenter;
		this.display = presenter.getDisplay();
		buildView();
		presenter.doNew();
	}

	public EditorWindow(GeneralEditorPresenter<T> presenter, String title) {
		this(presenter);
		setHeadingText(title);
	}

	public EditorWindow(GeneralEditorPresenter<T> presenter, T object) {
		this.presenter = presenter;
		this.display = presenter.getDisplay();
		buildView();
		presenter.edit(object);
	}

	public EditorWindow(GeneralEditorPresenter<T> presenter, T object, String title) {
		this(presenter, object);
		setHeadingText(title);
	}

	private void buildView() {
		setBodyBorder(false);
		setWidth(1000);
		setHeight(600);
		setPosition(30, 0);
		add(presenter.go());
		
		addSaveClick();

		final EditorWindow<T> window = this;
	
		display.getDeleteClick().addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				window.hide();
			}
		});
		
		//Prevents generalized type bug when compiled from ant ...
		Object temp = display;
		if (temp instanceof AssettypeFromAssetEditorDisplay) {
			AssettypeFromAssetEditorDisplay castedDisplay= (AssettypeFromAssetEditorDisplay) temp;
			castedDisplay.getSaveNewAssetClick().addSelectHandler(
			        new SelectHandler() {
				        @Override
				        public void onSelect(SelectEvent event) {
					        if (display.isFormValid()) {
						        window.hide();
					        }
				        }
			        });
		}
		display.setEnabled(true);
		show();
	}

	protected void addSaveClick() {
		display.getSaveClick().addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				if (display.isFormValid()) {
			        hide();
		        }
			}
		});
    }

}
