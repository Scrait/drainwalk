package com.tom.cpm.client;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CCustomPayloadPacket;
import net.minecraft.network.play.server.SCustomPayloadPlayPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

import io.netty.channel.Channel;

public class Platform {
	public static final ResourceLocation WHITE = new ResourceLocation("cpm", "textures/white.png");

	public static void initPlayerProfile() {
		if (false)
			FirstPersonDetector.init();
	}

	public static Matrix4f createMatrix(float[] array) {
		return new Matrix4f(array);
	}

	public static boolean isSitting(PlayerEntity player) {
		return player.isPassenger() && (player.getRidingEntity() != null /*&& player.getRidingEntity().shouldRiderSit()*/);
	}

	public static void setHeight(Widget w, int h) {
		w.setHeight(h);
	}

	public static Channel getChannel(NetworkManager conn) {
		return conn.getChannel();
	}

	public static ResourceLocation getName(SCustomPayloadPlayPacket p) {
		return p.getChannelName();
	}

	public static ResourceLocation getName(CCustomPayloadPacket p) {
		return p.getChannel();
	}

	public static PacketBuffer getData(SCustomPayloadPlayPacket p) {
		return p.getBufferData();
	}

	public static PacketBuffer getData(CCustomPayloadPacket p) {
		return p.getData();
	}
}
