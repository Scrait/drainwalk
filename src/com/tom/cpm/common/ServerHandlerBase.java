package com.tom.cpm.common;

import java.util.Collections;
import java.util.function.Function;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.server.SCustomPayloadPlayPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ChunkManager.EntityTracker;
import net.minecraft.world.server.ServerWorld;

import com.tom.cpm.shared.network.NetHandler;

import io.netty.buffer.Unpooled;

public class ServerHandlerBase {

	public static NetHandler<ResourceLocation, ServerPlayerEntity, ServerPlayNetHandler> init() {
		NetHandler<ResourceLocation, ServerPlayerEntity, ServerPlayNetHandler> netHandler = new NetHandler<>(ResourceLocation::new);
		netHandler.setGetPlayerUUID(ServerPlayerEntity::getUniqueID);
		netHandler.setSendPacketServer(d -> new PacketBuffer(Unpooled.wrappedBuffer(d)), (c, rl, pb) -> c.sendPacket(new SCustomPayloadPlayPacket(rl, pb)), ent -> {
			EntityTracker tr = ((ServerWorld)ent.world).getChunkProvider().chunkManager.getEntities().get(ent.getEntityId());
			if(tr != null) {
				return tr.getTrackingPlayers();
			}
			return Collections.emptyList();
		}, Function.identity());
		netHandler.setFindTracking((p, f) -> {
			for(EntityTracker tr : ((ServerWorld)p.world).getChunkProvider().chunkManager.getEntities().values()) {
				if(tr.getEntity() instanceof PlayerEntity && tr.getTrackingPlayers().contains(p)) {
					f.accept((ServerPlayerEntity) tr.getEntity());
				}
			}
		});
		netHandler.setSendChat((p, m) -> p.sendStatusMessage(m.remap(), false));
		netHandler.setGetNet(spe -> spe.connection);
		netHandler.setGetPlayer(net -> net.player);
		netHandler.setGetPlayerId(ServerPlayerEntity::getEntityId);
		netHandler.setKickPlayer((p, m) -> p.connection.disconnect(m.remap()));
		netHandler.setGetPlayerAnimGetters(new PlayerAnimUpdater());
		netHandler.addScaler(new AttributeScaler());
		return netHandler;
	}
}
