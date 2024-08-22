package com.tom.cpm.shared.editor.gui;

import com.tom.cpl.gui.Frame;
import com.tom.cpl.gui.MouseEvent;
import com.tom.cpl.math.Vec2f;
import com.tom.cpl.math.Vec2i;
import com.tom.cpm.shared.editor.ETextures;
import com.tom.cpm.shared.editor.Editor;
import com.tom.cpm.shared.editor.EditorRenderer;
import com.tom.cpm.shared.editor.EditorTool;
import com.tom.cpm.shared.editor.tree.VecType;

public class ViewportPaintPanel extends ViewportPanel {
	private int dragging;
	private boolean hovered;

	public ViewportPaintPanel(Frame frm, Editor editor) {
		super(frm, editor);
	}

	@Override
	public void draw(MouseEvent event, float partialTicks) {
		hovered = event.isHovered(bounds);
		super.draw(event, partialTicks);
	}

	public Vec2i getHoveredTexPos() {
		ETextures tex = editor.getTextureProvider();
		Vec2i size = tex != null ? tex.provider.size : new Vec2i(64, 64);
		EditorRenderer.Bounds hovered = editor.definition.bounds.stream().filter(b -> b.isHovered).findFirst().orElse(null);
		if(hovered == null)return null;
		Vec2f h = hovered.bb.getHoverPointer();
		return new Vec2i(h.x * size.x, h.y * size.y);
	}

	@Override
	public void mouseClick(MouseEvent event) {
		if(event.isHovered(bounds) && event.btn == 0 && editor.drawMode.get() != EditorTool.SELECT && editor.definition.bounds.stream().anyMatch(b -> b.isHovered && b.type != EditorRenderer.BoundType.DRAG_PANE)) {
			dragging = 1;
			Vec2i v = getHoveredTexPos();
			if(v != null) {
				if(gui.isCtrlDown() || editor.drawMode.get() == EditorTool.COLOR_PICKER) {
					ETextures tex = editor.getTextureProvider();
					if(tex != null) {
						editor.penColor = tex.getImage().getRGB(v.x, v.y);
						editor.setPenColor.accept(editor.penColor);
						dragging = 2;
					}
				} else
					editor.drawPixel(v.x, v.y, true);
			}
			event.consume();
		}
		super.mouseClick(event);
	}

	@Override
	public void mouseDrag(MouseEvent event) {
		if(event.btn == 0 && dragging > 0) {
			if(event.isHovered(bounds)) {
				Vec2i v = getHoveredTexPos();
				if(v != null) {
					if(dragging == 1) {
						editor.drawPixel(v.x, v.y, true);
					} else if(dragging == 2) {
						ETextures tex = editor.getTextureProvider();
						if(tex != null) {
							editor.penColor = tex.getImage().getRGB(v.x, v.y);
							editor.setPenColor.accept(editor.penColor);
						}
					}
				}
			}
			event.consume();
		}
		super.mouseDrag(event);
	}

	@Override
	public void mouseRelease(MouseEvent event) {
		if(event.btn == 0 && dragging > 0) {
			dragging = 0;
			event.consume();
		}
		super.mouseRelease(event);
	}

	@Override
	protected VecType[] getVecTypes() {
		return new VecType[0];
	}

	@Override
	protected boolean keysCanControlCamera() {
		return hovered;
	}
}
