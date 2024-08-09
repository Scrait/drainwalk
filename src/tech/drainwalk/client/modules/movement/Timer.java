package tech.drainwalk.client.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.util.math.MathHelper;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.api.impl.events.packet.EventSendPacket;
import tech.drainwalk.api.impl.events.player.EventEntitySync;
import tech.drainwalk.utils.movement.MoveUtils;

public class Timer extends Module {
    public float ticks = 0;
    public boolean active;
    public final FloatOption timerAmount = new FloatOption("Amount", 2f, 1f, 10f);
    public final BooleanOption smart = new BooleanOption("Smart", false);
    private final tech.drainwalk.utils.time.Timer timer = new tech.drainwalk.utils.time.Timer();

    public Timer() {
        super("Timer", Category.MOVEMENT);
        register(
                timerAmount,
                smart
        );
    }

    @EventTarget
    public void onPacket(EventSendPacket eventSendPacket) {
        if (eventSendPacket.getPacket() instanceof CPlayerPacket.PositionPacket || eventSendPacket.getPacket() instanceof CPlayerPacket.PositionRotationPacket) {
            if (ticks <= 30 && !active) {
                if (MoveUtils.isMovingSprint())
                    ticks++;
            }
        }
    }

    @EventTarget
    public void onEntitySync(EventEntitySync eventEntitySync) {
        if (smart.getValue()) {
            if (ticks <= 30 && !active) {
                if (MoveUtils.isMovingSprint())
                    mc.getTimer().timerSpeed = timerAmount.getValue();
                else {
                    mc.getTimer().timerSpeed = 1;
                }
            }
            if (ticks >= 30) {
                mc.getTimer().timerSpeed = 1;
            }

            if (!MoveUtils.isMovingSprint()) {
                ticks -= 1;
            } else {
                if (timer.delay(300, true)) {
                    ticks -= 0.1;
                }
            }

            if (ticks <= 30) {
                active = false;
            }
        } else {
            mc.getTimer().timerSpeed = timerAmount.getValue();
        }
        ticks = MathHelper.clamp(ticks, 0, 100);
    }

    @Override
    public void onDisable() {
        this.mc.getTimer().timerSpeed = 1.0f;
        super.onDisable();
    }
}
