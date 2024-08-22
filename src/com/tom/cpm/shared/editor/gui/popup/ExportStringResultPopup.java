package com.tom.cpm.shared.editor.gui.popup;

import com.tom.cpl.gui.Frame;
import com.tom.cpl.gui.elements.Button;
import com.tom.cpl.gui.elements.Label;
import com.tom.cpl.gui.elements.PopupPanel;
import com.tom.cpl.gui.elements.TextField;
import com.tom.cpl.math.Box;

public class ExportStringResultPopup extends PopupPanel {
	public ExportStringResultPopup(Frame frm, String resultName, String result) {
		super(frm.getGui());

		setBounds(new Box(0, 0, 260, 80));

		Label lbl1 = new Label(gui, gui.i18nFormat("label.cpm.result." + resultName));
		lbl1.setBounds(new Box(5, 0, 0, 0));
		addElement(lbl1);

		Label lbl2 = new Label(gui, gui.i18nFormat("label.cpm.result." + resultName + ".desc"));
		lbl2.setBounds(new Box(5, 10, 0, 0));
		addElement(lbl2);

		TextField txtf = new TextField(gui);
		txtf.setText(result);
		txtf.setBounds(new Box(5, 20, 205, 20));
		addElement(txtf);

		Button okBtn = new Button(gui, gui.i18nFormat("button.cpm.ok"), this::close);
		okBtn.setBounds(new Box(110, 50, 40, 20));
		addElement(okBtn);

		Button cpyBtn = new Button(gui, gui.i18nFormat("button.cpm.copy"), () -> gui.setClipboardText(result));
		cpyBtn.setBounds(new Box(215, 20, 40, 20));
		addElement(cpyBtn);
	}

	@Override
	public String getTitle() {
		return gui.i18nFormat("label.cpm.exportSkin");
	}
}
