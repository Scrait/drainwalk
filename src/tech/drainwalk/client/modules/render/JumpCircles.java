package tech.drainwalk.client.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import tech.drainwalk.services.animation.Animation;
import tech.drainwalk.services.animation.EasingList;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.*;
import tech.drainwalk.client.service.services.impl.render.GifPlayer;
import tech.drainwalk.services.render.GLService;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.movement.JumpEvent;
import tech.drainwalk.api.impl.events.render.EventRender3D;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.utils.minecraft.CastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class JumpCircles extends Module {
    private final CopyOnWriteArrayList<Circle> circles = new CopyOnWriteArrayList<>();
    private final List<Entity> collectedEntities = new ArrayList<>();
    private final FloatOption speed = new FloatOption("Speed", 3, 2, 10).addIncrementValue(0.1f);
    private final GifPlayer gifPlayer = new GifPlayer("circle", 120, 60);
    private final SelectOption colorMode =
            new SelectOption("Сolor Mode", 0,
                    new SelectOptionValue("Rainbow"),
                    new SelectOptionValue("Client")
            );
    private final MultiOption targets =
            new MultiOption("Targets", new MultiOptionValue("Players", true), new MultiOptionValue("Friends", true), new MultiOptionValue("Self", true));


    public JumpCircles() {
        super("JumpCircles", Category.RENDER);
        register(
                speed,
                colorMode
               // targets
        );
    }



    @EventTarget
    public void onSync(JumpEvent event) {
        if (!(mc.player.isInLava() || mc.player.isInWater() || mc.player.isOnLadder())) {
            this.circles.add(new Circle(mc.player.getPositionVec().add(new Vector3d(0, 0.01F, 0))));
        }
    }
//
//    @EventTarget
//    public void onJump(JumpEvent event) {
//        if (!(mc.player.isInLava() || mc.player.isInWater() || mc.player.isOnLadder())) {
//            this.circles.add(new Circle(mc.player.getPositionVec().add(new Vector3d(0, 0.01F, 0))));
//        }
//    }

    @EventTarget
    public void onRender3D(EventRender3D.PreEntity event) {

//        event.getMatrixStack().push();
//        RenderSystem.depthMask(false);
//        RenderSystem.disableCull();
//
//        final float partTicks = event.getPartialTicks();
//        final float x2 = (float) MathematicUtils.interpolate(mc.player.getPosX(), mc.player.lastTickPosX, partTicks);
//        final float y2 = (float) MathematicUtils.interpolate(mc.player.getPosY(), mc.player.lastTickPosY, partTicks);
//        final float z2 = (float) MathematicUtils.interpolate(mc.player.getPosZ(), mc.player.lastTickPosZ, partTicks);
//
//        event.getMatrixStack().translate(x2 - mc.getRenderManager().info.getProjectedView().getX(), y2 + 0.7f - mc.getRenderManager().info.getProjectedView().getY(), z2 - mc.getRenderManager().info.getProjectedView().getZ());
//        event.getMatrixStack().rotate(Vector3f.XP.rotationDegrees(90.0F));
//        event.getMatrixStack().rotate(Vector3f.ZP.rotationDegrees(mc.player.rotationYaw));
//
//        RenderService.renderImage(event.getMatrixStack(), new ResourceLocation("drainwalk/images/penis.png"), -1, -1, 2, 3);
//
//        RenderSystem.enableCull();
//        RenderSystem.depthMask(true);
//        event.getMatrixStack().pop();

        if (this.circles.isEmpty()) {
            return;
        }

        Collections.reverse(this.circles);

        for (final Circle c : this.circles) {
            final double x = c.getPosition().getX();
            final double y = c.getPosition().getY();
            final double z = c.getPosition().getZ();
            c.animation2.animate(0, 1, 0.05f, EasingList.ELASTIC_OUT, event.getPartialTicks());

            event.getMatrixStack().push();
            RenderSystem.depthMask(false);
            RenderSystem.disableCull();

            event.getMatrixStack().translate(x, y, z);
            event.getMatrixStack().rotate(Vector3f.XP.rotationDegrees(90.0F));
//            event.getMatrixStack().rotate(Vector3f.ZP.rotationDegrees(90));
//            event.getMatrixStack().rotate(Vector3f.ZP.rotationDegrees(mc.player.rotationYaw));

            GLService.INSTANCE.scaleAnimation(event.getMatrixStack(), -0.5f, -0.5f, 1, 1, c.animation2.getAnimationValue());
            //gifPlayer.play(event.getMatrixStack(),  -0.5f, -0.5f, 1, 1);
//            RenderService.drawCircle(event.getMatrixStack(),  0, 0, 0, 360, 0.5f, -1, 4);
//            RenderService.renderRect(event.getMatrixStack(), -0.5f, -0.5f, 1, 0.01f, -1);
            RenderService.drawImage(event.getMatrixStack(), new ResourceLocation("drainwalk/images/logo.png"), -0.5f, -0.5f, 1, 1, ColorService.getColorWithAlpha(-1, MathHelper.clamp(c.animation2.getAnimationValue(), 0, 1)));
            RenderService.drawCircle(event.getMatrixStack(),  0, 0, 0, 360, 0.5f, -1, 4);
//            RenderUtils.drawGlow(-0.5f, -0.5f, 1, 1, 5, ClientColor.panelMain);
            //RenderService.renderRoundedTexture(event.getMatrixStack(), new ResourceLocation("drainwalk/images/deus_mode.png"), -1, -1, 2, 2, 0, 2);
            RenderSystem.enableCull();
            RenderSystem.depthMask(true);
            event.getMatrixStack().pop();
        }

        Collections.reverse(this.circles);
    }

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        circles.removeIf(Circle::update);
    }

    private boolean isValid(final Entity entity) {
        final CastUtil castHelper = new CastUtil();
        if (targets.isSelected("Players")) {
            castHelper.apply(CastUtil.EntityType.PLAYERS);
        }
        if (targets.isSelected("Friends")) {
            castHelper.apply(CastUtil.EntityType.FRIENDS);
        }
        if (targets.isSelected("Self")) {
            castHelper.apply(CastUtil.EntityType.SELF_NO_CHECK);
        }
        return CastUtil.isInstanceof(entity, castHelper.build()) != null;
    }

    private void collectEntities() {
        this.collectedEntities.clear();
        for (final Entity entity : mc.world.getPlayers()) {
            if (true) {
                this.collectedEntities.add(entity);
            }
        }
    }

    private int getColor(final int count) {
        if (colorMode.getValueByIndex(0)) {
            return (int) ColorService.getSkyRainbow(speed.getValue(), count);
        }
        return ColorService.interpolateColorsBackAndForth(speed.getValue().intValue(), count, ClientColor.panelMain, ClientColor.main);
    }

    private class Circle
    {
        private final Vector3d vec;
        public final Animation animation2;
        private long startTime;

        private Circle(final Vector3d vec) {
            this.vec = vec;
            this.animation2 = new Animation();
            startTime = System.currentTimeMillis();
        }

        public boolean update() {
//            animation2.update(true);
            animation2.update(!(System.currentTimeMillis() - startTime > 1000));
            return animation2.getValue() == 0 && animation2.getPrevValue() == 0;
//            return false;
        }

        public Vector3d getPosition() {
            return new Vector3d(vec.x - mc.getRenderManager().info.getProjectedView().getX(),
                    vec.y - mc.getRenderManager().info.getProjectedView().getY(),
                    vec.z - mc.getRenderManager().info.getProjectedView().getZ());
        }
    }

}