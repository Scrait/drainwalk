package com.tom.cpm.shared.editor.gui.popup;

import com.tom.cpl.gui.IGui;
import com.tom.cpl.gui.elements.Button;
import com.tom.cpl.gui.elements.Checkbox;
import com.tom.cpl.gui.elements.Label;
import com.tom.cpl.gui.elements.PopupPanel;
import com.tom.cpl.math.Box;
import com.tom.cpm.shared.editor.Editor;
import com.tom.cpm.shared.editor.anim.AnimationEncodingData;
import com.tom.cpm.shared.editor.util.GetFreeSkinSlots;
import com.tom.cpm.shared.editor.util.PlayerSkinLayer;

public class AnimEncConfigPopup extends PopupPanel {

	public AnimEncConfigPopup(IGui gui, Editor editor, Runnable onOk) {
		super(gui);
		setBounds(new Box(0, 0, 300, 200));

		AnimationEncodingData dt = editor.animEnc != null ? new AnimationEncodingData(editor.animEnc) : GetFreeSkinSlots.getDefault(editor);

		Label lbl = new Label(gui, gui.i18nFormat("label.cpm.skin_layers_to_use"));
		lbl.setBounds(new Box(5, 0, 0, 0));
		addElement(lbl);

		int yC = 0;
		for (PlayerSkinLayer layer : PlayerSkinLayer.VALUES) {
			int y = yC++;
			Checkbox chkbxEnc = new Checkbox(gui, gui.i18nFormat("label.cpm.layer_" + layer.getLowerName()));
			chkbxEnc.setBounds(new Box(5, 10 + y * 25, 115, 20));
			addElement(chkbxEnc);

			Checkbox chkbxEn = new Checkbox(gui, gui.i18nFormat("label.cpm.visible"));
			chkbxEn.setBounds(new Box(125, 10 + y * 25, 80, 20));
			addElement(chkbxEn);

			chkbxEnc.setAction(() -> {
				boolean nv = !chkbxEnc.isSelected();
				chkbxEnc.setSelected(nv);
				chkbxEn.setEnabled(!nv);
				if(nv)dt.freeLayers.add(layer);
				else dt.freeLayers.remove(layer);
			});
			chkbxEn.setAction(() -> {
				chkbxEn.setSelected(!chkbxEn.isSelected());
				dt.defaultLayerValue.put(layer, chkbxEn.isSelected());
			});
			boolean enc = dt.freeLayers.contains(layer);
			chkbxEnc.setSelected(enc);
			chkbxEn.setEnabled(!enc);
			chkbxEn.setSelected(dt.defaultLayerValue.computeIfAbsent(layer, k -> !enc));

			Button clearLayer = new Button(gui, gui.i18nFormat("button.cpm.clearLayer"), () -> {
				GetFreeSkinSlots.clearLayerArea(editor.vanillaSkin, editor.skinType, layer);
			});
			clearLayer.setBounds(new Box(210, 10 + y * 25, 80, 20));
			addElement(clearLayer);
		}

		Button ok = new Button(gui, gui.i18nFormat("button.cpm.ok"), () -> {
			close();
			editor.animEnc = dt;
			editor.markDirty();
			if(onOk != null) {
				onOk.run();
			}
		});
		ok.setBounds(new Box(5, 170, 80, 20));
		addElement(ok);
	}

	@Override
	public String getTitle() {
		return gui.i18nFormat("button.cpm.animEncSettings");
	}
}
