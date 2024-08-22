package com.tom.cpm.mixin;

import lombok.experimental.UtilityClass;

import com.tom.cpm.client.CustomPlayerModelsClient;

@UtilityClass
public class ClientPlayerEntityController {

	public void onAiStep() {
		CustomPlayerModelsClient.INSTANCE.updateJump();
	}
}
