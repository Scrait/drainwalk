package tech.drainwalk.client.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import lombok.Setter;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.network.play.client.CClickWindowPacket;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CPlayerPacket;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.packet.EventSendPacket;
import tech.drainwalk.utils.movement.MoveUtils;

public class GuiWalk extends Module {
    @Setter
    private boolean pause = false;
    private final BooleanOption reallyworldFix = new BooleanOption("Reallyworld Fix", true);

    public GuiWalk() {
        super("Gui Walk", Category.MOVEMENT);
        addType(Type.SECONDARY);
        register(reallyworldFix);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
//        if (!(mc.currentScreen instanceof net.minecraft.client.gui.GuiChat)) {
//            mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
//            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
//            mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode());
//            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
//            mc.gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode());
//            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
//            mc.gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode());
//            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
//            mc.gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode());
//            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
//            mc.gameSettings.keyBindSprint.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode());
//            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
//        }
        if (mc.currentScreen != null && !(mc.currentScreen instanceof ChatScreen)) {
            KeyBinding[] key = { mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
                    mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight,
                    mc.gameSettings.keyBindSprint, mc.gameSettings.keyBindJump };
            KeyBinding[] array;
            for (int length = (array = key).length, i = 0; i < length; ++i) {
                KeyBinding b = array[i];
                KeyBinding.setKeyBindState(b.getDefault(), InputMappings.isKeyDown(mc.getMainWindow().getHandle(), b.getDefault().getKeyCode()));
            }
            //mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
//        if(pause){
//            pause = false;
//            return;
//        }
        if (event.getPacket() instanceof CClickWindowPacket) {
            if (reallyworldFix.getValue() && mc.player.isOnGround() && MoveUtils.isMovingSprint() && mc.world.hasNoCollisions(mc.player, mc.player.getBoundingBox().offset(0.0, 0.0656, 0.0))) {
                mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.STOP_SPRINTING));
                mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(mc.player.getPosX(), mc.player.getPosY() + 0.0656, mc.player.getPosZ(), false));
            }
        }
    }

//    @Override
//    public void onDisable() {
//        mc.gameSettings.keyBindJump.pressed = false;
//        mc.gameSettings.keyBindForward.pressed = false;
//        mc.gameSettings.keyBindBack.pressed = false;
//        mc.gameSettings.keyBindLeft.pressed = false;
//        mc.gameSettings.keyBindRight.pressed = false;
//        mc.gameSettings.keyBindSprint.pressed = false;
//        super.onDisable();
//    }
//
//    @Override
//    public void onEnable() {
//        super.onEnable();
//        }
}