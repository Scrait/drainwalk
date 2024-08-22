package com.tom.cpl.gui.elements;

import java.io.File;
import java.util.List;

import com.tom.cpl.gui.Frame.PopupLayer;
import com.tom.cpl.gui.IGui;

public class PopupPanel extends Panel {
	private Runnable onClosed;
	protected PopupLayer layer;

	protected PopupPanel(IGui gui) {
		super(gui);
	}

	public void onClosed() {
		if(onClosed != null)onClosed.run();
	}

	public void setOnClosed(Runnable onClosed) {
		this.onClosed = onClosed;
	}

	public boolean hasDecoration() {
		return true;
	}

	public void close() {
		layer.close();
	}

	public void setLayer(PopupLayer layer) {
		this.layer = layer;
	}

	public boolean onEscape() {
		close();
		return true;
	}

	public String getTitle() {
		return "";
	}

	public void filesDropped(List<File> files) {
	}

	public void updateTitle() {
		if(layer != null)
			layer.updateTitle();
	}

	public void onInit() {
	}
}
