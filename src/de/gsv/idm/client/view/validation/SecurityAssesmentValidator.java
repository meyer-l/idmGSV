package de.gsv.idm.client.view.validation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;

import de.gsv.idm.shared.dto.SecurityLevelDTO;

public class SecurityAssesmentValidator implements Validator<String> {

	ComboBox<SecurityLevelDTO> box;

	public SecurityAssesmentValidator(ComboBox<SecurityLevelDTO> box) {
		this.box = box;
	}

	@Override
	public List<EditorError> validate(Editor<String> editor, String value) {
		List<EditorError> res = null;
		if (box.getValue() != null
		        && box.getValue().getId() > SecurityLevelDTO.getDefaultSecurityLevel().getId()
		        && (value == null || "".equals(value))) {
			List<EditorError> errors = new ArrayList<EditorError>();
			errors.add(new DefaultEditorError(editor, "Dieses Feld muss ausgefüllt werden", ""));
			return errors;
		}
		return res;
	}

}
