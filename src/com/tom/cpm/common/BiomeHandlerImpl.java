package com.tom.cpm.common;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import com.tom.cpl.block.BiomeHandler;
import com.tom.cpm.MinecraftServerObject;
import com.tom.cpm.shared.MinecraftServerAccess;

public class BiomeHandlerImpl extends BiomeHandler<Biome> {
	public static final BiomeHandlerImpl clientImpl = new BiomeHandlerImpl(() -> Minecraft.getInstance().world.func_241828_r().getRegistry(Registry.BIOME_KEY));
	public static final BiomeHandlerImpl serverImpl = new BiomeHandlerImpl(() -> ((MinecraftServerObject) MinecraftServerAccess.get()).getServer().func_244267_aX().getRegistry(Registry.BIOME_KEY));

	public static BiomeHandlerImpl getImpl(World level) {
		return level.isRemote ? clientImpl : serverImpl;
	}

	private final Supplier<Registry<Biome>> registry;

	public BiomeHandlerImpl(Supplier<Registry<Biome>> registry) {
		this.registry = registry;
	}

	@Override
	public List<com.tom.cpl.block.Biome> listNativeEntries(String tag) {
		ResourceLocation rl = ResourceLocation.tryCreate(tag);
		Biome b = registry.get().getOrDefault(rl);
		if (b != null)return Collections.singletonList(wrap(b));
		return Collections.emptyList();
	}

	@Override
	public List<String> listNativeTags() {
		return Collections.emptyList();
	}

	@Override
	public com.tom.cpl.block.Biome emptyObject() {
		return wrap(null);
	}

	@Override
	public boolean isInTag(String tag, Biome state) {
		return getBiomeId(state).equals(tag);
	}

	@Override
	public List<String> listTags(Biome state) {
		return Collections.emptyList();
	}

	@Override
	public List<com.tom.cpl.block.Biome> getAllElements() {
		return registry.get().stream().map(this::wrap).collect(Collectors.toList());
	}

	@Override
	public boolean equals(Biome a, Biome b) {
		return a == b;
	}

	@Override
	public String getBiomeId(Biome state) {
		return registry.get().getKey(state).toString();
	}

	@Override
	public float getTemperature(Biome state) {
		return state.getTemperature();
	}

	@Override
	public float getHumidity(Biome state) {
		return state.getDownfall();
	}

	@Override
	public RainType getRainType(Biome state) {
		return RainType.get(state.getPrecipitation().name());
	}

	@Override
	public boolean isAvailable() {
		try {
			return registry.get() != null;
		} catch (Exception e) {
			return false;
		}
	}
}
