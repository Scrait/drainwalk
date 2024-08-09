package tech.drainwalk.utils.cape.renderlayers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import tech.drainwalk.utils.cape.CapeHolder;
import tech.drainwalk.utils.cape.CapeRenderer;
import tech.drainwalk.utils.cape.VanillaCapeRenderer;
import tech.drainwalk.utils.cape.sim.StickSimulation;
import tech.drainwalk.utils.minecraft.CastUtil;

public class CustomCapeLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>
{

    private static int partCount;
    private ModelRenderer[] customCape = new ModelRenderer[partCount];
    public CustomCapeLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> playerModelIn)
    {
        super(playerModelIn);
        partCount = 16;
        buildMesh();
    }

    private void buildMesh() {
        customCape = new ModelRenderer[partCount];

        for (int i = 0; i < partCount; i++) {
            ModelRenderer base = new ModelRenderer(64, 32, 0, i);
            this.customCape[i] = base.addBox(-5.0F, (float)i, -1.0F, 10.0F, 1.2F, 1F);
        }
    }

    private static VanillaCapeRenderer vanillaCape = new VanillaCapeRenderer();

    private CapeRenderer getCapeRenderer(IRenderTypeBuffer multiBufferSource) {
        vanillaCape.vertexConsumer = multiBufferSource
                .getBuffer(RenderType.getEntityCutout(new ResourceLocation("drainwalk/images/cape.png")));

        return vanillaCape;
    }

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn.isInvisible()) return;
        final CastUtil castHelper = new CastUtil();
        castHelper.apply(CastUtil.EntityType.FRIENDS);
        castHelper.apply(CastUtil.EntityType.SELF);
        if (CastUtil.isInstanceof(entitylivingbaseIn, castHelper.build()) != null && !entitylivingbaseIn.isDead()) {
            CapeRenderer renderer = getCapeRenderer(bufferIn);

            if (renderer == null) return;
            ItemStack itemStack = entitylivingbaseIn.getItemStackFromSlot(EquipmentSlotType.CHEST);

            if (itemStack.getItem() == Items.ELYTRA)
                return;

            CapeHolder holder = (CapeHolder) entitylivingbaseIn;
            holder.updateSimulation(entitylivingbaseIn, partCount);

            renderSmoothCape(matrixStackIn, bufferIn, renderer, entitylivingbaseIn, partialTicks, packedLightIn);
        }
    }

    private void renderSmoothCape(MatrixStack poseStack,
                                  IRenderTypeBuffer multiBufferSource,
                                  CapeRenderer capeRenderer,
                                  AbstractClientPlayerEntity abstractClientPlayer,
                                  float delta,
                                  int light) {
        IVertexBuilder bufferBuilder = capeRenderer.getVertexConsumer(multiBufferSource, abstractClientPlayer);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Matrix4f oldPositionMatrix = null;
        for (int part = 0; part < partCount; part++) {
            modifyPoseStack(poseStack, abstractClientPlayer, delta, part);

            if (oldPositionMatrix == null) {
                oldPositionMatrix = poseStack.getLast().getMatrix();
            }


            final float partSize =  partCount;


            if (part == 0) {
                addTopVertex(bufferBuilder, poseStack.getLast().getMatrix(), oldPositionMatrix,
                        0.3F,
                        0,
                        0F,
                        -0.3F,
                        0,
                        -0.06F, part, light);
            }

            if (part == partSize - 1) {
                addBottomVertex(bufferBuilder, poseStack.getLast().getMatrix(), poseStack.getLast().getMatrix(),
                        0.3F,
                        (part + 1) * (0.96F / partSize),
                        0F,
                        -0.3F,
                        (part + 1) * (0.96F / partSize),
                        -0.06F, part, light);
            }

            addLeftVertex(bufferBuilder, poseStack.getLast().getMatrix(), oldPositionMatrix,
                    -0.3F,
                    (part + 1) * (0.96F / partSize),
                    0F,
                    -0.3F,
                    part * (0.96F / partSize),
                    -0.06F, part, light);

            addRightVertex(bufferBuilder, poseStack.getLast().getMatrix(), oldPositionMatrix,
                    0.3F,
                    (part + 1) * (0.96F / partSize),
                    0F,
                    0.3F,
                    part * (0.96F / partSize),
                    -0.06F, part, light);

            addBackVertex(bufferBuilder, poseStack.getLast().getMatrix(), oldPositionMatrix,
                    0.3F,
                    (part + 1) * (0.96F / partSize),
                    -0.06F,
                    -0.3F,
                    part * (0.96F / partSize),
                    -0.06F, part, light);

            addFrontVertex(bufferBuilder, oldPositionMatrix, poseStack.getLast().getMatrix(),
                    0.3F,
                    (part + 1) * (0.96F / partSize),
                    0F,
                    -0.3F,
                    part * (0.96F / partSize),
                    0F, part, light);

            oldPositionMatrix = new Matrix4f(poseStack.getLast().getMatrix());
            poseStack.pop();
        }
    }

    private void modifyPoseStack(MatrixStack poseStack, AbstractClientPlayerEntity abstractClientPlayer, float h, int part) {
        modifyPoseStackSimulation(poseStack, abstractClientPlayer, h, part);
    }

    private void modifyPoseStackSimulation(MatrixStack poseStack, AbstractClientPlayerEntity abstractClientPlayer, float delta, int part) {
        StickSimulation simulation = ((CapeHolder) abstractClientPlayer).getSimulation();
        poseStack.push();
        poseStack.translate(0.0D, 0.0D, 0.125D);

        float z = simulation.points.get(part).getLerpX(delta) - simulation.points.get(0).getLerpX(delta);

        if (z > 0) {
            z = 0;
        }

        float y = simulation.points.get(0).getLerpY(delta) - part - simulation.points.get(part).getLerpY(delta);

        float sidewaysRotationOffset = 0;
        float partRotation = (float) -MathHelper.atan2(y, z);
        partRotation = Math.max(partRotation, 0);

        if (partRotation != 0)
            partRotation = (float) (Math.PI - partRotation);

        partRotation *= 57.2958;
        partRotation *= 2;

        float height = 0;

        if (abstractClientPlayer.isCrouching()) {
            height += 25.0F;
            poseStack.translate(0, 0.15F, 0);
        }

        float naturalWindSwing = getNatrualWindSwing(part);

        poseStack.rotate(Vector3f.XP.rotationDegrees(6.0F + height + naturalWindSwing));
        poseStack.rotate(Vector3f.ZP.rotationDegrees(sidewaysRotationOffset / 2.0F));
        poseStack.rotate(Vector3f.YP.rotationDegrees(180.0F - sidewaysRotationOffset / 2.0F));
        poseStack.translate(0, y / partCount, z / partCount); // movement from the simulation
        poseStack.translate(0, (0.48 / 16), -(0.48 / 16)); // (0.48/16)
        poseStack.translate(0, part * 1f / partCount, 0);
        poseStack.rotate(Vector3f.XP.rotationDegrees(-partRotation)); // apply actual rotation
        poseStack.translate(0, -part * 1f / partCount, -part / partCount);
        poseStack.translate(0, -(0.48 / 16), (0.48 / 16));
    }

    private float getNatrualWindSwing(int part) {
        long highlightedPart = (System.currentTimeMillis() / 3) % 360;
        float relativePart = (float) (part + 1) / partCount;

        return (float) (Math.sin(Math.toRadians((relativePart) * 360 - (highlightedPart))) * 3);
    }

    private static void addBackVertex(IVertexBuilder bufferBuilder, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part, int light) {
        float i;
        Matrix4f k;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;

            k = matrix;
            matrix = oldMatrix;
            oldMatrix = k;
        }

        float minU = .015625F;
        float maxU = .171875F;

        float minV = .03125F;
        float maxV = .53125F;

        float deltaV = maxV - minV;
        float vPerPart = deltaV / partCount;
        maxV = minV + (vPerPart * (part + 1));
        minV = minV + (vPerPart * part);

        bufferBuilder.pos(oldMatrix, x1, y2, z1).color(1f, 1f, 1f, 1f).tex(maxU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
        bufferBuilder.pos(oldMatrix, x2, y2, z1).color(1f, 1f, 1f, 1f).tex(minU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
        bufferBuilder.pos(matrix, x2, y1, z2).color(1f, 1f, 1f, 1f).tex(minU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
        bufferBuilder.pos(matrix, x1, y1, z2).color(1f, 1f, 1f, 1f).tex(maxU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
    }

    private static void addFrontVertex(IVertexBuilder bufferBuilder, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part, int light) {
        float i;
        Matrix4f k;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;

            k = matrix;
            matrix = oldMatrix;
            oldMatrix = k;
        }

        float minU = .1875F;
        float maxU = .34375F;

        float minV = .03125F;
        float maxV = .53125F;

        float deltaV = maxV - minV;
        float vPerPart = deltaV / partCount;
        maxV = minV + (vPerPart * (part + 1));
        minV = minV + (vPerPart * part);

        bufferBuilder.pos(oldMatrix, x1, y1, z1).color(1f, 1f, 1f, 1f).tex(maxU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
        bufferBuilder.pos(oldMatrix, x2, y1, z1).color(1f, 1f, 1f, 1f).tex(minU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
        bufferBuilder.pos(matrix, x2, y2, z2).color(1f, 1f, 1f, 1f).tex(minU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
        bufferBuilder.pos(matrix, x1, y2, z2).color(1f, 1f, 1f, 1f).tex(maxU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
    }

    private static void addLeftVertex(IVertexBuilder bufferBuilder, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part, int light) {
        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }

        float minU = 0;
        float maxU = .015625F;

        float minV = .03125F;
        float maxV = .53125F;

        float deltaV = maxV - minV;
        float vPerPart = deltaV / partCount;
        maxV = minV + (vPerPart * (part + 1));
        minV = minV + (vPerPart * part);

        bufferBuilder.pos(matrix, x2, y1, z1).color(1f, 1f, 1f, 1f).tex(maxU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
        bufferBuilder.pos(matrix, x2, y1, z2).color(1f, 1f, 1f, 1f).tex(minU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
        bufferBuilder.pos(oldMatrix, x2, y2, z2).color(1f, 1f, 1f, 1f).tex(minU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
        bufferBuilder.pos(oldMatrix, x2, y2, z1).color(1f, 1f, 1f, 1f).tex(maxU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
    }

    private static void addRightVertex(IVertexBuilder bufferBuilder, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part, int light) {
        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }

        float minU = .171875F;
        float maxU = .1875F;

        float minV = .03125F;
        float maxV = .53125F;

        float deltaV = maxV - minV;
        float vPerPart = deltaV / partCount;
        maxV = minV + (vPerPart * (part + 1));
        minV = minV + (vPerPart * part);

        bufferBuilder.pos(matrix, x2, y1, z2).color(1f, 1f, 1f, 1f).tex(minU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
        bufferBuilder.pos(matrix, x2, y1, z1).color(1f, 1f, 1f, 1f).tex(maxU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
        bufferBuilder.pos(oldMatrix, x2, y2, z1).color(1f, 1f, 1f, 1f).tex(maxU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
        bufferBuilder.pos(oldMatrix, x2, y2, z2).color(1f, 1f, 1f, 1f).tex(minU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
    }

    private static void addBottomVertex(IVertexBuilder bufferBuilder, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part, int light) {
        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }

        float minU = .171875F;
        float maxU = .328125F;

        float minV = 0;
        float maxV = .03125F;

        float deltaV = maxV - minV;
        float vPerPart = deltaV / partCount;
        maxV = minV + (vPerPart * (part + 1));
        minV = minV + (vPerPart * part);

        bufferBuilder.pos(oldMatrix, x1, y2, z2).color(1f, 1f, 1f, 1f).tex(maxU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
        bufferBuilder.pos(oldMatrix, x2, y2, z2).color(1f, 1f, 1f, 1f).tex(minU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
        bufferBuilder.pos(matrix, x2, y1, z1).color(1f, 1f, 1f, 1f).tex(minU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
        bufferBuilder.pos(matrix, x1, y1, z1).color(1f, 1f, 1f, 1f).tex(maxU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1, 0, 0).endVertex();
    }

    private static void addTopVertex(IVertexBuilder bufferBuilder, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part, int light) {
        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }

        float minU = .015625F;
        float maxU = .171875F;

        float minV = 0;
        float maxV = .03125F;

        float deltaV = maxV - minV;
        float vPerPart = deltaV / partCount;
        maxV = minV + (vPerPart * (part + 1));
        minV = minV + (vPerPart * part);

        bufferBuilder.pos(oldMatrix, x1, y2, z1).color(1f, 1f, 1f, 1f).tex(maxU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0, 1, 0).endVertex();
        bufferBuilder.pos(oldMatrix, x2, y2, z1).color(1f, 1f, 1f, 1f).tex(minU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0, 1, 0).endVertex();
        bufferBuilder.pos(matrix, x2, y1, z2).color(1f, 1f, 1f, 1f).tex(minU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0, 1, 0).endVertex();
        bufferBuilder.pos(matrix, x1, y1, z2).color(1f, 1f, 1f, 1f).tex(maxU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0, 1, 0).endVertex();
    }
}