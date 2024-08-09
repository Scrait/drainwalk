package tech.drainwalk.client.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CConfirmTeleportPacket;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.api.impl.events.packet.EventReceivePacket;
import tech.drainwalk.api.impl.events.player.EventEntitySync;
import tech.drainwalk.utils.movement.MoveUtils;
import tech.drainwalk.utils.time.Timer;

public class Spider extends Module {
    private final Timer timer = new Timer();
    private final FloatOption climbTicks = new FloatOption("Ticks", 2, 0, 10);

    public Spider() {
        super("Spider", Category.MOVEMENT);
        register(climbTicks);
    }

    @EventTarget
    public void onMotion(EventEntitySync event) {
        if (MoveUtils.isMovingSprint() && mc.player.collidedHorizontally && timer.isDelayComplete(climbTicks.getValue() * 100.0F)) {
            event.setOnGround(true);
            mc.player.setOnGround(true);
            mc.player.collidedVertically = true;
            //event.setPosX(mc.player.posX);
            //event.setPosY(mc.player.posY);
            //event.setPosZ(mc.player.posZ);
            mc.player.collidedHorizontally = true;
            mc.player.isAirBorne = true;
            mc.player.jump();
            timer.reset();
        }
    }

    @EventTarget
    public void onReceive(EventReceivePacket event) {
        if (event.getPacket() instanceof SPlayerPositionLookPacket) {
            mc.player.setPosition(((SPlayerPositionLookPacket) event.getPacket()).getX(), ((SPlayerPositionLookPacket) event.getPacket()).getY(), ((SPlayerPositionLookPacket) event.getPacket()).getZ());
            mc.player.connection.sendPacket(new CConfirmTeleportPacket(((SPlayerPositionLookPacket) event.getPacket()).getTeleportId()));
            mc.player.connection.sendPacket(new CPlayerPacket.PositionRotationPacket(((SPlayerPositionLookPacket) event.getPacket()).getX(), ((SPlayerPositionLookPacket) event.getPacket()).getY(), ((SPlayerPositionLookPacket) event.getPacket()).getZ(), ((SPlayerPositionLookPacket) event.getPacket()).getYaw(), ((SPlayerPositionLookPacket) event.getPacket()).getPitch(), false));
            event.setCancelled(true);
            //mc.timer.timerSpeed = 1;
        }
    }
}
