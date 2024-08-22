package com.tom.cpm.client;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;

import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager.ISkinAvailableCallback;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.systems.RenderSystem;

import com.tom.cpl.block.entity.ActiveEffect;
import com.tom.cpl.math.MathHelper;
import com.tom.cpl.util.Hand;
import com.tom.cpl.util.HandAnimation;
import com.tom.cpm.common.EntityTypeHandlerImpl;
import com.tom.cpm.common.PlayerInventory;
import com.tom.cpm.common.WorldImpl;
import com.tom.cpm.shared.config.Player;
import com.tom.cpm.shared.model.SkinType;
import com.tom.cpm.shared.model.render.PlayerModelSetup.ArmPose;
import com.tom.cpm.shared.skin.PlayerTextureLoader;

public class PlayerProfile extends Player<PlayerEntity> {
	public static boolean inGui;
	public static BooleanSupplier inFirstPerson;
	static {
		inFirstPerson = () -> false;
		Platform.initPlayerProfile();
	}

	private final GameProfile profile;
	private String skinType;

	public PlayerProfile(GameProfile profile) {
		this.profile = new GameProfile(profile.getId(), profile.getName());
		cloneProperties(profile.getProperties(), this.profile.getProperties());

		if(profile.getId() != null)
			this.skinType = DefaultPlayerSkin.getSkinType(profile.getId());
	}

	@Override
	public SkinType getSkinType() {
		return SkinType.get(skinType);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((profile == null) ? 0 : profile.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		PlayerProfile other = (PlayerProfile) obj;
		if (profile == null) {
			if (other.profile != null) return false;
		} else if (!profile.equals(other.profile)) return false;
		return true;
	}

	@Override
	public UUID getUUID() {
		return profile.getId();
	}

	@Override
	public void updateFromPlayer(PlayerEntity player) {
		Pose p = player.getPose();
		animState.resetPlayer();
		switch (p) {
		case FALL_FLYING:
			animState.elytraFlying = true;
			break;
		case SLEEPING:
			animState.sleeping = true;
			break;
		case SPIN_ATTACK:
			animState.tridentSpin = true;
			break;
		default:
			break;
		}
		animState.sneaking = player.isCrouching();
		animState.crawling = player.isVisuallyCrawling();
		animState.swimming = player.isVisuallySwimming();
		if(!player.isAlive())animState.dying = true;
		if(Platform.isSitting(player))animState.riding = true;
		if(player.isSprinting())animState.sprinting = true;
		if(player.isHandActive()) {
			animState.usingAnimation = HandAnimation.of(player.getActiveItemStack().getUseAction());
		}
		if(player.isInWater())animState.retroSwimming = true;
		animState.moveAmountX = (float) (player.getPosX() - player.prevPosX);
		animState.moveAmountY = (float) (player.getPosY() - player.prevPosY);
		animState.moveAmountZ = (float) (player.getPosZ() - player.prevPosZ);
		animState.yaw = player.rotationYaw;
		animState.pitch = player.rotationPitch;
		animState.bodyYaw = player.renderYawOffset;

		if(player.isWearing(PlayerModelPart.HAT))animState.encodedState |= 1;
		if(player.isWearing(PlayerModelPart.JACKET))animState.encodedState |= 2;
		if(player.isWearing(PlayerModelPart.LEFT_PANTS_LEG))animState.encodedState |= 4;
		if(player.isWearing(PlayerModelPart.RIGHT_PANTS_LEG))animState.encodedState |= 8;
		if(player.isWearing(PlayerModelPart.LEFT_SLEEVE))animState.encodedState |= 16;
		if(player.isWearing(PlayerModelPart.RIGHT_SLEEVE))animState.encodedState |= 32;

		ItemStack is = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
		animState.hasSkullOnHead = is.getItem() instanceof BlockItem && ((BlockItem)is.getItem()).getBlock() instanceof AbstractSkullBlock;
		animState.wearingHelm = !is.isEmpty();
		is = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
		animState.wearingElytra = is.getItem() instanceof ElytraItem;
		animState.wearingBody = !is.isEmpty();
		animState.wearingLegs = !player.getItemStackFromSlot(EquipmentSlotType.LEGS).isEmpty();
		animState.wearingBoots = !player.getItemStackFromSlot(EquipmentSlotType.FEET).isEmpty();
		animState.mainHand = Hand.of(player.getPrimaryHand());
		animState.activeHand = Hand.of(animState.mainHand, player.getActiveHand());
		animState.swingingHand = Hand.of(animState.mainHand, player.swingingHand);
		animState.hurtTime = player.hurtTime;
		animState.isOnLadder = player.isOnLadder();
		animState.isBurning = player.isBurning();
		animState.inGui = inGui;
		animState.firstPersonMod = inFirstPerson.getAsBoolean();
		PlayerInventory.setInv(animState, player.inventory);
		WorldImpl.setWorld(animState, player);
		if (player.getRidingEntity() != null)animState.vehicle = EntityTypeHandlerImpl.impl.wrap(player.getRidingEntity().getType());
		player.getActivePotionEffects().forEach(e -> animState.allEffects.add(new ActiveEffect(Registry.EFFECTS.getKey(e.getPotion()).toString(), e.getAmplifier(), e.getDuration(), !e.doesShowParticles())));

		if(player.getActiveItemStack().getItem() instanceof CrossbowItem) {
			float f = CrossbowItem.getChargeTime(player.getActiveItemStack());
			float f1 = MathHelper.clamp(player.getItemInUseMaxCount(), 0.0F, f);
			animState.crossbowPullback = f1 / f;
		}

		if(player.getActiveItemStack().getItem() instanceof BowItem) {
			float f = 20F;
			float f1 = MathHelper.clamp(player.getItemInUseMaxCount(), 0.0F, f);
			animState.bowPullback = f1 / f;
		}

		animState.parrotLeft = !player.getLeftShoulderEntity().getString("id").isEmpty();
		animState.parrotRight = !player.getRightShoulderEntity().getString("id").isEmpty();
	}

	@Override
	public void updateFromModel(Object model) {
		if(model instanceof PlayerModel) {
			PlayerModel m = (PlayerModel) model;
			animState.resetModel();
			animState.attackTime = m.swingProgress;
			animState.swimAmount = m.swimAnimation;
			animState.leftArm = ArmPose.of(m.leftArmPose);
			animState.rightArm = ArmPose.of(m.rightArmPose);
//			if(CustomPlayerModelsClient.vrLoaded)
//				animState.vrState = VRPlayerRenderer.getVRState(model);
		}
	}

	@Override
	protected PlayerTextureLoader initTextures() {
		return new PlayerTextureLoader(Minecraft.getInstance().getSkinManager().getSkinCacheDir()) {

			@Override
			protected CompletableFuture<Void> load0() {
				Map<Type, MinecraftProfileTexture> map = Minecraft.getInstance().getSkinManager().loadSkinFromCache(profile);
				defineAll(map, MinecraftProfileTexture::getUrl, MinecraftProfileTexture::getHash);
				if (map.containsKey(Type.SKIN)) {
					MinecraftProfileTexture tex = map.get(Type.SKIN);
					skinType = tex.getMetadata("model");

					if (skinType == null) {
						skinType = "default";
					}
					return CompletableFuture.completedFuture(null);
				}
				CompletableFuture<Void> cf = new CompletableFuture<>();
				Minecraft.getInstance().getSkinManager().loadProfileTextures(profile, new ISkinAvailableCallback() {

					@Override
					public void onSkinTextureAvailable(Type typeIn, ResourceLocation location, MinecraftProfileTexture profileTexture) {
						defineTexture(typeIn, profileTexture.getUrl(), profileTexture.getHash());
						switch (typeIn) {
						case SKIN:
							skinType = profileTexture.getMetadata("model");

							if (skinType == null) {
								skinType = "default";
							}
							RenderSystem.recordRenderCall(() -> cf.complete(null));
							break;

						default:
							break;
						}
					}
				}, true);
				return cf;
			}
		};
	}

	@Override
	public String getName() {
		return profile.getName();
	}

	@Override
	public Object getGameProfile() {
		return profile;
	}
}
