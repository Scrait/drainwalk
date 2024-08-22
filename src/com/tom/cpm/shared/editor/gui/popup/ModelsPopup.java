package com.tom.cpm.shared.editor.gui.popup;

import java.io.File;
import java.util.List;

import com.tom.cpl.gui.elements.PopupPanel;
import com.tom.cpl.math.Box;
import com.tom.cpm.shared.editor.gui.EditorGui;
import com.tom.cpm.shared.gui.ViewportCamera;
import com.tom.cpm.shared.gui.panel.ModelsPanel;

public class ModelsPopup extends PopupPanel {
	public ViewportCamera camera = new ViewportCamera();
	private ModelsPanel panel;

	public ModelsPopup(EditorGui gui) {
		super(gui.getGui());
		panel = new ModelsPanel(gui, camera);
		addElement(panel);
		panel.setSize(700, 400);
		setOnClosed(() -> {
			gui.getEditor().displayViewport.accept(true);
			panel.onClosed();
		});
		setBounds(new Box(0, 0, 700, 400));
		gui.getEditor().displayViewport.accept(false);
	}

	@Override
	public String getTitle() {
		return gui.i18nFormat("button.cpm.models");
	}

	@Override
	public void filesDropped(List<File> files) {
		panel.filesDropped(files);
	}
}
