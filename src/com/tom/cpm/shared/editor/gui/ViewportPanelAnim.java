package com.tom.cpm.shared.editor.gui;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.tom.cpl.gui.Frame;
import com.tom.cpl.gui.KeyboardEvent;
import com.tom.cpl.gui.MouseEvent;
import com.tom.cpl.math.MathHelper;
import com.tom.cpl.math.MatrixStack;
import com.tom.cpl.math.Vec3f;
import com.tom.cpl.render.VBuffers;
import com.tom.cpl.util.Hand;
import com.tom.cpl.util.ItemSlot;
import com.tom.cpm.shared.MinecraftClientAccess;
import com.tom.cpm.shared.animation.VanillaPose;
import com.tom.cpm.shared.editor.DisplayItem;
import com.tom.cpm.shared.editor.Editor;
import com.tom.cpm.shared.editor.anim.AnimationDisplayData;
import com.tom.cpm.shared.editor.anim.AnimationDisplayData.Type;
import com.tom.cpm.shared.editor.tree.VecType;
import com.tom.cpm.shared.editor.util.FilterBuffers;
import com.tom.cpm.shared.gui.Keybinds;
import com.tom.cpm.shared.model.builtin.VanillaPlayerModel;
import com.tom.cpm.shared.model.render.PlayerModelSetup.ArmPose;
import com.tom.cpm.shared.model.render.RenderMode;
import com.tom.cpm.shared.util.PlayerModelLayer;

public class ViewportPanelAnim extends ViewportPanel {
	private List<AnimationDisplayData> anims;
	private Vec3f animPos, animRot;
	private FilterBuffers filter = new FilterBuffers(r -> types.get(RenderMode.OUTLINE) == r);

	public ViewportPanelAnim(Frame frm, Editor editor) {
		super(frm, editor);
		editor.setAnimPos.add(v -> animPos = v);
		editor.setAnimRot.add(v -> animRot = v);
	}

	@Override
	public void draw(MouseEvent event, float partialTicks) {
		anims = null;
		if(editor.playFullAnim) {
			anims = editor.testPoses.stream().map(p -> AnimationDisplayData.getFor(p)).
					filter(d -> d.type == Type.LAYERS || d.type == Type.HAND || d.type == Type.PROGRESS).collect(Collectors.toList());
		}
		editor.applyAnim = true;
		super.draw(event, partialTicks);
		if (editor.selectedAnim != null && editor.selectedAnim.pose instanceof VanillaPose) {
			int spr = -1;
			switch ((VanillaPose) editor.selectedAnim.pose) {

			case HEALTH:
				spr = 0;
				break;

			case HUNGER:
				spr = 1;
				break;

			case AIR:
				spr = 2;
				break;

			default:
				break;
			}
			if (spr != -1) {
				int frms = editor.selectedAnim.getFrames().size();
				int val = (int) (frms > 1 ? (editor.selectedAnim.getSelectedFrameIndex() / (float) (frms - 1) * 20) : 20);
				for (int i = 0;i<10;i++) {
					int sy = MathHelper.clamp(val - i * 2, 0, 2);
					gui.drawTexture(bounds.x + i * 9 + 1, bounds.y + bounds.h - 10, 9, 9, spr * 9, sy * 9 + 64, "editor");
				}
			}
		}
		editor.applyAnim = false;
		anims = null;
	}

	@Override
	public DisplayItem getHeldItem(ItemSlot hand) {
		if(editor.selectedAnim != null) {
			if(editor.selectedAnim.pose != null && editor.selectedAnim.pose instanceof VanillaPose) {
				AnimationDisplayData dt = AnimationDisplayData.getFor((VanillaPose) editor.selectedAnim.pose);
				if(dt.slot == hand) {
					return dt.item;
				}
			}
			if(!editor.forceHeldItemInAnim.get())return DisplayItem.NONE;
		} else if(anims != null) {
			return anims.stream().filter(p -> p.slot == hand).map(p -> p.item).filter(p -> p != null).findFirst().orElse(DisplayItem.NONE);
		}
		return super.getHeldItem(hand);
	}

	@Override
	protected int getItemState(ItemSlot slot, int maxStates) {
		float progress = 0;
		AnimationDisplayData data = null;
		if(editor.selectedAnim != null) {
			data = editor.selectedAnim.pose instanceof VanillaPose ? AnimationDisplayData.getFor((VanillaPose) editor.selectedAnim.pose) : null;
			progress = getAnimProgress();
		} else if(anims != null) {
			data = anims.stream().filter(p -> p.slot == slot && p.layerSlot != null).findFirst().orElse(null);
			progress = data != null ? editor.animTestSliders.getOrDefault("__pose", 0f) : 0f;
		}
		DisplayItem i = getHeldItem(slot);
		if(i == DisplayItem.CROSSBOW) {
			if(data == AnimationDisplayData.CROSSBOW_CH_LEFT || data == AnimationDisplayData.CROSSBOW_CH_RIGHT)
				return (int) Math.min(progress * 3 + 1, maxStates - 1);
		} else if(i == DisplayItem.BOW) {
			return (int) Math.min(progress * 3, maxStates - 1);
		}
		return 0;
	}

	@Override
	protected Hand poseModel0(VanillaPlayerModel p, MatrixStack matrixstack, float partialTicks) {
		float progress = 0;
		AnimationDisplayData data = null;
		if(editor.selectedAnim != null) {
			data = editor.selectedAnim.pose instanceof VanillaPose ? AnimationDisplayData.getFor((VanillaPose) editor.selectedAnim.pose) : null;
			progress = getAnimProgress();
		} else if(anims != null) {
			data = anims.stream().filter(e -> e.type == Type.HAND).findFirst().orElse(null);
			progress = data != null ? editor.animTestSliders.getOrDefault("__pose", 0f) : 0f;
		}
		if(data == AnimationDisplayData.CROSSBOW_CH_LEFT || data == AnimationDisplayData.CROSSBOW_CH_RIGHT) {
			p.useAmount = progress;
			if(p.leftArmPose == ArmPose.CROSSBOW_HOLD)p.leftArmPose = ArmPose.CROSSBOW_CHARGE;
			if(p.rightArmPose == ArmPose.CROSSBOW_HOLD)p.rightArmPose = ArmPose.CROSSBOW_CHARGE;
		} else if(data == AnimationDisplayData.BOW_LEFT || data == AnimationDisplayData.BOW_RIGHT)
			p.useAmount = progress;
		else if(data == AnimationDisplayData.PUNCH_LEFT) {
			p.attackTime = progress;
			return Hand.LEFT;
		} else if(data == AnimationDisplayData.PUNCH_RIGHT)
			p.attackTime = progress;
		return Hand.RIGHT;
	}

	private float getAnimProgress() {
		if(editor.playFullAnim) {
			long playTime = MinecraftClientAccess.get().getPlayerRenderManager().getAnimationEngine().getTime();
			long currentStep = (playTime - editor.playStartTime);
			return (currentStep % editor.selectedAnim.duration) / (float) editor.selectedAnim.duration;
		} else
			return editor.selectedAnim.getAnimProgess();
	}

	@Override
	public Set<PlayerModelLayer> getArmorLayers() {
		if(anims != null)
			return anims.stream().map(e -> e.layer).filter(e -> e != null).collect(Collectors.toSet());
		else
			return super.getArmorLayers();
	}

	@Override
	protected int drawParrots() {
		int r = editor.forceHeldItemInAnim.get() ? super.drawParrots() : 0;
		if(anims != null) {
			if(anims.contains(AnimationDisplayData.PARROT_LEFT))r |= 1;
			if(anims.contains(AnimationDisplayData.PARROT_RIGHT))r |= 2;
		}
		return r;
	}

	@Override
	protected VecType[] getVecTypes() {
		return VecType.MOUSE_EDITOR_ANIM_TYPES;
	}

	@Override
	protected Vec3f getVec(VecType type) {
		if(editor.selectedAnim != null) {
			editor.selectedAnim.beginDrag();
		}
		switch (type) {
		case POSITION:
			return animPos != null ? animPos : new Vec3f();
		case ROTATION:
			return animRot != null ? animRot : new Vec3f();
		default:
			return new Vec3f();
		}
	}

	@Override
	protected void setVec(VecType type, Vec3f vec, boolean temp) {
		if(temp) {
			switch (type) {
			case POSITION:
				editor.setAnimPos.accept(vec);
				if(editor.selectedAnim != null) {
					editor.selectedAnim.dragVal(type, vec);
				}
				break;

			case ROTATION:
				editor.setAnimRot.accept(vec);
				if(editor.selectedAnim != null) {
					editor.selectedAnim.dragVal(type, vec);
				}
				break;

			default:
				break;
			}
		} else {
			switch (type) {
			case POSITION:
				editor.setAnimPos(vec);
				editor.setAnimPos.accept(vec);
				break;

			case ROTATION:
				editor.setAnimRot(vec);
				editor.setAnimRot.accept(vec);
				break;

			default:
				break;
			}
		}
	}

	@Override
	protected void endGizmoDrag(boolean apply) {
		super.endGizmoDrag(apply);
		if(editor.selectedAnim != null)
			editor.selectedAnim.endDrag();
	}

	@Override
	public boolean canEdit() {
		return editor.selectedAnim != null && super.canEdit();
	}

	@Override
	public void render(MatrixStack stack, VBuffers buf, float partialTicks) {
		if(editor.showPreviousFrame.get() && editor.selectedAnim != null && editor.selectedElement != null && editor.selectedAnim.getFrames().size() > 1) {
			editor.selectedAnim.prevFrame();
			editor.definition.renderingPanel = this;
			editor.definition.outlineOnly = true;
			renderModel(stack, filter.filter(buf), partialTicks);
			editor.definition.renderingPanel = null;
			editor.definition.outlineOnly = false;
			editor.selectedAnim.nextFrame();
		}
		super.render(stack, buf, partialTicks);
	}

	@Override
	public void keyPressed(KeyboardEvent event) {
		super.keyPressed(event);
		frame.getKeybindHandler().registerKeybind(Keybinds.TOGGLE_HIDDEN_ACTION, editor::switchAnimShow);
	}
}
