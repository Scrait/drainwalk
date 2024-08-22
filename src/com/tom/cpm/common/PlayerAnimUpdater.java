package com.tom.cpm.common;

import java.util.function.BiConsumer;

import net.minecraft.entity.player.PlayerEntity;

import com.tom.cpm.shared.animation.ServerAnimationState;

public class PlayerAnimUpdater implements BiConsumer<PlayerEntity, ServerAnimationState> {

	@Override
	public void accept(PlayerEntity t, ServerAnimationState u) {
		u.updated = true;
		u.creativeFlying = t.abilities.isFlying;
		u.falling = t.fallDistance;
		u.health = t.getHealth() / t.getMaxHealth();
		u.air = Math.max(t.getAir() / (float) t.getMaxAir(), 0);
		u.hunger = t.getFoodStats().getFoodLevel() / 20f;
		u.inMenu = t.openContainer != t.container;
	}

}
