package com.tom.cpm.common;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.player.ServerPlayerEntity;

import com.tom.cpm.shared.network.NetHandler.ScalerInterface;
import com.tom.cpm.shared.util.ScalingOptions;

import java.util.List;

public class PehkuiInterface implements ScalerInterface<ServerPlayerEntity, List<Attribute>> {

	@Override
	public void setScale(List<Attribute> key, ServerPlayerEntity player, float value) {

	}

	@Override
	public List<Attribute> toKey(ScalingOptions opt) {
		return List.of();
	}

	@Override
	public String getMethodName() {
		return "";
	}

}
