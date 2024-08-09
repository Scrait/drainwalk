package tech.drainwalk.client.modules.overlay;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.settings.PointOfView;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.api.impl.events.modules.CrosshairEvent;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.services.render.GLService;
import tech.drainwalk.utils.math.MathematicUtils;
import tech.drainwalk.utils.render.RenderUtils;

public class Crosshair extends Module {

    private float coldwnLast;

    public Crosshair() {
        super("Crosshair", Category.OVERLAY);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        if (mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON) {
            float screenWidth = event.getMainWindow().getScaledWidthWithoutAutisticMojangIssue(2);
            float screenHeight = event.getMainWindow().getScaledHeightWithoutAutisticMojangIssue(2);
            float width = screenWidth / 2;
            float height = screenHeight / 2;

            double cinc = mc.player.getCooledAttackStrength(0.0f) * 359.0f;
            this.coldwnLast = MathematicUtils.lerp(this.coldwnLast, (float)cinc, 0.3f);
            GLService.INSTANCE.rescale(2);
            RenderUtils.drawCircle(width, height, 1.0f, 360.0f, 3.0f, ClientColor.panelLines, 4);
            RenderUtils.drawCircle(width, height, 1.0f, 1.0f + this.coldwnLast, 3.0f, ClientColor.panelMain, 4);
            GLService.INSTANCE.rescaleMC();
        }
    }

    @EventTarget
    public void onCrosshair(CrosshairEvent event) {
        event.setCancelled(true);
    }
}
