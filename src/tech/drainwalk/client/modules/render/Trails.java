package tech.drainwalk.client.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.*;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.api.impl.events.render.EventRender3D;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.utils.math.MathematicUtils;
import tech.drainwalk.utils.minecraft.CastUtil;

import java.util.ArrayList;
import java.util.List;

public class Trails extends Module {

    private final ArrayList<Point> points = new ArrayList<>();
    private final List<Entity> collectedEntities = new ArrayList<>();
    private final FloatOption speed = new FloatOption("Speed", 3, 2, 10).addIncrementValue(0.1f);
    private final SelectOption colorMode =
            new SelectOption("Сolor Mode", 0,
                    new SelectOptionValue("Rainbow"),
                    new SelectOptionValue("Client")
            );
    private final MultiOption targets =
            new MultiOption("Targets", new MultiOptionValue("Players", true), new MultiOptionValue("Friends", true), new MultiOptionValue("Self", true));

    public Trails() {
        super("Trails", Category.RENDER);
        register(
                speed,
                colorMode
                //targets
        );
    }

    @EventTarget
    public void onRender3D(EventRender3D.PostEntity event) {
        points.removeIf(p -> p.age >= 100);

        final float partTicks = event.getPartialTicks();


        final float x = (float) MathematicUtils.interpolate(mc.player.getPosX(), mc.player.lastTickPosX, partTicks);
        final float y = (float) MathematicUtils.interpolate(mc.player.getPosY(), mc.player.lastTickPosY, partTicks);
        final float z = (float) MathematicUtils.interpolate(mc.player.getPosZ(), mc.player.lastTickPosZ, partTicks);

        points.add(new Point(x, y, z));

        RenderSystem.pushMatrix();
        RenderSystem.depthMask(false);
        RenderSystem.enableDepthTest();
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        RenderSystem.disableTexture();
        RenderSystem.blendFunc(770, 771);
        GL11.glDisable(GL11.GL_CULL_FACE);

        mc.gameRenderer.sukazaebalrabotai(event.getMatrixStack(), event.getPartialTicks());

        for (final Point t : points) {
            if (points.indexOf(t) >= points.size() - 1) continue;

            Point temp = points.get(points.indexOf(t) + 1);
            final int color = getColor(points.indexOf(t));

            GL11.glBegin(GL11.GL_QUAD_STRIP);

            final double x2 = t.x - mc.getRenderManager().info.getProjectedView().getX();
            double y2 = t.y - mc.getRenderManager().info.getProjectedView().getY() + 0.2;
            final double z2 = t.z - mc.getRenderManager().info.getProjectedView().getZ();

            final double x1 = temp.x - mc.getRenderManager().info.getProjectedView().getX();
            final double y1 = temp.y - mc.getRenderManager().info.getProjectedView().getY() + 0.2;
            final double z1 = temp.z - mc.getRenderManager().info.getProjectedView().getZ();

            ColorService.glHexColor(color, 0);
            GL11.glVertex3d(x2, y2 + 0.6f + 0.5, z2);

            ColorService.glHexColor(color, 255 / 2);
            //GL11.glColor4f(colorRGBA[0], colorRGBA[1], colorRGBA[2], 0.5f);
            GL11.glVertex3d(x2, y2 + 0.2, z2);
            GL11.glVertex3d(x1, y1 + 0.6f + 0.5, z1);
            GL11.glVertex3d(x1, y1 + 0.2, z1);
            GL11.glEnd();
            ++t.age;
        }

        RenderSystem.clearCurrentColor();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        RenderSystem.depthMask(true);
        RenderSystem.popMatrix();
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
            castHelper.apply(CastUtil.EntityType.SELF);
        }
        return CastUtil.isInstanceof(entity, castHelper.build()) != null;
    }

    private void collectEntities() {
        this.collectedEntities.clear();
        for (final Entity entity : mc.world.getPlayers()) {
            if (isValid(entity)) {
                //this.points.add(new Point(entity));
            }
        }
    }

    private int getColor(final int count) {
        if (colorMode.getValueByIndex(0)) {
            return (int) ColorService.getSkyRainbow(speed.getValue(), count);
        }
        return ColorService.interpolateColorsBackAndForth(speed.getValue().intValue(), count, ClientColor.panelMain, ClientColor.main);
    }

    @Override
    public void onDisable() {
        points.clear();
        super.onDisable();
    }

    private class Point {
        private float x, y, z;

        private float age = 0;

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}