package com.tom.cpm.common;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.util.ResourceLocation;

import com.mojang.brigadier.CommandDispatcher;

import com.tom.cpm.shared.network.NetH;
import com.tom.cpm.shared.network.NetHandler;
import tech.drainwalk.api.impl.events.movement.JumpEvent;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;

public class ServerHandler extends ServerHandlerBase implements IInstanceAccess {
	public static NetHandler<ResourceLocation, ServerPlayerEntity, ServerPlayNetHandler> netHandler;

	static {
		netHandler = init();
		netHandler.setExecutor(() -> mc.world.getServer());
		netHandler.setGetOnlinePlayers(() ->  mc.world.getServer().getPlayerList().getPlayers());
		if(false) {
			netHandler.addScaler(new PehkuiInterface());
		}
	}

//	@SubscribeEvent
//	public void onPlayerJoin(PlayerLoggedInEvent evt) {
//		netHandler.onJoin((ServerPlayerEntity) evt.getPlayer());
//	}
//
//	@SubscribeEvent
//	public void onTrackingStart(PlayerEvent.StartTracking evt) {
//		ServerPlayNetHandler handler = ((ServerPlayerEntity)evt.getPlayer()).connection;
//		NetH h = (NetH) handler;
//		if(h.cpm$hasMod()) {
//			if(evt.getTarget() instanceof PlayerEntity) {
//				netHandler.sendPlayerData((ServerPlayerEntity) evt.getTarget(), (ServerPlayerEntity) evt.getPlayer());
//			}
//		}
//	}
//
//	@SubscribeEvent
//	public void registerCommands(RegisterCommandsEvent evt) {
//		CommandDispatcher<CommandSource> d = evt.getDispatcher();
//		new Command(d, false);
//	}
//
//	@SubscribeEvent
//	public void onRespawn(PlayerRespawnEvent evt) {
//		if(!evt.isEndConquered()) {
//			netHandler.onRespawn((ServerPlayerEntity) evt.getPlayer());
//		}
//	}
//
//	@SubscribeEvent
//	public void onTick(ServerTickEvent evt) {
//		if(evt.phase == Phase.END) {
//			netHandler.tick();
//		}
//	}
//
//	@SubscribeEvent
//	public void onJump(JumpEvent evt) {
//		if(evt.getEntityLiving() instanceof ServerPlayerEntity) {
//			netHandler.onJump((ServerPlayerEntity) evt.getEntityLiving());
//		}
//	}
}
