package tech.drainwalk.client.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerTryUseItemOnBlockPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.client.option.options.SelectOption;
import tech.drainwalk.client.option.options.SelectOptionValue;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.utils.inventory.InvenotryUtil;

import java.util.ArrayList;
import java.util.List;

public class AnchorAura extends Module {

    private final List<BlockPos> anchors = new ArrayList<>();
    private int ticks = 0;
    private final FloatOption targetRange = new FloatOption("Target Range", 4, 0, 5);
    private final FloatOption maxSelfDamage = new FloatOption("Max Self Damage", 8, 0, 20);
    private final FloatOption minHealth = new FloatOption("Min Health", 15, 0, 20);
    private final BooleanOption absorption = new BooleanOption("Absorption", true);
    private final BooleanOption autoPlace = new BooleanOption("Auto Place", true);
    private final FloatOption placeDelay = new FloatOption("Place Delay", 2, 0, 10);
    private final SelectOption placeMode =
            new SelectOption("Place Mode", 0, new SelectOptionValue("Safe"), new SelectOptionValue("MnePizdec..."));
    private final FloatOption placeRange = new FloatOption("Place Range", 5, 0, 5);
    private final FloatOption breakDelay = new FloatOption("Break Delay", 2, 0, 10);
    private final SelectOption breakMode =
            new SelectOption("Break Mode", 0, new SelectOptionValue("Safe"), new SelectOptionValue("MnePizdec..."));
    private final FloatOption breakRange = new FloatOption("Break Range", 5, 0, 5);

    private int placeDelayLeft, breakDelayLeft;
    private Entity target;

    public AnchorAura() {
        super("AnchorAura", Category.COMBAT);
        register(
                targetRange,
                maxSelfDamage,
                minHealth,
                absorption,
                autoPlace,
                placeDelay,
                placeMode,
                placeRange,
                breakDelay,
                breakMode,
                breakRange
        );
    }

    @Override
    public void onEnable() {
        placeDelayLeft = 0;
        breakDelayLeft = 0;
        target = null;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (healthCheck()) return;

        findTarget();
        if (target == null) return;

        final int anchor = InvenotryUtil.findItemInHotbar(Items.RESPAWN_ANCHOR);
        final int glowStone = InvenotryUtil.findItemInHotbar(Items.GLOWSTONE);

        if (anchor < 0 || glowStone < 0) return;

        if (breakDelayLeft >= breakDelay.getValue()) {
            BlockPos breakPos = findBreakPos(target.getPosition());
            if (breakPos != null) {
                breakDelayLeft = 0;

                /*if (rotationMode.get() == RotationMode.Both || rotationMode.get() == RotationMode.Break) {
                    Rotations.rotate(Rotations.getYaw(breakPos), Rotations.getPitch(breakPos), 50, () -> breakAnchor(breakPos, anchor, glowStone));
                } else */breakAnchor(breakPos, anchor, glowStone);
            }
        }

        if (placeDelayLeft >= placeDelay.getValue() && autoPlace.getValue()) {
            BlockPos placePos = findPlacePos(target.getPosition());

            if (placePos != null) {
                placeDelayLeft = 0;
                place(placePos, anchor, false, 50);
            }
        }

        placeDelayLeft++;
        breakDelayLeft++;
    }

    private boolean place(BlockPos blockPos, int findItemResult, boolean rotate, int rotationPriority) {
        return place(blockPos, findItemResult, rotate, rotationPriority, true);
    }

    private boolean place(BlockPos blockPos, int findItemResult, boolean rotate, int rotationPriority, boolean checkEntities) {
        return place(blockPos, findItemResult, rotate, rotationPriority, true, checkEntities);
    }

    private boolean place(BlockPos blockPos, int findItemResult, boolean rotate, int rotationPriority, boolean swingHand, boolean checkEntities) {
        return place(blockPos, findItemResult, rotate, rotationPriority, swingHand, checkEntities, true);
    }

    private boolean place(BlockPos blockPos, int findItemResult, boolean rotate, int rotationPriority, boolean swingHand, boolean checkEntities, boolean swapBack) {
        return place(blockPos, Hand.MAIN_HAND, findItemResult, rotate, rotationPriority, swingHand, checkEntities, swapBack);
    }


    private boolean place(BlockPos blockPos, Hand hand, int slot, boolean rotate, int rotationPriority, boolean swingHand, boolean checkEntities, boolean swapBack) {
        if (slot < 0 || slot > 8) return false;
        if (!canPlace(blockPos, checkEntities)) return false;

        Vector3d hitPos = Vector3d.copyCentered(blockPos);

        BlockPos neighbour;
        Direction side = getPlaceSide(blockPos);

        if (side == null) {
            side = Direction.UP;
            neighbour = blockPos;
        } else {
            neighbour = blockPos.offset(side.getOpposite());
            hitPos.add(side.getXOffset() * 0.5, side.getYOffset() * 0.5, side.getZOffset() * 0.5);
        }

        BlockRayTraceResult bhr = new BlockRayTraceResult(hitPos, side, neighbour, false);
        interact(bhr, hand, true);
        return true;
    }

    private void interact(BlockRayTraceResult blockHitResult, Hand hand, boolean swing) {
        boolean wasSneaking = mc.player.isSneaking();
        mc.player.setSneaking(false);

        mc.player.connection.sendPacket(new CPlayerTryUseItemOnBlockPacket(Hand.MAIN_HAND, blockHitResult));

        mc.player.swingArm(hand);

        mc.player.setSneaking(wasSneaking);
    }

    private Direction getPlaceSide(BlockPos blockPos) {
        for (Direction side : Direction.values()) {
            BlockPos neighbor = blockPos.offset(side);
            Direction side2 = side.getOpposite();

            BlockState state = mc.world.getBlockState(neighbor);

            // Check if neighbour isn't empty
            if (state.isAir() || isClickable(state.getBlock())) continue;

            // Check if neighbour is a fluid
            if (!state.getFluidState().isEmpty()) continue;

            return side2;
        }

        return null;
    }

    private boolean isClickable(Block block) {
        return block instanceof CraftingTableBlock
                || block instanceof AnvilBlock
                || block instanceof AbstractButtonBlock
                || block instanceof AbstractPressurePlateBlock
                || block instanceof BedBlock
                || block instanceof FenceGateBlock
                || block instanceof DoorBlock
                || block instanceof NoteBlock;
    }


    private boolean canPlace(BlockPos blockPos, boolean checkEntities) {
        if (blockPos == null) return false;

        // Check y level
        if (!World.isValid(blockPos)) return false;


        // Check if intersects entities
        return !checkEntities || mc.world.placedBlockCollides(Blocks.OBSIDIAN.getDefaultState(), blockPos, ISelectionContext.dummy());
    }

    private boolean canPlace(BlockPos blockPos) {
        return canPlace(blockPos, true);
    }

    private void findTarget() {
        final List<LivingEntity> targets = new ArrayList<>();

        for (Entity entity : mc.world.getAllEntities()) {
            if (entity instanceof LivingEntity && isTargetValid((LivingEntity) entity)) {
                targets.add((LivingEntity) entity);
            }
        }
        targets.sort((e1, e2) -> {
            final int dst1 = (int) (mc.player.getDistance(e1) * 1000);
            final int dst2 = (int) (mc.player.getDistance(e2) * 1000);
            return dst1 - dst2;
        });

        target = targets.isEmpty() ? null : targets.get(0);
    }

    private boolean isTargetValid(LivingEntity target) {
        if (mc.player.getDistance(target) > targetRange.getValue()) {
            return false;
        }
        if (target == mc.player) {
            return false;
        }
        return true;
    }

    private void breakAnchor(BlockPos pos, int anchor, int glowStone) {
        if (pos == null || mc.world.getBlockState(pos).getBlock() != Blocks.RESPAWN_ANCHOR) return;

        mc.player.setSneaking(false);

        int preSlot = -1;
        preSlot = InvenotryUtil.findItemInHotbar(mc.player.getHeldItemMainhand().getItem());

        mc.player.connection.sendPacket(new CHeldItemChangePacket(glowStone));
        mc.player.inventory.currentItem = glowStone;
        mc.playerController.tick();
        mc.player.connection.sendPacket(new CPlayerTryUseItemOnBlockPacket(Hand.MAIN_HAND, new BlockRayTraceResult(new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), Direction.UP, pos, true)));

        mc.player.connection.sendPacket(new CHeldItemChangePacket(anchor));
        mc.player.inventory.currentItem = anchor;
        mc.playerController.tick();
        mc.player.connection.sendPacket(new CPlayerTryUseItemOnBlockPacket(Hand.MAIN_HAND, new BlockRayTraceResult(new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), Direction.UP, pos, true)));

        mc.player.connection.sendPacket(new CHeldItemChangePacket(preSlot));
        mc.player.inventory.currentItem = preSlot;
        mc.playerController.tick();
    }
    private boolean healthCheck() {
        if (absorption.getValue()) {
            return mc.player.getHealth() + mc.player.getAbsorptionAmount() <= minHealth.getValue();
        } else {
            return mc.player.getHealth() <= minHealth.getValue();
        }
    }

    private BlockPos findPlacePos(BlockPos targetPlacePos) {
        return targetPlacePos.south(1);

    }

    private BlockPos findBreakPos(BlockPos targetPos) {
        if (isValidBreak(targetPos.down())) return targetPos.down();
        else if (isValidBreak(targetPos.up(2))) return targetPos.up(2);
        else if (isValidBreak(targetPos.add(1, 0, 0))) return targetPos.add(1, 0, 0);
        else if (isValidBreak(targetPos.add(-1, 0, 0))) return targetPos.add(-1, 0, 0);
        else if (isValidBreak(targetPos.add(0, 0, 1))) return targetPos.add(0, 0, 1);
        else if (isValidBreak(targetPos.add(0, 0, -1))) return targetPos.add(0, 0, -1);
        else if (isValidBreak(targetPos.add(1, 1, 0))) return targetPos.add(1, 1, 0);
        else if (isValidBreak(targetPos.add(-1, -1, 0))) return targetPos.add(-1, -1, 0);
        else if (isValidBreak(targetPos.add(0, 1, 1))) return targetPos.add(0, 1, 1);
        else if (isValidBreak(targetPos.add(0, 0, -1))) return targetPos.add(0, 0, -1);
        return null;
    }

    private boolean getDamagePlace(BlockPos pos) {
        return true;
    }

    private boolean isValidPlace(BlockPos pos) {
        return (mc.world.getBlockState(pos).isAir() || mc.world.getBlockState(pos).getFluidState().getFluid() instanceof FlowingFluid) && Math.sqrt(mc.player.getPosition().distanceSq(pos)) <= placeRange.getValue() && getDamagePlace(pos);
    }

    private boolean getDamageBreak(BlockPos pos) {
        return true;
    }

    private boolean isValidBreak(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.RESPAWN_ANCHOR && Math.sqrt(mc.player.getPosition().distanceSq(pos)) <= breakRange.getValue() && getDamageBreak(pos);
    }

}
