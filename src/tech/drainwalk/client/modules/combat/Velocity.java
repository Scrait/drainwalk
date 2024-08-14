package tech.drainwalk.client.modules.combat;

import net.minecraft.network.play.server.SConfirmTransactionPacket;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.network.play.server.SExplosionPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.SelectOption;
import tech.drainwalk.client.option.options.SelectOptionValue;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.packet.EventReceivePacket;
import com.darkmagician6.eventapi.EventTarget;

public class Velocity extends Module {

    private final SelectOption mode =
            new SelectOption("Mode", 0, new SelectOptionValue("Advanced"), new SelectOptionValue("Grim"));
    private int cancelPacket = 6, resetPersec = 8, grimTCancel = 0, updates = 0;


    public Velocity() {
        super("Velocity", Category.COMBAT);
        register(mode);
        addDescription("Velocity, removes the knock back effect.");
    }

    @Override
    public void onEnable() {
        if (mode.getValueByIndex(1)) grimTCancel = 0;
    }

    @EventTarget
    public void onReceive(EventReceivePacket eventPacket) {
        if (eventPacket.getPacket() instanceof SEntityVelocityPacket) {
            SEntityVelocityPacket sPacketEntityVelocity = (SEntityVelocityPacket)eventPacket.getPacket();
            if (sPacketEntityVelocity.getEntityID() == mc.player.getEntityId()) {
                if (mode.getValueByIndex(0) || grimTCancel != -6) {
                    eventPacket.setCancelled(true);
                }
                if (mode.getValueByIndex(1)) grimTCancel = cancelPacket;
                //mc.player.velocityChanged = false;
            }
        }
        if (eventPacket.getPacket() instanceof SExplosionPacket) {
            eventPacket.setCancelled(true);
            if (mode.getValueByIndex(1)) grimTCancel += cancelPacket;
            //mc.player.velocityChanged = false;
        }
        if (eventPacket.getPacket() instanceof SConfirmTransactionPacket && mode.getValueByIndex(1) && grimTCancel > 0) {
            //SConfirmTransactionPacket sConfirmTransactionPacket = (SConfirmTransactionPacket)eventPacket.getPacket();
            //mc.getConnection().sendPacket(new CConfirmTransactionPacket(sConfirmTransactionPacket.getWindowId(), sConfirmTransactionPacket.getActionNumber(), true));
            //eventPacket.setPacket(new SConfirmTransactionPacket(0, (short) 0, sConfirmTransactionPacket.wasAccepted()));
            eventPacket.setCancelled(true);
            grimTCancel--;
        }
        if (eventPacket.getPacket() instanceof SPlayerPositionLookPacket && mode.getValueByIndex(1)) {
            grimTCancel = -6;
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mode.getValueByIndex(1)) {
            updates++;
            if (resetPersec > 0) {
                if (updates >= 0) {
                    updates = 0;
                    if (grimTCancel > 0){
                        grimTCancel--;
                    }
                }
            }
        }
    }
}
