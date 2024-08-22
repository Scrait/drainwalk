package com.tom.cpm.common;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;

public class PlatformCommon {

	public static List<Attribute> getReachAttr() {
		return Collections.singletonList(Attributes.REACH_DISTANCE);
	}
}
