package tech.drainwalk.client.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.entity.Entity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
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

public class ChinaHat extends Module {

    private final List<Entity> collectedEntities = new ArrayList<>();
    private final FloatOption speed = new FloatOption("Speed", 3, 2, 10).addIncrementValue(0.1f);
    private final SelectOption colorMode =
            new SelectOption("Сolor Mode", 0,
                    new SelectOptionValue("Rainbow"),
                    new SelectOptionValue("Client")
            );
    private final MultiOption targets =
            new MultiOption("Targets", new MultiOptionValue("Players", true), new MultiOptionValue("Friends", true), new MultiOptionValue("Self", true));


    public ChinaHat() {
        super("ChinaHat", Category.RENDER);
        register(
                speed,
                colorMode,
                targets
        );
    }

    @EventTarget
    public void onEvent(EventRender3D.PostEntity event) {
        collectEntities();
        for (Entity entity : collectedEntities) {
            ItemStack stack = entity.getHeldEquipment().iterator().next();
            final double height = stack.getItem() instanceof ArmorItem ? entity.isSneaking() ? -0.1 : 0.12 : 0;
            GlStateManager.pushMatrix();

            final float partTicks = event.getPartialTicks();

            mc.gameRenderer.sukazaebalrabotai(event.getMatrixStack(), partTicks);
            final double x = MathematicUtils.interpolate(entity.getPosX(), entity.lastTickPosX, partTicks);
            final double y = MathematicUtils.interpolate(entity.getPosY(), entity.lastTickPosY, partTicks);
            final double z = MathematicUtils.interpolate(entity.getPosZ(), entity.lastTickPosZ, partTicks);

            final Vector3d vector = new Vector3d(x - mc.getRenderManager().info.getProjectedView().getX(),
                    y - mc.getRenderManager().info.getProjectedView().getY() + height,
                    z - mc.getRenderManager().info.getProjectedView().getZ());

            GlStateManager.translated(vector.getX(), vector.getY(), vector.getZ());
            RenderSystem.depthMask(false);
            RenderSystem.enableDepthTest();
            GL11.glDisable(GL11.GL_CULL_FACE);
            RenderSystem.enableBlend();
            RenderSystem.disableTexture();
            RenderSystem.disableAlphaTest();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            RenderSystem.translatef(0f, (float) (entity.getHeight() + height), 0f);

            GL11.glBegin(GL11.GL_TRIANGLE_FAN);
            GL11.glVertex3d(0.0, 0.25, 0.0);

            for (float i = 0; i < 721; i++) {
                ColorService.glHexColor(getColor((int) i), 255 / 2);
                GL11.glVertex3d(Math.cos(i * Math.PI / 360.0) * 0.5, 0, Math.sin(i * Math.PI / 360) * 0.5);
            }

            GL11.glEnd();
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            RenderSystem.enableAlphaTest();
            RenderSystem.depthMask(true);
            RenderSystem.clearCurrentColor();
            RenderSystem.popMatrix();
        }
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

}
