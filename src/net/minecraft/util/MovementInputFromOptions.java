package net.minecraft.util;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.GameSettings;
import tech.drainwalk.api.impl.events.input.MoveInputEvent;

public class MovementInputFromOptions extends MovementInput
{
    private final GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings gameSettingsIn)
    {
        this.gameSettings = gameSettingsIn;
    }

    public void tickMovement(boolean p_225607_1_)
    {
        this.forwardKeyDown = this.gameSettings.keyBindForward.isKeyDown();
        this.backKeyDown = this.gameSettings.keyBindBack.isKeyDown();
        this.leftKeyDown = this.gameSettings.keyBindLeft.isKeyDown();
        this.rightKeyDown = this.gameSettings.keyBindRight.isKeyDown();
        final MoveInputEvent mie = new MoveInputEvent(this.forwardKeyDown == this.backKeyDown ? 0.0F : (this.forwardKeyDown ? 1.0F : -1.0F), this.leftKeyDown == this.rightKeyDown ? 0.0F : (this.leftKeyDown ? 1.0F : -1.0F));
        EventManager.call(mie);
        this.moveForward = mie.getForward();
        this.moveStrafe = mie.getStrafe();
        this.jump = this.gameSettings.keyBindJump.isKeyDown();
        this.sneaking = this.gameSettings.keyBindSneak.isKeyDown();

        if (p_225607_1_)
        {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.moveForward = (float)((double)this.moveForward * 0.3D);
        }
    }
}
