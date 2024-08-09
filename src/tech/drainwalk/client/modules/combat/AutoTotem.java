package tech.drainwalk.client.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.CryingObsidianBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.item.minecart.TNTMinecartEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.AirItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CClickWindowPacket;
import net.minecraft.network.play.client.CCloseWindowPacket;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.modules.movement.GuiWalk;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.client.option.options.MultiOption;
import tech.drainwalk.client.option.options.MultiOptionValue;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.packet.EventSendPacket;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.client.font.FontManager;
import tech.drainwalk.utils.inventory.InvenotryUtil;
import tech.drainwalk.utils.movement.MoveUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AutoTotem extends Module {
    private final FloatOption health = new FloatOption("Health", 5.5f, 1.f, 20.f).addIncrementValue(0.1f);
    public final MultiOption checks = new MultiOption("Checks",
            new MultiOptionValue("Absorption", false),
            new MultiOptionValue("Fall", false),
            new MultiOptionValue("Obsidian", false),
            new MultiOptionValue("Explosion", false));
    private final FloatOption obsidianRadius = new FloatOption("Dist. to obs.", 6, 1, 8).addVisibleCondition(() -> checks.isSelected("Obsidian")).addIncrementValue(0.5f);
    private final FloatOption explosionRadius = new FloatOption("Dist. to expl.", 6, 1, 8).addVisibleCondition(() -> checks.isSelected("Explosion")).addIncrementValue(0.5f);
    private final BooleanOption totemCounter = new BooleanOption("Totem Counter", true);
    private final BooleanOption vulkanFix = new BooleanOption("Vulkan Fix", true);
    private int swapBack = -1;
    private long delay = 0;

    public AutoTotem() {
        super("AutoTotem", Category.COMBAT);
        register(
                health,
                checks,
                obsidianRadius,
                explosionRadius,
                totemCounter,
                vulkanFix
        );
    }

    private int foundTotemCount() {
        int count = 0;
        for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                count++;
            }
        }
        return count;
    }


    @EventTarget
    public void onRender2D(EventRender2D event) {
//        if (foundTotemCount() > 0 && totemCounter.getValue()) {
//            GLUtils.INSTANCE.rescale(2);
//            FontManager.SEMI_BOLD_16.drawStringWithShadow(event.getMatrixStack(),foundTotemCount() + "", (event.getMainWindow().getScaledWidthWithoutAutisticMojangIssue(2) / 2f + 19), (event.getMainWindow().getScaledHeightWithoutAutisticMojangIssue(2) / 2f), -1);
//            for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
//                final ItemStack stack = mc.player.inventory.getStackInSlot(i);
//                if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
//                    GlStateManager.pushMatrix();
//                    GlStateManager.disableBlend();
//                    mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, event.getMainWindow().getScaledWidthWithoutAutisticMojangIssue(2) / 2 + 4, event.getMainWindow().getScaledHeightWithoutAutisticMojangIssue(2) / 2 - 7);
//                    GlStateManager.popMatrix();
//                }
//            }
//            GLUtils.INSTANCE.rescaleMC();
//        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        final int slot = InvenotryUtil.getSlotIDFromItem(Items.TOTEM_OF_UNDYING);

        if (vulkanFix.getValue()) mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.STOP_SPRINTING));;
        final boolean handNotNull = !(mc.player.getHeldItemOffhand().getItem() instanceof AirItem);
        if (condition()) {
            if (slot >= 0) {
                if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
                    mc.playerController.windowClick(0, slot, 1, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, 45, 1, ClickType.PICKUP, mc.player);
                    if (handNotNull) {
                        mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
                        if (swapBack == -1) swapBack = slot;
                    }
                    mc.getConnection().sendPacket(new CCloseWindowPacket(0));
                }
            }

        } else if (swapBack >= 0) {
            mc.playerController.windowClick(0, swapBack, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            if (handNotNull) mc.playerController.windowClick(0, swapBack, 0, ClickType.PICKUP, mc.player);
            swapBack = -1;
            mc.getConnection().sendPacket(new CCloseWindowPacket(0));
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        dw.getApiMain().getModuleManager().findByClass(GuiWalk.class).setPause(true);
        if (event.getPacket() instanceof CClickWindowPacket) {
            if (vulkanFix.getValue() && mc.player.isOnGround() && MoveUtils.isMovingSprint() && mc.world.hasNoCollisions(mc.player, mc.player.getBoundingBox().offset(0.0, 0.0656, 0.0))) {
                mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.STOP_SPRINTING));
                mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(mc.player.getPosX(), mc.player.getPosY() + 0.0656, mc.player.getPosZ(), false));
            }
        }
    }

    private boolean condition() {

        final float hp = checks.isSelected("Absorption") ? mc.player.getHealth() + mc.player.getAbsorptionAmount() : mc.player.getHealth();

        if (health.getValue() >= hp) {
            return true;
        }

        if (checks.isSelected("Explosion")) {
            for (Entity entity : mc.world.getAllEntities()) {
                if (entity instanceof EnderCrystalEntity && mc.player.getDistance(entity) <= explosionRadius.getValue()) {
                    return true;
                }
                if (entity instanceof TNTEntity && mc.player.getDistance(entity) <= explosionRadius.getValue()) {
                    return true;
                }
                if (entity instanceof TNTMinecartEntity && mc.player.getDistance(entity) <= explosionRadius.getValue()) {
                    return true;
                }
            }
        }

        if (checks.isSelected("Obsidian")) {
            final BlockPos pos = getSphere(getPlayerPosLocal(), obsidianRadius.getValue(), 6, false, true, 0).stream().filter(this::IsValidBlockPos).min(Comparator.comparing(blockPos -> getDistanceOfEntityToBlock(mc.player, blockPos))).orElse(null);
            return pos != null;
        }

        if (checks.isSelected("Fall")) {
            return mc.player.fallDistance >= 30;
        }

        return false;
    }

    private double getDistanceOfEntityToBlock(final Entity entity, final BlockPos blockPos) {
        return getDistance(entity.getPosX(), entity.getPosY(), entity.getPosZ(), blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }


    private double getDistance(final double n, final double n2, final double n3, final double n4, final double n5, final double n6) {
        final double n7 = n - n4;
        final double n8 = n2 - n5;
        final double n9 = n3 - n6;
        return MathHelper.sqrt(n7 * n7 + n8 * n8 + n9 * n9);
    }

    private boolean IsValidBlockPos(BlockPos pos) {
        final BlockState state = mc.world.getBlockState(pos);
        return state.getBlock() instanceof CryingObsidianBlock;
    }

    private List<BlockPos> getSphere(final BlockPos blockPos, final float n, final int n2, final boolean b, final boolean b2, final int n3) {
        final ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        final int x = blockPos.getX();
        final int y = blockPos.getY();
        final int z = blockPos.getZ();
        for (int n4 = x - (int) n; n4 <= x + n; ++n4) {
            for (int n5 = z - (int) n; n5 <= z + n; ++n5) {
                for (int n6 = b2 ? (y - (int) n) : y; n6 < (b2 ? (y + n) : ((float) (y + n2))); ++n6) {
                    final double n7 = (x - n4) * (x - n4) + (z - n5) * (z - n5) + (b2 ? ((y - n6) * (y - n6)) : 0);
                    if (n7 < n * n && (!b || n7 >= (n - 1.0f) * (n - 1.0f))) {
                        list.add(new BlockPos(n4, n6 + n3, n5));
                    }
                }
            }
        }
        return list;
    }

    private BlockPos getPlayerPosLocal() {
//        if (mc.player == null) {
//            return BlockPos.ORIGIN;
//        }
        return new BlockPos(Math.floor(mc.player.getPosX()), Math.floor(mc.player.getPosY()), Math.floor(mc.player.getPosZ()));
    }
}
