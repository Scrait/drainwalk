package tech.drainwalk.client.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import lombok.Getter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector2f;
import org.lwjgl.glfw.GLFW;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.*;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.input.MoveInputEvent;
import tech.drainwalk.api.impl.events.movement.JumpEvent;
import tech.drainwalk.api.impl.events.movement.StrafeEvent;
import tech.drainwalk.api.impl.events.player.EventEntitySync;
import tech.drainwalk.services.modules.AuraService;

import java.util.Random;

public class Aura extends Module {

    private final FloatOption distance = new FloatOption("Distance", 3.6f, 1.0f, 6.0f).addIncrementValue(0.1f);
    private final FloatOption preDistance = new FloatOption("PreDistance", 0.4f, 0.0f, 6.0f).addIncrementValue(0.1f);
    private final FloatOption lerpSpeed = new FloatOption("Lerp Speed", 6, 1, 10).addIncrementValue(0.5f);
    private final MultiOption targets =
            new MultiOption("Targets",
                    new MultiOptionValue("Players", true),
                    new MultiOptionValue("Friends", false),
                    new MultiOptionValue("Mobs", false),
                    new MultiOptionValue("Animals", false)
            );
    private final BooleanOption criticals = new BooleanOption("Criticals", true);
    private final BooleanOption ignoreNaked = new BooleanOption("Ignore Naked", true);
    private final BooleanOption moveFix = new BooleanOption("Movement Fix", true);
    private Vector2f currentRotation, targetRotation;
    private final AuraService auraService = new AuraService();

    @Getter
    private LivingEntity target = null;

    public Aura() {
        super("Aura", Category.COMBAT);
        register(
                distance,
                preDistance,
                lerpSpeed,
                targets,
                criticals,
                ignoreNaked,
                moveFix
        );
        addKey(GLFW.GLFW_KEY_R);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        updateTarget();
        if (target == null)  {
            currentRotation = new Vector2f(mc.player.rotationYaw, mc.player.rotationPitch);
            return;
        }
        updateRotation();
        lerpRotation();
        attackTarget();
    }

    @EventTarget
    public void onSync(EventEntitySync event) {
        if (target == null) return;
        mc.player.rotationYawHead = currentRotation.x;
        mc.player.renderYawOffset = currentRotation.x;
        mc.player.rotationPitchHead = currentRotation.y;
        event.setYaw(currentRotation.x);
        event.setPitch(currentRotation.y);
    }

    @EventTarget
    public void onInput(MoveInputEvent event) {
        if (moveFix.getValue() && currentRotation != null && target != null) {
            auraService.fixMovement(event, currentRotation.x);
        }
    }

    @EventTarget
    public void onStrafe(StrafeEvent event) {
        if (moveFix.getValue() && currentRotation != null && target != null) {
            event.setYaw(currentRotation.x);
        }
    }

    @EventTarget
    public void onJump(JumpEvent event) {
        if (moveFix.getValue() && currentRotation != null && target != null) {
            event.setYaw(currentRotation.x);
        }
    }

    private void updateTarget() {
        this.target = auraService.smartSortEntities(target);
    }

    private void updateRotation() {
        targetRotation = auraService.calculateLookAngles(target);
    }

    private void lerpRotation() {
        currentRotation = auraService.applyRotationPatch(
                auraService.smoothRotation(currentRotation, targetRotation, lerpSpeed.getValue() * 5 + new Random().nextFloat(5))
        );
    }

    private void attackTarget() {
        if (auraService.attackCondition(currentRotation, target)) {
            System.out.println("attack");
            mc.playerController.attackEntity(mc.player, target);
            mc.player.swingArm(Hand.MAIN_HAND);
            auraService.randValX.generate();
            auraService.randValY.generate();
        }
    }

    @Override
    public void onEnable() {
        currentRotation = new Vector2f(mc.player.rotationYaw, mc.player.rotationPitch);
    }

    @Override
    public void onDisable() {
        target = null;
    }

}
