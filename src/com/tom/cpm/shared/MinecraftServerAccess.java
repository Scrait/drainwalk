package com.tom.cpm.shared;

import com.tom.cpl.block.BiomeHandler;
import com.tom.cpl.config.ModConfigFile;
import com.tom.cpm.shared.network.NetHandler;

public interface MinecraftServerAccess {

	public static MinecraftServerAccess get() {
		System.out.println("MinecraftServerAccess");
		return MinecraftObjectHolder.serverAccess;
	}

	ModConfigFile getConfig();
	NetHandler<?, ?, ?> getNetHandler();
	BiomeHandler<?> getBiomeHandler();
}
