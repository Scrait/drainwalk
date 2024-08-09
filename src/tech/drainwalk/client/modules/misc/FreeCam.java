package tech.drainwalk.client.modules.misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.MainWindow;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.network.play.client.CPlayerPacket;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.api.impl.events.player.EventEntitySync;
import tech.drainwalk.api.impl.events.player.EventLivingUpdate;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.client.font.FontManager;
import tech.drainwalk.services.render.GLService;
import tech.drainwalk.utils.movement.MoveUtils;

public class FreeCam extends Module {

    private final FloatOption speed = new FloatOption("Speed", 2, 0.1f, 2);
    private double oldPosX, oldPosY, oldPosZ;
    private float oldFlySpeed;

    public FreeCam() {
        super("FreeCam", Category.MISC);
        register(speed);
    }

    @Override
    public void onEnable() {
        oldPosX = mc.player.getPosX();
        oldPosY = mc.player.getPosY();
        oldPosZ = mc.player.getPosZ();
        oldFlySpeed = mc.player.abilities.getFlySpeed();
        final RemoteClientPlayerEntity player = new RemoteClientPlayerEntity(mc.world, mc.player.getGameProfile());
        player.copyLocationAndAnglesFrom(mc.player);
        player.rotationYawHead = mc.player.rotationYawHead;
        mc.world.addEntity(1337, player);
    }

    @EventTarget
    public void onSync(EventEntitySync event) {
        if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP != null) {
            if (mc.player.ticksExisted % 10 == 0) {
                mc.player.connection.sendPacket(new CPlayerPacket(mc.player.isOnGround()));
            }
        }
        event.setCancelled(true);
    }

    @EventTarget
    public void onUpdateLiving(EventLivingUpdate event) {
        mc.player.noClip = true;
        mc.player.setOnGround(false);
        MoveUtils.setSpeed(speed.getValue());
        mc.player.abilities.setWalkSpeed(2);
        mc.player.abilities.setFlySpeed(speed.getValue() / 13);
        mc.player.abilities.isFlying = true;
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        MainWindow mainWindow = event.getMainWindow();
        String posY = String.valueOf((int) -(oldPosY - mc.player.getPosY()));
        String plusOrMinusY = !posY.contains("-") && !posY.equals("0") ? "+" : "";
        String clipValue = "vclip: " + plusOrMinusY + posY;
        GLService.INSTANCE.rescale(2);
        FontManager.SEMI_BOLD_16.drawCenteredStringWithShadow(event.getMatrixStack(), clipValue, mainWindow.getScaledWidthWithoutAutisticMojangIssue(2) / 2,
                mainWindow.getScaledHeightWithoutAutisticMojangIssue(2) / 2F + 8, -1);
        GLService.INSTANCE.rescaleMC();
    }

    @Override
    public void onDisable() {
        mc.player.abilities.isFlying = false;
        mc.player.abilities.setFlySpeed(oldFlySpeed);
        mc.player.setPositionAndRotation(oldPosX, oldPosY, oldPosZ, mc.player.rotationYaw,
                mc.player.rotationPitch);
        mc.world.removeEntityFromWorld(1337);
        mc.player.setMotion(0, 0);
    }
}
