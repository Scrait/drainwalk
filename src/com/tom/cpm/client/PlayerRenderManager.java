package com.tom.cpm.client;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.ElytraModel;
import net.minecraft.client.renderer.entity.model.HumanoidHeadModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import com.tom.cpl.math.Vec4f;
import com.tom.cpl.render.VBuffers;
import com.tom.cpm.client.MinecraftObject.DynTexture;
import com.tom.cpm.client.optifine.OptifineTexture;
import com.tom.cpm.client.optifine.RedirectRendererOF;
import com.tom.cpm.shared.model.PlayerModelParts;
import com.tom.cpm.shared.model.RootModelType;
import com.tom.cpm.shared.model.TextureSheetType;
import com.tom.cpm.shared.model.render.BatchedBuffers;
import com.tom.cpm.shared.model.render.ModelRenderManager;
import com.tom.cpm.shared.model.render.VanillaModelPart;
import com.tom.cpm.shared.skin.TextureProvider;

public class PlayerRenderManager extends ModelRenderManager<IRenderTypeBuffer, ModelTexture, ModelRenderer, Model> {
	public static final Function<ResourceLocation, RenderType> armor = RenderType::getArmorCutoutNoCull;
	public static final Function<ResourceLocation, RenderType> entity = RenderType::getEntityTranslucent;

	public PlayerRenderManager() {
		setFactory(new RedirectHolderFactory<>() {

            @SuppressWarnings("unchecked")
            @Override
            public <M> RedirectHolder<?, IRenderTypeBuffer, ModelTexture, ModelRenderer> create(
                    M model, String arg) {
				System.out.println("PlayerRenderManager.RedirectHolderFactory.create");
				if ("api".equals(arg) && model instanceof BipedModel) {
                    return new RedirectHolderApi(PlayerRenderManager.this, (BipedModel<LivingEntity>) model);
                }/* else if(CustomPlayerModelsClient.vrLoaded && VRPlayerRenderer.isVRPlayer(model)) {
					return VRPlayerRenderer.createVRPlayer(PlayerRenderManager.this, model);
				}*/ else if (model instanceof PlayerModel) {
                    return new RedirectHolderPlayer(PlayerRenderManager.this, (PlayerModel<LivingEntity>) model);
                } else if (model instanceof HumanoidHeadModel) {
                    return new RedirectHolderSkull(PlayerRenderManager.this, (HumanoidHeadModel) model);
                } else if (model instanceof ElytraModel) {
                    return new RedirectHolderElytra(PlayerRenderManager.this, (ElytraModel<LivingEntity>) model);
                } else if (model instanceof BipedModel && "armor1".equals(arg)) {
                    return new RedirectHolderArmor1(PlayerRenderManager.this, (BipedModel<LivingEntity>) model);
                } else if (model instanceof BipedModel && "armor2".equals(arg)) {
                    return new RedirectHolderArmor2(PlayerRenderManager.this, (BipedModel<LivingEntity>) model);
                }
				System.out.println("null");
				return null;
            }
        });
		setRedirectFactory((model, access, modelPart, part) -> {
            return /*CustomPlayerModelsClient.optifineLoaded ?*/
                    new RedirectModelRendererVanilla((RDH) access, modelPart, part)/* :
                        new RedirectModelRendererVanilla((RDH) access, modelPart, part)*/;
        });
		setVis(m -> m.showModel, (m, v) -> m.showModel = v);
		setModelPosGetters(m -> m.rotationPointX, m -> m.rotationPointY, m -> m.rotationPointZ);
		setModelRotGetters(m -> m.rotateAngleX, m -> m.rotateAngleY, m -> m.rotateAngleZ);
		setModelSetters((m, x, y, z) -> {
			m.rotationPointX = x;
			m.rotationPointY = y;
			m.rotationPointZ = z;
		}, (m, x, y, z) -> {
			m.rotateAngleX = x;
			m.rotateAngleY = y;
			m.rotateAngleZ = z;
		});
		setRenderPart(new ModelRenderer(0, 0, 0, 0));
	}

	public static abstract class RDH extends ModelRenderManager.RedirectHolder<Model, IRenderTypeBuffer, ModelTexture, ModelRenderer> {

		public RDH(
				ModelRenderManager<IRenderTypeBuffer, ModelTexture, ModelRenderer, Model> mngr,
				Model model) {
			super(mngr, model);
			batch = new BatchedBuffers<>(this, IRenderTypeBuffer::getBuffer);
		}

		@Override
		public void setupRenderSystem(ModelTexture cbi, TextureSheetType tex) {
			CustomPlayerModelsClient.mc.renderBuilder.build(renderTypes, cbi);
		}

		@Override
		protected void bindTexture(ModelTexture cbi, TextureProvider skin) {
			skin.bind();
			OptifineTexture.applyOptifineTexture(cbi.getTexture(), skin);
			System.out.println("MAMOM");
			cbi.setTexture(DynTexture.getBoundLoc());
		}

		@Override
		public void swapOut0() {
		}

		@Override
		public void swapIn0() {}
	}

	private static class RedirectHolderPlayer extends RDH {
		private RedirectRenderer<ModelRenderer> head;

		public RedirectHolderPlayer(PlayerRenderManager mngr, PlayerModel<LivingEntity> model) {
			super(mngr, model);
			head = registerHead(new Field<>(    () -> model.bipedHead    , v -> model.bipedHead     = v, PlayerModelParts.HEAD));
			register(new Field<>(() -> model.bipedBody    , v -> model.bipedBody     = v, PlayerModelParts.BODY));
			register(new Field<>(() -> model.bipedRightArm, v -> model.bipedRightArm = v, PlayerModelParts.RIGHT_ARM));
			register(new Field<>(() -> model.bipedLeftArm , v -> model.bipedLeftArm  = v, PlayerModelParts.LEFT_ARM));
			register(new Field<>(() -> model.bipedRightLeg, v -> model.bipedRightLeg = v, PlayerModelParts.RIGHT_LEG));
			register(new Field<>(() -> model.bipedLeftLeg , v -> model.bipedLeftLeg  = v, PlayerModelParts.LEFT_LEG));

			register(new Field<>(() -> model.bipedHeadwear        , v -> model.bipedHeadwear         = v, null)).setCopyFrom(head);
			register(new Field<>(() -> model.bipedLeftArmwear , v -> model.bipedLeftArmwear  = v, null));
			register(new Field<>(() -> model.bipedRightArmwear, v -> model.bipedRightArmwear = v, null));
			register(new Field<>(() -> model.bipedLeftLegwear  , v -> model.bipedLeftLegwear   = v, null));
			register(new Field<>(() -> model.bipedRightLegwear , v -> model.bipedRightLegwear  = v, null));
			register(new Field<>(() -> model.bipedBodyWear     , v -> model.bipedBodyWear      = v, null));

			register(new Field<>(() -> model.bipedCape, v -> model.bipedCape = v, RootModelType.CAPE));
		}
	}

	private static class RedirectHolderApi extends RDH {
		private RedirectRenderer<ModelRenderer> head;

		public RedirectHolderApi(PlayerRenderManager mngr, BipedModel<LivingEntity> model) {
			super(mngr, model);
			head = registerHead(new Field<>(    () -> model.bipedHead    , v -> model.bipedHead     = v, PlayerModelParts.HEAD));
			register(new Field<>(() -> model.bipedBody    , v -> model.bipedBody     = v, PlayerModelParts.BODY));
			register(new Field<>(() -> model.bipedRightArm, v -> model.bipedRightArm = v, PlayerModelParts.RIGHT_ARM));
			register(new Field<>(() -> model.bipedLeftArm , v -> model.bipedLeftArm  = v, PlayerModelParts.LEFT_ARM));
			register(new Field<>(() -> model.bipedRightLeg, v -> model.bipedRightLeg = v, PlayerModelParts.RIGHT_LEG));
			register(new Field<>(() -> model.bipedLeftLeg , v -> model.bipedLeftLeg  = v, PlayerModelParts.LEFT_LEG));

			register(new Field<>(() -> model.bipedHeadwear        , v -> model.bipedHeadwear         = v, null)).setCopyFrom(head);
			if(model instanceof PlayerModel) {
				PlayerModel<LivingEntity> pm = (PlayerModel<LivingEntity>) model;
				register(new Field<>(() -> pm.bipedLeftArmwear , v -> pm.bipedLeftArmwear  = v, null));
				register(new Field<>(() -> pm.bipedRightArmwear, v -> pm.bipedRightArmwear = v, null));
				register(new Field<>(() -> pm.bipedLeftLegwear  , v -> pm.bipedLeftLegwear   = v, null));
				register(new Field<>(() -> pm.bipedRightLegwear , v -> pm.bipedRightLegwear  = v, null));
				register(new Field<>(() -> pm.bipedBodyWear     , v -> pm.bipedBodyWear      = v, null));

				register(new Field<>(() -> pm.bipedCape, v -> pm.bipedCape = v, RootModelType.CAPE));
			}
		}

		@Override
		protected boolean isDirectMode() {
			return true;
		}
	}

	private static class RedirectHolderSkull extends RDH {

		public RedirectHolderSkull(PlayerRenderManager mngr, HumanoidHeadModel model) {
			super(mngr, model);

			register(new Field<>(model::getField_217105_a, model::setField_217105_a, PlayerModelParts.HEAD));
			register(new Field<>(model::getHead, model::setHead, null));
		}

	}

	private static class RedirectHolderElytra extends RDH {

		public RedirectHolderElytra(PlayerRenderManager mngr, ElytraModel<LivingEntity> model) {
			super(mngr, model);

			register(new Field<>(model::getRightWing, model::setRightWing, RootModelType.ELYTRA_RIGHT));
			register(new Field<>(model::getLeftWing, model::setLeftWing, RootModelType.ELYTRA_LEFT));
		}

	}

	private static class RedirectHolderArmor1 extends RDH {

		public RedirectHolderArmor1(PlayerRenderManager mngr, BipedModel<LivingEntity> model) {
			super(mngr, model);

			register(new Field<>(() -> model.bipedHead,     v -> model.bipedHead     = v, RootModelType.ARMOR_HELMET));
			register(new Field<>(() -> model.bipedBody,     v -> model.bipedBody     = v, RootModelType.ARMOR_BODY));
			register(new Field<>(() -> model.bipedRightArm, v -> model.bipedRightArm = v, RootModelType.ARMOR_RIGHT_ARM));
			register(new Field<>(() -> model.bipedLeftArm,  v -> model.bipedLeftArm  = v, RootModelType.ARMOR_LEFT_ARM));
			register(new Field<>(() -> model.bipedRightLeg, v -> model.bipedRightLeg = v, RootModelType.ARMOR_RIGHT_FOOT));
			register(new Field<>(() -> model.bipedLeftLeg,  v -> model.bipedLeftLeg  = v, RootModelType.ARMOR_LEFT_FOOT));

			register(new Field<>(() -> model.bipedHeadwear , v -> model.bipedHeadwear  = v, null));
		}

	}

	private static class RedirectHolderArmor2 extends RDH {

		public RedirectHolderArmor2(PlayerRenderManager mngr, BipedModel<LivingEntity> model) {
			super(mngr, model);

			register(new Field<>(() -> model.bipedBody,     v -> model.bipedBody     = v, RootModelType.ARMOR_LEGGINGS_BODY));
			register(new Field<>(() -> model.bipedRightLeg, v -> model.bipedRightLeg = v, RootModelType.ARMOR_RIGHT_LEG));
			register(new Field<>(() -> model.bipedLeftLeg,  v -> model.bipedLeftLeg  = v, RootModelType.ARMOR_LEFT_LEG));
		}

	}

	public static abstract class RedirectModelRendererBase extends ModelRenderer implements RedirectRenderer<ModelRenderer> {
		protected final RDH holder;
		protected final VanillaModelPart part;
		protected final Supplier<ModelRenderer> parentProvider;
		protected ModelRenderer parent;
		protected VBuffers buffers;

		public RedirectModelRendererBase(RDH holder, Supplier<ModelRenderer> parent, VanillaModelPart part) {
			super(0, 0, 0, 0);
			this.part = part;
			this.holder = holder;
			this.parentProvider = parent;
		}

		@Override
		public VBuffers getVBuffers() {
			return buffers;
		}

		@Override
		public ModelRenderer swapIn() {
			if(parent != null)return this;
			parent = parentProvider.get();
			holder.copyModel(parent, this);
			return this;
		}

		@Override
		public ModelRenderer swapOut() {
			if(parent == null)return parentProvider.get();
			ModelRenderer p = parent;
			parent = null;
			return p;
		}

		@Override
		public RedirectHolder<?, ?, ?, ModelRenderer> getHolder() {
			return holder;
		}

		@Override
		public ModelRenderer getParent() {
			return parent;
		}

		@Override
		public VanillaModelPart getPart() {
			return part;
		}

		protected float red, green, blue, alpha;

		@Override
		public Vec4f getColor() {
			return new Vec4f(red, green, blue, alpha);
		}

		@Override
		public void translateRotate(MatrixStack stack) {
			com.tom.cpl.math.MatrixStack.Entry e = getPartTransform();
			if(e != null) {
				multiplyStacks(e, stack);
			} else
				super.translateRotate(stack);
		}

		@Override
		public ModelBox getRandomCube(Random pRandom) {
			if(parent != null)return parent.getRandomCube(pRandom);
			return super.getRandomCube(pRandom);
		}
	}

	public static class RedirectModelRendererVanilla extends RedirectModelRendererBase {

		public RedirectModelRendererVanilla(RDH holder, Supplier<ModelRenderer> parent, VanillaModelPart part) {
			super(holder, parent, part);
		}

		private MatrixStack matrixStackIn;
		private IVertexBuilder bufferIn;
		private int packedLightIn, packedOverlayIn;

		@Override
		public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
			if(!holder.renderTypes.isInitialized()) {
				holder.copyModel(this, parent);
				parent.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				holder.logWarning();
				return;
			}
			this.matrixStackIn   = matrixStackIn  ;
			this.bufferIn        = bufferIn       ;
			this.packedLightIn   = packedLightIn  ;
			this.packedOverlayIn = packedOverlayIn;
			this.red             = red            ;
			this.green           = green          ;
			this.blue            = blue           ;
			this.alpha           = alpha          ;
			this.buffers = holder.nextBatch(() -> new VBufferOut(packedLightIn, packedOverlayIn, matrixStackIn), bufferIn);
			//this.buffers = new VBuffers(rt -> new VBuffer(holder.addDt.getBuffer(rt.getNativeType()), packedLightIn, packedOverlayIn, matrixStackIn), new VBuffer(bufferIn, packedLightIn, packedOverlayIn, matrixStackIn));
			render();
			//holder.addDt.getBuffer(holder.renderTypes.get(RenderMode.DEFAULT).getNativeType());
			this.matrixStackIn = null;
			this.bufferIn = null;
		}

		@Override
		public void renderParent() {
			parent.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		}
	}

	public static void multiplyStacks(com.tom.cpl.math.MatrixStack.Entry e, MatrixStack stack) {
		stack.getLast().getMatrix().mul(Platform.createMatrix(e.getMatrixArray()));
		stack.getLast().getNormal().mul(new Matrix3f(Platform.createMatrix(e.getNormalArray())));
	}
}
