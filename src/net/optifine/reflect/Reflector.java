package net.optifine.reflect;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.FontResourceManager;
import net.minecraft.client.gui.screen.EnchantmentScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EnderCrystalRenderer;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.EvokerFangsRenderer;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.LeashKnotRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.LlamaSpitRenderer;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.entity.PufferfishRenderer;
import net.minecraft.client.renderer.entity.ShulkerBulletRenderer;
import net.minecraft.client.renderer.entity.TridentRenderer;
import net.minecraft.client.renderer.entity.TropicalFishRenderer;
import net.minecraft.client.renderer.entity.WitherSkullRenderer;
import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
import net.minecraft.client.renderer.entity.model.ArmorStandModel;
import net.minecraft.client.renderer.entity.model.BatModel;
import net.minecraft.client.renderer.entity.model.BeeModel;
import net.minecraft.client.renderer.entity.model.BlazeModel;
import net.minecraft.client.renderer.entity.model.BoarModel;
import net.minecraft.client.renderer.entity.model.BoatModel;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.entity.model.ChickenModel;
import net.minecraft.client.renderer.entity.model.CodModel;
import net.minecraft.client.renderer.entity.model.CreeperModel;
import net.minecraft.client.renderer.entity.model.EndermiteModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.EvokerFangsModel;
import net.minecraft.client.renderer.entity.model.FoxModel;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.client.renderer.entity.model.GuardianModel;
import net.minecraft.client.renderer.entity.model.HorseArmorChestsModel;
import net.minecraft.client.renderer.entity.model.HorseModel;
import net.minecraft.client.renderer.entity.model.HumanoidHeadModel;
import net.minecraft.client.renderer.entity.model.IllagerModel;
import net.minecraft.client.renderer.entity.model.IronGolemModel;
import net.minecraft.client.renderer.entity.model.LeashKnotModel;
import net.minecraft.client.renderer.entity.model.LlamaModel;
import net.minecraft.client.renderer.entity.model.LlamaSpitModel;
import net.minecraft.client.renderer.entity.model.MagmaCubeModel;
import net.minecraft.client.renderer.entity.model.MinecartModel;
import net.minecraft.client.renderer.entity.model.OcelotModel;
import net.minecraft.client.renderer.entity.model.ParrotModel;
import net.minecraft.client.renderer.entity.model.PhantomModel;
import net.minecraft.client.renderer.entity.model.PufferFishBigModel;
import net.minecraft.client.renderer.entity.model.PufferFishMediumModel;
import net.minecraft.client.renderer.entity.model.PufferFishSmallModel;
import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.entity.model.RabbitModel;
import net.minecraft.client.renderer.entity.model.RavagerModel;
import net.minecraft.client.renderer.entity.model.SalmonModel;
import net.minecraft.client.renderer.entity.model.ShulkerBulletModel;
import net.minecraft.client.renderer.entity.model.ShulkerModel;
import net.minecraft.client.renderer.entity.model.SilverfishModel;
import net.minecraft.client.renderer.entity.model.SlimeModel;
import net.minecraft.client.renderer.entity.model.SnowManModel;
import net.minecraft.client.renderer.entity.model.SpiderModel;
import net.minecraft.client.renderer.entity.model.SquidModel;
import net.minecraft.client.renderer.entity.model.StriderModel;
import net.minecraft.client.renderer.entity.model.TridentModel;
import net.minecraft.client.renderer.entity.model.TropicalFishAModel;
import net.minecraft.client.renderer.entity.model.TropicalFishBModel;
import net.minecraft.client.renderer.entity.model.TurtleModel;
import net.minecraft.client.renderer.entity.model.VexModel;
import net.minecraft.client.renderer.entity.model.VillagerModel;
import net.minecraft.client.renderer.entity.model.WitchModel;
import net.minecraft.client.renderer.entity.model.WitherModel;
import net.minecraft.client.renderer.entity.model.WolfModel;
import net.minecraft.client.renderer.model.ItemOverride;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.BannerTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.BedTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.BellTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.ConduitTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.EnchantmentTableTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.LecternTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.ShulkerBoxTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.SkullTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.model.DragonHeadModel;
import net.minecraft.client.resources.LegacyResourcePackWrapper;
import net.minecraft.client.resources.LegacyResourcePackWrapperV4;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.resources.IResourcePack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.Ticket;
import net.minecraft.world.server.TicketType;
import net.optifine.Log;
import net.optifine.util.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reflector
{
    private static final Logger LOGGER = LogManager.getLogger();

    // TODO: 29.07.2023 SUKA NE TROGAT!!!!

    public static ReflectorClass EntityItem = new ReflectorClass(ItemEntity.class);
    public static ReflectorField EntityItem_ITEM = new ReflectorField(EntityItem, DataParameter.class);
    public static ReflectorClass EnderDragonRenderer = new ReflectorClass(EnderDragonRenderer.class);
    public static ReflectorField EnderDragonRenderer_model = new ReflectorField(EnderDragonRenderer, EnderDragonRenderer.EnderDragonModel.class);
    public static ReflectorClass GuiEnchantment = new ReflectorClass(EnchantmentScreen.class);
    public static ReflectorField GuiEnchantment_bookModel = new ReflectorField(GuiEnchantment, BookModel.class);
    public static ReflectorClass GuiMainMenu = new ReflectorClass(MainMenuScreen.class);
    public static ReflectorField GuiMainMenu_splashText = new ReflectorField(GuiMainMenu, String.class);
    public static ReflectorClass ItemOverride = new ReflectorClass(ItemOverride.class);
    public static ReflectorField ItemOverride_mapResourceValues = new ReflectorField(ItemOverride, Map.class);
    public static ReflectorClass LegacyResourcePackWrapper = new ReflectorClass(LegacyResourcePackWrapper.class);
    public static ReflectorField LegacyResourcePackWrapper_pack = new ReflectorField(LegacyResourcePackWrapper, IResourcePack.class);
    public static ReflectorClass LegacyResourcePackWrapperV4 = new ReflectorClass(LegacyResourcePackWrapperV4.class);
    public static ReflectorField LegacyResourcePackWrapperV4_pack = new ReflectorField(LegacyResourcePackWrapperV4, IResourcePack.class);
    public static ReflectorClass LayerLlamaDecor = new ReflectorClass(LlamaDecorLayer.class);
    public static ReflectorField LayerLlamaDecor_model = new ReflectorField(LayerLlamaDecor, LlamaModel.class);
    public static ReflectorClass Minecraft = new ReflectorClass(Minecraft.class);
    public static ReflectorField Minecraft_debugFPS = new ReflectorField(new FieldLocatorTypes(Minecraft.class, new Class[] {CrashReport.class}, Integer.TYPE, new Class[] {String.class}, "debugFPS"));
    public static ReflectorField Minecraft_fontResourceManager = new ReflectorField(Minecraft, FontResourceManager.class);
    public static ReflectorClass ModelHumanoidHead = new ReflectorClass(HumanoidHeadModel.class);
    public static ReflectorField ModelHumanoidHead_head = new ReflectorField(ModelHumanoidHead, ModelRenderer.class);
    public static ReflectorClass ModelArmorStand = new ReflectorClass(ArmorStandModel.class);
    public static ReflectorFields ModelArmorStand_ModelRenderers = new ReflectorFields(ModelArmorStand, ModelRenderer.class, 4);
    public static ReflectorClass ModelBat = new ReflectorClass(BatModel.class);
    public static ReflectorFields ModelBat_ModelRenderers = new ReflectorFields(ModelBat, ModelRenderer.class, 6);
    public static ReflectorClass ModelBee = new ReflectorClass(BeeModel.class);
    public static ReflectorFields ModelBee_ModelRenderers = new ReflectorFields(ModelBee, ModelRenderer.class, 10);
    public static ReflectorClass ModelBlaze = new ReflectorClass(BlazeModel.class);
    public static ReflectorField ModelBlaze_blazeHead = new ReflectorField(ModelBlaze, ModelRenderer.class);
    public static ReflectorField ModelBlaze_blazeSticks = new ReflectorField(ModelBlaze, ModelRenderer[].class);
    public static ReflectorClass ModelBoar = new ReflectorClass(BoarModel.class);
    public static ReflectorFields ModelBoar_ModelRenderers = new ReflectorFields(ModelBoar, ModelRenderer.class, 9);
    public static ReflectorClass ModelBook = new ReflectorClass(BookModel.class);
    public static ReflectorFields ModelBook_ModelRenderers = new ReflectorFields(ModelBook, ModelRenderer.class, 7);
    public static ReflectorField ModelBook_bookParts = new ReflectorField(ModelBook, List.class);
    public static ReflectorClass ModelChicken = new ReflectorClass(ChickenModel.class);
    public static ReflectorFields ModelChicken_ModelRenderers = new ReflectorFields(ModelChicken, ModelRenderer.class, 8);
    public static ReflectorClass ModelCod = new ReflectorClass(CodModel.class);
    public static ReflectorFields ModelCod_ModelRenderers = new ReflectorFields(ModelCod, ModelRenderer.class, 7);
    public static ReflectorClass ModelCreeper = new ReflectorClass(CreeperModel.class);
    public static ReflectorFields ModelCreeper_ModelRenderers = new ReflectorFields(ModelCreeper, ModelRenderer.class, 7);
    public static ReflectorClass ModelDragon = new ReflectorClass(EnderDragonRenderer.EnderDragonModel.class);
    public static ReflectorFields ModelDragon_ModelRenderers = new ReflectorFields(ModelDragon, ModelRenderer.class, 20);
    public static ReflectorClass RenderEnderCrystal = new ReflectorClass(EnderCrystalRenderer.class);
    public static ReflectorFields RenderEnderCrystal_modelRenderers = new ReflectorFields(RenderEnderCrystal, ModelRenderer.class, 3);
    public static ReflectorClass ModelEnderMite = new ReflectorClass(EndermiteModel.class);
    public static ReflectorField ModelEnderMite_bodyParts = new ReflectorField(ModelEnderMite, ModelRenderer[].class);
    public static ReflectorClass ModelEvokerFangs = new ReflectorClass(EvokerFangsModel.class);
    public static ReflectorFields ModelEvokerFangs_ModelRenderers = new ReflectorFields(ModelEvokerFangs, ModelRenderer.class, 3);
    public static ReflectorClass ModelGuardian = new ReflectorClass(GuardianModel.class);
    public static ReflectorField ModelGuardian_body = new ReflectorField(ModelGuardian, ModelRenderer.class, 0);
    public static ReflectorField ModelGuardian_eye = new ReflectorField(ModelGuardian, ModelRenderer.class, 1);
    public static ReflectorField ModelGuardian_spines = new ReflectorField(ModelGuardian, ModelRenderer[].class, 0);
    public static ReflectorField ModelGuardian_tail = new ReflectorField(ModelGuardian, ModelRenderer[].class, 1);
    public static ReflectorClass ModelDragonHead = new ReflectorClass(DragonHeadModel.class);
    public static ReflectorField ModelDragonHead_head = new ReflectorField(ModelDragonHead, ModelRenderer.class, 0);
    public static ReflectorField ModelDragonHead_jaw = new ReflectorField(ModelDragonHead, ModelRenderer.class, 1);
    public static ReflectorClass ModelHorse = new ReflectorClass(HorseModel.class);
    public static ReflectorFields ModelHorse_ModelRenderers = new ReflectorFields(ModelHorse, ModelRenderer.class, 11);
    public static ReflectorClass ModelHorseChests = new ReflectorClass(HorseArmorChestsModel.class);
    public static ReflectorFields ModelHorseChests_ModelRenderers = new ReflectorFields(ModelHorseChests, ModelRenderer.class, 2);
    public static ReflectorClass ModelIllager = new ReflectorClass(IllagerModel.class);
    public static ReflectorFields ModelIllager_ModelRenderers = new ReflectorFields(ModelIllager, ModelRenderer.class, 8);
    public static ReflectorClass ModelIronGolem = new ReflectorClass(IronGolemModel.class);
    public static ReflectorFields ModelIronGolem_ModelRenderers = new ReflectorFields(ModelIronGolem, ModelRenderer.class, 6);
    public static ReflectorClass ModelFox = new ReflectorClass(FoxModel.class);
    public static ReflectorFields ModelFox_ModelRenderers = new ReflectorFields(ModelFox, ModelRenderer.class, 10);
    public static ReflectorClass ModelLeashKnot = new ReflectorClass(LeashKnotModel.class);
    public static ReflectorField ModelLeashKnot_knotRenderer = new ReflectorField(ModelLeashKnot, ModelRenderer.class);
    public static ReflectorClass RenderLeashKnot = new ReflectorClass(LeashKnotRenderer.class);
    public static ReflectorField RenderLeashKnot_leashKnotModel = new ReflectorField(RenderLeashKnot, LeashKnotModel.class);
    public static ReflectorClass ModelLlama = new ReflectorClass(LlamaModel.class);
    public static ReflectorFields ModelLlama_ModelRenderers = new ReflectorFields(ModelLlama, ModelRenderer.class, 8);
    public static ReflectorClass ModelLlamaSpit = new ReflectorClass(LlamaSpitModel.class);
    public static ReflectorField ModelLlamaSpit_renderer = new ReflectorField(ModelLlamaSpit, ModelRenderer.class);
    public static ReflectorClass ModelMinecart = new ReflectorClass(MinecartModel.class);
    public static ReflectorField ModelMinecart_sideModels = new ReflectorField(ModelMinecart, ModelRenderer[].class);
    public static ReflectorClass ModelMagmaCube = new ReflectorClass(MagmaCubeModel.class);
    public static ReflectorField ModelMagmaCube_core = new ReflectorField(ModelMagmaCube, ModelRenderer.class);
    public static ReflectorField ModelMagmaCube_segments = new ReflectorField(ModelMagmaCube, ModelRenderer[].class);
    public static ReflectorClass ModelOcelot = new ReflectorClass(OcelotModel.class);
    public static ReflectorFields ModelOcelot_ModelRenderers = new ReflectorFields(ModelOcelot, ModelRenderer.class, 8);
    public static ReflectorClass ModelPhantom = new ReflectorClass(PhantomModel.class);
    public static ReflectorFields ModelPhantom_ModelRenderers = new ReflectorFields(ModelPhantom, ModelRenderer.class, 7);
    public static ReflectorClass ModelParrot = new ReflectorClass(ParrotModel.class);
    public static ReflectorFields ModelParrot_ModelRenderers = new ReflectorFields(ModelParrot, ModelRenderer.class, 11);
    public static ReflectorClass ModelPufferFishBig = new ReflectorClass(PufferFishBigModel.class);
    public static ReflectorFields ModelPufferFishBig_ModelRenderers = new ReflectorFields(ModelPufferFishBig, ModelRenderer.class, 13);
    public static ReflectorClass ModelPufferFishMedium = new ReflectorClass(PufferFishMediumModel.class);
    public static ReflectorFields ModelPufferFishMedium_ModelRenderers = new ReflectorFields(ModelPufferFishMedium, ModelRenderer.class, 11);
    public static ReflectorClass ModelPufferFishSmall = new ReflectorClass(PufferFishSmallModel.class);
    public static ReflectorFields ModelPufferFishSmall_ModelRenderers = new ReflectorFields(ModelPufferFishSmall, ModelRenderer.class, 6);
    public static ReflectorClass ModelQuadruped = new ReflectorClass(QuadrupedModel.class);
    public static ReflectorFields ModelQuadruped_ModelRenderers = new ReflectorFields(ModelQuadruped, ModelRenderer.class, 6);
    public static ReflectorClass ModelRabbit = new ReflectorClass(RabbitModel.class);
    public static ReflectorFields ModelRabbit_ModelRenderers = new ReflectorFields(ModelRabbit, ModelRenderer.class, 12);
    public static ReflectorClass ModelRavager = new ReflectorClass(RavagerModel.class);
    public static ReflectorFields ModelRavager_ModelRenderers = new ReflectorFields(ModelRavager, ModelRenderer.class, 8);
    public static ReflectorClass ModelSalmon = new ReflectorClass(SalmonModel.class);
    public static ReflectorFields ModelSalmon_ModelRenderers = new ReflectorFields(ModelSalmon, ModelRenderer.class, 5);
    public static ReflectorClass ModelShulker = new ReflectorClass(ShulkerModel.class);
    public static ReflectorFields ModelShulker_ModelRenderers = new ReflectorFields(ModelShulker, ModelRenderer.class, 3);
    public static ReflectorClass ModelShulkerBullet = new ReflectorClass(ShulkerBulletModel.class);
    public static ReflectorField ModelShulkerBullet_renderer = new ReflectorField(ModelShulkerBullet, ModelRenderer.class);
    public static ReflectorClass ModelSign = new ReflectorClass(SignTileEntityRenderer.SignModel.class);
    public static ReflectorFields ModelSign_ModelRenderers = new ReflectorFields(ModelSign, ModelRenderer.class, 2);
    public static ReflectorClass ModelGenericHead = new ReflectorClass(GenericHeadModel.class);
    public static ReflectorField ModelGenericHead_skeletonHead = new ReflectorField(ModelGenericHead, ModelRenderer.class);
    public static ReflectorClass ModelSilverfish = new ReflectorClass(SilverfishModel.class);
    public static ReflectorField ModelSilverfish_bodyParts = new ReflectorField(ModelSilverfish, ModelRenderer[].class, 0);
    public static ReflectorField ModelSilverfish_wingParts = new ReflectorField(ModelSilverfish, ModelRenderer[].class, 1);
    public static ReflectorClass ModelSlime = new ReflectorClass(SlimeModel.class);
    public static ReflectorFields ModelSlime_ModelRenderers = new ReflectorFields(ModelSlime, ModelRenderer.class, 4);
    public static ReflectorClass ModelSnowman = new ReflectorClass(SnowManModel.class);
    public static ReflectorFields ModelSnowman_ModelRenderers = new ReflectorFields(ModelSnowman, ModelRenderer.class, 5);
    public static ReflectorClass ModelSpider = new ReflectorClass(SpiderModel.class);
    public static ReflectorFields ModelSpider_ModelRenderers = new ReflectorFields(ModelSpider, ModelRenderer.class, 11);
    public static ReflectorClass ModelSquid = new ReflectorClass(SquidModel.class);
    public static ReflectorField ModelSquid_body = new ReflectorField(ModelSquid, ModelRenderer.class);
    public static ReflectorField ModelSquid_tentacles = new ReflectorField(ModelSquid, ModelRenderer[].class);
    public static ReflectorClass ModelStrider = new ReflectorClass(StriderModel.class);
    public static ReflectorFields ModelStrider_ModelRenderers = new ReflectorFields(ModelStrider, ModelRenderer.class, 9);
    public static ReflectorClass ModelTrident = new ReflectorClass(TridentModel.class);
    public static ReflectorField ModelTrident_tridentRenderer = new ReflectorField(ModelTrident, ModelRenderer.class);
    public static ReflectorClass ModelTropicalFishA = new ReflectorClass(TropicalFishAModel.class);
    public static ReflectorFields ModelTropicalFishA_ModelRenderers = new ReflectorFields(ModelTropicalFishA, ModelRenderer.class, 5);
    public static ReflectorClass ModelTropicalFishB = new ReflectorClass(TropicalFishBModel.class);
    public static ReflectorFields ModelTropicalFishB_ModelRenderers = new ReflectorFields(ModelTropicalFishB, ModelRenderer.class, 6);
    public static ReflectorClass ModelTurtle = new ReflectorClass(TurtleModel.class);
    public static ReflectorField ModelTurtle_body2 = new ReflectorField(ModelTurtle, ModelRenderer.class, 0);
    public static ReflectorClass ModelVex = new ReflectorClass(VexModel.class);
    public static ReflectorField ModelVex_leftWing = new ReflectorField(ModelVex, ModelRenderer.class, 0);
    public static ReflectorField ModelVex_rightWing = new ReflectorField(ModelVex, ModelRenderer.class, 1);
    public static ReflectorClass ModelVillager = new ReflectorClass(VillagerModel.class);
    public static ReflectorFields ModelVillager_ModelRenderers = new ReflectorFields(ModelVillager, ModelRenderer.class, 9);
    public static ReflectorClass ModelWitch = new ReflectorClass(WitchModel.class);
    public static ReflectorField ModelWitch_mole = new ReflectorField(ModelWitch, ModelRenderer.class, 0);
    public static ReflectorClass ModelWither = new ReflectorClass(WitherModel.class);
    public static ReflectorField ModelWither_bodyParts = new ReflectorField(ModelWither, ModelRenderer[].class, 0);
    public static ReflectorField ModelWither_heads = new ReflectorField(ModelWither, ModelRenderer[].class, 1);
    public static ReflectorClass ModelWolf = new ReflectorClass(WolfModel.class);
    public static ReflectorFields ModelWolf_ModelRenderers = new ReflectorFields(ModelWolf, ModelRenderer.class, 10);
    public static ReflectorClass OptiFineResourceLocator = ReflectorForge.getReflectorClassOptiFineResourceLocator();
    public static ReflectorMethod OptiFineResourceLocator_getOptiFineResourceStream = new ReflectorMethod(OptiFineResourceLocator, "getOptiFineResourceStream");
    public static ReflectorClass RenderBoat = new ReflectorClass(BoatRenderer.class);
    public static ReflectorField RenderBoat_modelBoat = new ReflectorField(RenderBoat, BoatModel.class);
    public static ReflectorClass RenderEvokerFangs = new ReflectorClass(EvokerFangsRenderer.class);
    public static ReflectorField RenderEvokerFangs_model = new ReflectorField(RenderEvokerFangs, EvokerFangsModel.class);
    public static ReflectorClass RenderLlamaSpit = new ReflectorClass(LlamaSpitRenderer.class);
    public static ReflectorField RenderLlamaSpit_model = new ReflectorField(RenderLlamaSpit, LlamaSpitModel.class);
    public static ReflectorClass RenderPufferfish = new ReflectorClass(PufferfishRenderer.class);
    public static ReflectorField RenderPufferfish_modelSmall = new ReflectorField(RenderPufferfish, PufferFishSmallModel.class);
    public static ReflectorField RenderPufferfish_modelMedium = new ReflectorField(RenderPufferfish, PufferFishMediumModel.class);
    public static ReflectorField RenderPufferfish_modelBig = new ReflectorField(RenderPufferfish, PufferFishBigModel.class);
    public static ReflectorClass RenderMinecart = new ReflectorClass(MinecartRenderer.class);
    public static ReflectorField RenderMinecart_modelMinecart = new ReflectorField(RenderMinecart, EntityModel.class);
    public static ReflectorClass RenderShulkerBullet = new ReflectorClass(ShulkerBulletRenderer.class);
    public static ReflectorField RenderShulkerBullet_model = new ReflectorField(RenderShulkerBullet, ShulkerBulletModel.class);
    public static ReflectorClass RenderTrident = new ReflectorClass(TridentRenderer.class);
    public static ReflectorField RenderTrident_modelTrident = new ReflectorField(RenderTrident, TridentModel.class);
    public static ReflectorClass RenderTropicalFish = new ReflectorClass(TropicalFishRenderer.class);
    public static ReflectorField RenderTropicalFish_modelA = new ReflectorField(RenderTropicalFish, TropicalFishAModel.class);
    public static ReflectorField RenderTropicalFish_modelB = new ReflectorField(RenderTropicalFish, TropicalFishBModel.class);
    public static ReflectorClass RenderWitherSkull = new ReflectorClass(WitherSkullRenderer.class);
    public static ReflectorField RenderWitherSkull_model = new ReflectorField(RenderWitherSkull, GenericHeadModel.class);
    public static ReflectorClass TileEntityBannerRenderer = new ReflectorClass(BannerTileEntityRenderer.class);
    public static ReflectorFields TileEntityBannerRenderer_modelRenderers = new ReflectorFields(TileEntityBannerRenderer, ModelRenderer.class, 3);
    public static ReflectorClass TileEntityBedRenderer = new ReflectorClass(BedTileEntityRenderer.class);
    public static ReflectorField TileEntityBedRenderer_headModel = new ReflectorField(TileEntityBedRenderer, ModelRenderer.class, 0);
    public static ReflectorField TileEntityBedRenderer_footModel = new ReflectorField(TileEntityBedRenderer, ModelRenderer.class, 1);
    public static ReflectorField TileEntityBedRenderer_legModels = new ReflectorField(TileEntityBedRenderer, ModelRenderer[].class);
    public static ReflectorClass TileEntityBellRenderer = new ReflectorClass(BellTileEntityRenderer.class);
    public static ReflectorField TileEntityBellRenderer_modelRenderer = new ReflectorField(TileEntityBellRenderer, ModelRenderer.class);
    public static ReflectorClass TileEntityBeacon = new ReflectorClass(BeaconTileEntity.class);
    public static ReflectorField TileEntityBeacon_customName = new ReflectorField(TileEntityBeacon, ITextComponent.class);
    public static ReflectorClass TileEntityChestRenderer = new ReflectorClass(ChestTileEntityRenderer.class);
    public static ReflectorFields TileEntityChestRenderer_modelRenderers = new ReflectorFields(TileEntityChestRenderer, ModelRenderer.class, 9);
    public static ReflectorClass TileEntityConduitRenderer = new ReflectorClass(ConduitTileEntityRenderer.class);
    public static ReflectorFields TileEntityConduitRenderer_modelRenderers = new ReflectorFields(TileEntityConduitRenderer, ModelRenderer.class, 4);
    public static ReflectorClass TileEntityEnchantmentTableRenderer = new ReflectorClass(EnchantmentTableTileEntityRenderer.class);
    public static ReflectorField TileEntityEnchantmentTableRenderer_modelBook = new ReflectorField(TileEntityEnchantmentTableRenderer, BookModel.class);
    public static ReflectorClass TileEntityLecternRenderer = new ReflectorClass(LecternTileEntityRenderer.class);
    public static ReflectorField TileEntityLecternRenderer_modelBook = new ReflectorField(TileEntityLecternRenderer, BookModel.class);
    public static ReflectorClass TileEntityShulkerBoxRenderer = new ReflectorClass(ShulkerBoxTileEntityRenderer.class);
    public static ReflectorField TileEntityShulkerBoxRenderer_model = new ReflectorField(TileEntityShulkerBoxRenderer, ShulkerModel.class);
    public static ReflectorClass TileEntitySignRenderer = new ReflectorClass(SignTileEntityRenderer.class);
    public static ReflectorField TileEntitySignRenderer_model = new ReflectorField(TileEntitySignRenderer, SignTileEntityRenderer.SignModel.class);
    public static ReflectorClass TileEntitySkullRenderer = new ReflectorClass(SkullTileEntityRenderer.class);
    public static ReflectorField TileEntitySkullRenderer_MODELS = new ReflectorField(TileEntitySkullRenderer, Map.class, 0);

    public static void callVoid(ReflectorMethod refMethod, Object... params)
    {
        try
        {
            Method method = refMethod.getTargetMethod();

            if (method == null)
            {
                return;
            }

            method.invoke((Object)null, params);
        }
        catch (Throwable throwable)
        {
            handleException(throwable, (Object)null, refMethod, params);
        }
    }

    public static boolean callBoolean(ReflectorMethod refMethod, Object... params)
    {
        try
        {
            Method method = refMethod.getTargetMethod();

            if (method == null)
            {
                return false;
            }
            else
            {
                Boolean obool = (Boolean)method.invoke((Object)null, params);
                return obool;
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, (Object)null, refMethod, params);
            return false;
        }
    }

    public static int callInt(ReflectorMethod refMethod, Object... params)
    {
        try
        {
            Method method = refMethod.getTargetMethod();

            if (method == null)
            {
                return 0;
            }
            else
            {
                Integer integer = (Integer)method.invoke((Object)null, params);
                return integer;
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, (Object)null, refMethod, params);
            return 0;
        }
    }

    public static long callLong(ReflectorMethod refMethod, Object... params)
    {
        try
        {
            Method method = refMethod.getTargetMethod();

            if (method == null)
            {
                return 0L;
            }
            else
            {
                Long olong = (Long)method.invoke((Object)null, params);
                return olong;
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, (Object)null, refMethod, params);
            return 0L;
        }
    }

    public static float callFloat(ReflectorMethod refMethod, Object... params)
    {
        try
        {
            Method method = refMethod.getTargetMethod();

            if (method == null)
            {
                return 0.0F;
            }
            else
            {
                Float f = (Float)method.invoke((Object)null, params);
                return f;
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, (Object)null, refMethod, params);
            return 0.0F;
        }
    }

    public static double callDouble(ReflectorMethod refMethod, Object... params)
    {
        try
        {
            Method method = refMethod.getTargetMethod();

            if (method == null)
            {
                return 0.0D;
            }
            else
            {
                Double d0 = (Double)method.invoke((Object)null, params);
                return d0;
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, (Object)null, refMethod, params);
            return 0.0D;
        }
    }

    public static String callString(ReflectorMethod refMethod, Object... params)
    {
        try
        {
            Method method = refMethod.getTargetMethod();
            return method == null ? null : (String)method.invoke((Object)null, params);
        }
        catch (Throwable throwable)
        {
            handleException(throwable, (Object)null, refMethod, params);
            return null;
        }
    }

    public static Object call(ReflectorMethod refMethod, Object... params)
    {
        try
        {
            Method method = refMethod.getTargetMethod();
            return method == null ? null : method.invoke((Object)null, params);
        }
        catch (Throwable throwable)
        {
            handleException(throwable, (Object)null, refMethod, params);
            return null;
        }
    }

    public static void callVoid(Object obj, ReflectorMethod refMethod, Object... params)
    {
        try
        {
            if (obj == null)
            {
                return;
            }

            Method method = refMethod.getTargetMethod();

            if (method == null)
            {
                return;
            }

            method.invoke(obj, params);
        }
        catch (Throwable throwable)
        {
            handleException(throwable, obj, refMethod, params);
        }
    }

    public static boolean callBoolean(Object obj, ReflectorMethod refMethod, Object... params)
    {
        try
        {
            Method method = refMethod.getTargetMethod();

            if (method == null)
            {
                return false;
            }
            else
            {
                Boolean obool = (Boolean)method.invoke(obj, params);
                return obool;
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, obj, refMethod, params);
            return false;
        }
    }

    public static int callInt(Object obj, ReflectorMethod refMethod, Object... params)
    {
        try
        {
            Method method = refMethod.getTargetMethod();

            if (method == null)
            {
                return 0;
            }
            else
            {
                Integer integer = (Integer)method.invoke(obj, params);
                return integer;
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, obj, refMethod, params);
            return 0;
        }
    }

    public static long callLong(Object obj, ReflectorMethod refMethod, Object... params)
    {
        try
        {
            Method method = refMethod.getTargetMethod();

            if (method == null)
            {
                return 0L;
            }
            else
            {
                Long olong = (Long)method.invoke(obj, params);
                return olong;
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, obj, refMethod, params);
            return 0L;
        }
    }

    public static float callFloat(Object obj, ReflectorMethod refMethod, Object... params)
    {
        try
        {
            Method method = refMethod.getTargetMethod();

            if (method == null)
            {
                return 0.0F;
            }
            else
            {
                Float f = (Float)method.invoke(obj, params);
                return f;
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, obj, refMethod, params);
            return 0.0F;
        }
    }

    public static double callDouble(Object obj, ReflectorMethod refMethod, Object... params)
    {
        try
        {
            Method method = refMethod.getTargetMethod();

            if (method == null)
            {
                return 0.0D;
            }
            else
            {
                Double d0 = (Double)method.invoke(obj, params);
                return d0;
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, obj, refMethod, params);
            return 0.0D;
        }
    }

    public static Object call(Object obj, ReflectorMethod refMethod, Object... params)
    {
        try
        {
            Method method = refMethod.getTargetMethod();
            return method == null ? null : method.invoke(obj, params);
        }
        catch (Throwable throwable)
        {
            handleException(throwable, obj, refMethod, params);
            return null;
        }
    }

    public static Object getFieldValue(ReflectorField refField)
    {
        return getFieldValue((Object)null, refField);
    }

    public static Object getFieldValue(Object obj, ReflectorField refField)
    {
        try
        {
            Field field = refField.getTargetField();
            return field == null ? null : field.get(obj);
        }
        catch (Throwable throwable)
        {
            Log.error("", throwable);
            return null;
        }
    }

    public static boolean getFieldValueBoolean(Object obj, ReflectorField refField, boolean def)
    {
        try
        {
            Field field = refField.getTargetField();
            return field == null ? def : field.getBoolean(obj);
        }
        catch (Throwable throwable)
        {
            Log.error("", throwable);
            return def;
        }
    }

    public static Object getFieldValue(ReflectorFields refFields, int index)
    {
        ReflectorField reflectorfield = refFields.getReflectorField(index);
        return reflectorfield == null ? null : getFieldValue(reflectorfield);
    }

    public static Object getFieldValue(Object obj, ReflectorFields refFields, int index)
    {
        ReflectorField reflectorfield = refFields.getReflectorField(index);
        return reflectorfield == null ? null : getFieldValue(obj, reflectorfield);
    }

    public static float getFieldValueFloat(Object obj, ReflectorField refField, float def)
    {
        try
        {
            Field field = refField.getTargetField();
            return field == null ? def : field.getFloat(obj);
        }
        catch (Throwable throwable)
        {
            Log.error("", throwable);
            return def;
        }
    }

    public static int getFieldValueInt(ReflectorField refField, int def)
    {
        return getFieldValueInt((Object)null, refField, def);
    }

    public static int getFieldValueInt(Object obj, ReflectorField refField, int def)
    {
        try
        {
            Field field = refField.getTargetField();
            return field == null ? def : field.getInt(obj);
        }
        catch (Throwable throwable)
        {
            Log.error("", throwable);
            return def;
        }
    }

    public static long getFieldValueLong(Object obj, ReflectorField refField, long def)
    {
        try
        {
            Field field = refField.getTargetField();
            return field == null ? def : field.getLong(obj);
        }
        catch (Throwable throwable)
        {
            Log.error("", throwable);
            return def;
        }
    }

    public static boolean setFieldValue(ReflectorField refField, Object value)
    {
        return setFieldValue((Object)null, refField, value);
    }

    public static boolean setFieldValue(Object obj, ReflectorFields refFields, int index, Object value)
    {
        ReflectorField reflectorfield = refFields.getReflectorField(index);

        if (reflectorfield == null)
        {
            return false;
        }
        else
        {
            setFieldValue(obj, reflectorfield, value);
            return true;
        }
    }

    public static boolean setFieldValue(Object obj, ReflectorField refField, Object value)
    {
        try
        {
            Field field = refField.getTargetField();

            if (field == null)
            {
                return false;
            }
            else
            {
                field.set(obj, value);
                return true;
            }
        }
        catch (Throwable throwable)
        {
            Log.error("", throwable);
            return false;
        }
    }

    public static boolean setFieldValueInt(ReflectorField refField, int value)
    {
        return setFieldValueInt((Object)null, refField, value);
    }

    public static boolean setFieldValueInt(Object obj, ReflectorField refField, int value)
    {
        try
        {
            Field field = refField.getTargetField();

            if (field == null)
            {
                return false;
            }
            else
            {
                field.setInt(obj, value);
                return true;
            }
        }
        catch (Throwable throwable)
        {
            Log.error("", throwable);
            return false;
        }
    }

    public static Object newInstance(ReflectorConstructor constr, Object... params)
    {
        Constructor constructor = constr.getTargetConstructor();

        if (constructor == null)
        {
            return null;
        }
        else
        {
            try
            {
                return constructor.newInstance(params);
            }
            catch (Throwable throwable)
            {
                handleException(throwable, constr, params);
                return null;
            }
        }
    }

    public static boolean matchesTypes(Class[] pTypes, Class[] cTypes)
    {
        if (pTypes.length != cTypes.length)
        {
            return false;
        }
        else
        {
            for (int i = 0; i < cTypes.length; ++i)
            {
                Class oclass = pTypes[i];
                Class oclass1 = cTypes[i];

                if (oclass != oclass1)
                {
                    return false;
                }
            }

            return true;
        }
    }

    private static void dbgCall(boolean isStatic, String callType, ReflectorMethod refMethod, Object[] params, Object retVal)
    {
        String s = refMethod.getTargetMethod().getDeclaringClass().getName();
        String s1 = refMethod.getTargetMethod().getName();
        String s2 = "";

        if (isStatic)
        {
            s2 = " static";
        }

        Log.dbg(callType + s2 + " " + s + "." + s1 + "(" + ArrayUtils.arrayToString(params) + ") => " + retVal);
    }

    private static void dbgCallVoid(boolean isStatic, String callType, ReflectorMethod refMethod, Object[] params)
    {
        String s = refMethod.getTargetMethod().getDeclaringClass().getName();
        String s1 = refMethod.getTargetMethod().getName();
        String s2 = "";

        if (isStatic)
        {
            s2 = " static";
        }

        Log.dbg(callType + s2 + " " + s + "." + s1 + "(" + ArrayUtils.arrayToString(params) + ")");
    }

    private static void dbgFieldValue(boolean isStatic, String accessType, ReflectorField refField, Object val)
    {
        String s = refField.getTargetField().getDeclaringClass().getName();
        String s1 = refField.getTargetField().getName();
        String s2 = "";

        if (isStatic)
        {
            s2 = " static";
        }

        Log.dbg(accessType + s2 + " " + s + "." + s1 + " => " + val);
    }

    private static void handleException(Throwable e, Object obj, ReflectorMethod refMethod, Object[] params)
    {
        if (e instanceof InvocationTargetException)
        {
            Throwable throwable = e.getCause();

            if (throwable instanceof RuntimeException)
            {
                RuntimeException runtimeexception = (RuntimeException)throwable;
                throw runtimeexception;
            }
            else
            {
                Log.error("", e);
            }
        }
        else
        {
            Log.warn("*** Exception outside of method ***");
            Log.warn("Method deactivated: " + refMethod.getTargetMethod());
            refMethod.deactivate();

            if (e instanceof IllegalArgumentException)
            {
                Log.warn("*** IllegalArgumentException ***");
                Log.warn("Method: " + refMethod.getTargetMethod());
                Log.warn("Object: " + obj);
                Log.warn("Parameter classes: " + ArrayUtils.arrayToString(getClasses(params)));
                Log.warn("Parameters: " + ArrayUtils.arrayToString(params));
            }

            Log.warn("", e);
        }
    }

    private static void handleException(Throwable e, ReflectorConstructor refConstr, Object[] params)
    {
        if (e instanceof InvocationTargetException)
        {
            Log.error("", e);
        }
        else
        {
            Log.warn("*** Exception outside of constructor ***");
            Log.warn("Constructor deactivated: " + refConstr.getTargetConstructor());
            refConstr.deactivate();

            if (e instanceof IllegalArgumentException)
            {
                Log.warn("*** IllegalArgumentException ***");
                Log.warn("Constructor: " + refConstr.getTargetConstructor());
                Log.warn("Parameter classes: " + ArrayUtils.arrayToString(getClasses(params)));
                Log.warn("Parameters: " + ArrayUtils.arrayToString(params));
            }

            Log.warn("", e);
        }
    }

    private static Object[] getClasses(Object[] objs)
    {
        if (objs == null)
        {
            return new Class[0];
        }
        else
        {
            Class[] aclass = new Class[objs.length];

            for (int i = 0; i < aclass.length; ++i)
            {
                Object object = objs[i];

                if (object != null)
                {
                    aclass[i] = object.getClass();
                }
            }

            return aclass;
        }
    }

    private static ReflectorField[] getReflectorFields(ReflectorClass parentClass, Class fieldType, int count)
    {
        ReflectorField[] areflectorfield = new ReflectorField[count];

        for (int i = 0; i < areflectorfield.length; ++i)
        {
            areflectorfield[i] = new ReflectorField(parentClass, fieldType, i);
        }

        return areflectorfield;
    }

    private static boolean registerResolvable(final String str)
    {
        String s = str;
        IResolvable iresolvable = new IResolvable()
        {
            public void resolve()
            {
                Reflector.LOGGER.info("[OptiFine] " + str);
            }
        };
        ReflectorResolver.register(iresolvable);
        return true;
    }
}
