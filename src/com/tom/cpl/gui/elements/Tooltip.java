package com.tom.cpl.gui.elements;

import java.util.Locale;

import com.tom.cpl.gui.Frame;
import com.tom.cpl.gui.MouseEvent;
import com.tom.cpl.math.Box;
import com.tom.cpm.shared.config.Keybind;
import com.tom.cpm.shared.gui.Keybinds;

public class Tooltip extends Panel {
	private final Frame frm;
	private String openWikiPage;

	protected Tooltip(Frame frm) {
		super(frm.getGui());
		this.frm = frm;
		setBackgroundColor(gui.getColors().popup_border);
	}

	public Tooltip(Frame frm, String text) {
		this(frm);
		String[] lines = text.split("\\\\");

		int wm = 180;
		for (int i = 0; i < lines.length; i++) {
			int w = gui.textWidth(lines[i]);
			if(w > wm)wm = w;
		}

		for (int i = 0; i < lines.length; i++) {
			addElement(new Label(gui, lines[i]).setBounds(new Box(5, 5 + i * 10, 0, 0)));
		}
		setBounds(new Box(0, 0, wm + 20, 8 + lines.length * 10));
	}

	public Tooltip(Frame frm, String text, Keybind kb) {
		this(frm, kb.formatTooltip(frm.getGui(), text));
	}

	public Tooltip(Frame frm, String text, String openWiki) {
		this(frm, frm.gui.i18nFormat("tooltip.cpm.openWikiTooltip", text, Keybinds.OPEN_WIKI.getSetKey(frm.gui), frm.gui.i18nFormat("label.cpm.wiki.page." + openWiki.toLowerCase(Locale.ROOT))));
		this.openWikiPage = openWiki;
	}

	public void set() {
		frm.setTooltip(this);
	}

	@Override
	public void draw(MouseEvent event, float partialTicks) {
		gui.pushMatrix();
		gui.setPosOffset(getBounds());
		gui.drawBox(0, 0, bounds.w, bounds.h, backgroundColor);
		gui.drawBox(1, 1, bounds.w - 2, bounds.h - 2, gui.getColors().panel_background);
		for (GuiElement guiElement : elements) {
			if(guiElement.isVisible())
				guiElement.draw(event.offset(bounds), partialTicks);
		}
		gui.popMatrix();
	}

	public String getWikiPage() {
		return openWikiPage;
	}
}
