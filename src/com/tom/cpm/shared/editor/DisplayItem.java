package com.tom.cpm.shared.editor;

import com.tom.cpl.util.ItemSlot;
import com.tom.cpm.shared.model.render.PlayerModelSetup.ArmPose;

public enum DisplayItem {
	NONE(ArmPose.EMPTY, true),
	BLOCK(ArmPose.ITEM, true),
	SWORD(ArmPose.ITEM, false),
	SKULL(ArmPose.ITEM, true),
	BOW(ArmPose.BOW_AND_ARROW, false),
	CROSSBOW(ArmPose.CROSSBOW_HOLD, false),
	TRIDENT(ArmPose.THROW_SPEAR, false),
	SHIELD(ArmPose.BLOCK, false),
	FOOD(ArmPose.ITEM, false),
	SPYGLASS(ArmPose.SPYGLASS, false, ItemSlot.HEAD),
	GOAT_HORN(ArmPose.TOOT_HORN, false),
	BRUSH(ArmPose.BRUSH, false),
	;
	public static final DisplayItem[] VALUES = values();

	public final ArmPose pose;
	public final boolean canBeOnHead;
	public final ItemSlot positionOverride;

	private DisplayItem(ArmPose pose, boolean canBeOnHead) {
		this.pose = pose;
		this.canBeOnHead = canBeOnHead;
		this.positionOverride = null;
	}

	private DisplayItem(ArmPose pose, boolean canBeOnHead, ItemSlot positionOverride) {
		this.pose = pose;
		this.canBeOnHead = canBeOnHead;
		this.positionOverride = positionOverride;
	}
}
