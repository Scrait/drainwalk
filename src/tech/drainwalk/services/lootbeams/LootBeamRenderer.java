package tech.drainwalk.services.lootbeams;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.models.DrainwalkResource;

import java.awt.*;
import java.util.List;

import static tech.drainwalk.services.lootbeams.GetColorHelper.getItemColor;

public class LootBeamRenderer extends RenderState implements IInstanceAccess {

	/**
	 * ISSUES:
	 * Beam renders behind things like chests/clouds/water/beds/entities.
	 */

	private static final ResourceLocation LOOT_BEAM_TEXTURE = new DrainwalkResource("textures/entity/loot_beam.png");
	private static final RenderType LOOT_BEAM_RENDERTYPE = createRenderType();

	public LootBeamRenderer(String string, Runnable run, Runnable run2) {
		super(string, run, run2);
	}

	public static void renderLootBeam(MatrixStack stack, IRenderTypeBuffer buffer, float pticks, long worldtime, ItemEntity item) {
		float beamAlpha = LootBeamConfiguration.BEAM_ALPHA.getValue();
		//Fade out when close
		if (mc.player.getDistanceSq(item) < 2f) {
			beamAlpha *= mc.player.getDistanceSq(item);
		}
		//Dont render beam if its too transparent
		if (beamAlpha <= 0.15f) {
			return;
		}

		float beamRadius = 0.05f * LootBeamConfiguration.BEAM_RADIUS.getValue();
		float glowRadius = beamRadius + (beamRadius * 0.2f);
		float beamHeight = LootBeamConfiguration.BEAM_HEIGHT.getValue();
		float yOffset = LootBeamConfiguration.BEAM_Y_OFFSET.getValue();

		Color color = getItemColor(item);
		float R = color.getRed() / 255f;
		float G = color.getGreen() / 255f;
		float B = color.getBlue() / 255f;

		//I will rewrite the beam rendering code soon! I promise!

		stack.push();

		//Render main beam
		stack.push();
		float rotation = (float) Math.floorMod(worldtime, 40L) + pticks;
		stack.rotate(Vector3f.YP.rotationDegrees(rotation * 2.25F - 45.0F));
		stack.translate(0, yOffset, 0);
		stack.translate(0, 1, 0);
		stack.rotate(Vector3f.XP.rotationDegrees(180));
		renderPart(stack, buffer.getBuffer(LOOT_BEAM_RENDERTYPE), R, G, B, beamAlpha, beamHeight, 0.0F, beamRadius, beamRadius, 0.0F, -beamRadius, 0.0F, 0.0F, -beamRadius);
		stack.rotate(Vector3f.XP.rotationDegrees(-180));
		renderPart(stack, buffer.getBuffer(LOOT_BEAM_RENDERTYPE), R, G, B, beamAlpha, beamHeight, 0.0F, beamRadius, beamRadius, 0.0F, -beamRadius, 0.0F, 0.0F, -beamRadius);
		stack.pop();

		//Render glow around main beam
		stack.translate(0, yOffset, 0);
		stack.translate(0, 1, 0);
		stack.rotate(Vector3f.XP.rotationDegrees(180));
		renderPart(stack, buffer.getBuffer(LOOT_BEAM_RENDERTYPE), R, G, B, beamAlpha * 0.4f, beamHeight, -glowRadius, -glowRadius, glowRadius, -glowRadius, -beamRadius, glowRadius, glowRadius, glowRadius);
		stack.rotate(Vector3f.XP.rotationDegrees(-180));
		renderPart(stack, buffer.getBuffer(LOOT_BEAM_RENDERTYPE), R, G, B, beamAlpha * 0.4f, beamHeight, -glowRadius, -glowRadius, glowRadius, -glowRadius, -beamRadius, glowRadius, glowRadius, glowRadius);

		stack.pop();

		if (LootBeamConfiguration.RENDER_NAMETAGS.getValue()) {
			renderNameTag(stack, buffer, item, color);
		}
	}

	private static void renderNameTag(MatrixStack stack, IRenderTypeBuffer buffer, ItemEntity item, Color color) {
		//If player is crouching or looking at the item
		if (mc.player.isCrouching() || (LootBeamConfiguration.RENDER_NAMETAGS_ONLOOK.getValue() && isLookingAt(mc.player, item, LootBeamConfiguration.NAMETAG_LOOK_SENSITIVITY.getValue().doubleValue()))) {

			float foregroundAlpha = LootBeamConfiguration.NAMETAG_TEXT_ALPHA.getValue();
			float backgroundAlpha = LootBeamConfiguration.NAMETAG_BACKGROUND_ALPHA.getValue();
			double yOffset = LootBeamConfiguration.NAMETAG_Y_OFFSET.getValue().doubleValue();
			int foregroundColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (255 * foregroundAlpha)).getRGB();
			int backgroundColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (255 * backgroundAlpha)).getRGB();

			stack.push();

			//Render nametags at heights based on player distance
			stack.translate(0.0D, Math.min(1D, mc.player.getDistanceSq(item) * 0.025D) + yOffset, 0.0D);
			stack.rotate(mc.getRenderManager().getCameraOrientation());

			float nametagScale = LootBeamConfiguration.NAMETAG_SCALE.getValue();
			stack.scale(-0.02F * nametagScale, -0.02F * nametagScale, 0.02F * nametagScale);

			//Render stack counts on nametag
			FontRenderer fontrenderer = mc.fontRenderer;
			String itemName = StringUtils.stripControlCodes(item.getItem().getDisplayName().getString());
			if (LootBeamConfiguration.RENDER_STACKCOUNT.getValue()) {
				int count = item.getItem().getCount();
				if (count > 1) {
					itemName = itemName + " x" + count;
				}
			}

			//Move closer to the player so we dont render in beam, and render the tag
			stack.translate(0, 0, -10);
			RenderText(fontrenderer, stack, buffer, itemName, foregroundColor, backgroundColor, backgroundAlpha);

			//Render small tags
			stack.translate(0.0D, 10, 0.0D);
			stack.scale(0.75f, 0.75f, 0.75f);
			boolean textDrawn = false;
			List<ITextComponent> tooltip = item.getItem().getTooltip(null, ITooltipFlag.TooltipFlags.NORMAL);
			if (tooltip.size() >= 2) {
				ITextComponent tooltipRarity = tooltip.get(1);

//				//Render custom rarities
//				if (!textDrawn && LootBeamConfiguration.CUSTOM_RARITIES.get().contains(tooltipRarity.getString())) {
//					Color rarityColor = LootBeamConfiguration.WHITE_RARITIES.get() ? Color.WHITE : getRawColor(tooltipRarity);
//					foregroundColor = new Color(rarityColor.getRed(), rarityColor.getGreen(), rarityColor.getBlue(), (int) (255 * foregroundAlpha)).getRGB();
//					backgroundColor = new Color(rarityColor.getRed(), rarityColor.getGreen(), rarityColor.getBlue(), (int) (255 * backgroundAlpha)).getRGB();
//					RenderText(fontrenderer, stack, buffer, tooltipRarity.getString(), foregroundColor, backgroundColor, backgroundAlpha);
//				}
			}

			stack.pop();
		}
	}

	private static void RenderText(FontRenderer fontRenderer, MatrixStack stack, IRenderTypeBuffer buffer, String text, int foregroundColor, int backgroundColor, float backgroundAlpha) {
		if (LootBeamConfiguration.BORDERS.getValue()) {
			float w = -fontRenderer.getStringWidth(text) / 2f;
			int bg = new Color(0, 0, 0, (int) (255 * backgroundAlpha)).getRGB();

			//Draws background (border) text
			fontRenderer.drawString(stack, text, w + 1f, 0, bg);
			fontRenderer.drawString(stack, text, w - 1f, 0, bg);
			fontRenderer.drawString(stack, text, w, 1f, bg);
			fontRenderer.drawString(stack, text, w, -1f, bg);

			//Draws foreground text in front of border
			stack.translate(0.0D, 0.0D, -0.01D);
			fontRenderer.drawString(stack, text, w, 0, foregroundColor);
			stack.translate(0.0D, 0.0D, 0.01D);
		} else {
//			fontRenderer.dr(text, (float) (-fontRenderer.getStringWidth(text) / 2), 0f, foregroundColor, false, stack.getLast().getMatrix(), buffer, false, backgroundColor, 15728864);
		}
	}

	private static void renderPart(MatrixStack stack, IVertexBuilder builder, float red, float green, float blue, float alpha, float height, float radius_1, float radius_2, float radius_3, float radius_4, float radius_5, float radius_6, float radius_7, float radius_8) {
		MatrixStack.Entry matrixentry = stack.getLast();
		Matrix4f matrixpose = matrixentry.getMatrix();
		Matrix3f matrixnormal = matrixentry.getNormal();
		renderQuad(matrixpose, matrixnormal, builder, red, green, blue, alpha, height, radius_1, radius_2, radius_3, radius_4);
		renderQuad(matrixpose, matrixnormal, builder, red, green, blue, alpha, height, radius_7, radius_8, radius_5, radius_6);
		renderQuad(matrixpose, matrixnormal, builder, red, green, blue, alpha, height, radius_3, radius_4, radius_7, radius_8);
		renderQuad(matrixpose, matrixnormal, builder, red, green, blue, alpha, height, radius_5, radius_6, radius_1, radius_2);
	}

	private static void renderQuad(Matrix4f pose, Matrix3f normal, IVertexBuilder builder, float red, float green, float blue, float alpha, float y, float z1, float texu1, float z, float texu) {
		addVertex(pose, normal, builder, red, green, blue, alpha, y, z1, texu1, 1f, 0f);
		addVertex(pose, normal, builder, red, green, blue, alpha, 0f, z1, texu1, 1f, 1f);
		addVertex(pose, normal, builder, red, green, blue, alpha, 0f, z, texu, 0f, 1f);
		addVertex(pose, normal, builder, red, green, blue, alpha, y, z, texu, 0f, 0f);
	}

	private static void addVertex(Matrix4f pose, Matrix3f normal, IVertexBuilder builder, float red, float green, float blue, float alpha, float y, float x, float z, float texu, float texv) {
		builder.pos(pose, x, y, z).color(red, green, blue, alpha).tex(texu, texv).overlay(OverlayTexture.NO_OVERLAY).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
	}

	private static RenderType createRenderType() {
		RenderType.State state = RenderType.State.getBuilder().texture(new RenderState.TextureState(LOOT_BEAM_TEXTURE, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).writeMask(RenderState.COLOR_WRITE).fog(NO_FOG).build(false);
		return RenderType.makeType("loot_beam", DefaultVertexFormats.BLOCK, 7, 256, false, true, state);
	}

	/**
	 * Checks if the player is looking at the given entity, accuracy determines how close the player has to look.
	 */
	private static boolean isLookingAt(ClientPlayerEntity player, Entity target, double accuracy) {
		Vector3d difference = new Vector3d(target.getPosX() - player.getPosX(), target.getPosYEye() - player.getPosYEye(), target.getPosZ() - player.getPosZ());
		double length = difference.length();
		double dot = player.getLook(1.0F).normalize().dotProduct(difference.normalize());
		return dot > 1.0D - accuracy / length && player.canEntityBeSeen(target);
	}

}