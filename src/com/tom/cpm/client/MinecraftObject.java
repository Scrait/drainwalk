package com.tom.cpm.client;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.util.ResourceLocation;

import com.mojang.authlib.GameProfile;

import com.tom.cpl.block.BiomeHandler;
import com.tom.cpl.gui.Frame;
import com.tom.cpl.gui.IGui;
import com.tom.cpl.gui.IKeybind;
import com.tom.cpl.render.RenderTypeBuilder;
import com.tom.cpl.tag.AllTagManagers;
import com.tom.cpl.util.DynamicTexture.ITexture;
import com.tom.cpl.util.Image;
import com.tom.cpl.util.ImageIO.IImageIO;
import com.tom.cpm.common.BiomeHandlerImpl;
import com.tom.cpm.shared.MinecraftClientAccess;
import com.tom.cpm.shared.MinecraftObjectHolder;
import com.tom.cpm.shared.definition.ModelDefinitionLoader;
import com.tom.cpm.shared.model.SkinType;
import com.tom.cpm.shared.model.render.RenderMode;
import com.tom.cpm.shared.network.NetH;
import com.tom.cpm.shared.network.NetHandler;
import com.tom.cpm.shared.util.MojangAPI;

public class MinecraftObject implements MinecraftClientAccess {
	private final Minecraft mc;
	private final ModelDefinitionLoader<GameProfile> loader;
	private final PlayerRenderManager prm;
	private final AllTagManagers tags;
	public RenderTypeBuilder<ResourceLocation, RenderType> renderBuilder;

	public MinecraftObject(Minecraft mc) {
		this.mc = mc;
		MinecraftObjectHolder.setClientObject(this);
		loader = new ModelDefinitionLoader<>(PlayerProfile::new, GameProfile::getId, GameProfile::getName);
		prm = new PlayerRenderManager();
		renderBuilder = new RenderTypeBuilder<>();
		renderBuilder.register(RenderMode.DEFAULT, RenderType::getEntityTranslucent, 0);
		renderBuilder.register(RenderMode.GLOW, CustomRenderTypes::glowingEyes, 1);
		renderBuilder.register(RenderMode.COLOR, CustomRenderTypes::entityColorTranslucent, 0);
		renderBuilder.register(RenderMode.COLOR_GLOW, CustomRenderTypes::glowingEyesColor, 1);
		renderBuilder.register(RenderMode.OUTLINE, CustomRenderTypes::linesNoDepth, 2);
		tags = new AllTagManagers(mc, CPMTagLoader::new);
	}

	@Override
	public PlayerRenderManager getPlayerRenderManager() {
		return prm;
	}

	@Override
	public ITexture createTexture() {
		return new DynTexture(mc);
	}

	public static class DynTexture implements ITexture {
		private final DynamicTexture dynTex;
		private final ResourceLocation loc;
		private final Minecraft mc;
		private static ResourceLocation bound_loc;

		public DynTexture(Minecraft mc) {
			dynTex = new DynamicTexture(1, 1, true);
			this.mc = mc;
			loc = mc.getTextureManager().getDynamicTextureLocation("cpm", dynTex);
		}

		@Override
		public void bind() {
			bound_loc = loc;
			if(mc.getTextureManager().getTexture(loc) == null)
				mc.getTextureManager().loadTexture(loc, dynTex);
		}

		@Override
		public void load(Image texture) {
			NativeImage ni = NativeImageIO.createFromBufferedImage(texture);
			dynTex.setTextureData(ni);
			TextureUtil.prepareImage(dynTex.getGlTextureId(), ni.getWidth(), ni.getHeight());
			dynTex.updateDynamicTexture();
		}

		public static ResourceLocation getBoundLoc() {
			return bound_loc;
		}

		@Override
		public void free() {
			mc.getTextureManager().deleteTexture(loc);
		}

		public DynamicTexture getDynTex() {
			return dynTex;
		}
	}

	@Override
	public void executeOnGameThread(Runnable r) {
		mc.execute(r);
	}

	@Override
	public void executeNextFrame(Runnable r) {
		mc.enqueue(r);
	}

	@Override
	public ModelDefinitionLoader<GameProfile> getDefinitionLoader() {
		return loader;
	}

	@Override
	public SkinType getSkinType() {
		return SkinType.get(DefaultPlayerSkin.getSkinType(mc.getSession().getProfile().getId()));
	}

	@Override
	public void setEncodedGesture(int value) {
		Set<PlayerModelPart> s = mc.gameSettings.getModelParts();
		setEncPart(s, value, 0, PlayerModelPart.HAT);
		setEncPart(s, value, 1, PlayerModelPart.JACKET);
		setEncPart(s, value, 2, PlayerModelPart.LEFT_PANTS_LEG);
		setEncPart(s, value, 3, PlayerModelPart.RIGHT_PANTS_LEG);
		setEncPart(s, value, 4, PlayerModelPart.LEFT_SLEEVE);
		setEncPart(s, value, 5, PlayerModelPart.RIGHT_SLEEVE);
		mc.gameSettings.sendSettingsToServer();
	}

	private static void setEncPart(Set<PlayerModelPart> s, int value, int off, PlayerModelPart part) {
		if((value & (1 << off)) != 0)s.add(part);
		else s.remove(part);
	}

	@Override
	public boolean isInGame() {
		return mc.player != null;
	}

	@Override
	public Object getPlayerIDObject() {
		return mc.getSession().getProfile();
	}

	@Override
	public Object getCurrentPlayerIDObject() {
		return mc.player != null ? mc.player.getGameProfile() : null;
	}

	@Override
	public List<IKeybind> getKeybinds() {
		return KeyBindings.kbs;
	}

	@Override
	public ServerStatus getServerSideStatus() {
		return mc.player != null ? ((NetH)mc.getConnection()).cpm$hasMod() ? ServerStatus.INSTALLED : ServerStatus.SKIN_LAYERS_ONLY : ServerStatus.OFFLINE;
	}

	@Override
	public File getGameDir() {
		return mc.gameDir;
	}

	@Override
	public void openGui(Function<IGui, Frame> creator) {
		mc.displayGuiScreen(new GuiImpl(creator, mc.currentScreen));
	}

	@Override
	public Runnable openSingleplayer() {
		return () -> mc.displayGuiScreen(new WorldSelectionScreen(mc.currentScreen));
	}

	@Override
	public NetHandler<?, ?, ?> getNetHandler() {
		return CustomPlayerModelsClient.INSTANCE.netHandler;
	}

	@Override
	public IImageIO getImageIO() {
		return new NativeImageIO();
	}

	@Override
	public MojangAPI getMojangAPI() {
		return new MojangAPI(mc.getSession().getProfile().getName(), mc.getSession().getProfile().getId(), mc.getSession().getToken());
	}

	@Override
	public void clearSkinCache() {
		MojangAPI.clearYggdrasilCache(mc.getSessionService());
		mc.getProfileProperties().clear();
		mc.getProfileProperties();//refresh
	}

	@Override
	public String getConnectedServer() {
		if(mc.getConnection() == null)return null;
		SocketAddress sa = Platform.getChannel(mc.getConnection().getNetworkManager()).remoteAddress();
		if(sa instanceof InetSocketAddress)
			return ((InetSocketAddress)sa).getHostString();
		return null;
	}

	@Override
	public List<Object> getPlayers() {
		if(mc.getConnection() == null)return Collections.emptyList();
		return mc.getConnection().getPlayerInfoMap().stream().map(NetworkPlayerInfo::getGameProfile).collect(Collectors.toList());
	}

	@Override
	public Proxy getProxy() {
		return mc.getProxy();
	}

	@Override
	public RenderTypeBuilder<?, ?> getRenderBuilder() {
		return renderBuilder;
	}

	@Override
	public AllTagManagers getBuiltinTags() {
		return tags;
	}

	@Override
	public BiomeHandler<?> getBiomeHandler() {
		return BiomeHandlerImpl.clientImpl;
	}
}
