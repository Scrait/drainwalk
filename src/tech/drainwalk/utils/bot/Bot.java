package tech.drainwalk.utils.bot;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import tech.drainwalk.utils.Utils;
import tech.drainwalk.utils.minecraft.ChatUtils;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.util.math.MathHelper.wrapDegrees;

public abstract class Bot extends Utils {

    protected int lastState;
    protected int lastAction;
    protected int lastState2;
    protected int lastAction2;
    protected List<Move> moves = new ArrayList<>();

    protected abstract int selectAction(int state);

    protected abstract int selectAction2(int state);

    public abstract void updateStrategy(int yawDelta);

    public abstract void updateStrategy2();

    public int act() {
        final double x = (findTarget().getPositionVec().x - mc.player.getPosX());
        final double z = findTarget().getPositionVec().z - mc.player.getPosZ();
        final int yawToTarget = (int) (((float) wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90)));
        final int yawDelta = (int) wrapDegrees(yawToTarget - mc.player.rotationYaw);
        ChatUtils.addChatMessage("State Yaw -> " + yawDelta);

        int action = selectAction(yawDelta);
        if (lastState != -1) {
            this.moves.add(new Move(lastState, lastAction, yawDelta, 1)); // Add the experience to the history
        }

        if (!(yawDelta > -20 && yawDelta < 20)) {
            updateStrategy(yawDelta);
        }



        ChatUtils.addChatMessage("Action Yaw -> " + action);


        lastState = yawDelta; // Update the last_state with the current state
        lastAction = action;

        return action;
    }

    public int act2() {
        final double x = (findTarget().getPositionVec().x - mc.player.getPosX());
        final double y = findTarget().getPositionVec().y - mc.player.getEyePosition(1).y - 0.5f;
        final double z = findTarget().getPositionVec().z - mc.player.getPosZ();
        final double dst = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
        final int pitchToTarget = (int) -Math.toDegrees(Math.atan2(y, dst));
        final int pitchDelta = (int) (pitchToTarget - mc.player.rotationPitch);
        ChatUtils.addChatMessage("State Pitch -> " + pitchDelta);

        int action = selectAction2(pitchDelta);

        if(lastState2 != -1) {
            this.moves.add(new Move(lastState2, lastAction2, pitchDelta, 1)); // Add the experience to the history
        }

        if (!(pitchDelta > -10 && pitchDelta < 20)) {
            updateStrategy2();
            ChatUtils.addChatMessage("Update Strategy Pitch");
        }



        ChatUtils.addChatMessage("Action Pitch -> " + action);


        lastState2 = pitchDelta; // Update the last_state with the current state
        lastAction2 = action;

        return action;
    }

    private Entity findTarget() {
        final List<LivingEntity> targets = new ArrayList<>();

        for (Entity entity : mc.world.getAllEntities()) {
            if (entity instanceof LivingEntity && entity != mc.player) {
                targets.add((LivingEntity) entity);
            }
        }
        targets.sort((e1, e2) -> {
            final int dst1 = (int) (mc.player.getDistance(e1) * 1000);
            final int dst2 = (int) (mc.player.getDistance(e2) * 1000);
            return dst1 - dst2;
        });

        return targets.isEmpty() ? null : targets.get(0);
    }

    public void reset() {
        lastState = -1;
        lastAction = -1;
        moves.clear();
    }

    public void reset2() {
        lastState2 = -1;
        lastAction2 = -1;
        moves.clear();
    }
}
