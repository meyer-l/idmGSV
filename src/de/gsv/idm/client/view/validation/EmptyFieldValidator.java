package de.gsv.idm.client.view.validation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;

public class EmptyFieldValidator implements Validator<String> {

	@Override
	public List<EditorError> validate(Editor<String> editor, String value) {
		List<EditorError> res = null;
		if (value == null || "".equals(value)) {
			List<EditorError> errors = new ArrayList<EditorError>();
			errors.add(new DefaultEditorError(editor, "Dieses Feld muss ausgefüllt werden", ""));
			return errors;
		}
		return res;
	}

}
