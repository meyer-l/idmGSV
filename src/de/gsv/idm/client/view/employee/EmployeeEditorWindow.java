package de.gsv.idm.client.view.employee;

import de.gsv.idm.client.presenter.employee.EmployeeEditorPresenter;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.dto.EmployeeDTO;

public class EmployeeEditorWindow extends EditorWindow<EmployeeDTO> {
	public EmployeeEditorWindow(Integer domain_id) {
		super(new EmployeeEditorPresenter(new EmployeeEditor(), domain_id));
		buildInformationView("Neuen Mitarbeiter anlegen");
	}

	public EmployeeEditorWindow(EmployeeDTO employee) {
		super(new EmployeeEditorPresenter(new EmployeeEditor(), employee.getDomainId()), employee);
		buildInformationView("Mitarbeiter berarbeiten");
	}

	private void buildInformationView(String text) {
		setHeadingText(text);
	}
}
