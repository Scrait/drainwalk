package com.tom.cpm.client;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.CustomizeSkinScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.util.text.TranslationTextComponent;

//import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
//import net.minecraftforge.client.event.GuiOpenEvent;
//import net.minecraftforge.client.event.GuiScreenEvent;
//import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
//import net.minecraftforge.client.event.RenderPlayerEvent;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.event.TickEvent.ClientTickEvent;
//import net.minecraftforge.event.TickEvent.Phase;
//import net.minecraftforge.event.TickEvent.RenderTickEvent;
//import net.minecraftforge.eventbus.api.EventPriority;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.ExtensionPoint;
//import net.minecraftforge.fml.ModLoadingContext;

import com.tom.cpm.shared.config.ConfigKeys;
import com.tom.cpm.shared.config.ModConfig;
import com.tom.cpm.shared.config.Player;
import com.tom.cpm.shared.editor.gui.EditorGui;
import com.tom.cpm.shared.gui.GestureGui;
import com.tom.cpm.shared.gui.SettingsGui;
import tech.drainwalk.api.impl.events.TickEvent;
import tech.drainwalk.api.impl.events.player.RenderPlayerEvent;

public class CustomPlayerModelsClient extends ClientBase {
	public static final CustomPlayerModelsClient INSTANCE = new CustomPlayerModelsClient();

	public void init() {
		init0();
		EventManager.register(this);
//		MinecraftForge.EVENT_BUS.register(this);
		KeyBindings.init();
		init1();
//		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, scr) -> new GuiImpl(SettingsGui::new, scr));
	}

	@EventTarget
	public void playerRenderPre(RenderPlayerEvent.Pre event) {
		playerRenderPre(event.getEntityPlayer(), event.getBuffers(), event.getRenderer().getEntityModel());
	}

	@EventTarget
	public void playerRenderPost(RenderPlayerEvent.Post event) {
		playerRenderPost(event.getBuffers(), event.getRenderer().getEntityModel());
	}
//
//	@SubscribeEvent
//	public void initGui(GuiScreenEvent.InitGuiEvent.Post evt) {
//		if((evt.getGui() instanceof MainMenuScreen && ModConfig.getCommonConfig().getSetBoolean(ConfigKeys.TITLE_SCREEN_BUTTON, true)) ||
//				evt.getGui() instanceof CustomizeSkinScreen) {
//			evt.addWidget(new Button(0, 0, () -> Minecraft.getInstance().setScreen(new GuiImpl(EditorGui::new, evt.getGui()))));
//		}
//	}
//
	@EventTarget
	public void renderTick(TickEvent.Render evt) {
		if(evt.getPhase() == TickEvent.Phase.START) {
			mc.getPlayerRenderManager().getAnimationEngine().update(evt.getRenderTickTime());
		}
	}

	@EventTarget
	public void clientTick(TickEvent evt) {
		if(evt.getPhase() == TickEvent.Phase.START && !minecraft.isGamePaused()) {
			mc.getPlayerRenderManager().getAnimationEngine().tick();
		}
		if (minecraft.player == null || evt.getPhase() == TickEvent.Phase.START)
			return;

		if(KeyBindings.gestureMenuBinding.isPressed()) {
			Minecraft.getInstance().displayGuiScreen(new GuiImpl(GestureGui::new, null));
		}

		if(KeyBindings.renderToggleBinding.isPressed()) {
			Player.setEnableRendering(!Player.isEnableRendering());
		}

		mc.getPlayerRenderManager().getAnimationEngine().updateKeys(KeyBindings.quickAccess);
	}
//
//	@SubscribeEvent
//	public void openGui(GuiOpenEvent openGui) {
//		if(openGui.getGui() == null && minecraft.screen instanceof GuiImpl.Overlay) {
//			openGui.setGui(((GuiImpl.Overlay) minecraft.screen).getGui());
//		}
//		if(openGui.getGui() instanceof MainMenuScreen && EditorGui.doOpenEditor()) {
//			openGui.setGui(new GuiImpl(EditorGui::new, openGui.getGui()));
//		}
//		if(openGui.getGui() instanceof GuiImpl)((GuiImpl)openGui.getGui()).onOpened();
//	}
//
//	@SubscribeEvent
//	public void drawGuiPre(DrawScreenEvent.Pre evt) {
//		PlayerProfile.inGui = true;
//	}
//
//	@SubscribeEvent
//	public void drawGuiPost(DrawScreenEvent.Post evt) {
//		PlayerProfile.inGui = false;
//	}

	public static class Button extends net.minecraft.client.gui.widget.button.Button {

		public Button(int x, int y, Runnable r) {
			super(x, y, 100, 20, new TranslationTextComponent("button.cpm.open_editor"), b -> r.run());
		}

	}

//	@SubscribeEvent
//	public void onLogout(ClientPlayerNetworkEvent.LoggedOutEvent evt) {
//		mc.onLogOut();
//	}
}
