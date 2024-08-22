package com.tom.cpm;

import java.io.File;
import java.util.Collections;
import java.util.EnumSet;
import java.util.function.Supplier;

import com.darkmagician6.eventapi.EventManager;
import com.tom.cpm.client.PlayerProfile;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.Style;

/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.versions.forge.ForgeVersion;*/

import com.tom.cpl.config.ModConfigFile;
import com.tom.cpl.text.TextStyle;
import com.tom.cpm.api.ICPMPlugin;
import com.tom.cpm.client.CustomPlayerModelsClient;
import com.tom.cpm.common.ServerHandler;
import com.tom.cpm.shared.MinecraftObjectHolder;
import com.tom.cpm.shared.PlatformFeature;
import com.tom.cpm.shared.config.ModConfig;
import com.tom.cpm.shared.util.IVersionCheck;
import com.tom.cpm.shared.util.VersionCheck;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;

//@Mod("cpm")
public class CustomPlayerModels extends CommonBase implements IInstanceAccess {

	public CustomPlayerModels() {
	/*	FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);*/
		cfg = new ModConfigFile(new File("cpm.json"));
		MinecraftObjectHolder.setCommonObject(this);
		CustomPlayerModelsClient.INSTANCE.init();
		apiInit();
		CustomPlayerModelsClient.apiInit();


//		MinecraftForge.EVENT_BUS.register(this);
	}

	/*private void doClientStuff(final FMLClientSetupEvent event) {
		CustomPlayerModelsClient.INSTANCE.init();
	}

	public void setup(FMLCommonSetupEvent evt) {
		cfg = new ModConfigFile(new File(FMLPaths.CONFIGDIR.get().toFile(), "cpm.json"));
		MinecraftForge.EVENT_BUS.register(new ServerHandler());
		LOG.info("Customizable Player Models Initialized");
	}

	@SuppressWarnings("unchecked")
	private void processIMC(final InterModProcessEvent event) {
		event.getIMCStream().forEach(m -> {
			try {
				if(m.getMethod().equals("api")) {
					ICPMPlugin plugin = ((Supplier<ICPMPlugin>) m.getMessageSupplier().get()).get();
					api.register(plugin);
				}
			} catch (Throwable e) {
				LOG.error("Mod {} provides a broken implementation of CPM api", m.getSenderModId(), e);
			}
		});
		apiInit();
		if (FMLEnvironment.dist == Dist.CLIENT) CustomPlayerModelsClient.apiInit();
	}

	@SubscribeEvent
	public void onStart(FMLServerStartingEvent e) {
		MinecraftObjectHolder.setServerObject(new MinecraftServerObject(e.getServer()));
	}

	@SubscribeEvent
	public void onStop(FMLServerStoppingEvent e) {
		ModConfig.getWorldConfig().save();
		MinecraftObjectHolder.setServerObject(null);
	}*/

	private static final EnumSet<PlatformFeature> features = EnumSet.of(
			PlatformFeature.EDITOR_HELD_ITEM,
			PlatformFeature.EDITOR_SUPPORTED
			);

	@Override
	public EnumSet<PlatformFeature> getSupportedFeatures() {
		return features;
	}

	@Override
	public String getMCBrand() {
		return "(drainwalk/" + dw.getClientInfo().getVERSION() + ")";
	}

	@Override
	public String getModVersion() {
		return "by Scrait";
	}

	@Override
	public IVersionCheck getVersionCheck() {
		return null;
	}

	@Override
	protected IFormattableTextComponent styleText(IFormattableTextComponent in, TextStyle style) {
		return in.setStyle(Style.EMPTY.setBold(style.bold).setItalic(style.italic).func_244282_c(style.underline).setStrikethrough(style.strikethrough));
	}
}
