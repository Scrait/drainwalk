package com.tom.cpm.shared.editor.tree;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import com.tom.cpl.gui.IGui;
import com.tom.cpl.gui.MouseEvent;
import com.tom.cpl.gui.elements.PopupMenu;
import com.tom.cpl.gui.elements.Tooltip;
import com.tom.cpl.gui.elements.Tree.TreeModel;
import com.tom.cpl.math.Box;
import com.tom.cpl.math.Vec2i;
import com.tom.cpl.math.Vec3f;
import com.tom.cpm.shared.editor.ETextures;
import com.tom.cpm.shared.editor.Editor;
import com.tom.cpm.shared.editor.Effect;

public abstract interface TreeElement {
	public class ModelTree extends TreeModel<TreeElement> {
		private Editor e;
		private TreeElement moveElem;

		public ModelTree(Editor e) {
			this.e = e;
		}

		@Override
		protected int textColor(TreeElement val, IGui gui) {
			return val.textColor(gui);
		}

		@Override
		protected void getElements(TreeElement parent, Consumer<TreeElement> c) {
			if(parent == null) {
				e.elements.forEach(c);
				e.templates.forEach(c);
				if(e.templateSettings != null)c.accept(e.templateSettings);
				if(e.scalingElem.enabled)c.accept(e.scalingElem);
				c.accept(e.texElem);
			} else
				parent.getTreeElements(c);
		}

		@Override
		protected int bgColor(TreeElement val, IGui gui) {
			int bg = val.bgColor(gui);
			if(bg != 0)return bg;
			if(moveElem != null && moveElem == val)return gui.getColors().move_background;
			if(isSelected(val))return gui.getColors().select_background;
			return 0;
		}

		@Override
		protected void treeUpdated() {
			e.updateGui();
		}

		@Override
		protected void onClick(IGui gui, MouseEvent evt, TreeElement elem) {
			if(evt.btn == 1 && elem != null) {
				displayPopup(gui, evt, elem);
			} else {
				if(elem != null)
					elem.onClick(gui, e, evt);
				else
					e.selectedElement = null;
			}
		}

		public void displayPopup(IGui gui, MouseEvent evt, TreeElement elem) {
			PopupMenu popup = new PopupMenu(gui, gui.getFrame());
			if(elem.canMove() || (moveElem != null && elem.canAccept(moveElem))) {
				String btnTxt;
				if(moveElem != null) {
					if(moveElem == elem)btnTxt = gui.i18nFormat("button.cpm.tree.cancelMove");
					else btnTxt = gui.i18nFormat("button.cpm.tree.put");
				} else btnTxt = gui.i18nFormat("button.cpm.tree.move");
				popup.addButton(btnTxt, () -> {
					if(moveElem != null) {
						if(moveElem != elem)
							elem.accept(moveElem);
						moveElem = null;
					} else moveElem = elem;
				});
			}
			elem.populatePopup(popup);
			if(popup.getY() > 0) {
				Vec2i p = evt.getPos();
				popup.display(p.x + 1, p.y + 1);
			}
		}

		@Override
		protected String getName(TreeElement elem) {
			return elem.getName();
		}

		@Override
		protected Tooltip getTooltip(TreeElement elem, IGui gui) {
			if(elem != null)return elem.getTooltip(gui);
			return null;
		}

		@Override
		protected void refresh(TreeElement elem) {
			elem.onRefreshTree();
		}

		@Override
		protected boolean isSelected(TreeElement elem) {
			return e.selectedElement != null && e.selectedElement.isSelected(e, elem);
		}

		@Override
		protected boolean canSelect(TreeElement elem) {
			return elem.canSelect();
		}

		@Override
		protected void drawName(TreeElement elem, IGui gui, int x, int y, int color) {
			if(elem != null)elem.drawName(gui, x, y, color);
		}

		@Override
		protected int getExtraWidth(TreeElement value, IGui gui) {
			return value != null ? value.getExtraWidth(gui) : 0;
		}
	}

	public default void onClick(IGui gui, Editor e, MouseEvent evt) {
		e.selectedElement = this;
	}

	public default boolean isSelected(Editor e, TreeElement other) {
		return e.selectedElement == other;
	}

	public String getName();
	public default int textColor(IGui gui) { return 0; }
	public default int bgColor(IGui gui) { return 0; }
	public default void accept(TreeElement elem) { throw new UnsupportedOperationException(); }
	public default boolean canAccept(TreeElement elem) { return false; }
	public default boolean canMove() { return false; }
	public default void getTreeElements(Consumer<TreeElement> c) {}
	public default void populatePopup(PopupMenu popup) {}
	public default Tooltip getTooltip(IGui gui) { return null; }
	public default void setVec(Vec3f v, VecType object) {}
	public default void setElemName(String name) {}
	public default String getElemName() { return ""; }
	public default void drawTexture(IGui gui, int x, int y, float xs, float ys) {}
	public default ETextures getTexture() { return null; }
	public default Box getTextureBox() { return null; }
	public default List<TreeSettingElement> getSettingsElements() { return Collections.emptyList(); }
	public default void modeSwitch() {}
	public default void updateGui() {}
	public default void onRefreshTree() {}
	public default void addNew() {}
	public default void delete() {}
	public default void setElemColor(int color) {}
	public default void setMCScale(float scale) {}
	public default void switchVis() {}
	public default void switchEffect(Effect effect) {}
	public default float getValue() { return 0; }
	public default void setValue(float value) {}
	public default void setVecTemp(VecType type, Vec3f vec) {}
	public default Vec3f getVec(VecType type) {return Vec3f.ZERO;}
	public default boolean canEditVec(VecType type) {return false;}
	public default boolean canSelect() {return true;}
	public default int getExtraWidth(IGui gui) { return 0; }
	public default void drawName(IGui gui, int x, int y, int color) {}

	public static interface TreeSettingElement extends TreeElement {
		TreeElement getParent();

		@Override
		default String getName() {
			return "";
		}

		@Override
		default ETextures getTexture() {
			return getParent().getTexture();
		}

		@Override
		default boolean isSelected(Editor e, TreeElement other) {
			return other == getParent();
		}

		@Override
		default void drawTexture(IGui gui, int x, int y, float xs, float ys) {
			getParent().drawTexture(gui, x, y, xs, ys);
		}
	}
}
