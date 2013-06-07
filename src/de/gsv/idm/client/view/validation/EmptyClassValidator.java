package de.gsv.idm.client.view.validation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;


public class EmptyClassValidator<T> implements Validator<T> {

	@Override
	public List<EditorError> validate(Editor<T> editor, T value) {
		List<EditorError> res = null;
		if (value == null) {
			List<EditorError> errors = new ArrayList<EditorError>();
			errors.add(new DefaultEditorError(editor, "Dieses Feld muss ausgefüllt werden", ""));
			return errors;
		}
		return res;
	}

}
