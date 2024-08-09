package tech.drainwalk.client.modules.overlay;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.math.vector.Vector2f;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.api.impl.models.Notification;
import tech.drainwalk.client.option.options.DraggableOption;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.render.EventRender2D;

public class Notifications extends Module {

    private final DraggableOption drag = new DraggableOption(this.getName(), new Vector2f(50, 200), 64, 20).addVisibleCondition(() -> mc.currentScreen instanceof ChatScreen && this.isEnabled());
    private float widthBrain;

    public Notifications() {
        super("Notifications", Category.OVERLAY);
        addType(Type.SECONDARY);
        register(drag);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        for (Notification notification : dw.getApiMain().getNotificationManager()) {
            notification.getAnimation().update(notification.isDirection());
        }
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
//        GLService.INSTANCE.rescale(2);
//
//        int offset = 40;
//        final Vector2f cord = drag.getValue();
//        float height = cord.y + 9.7f + offset;
//        float width = cord.x + 132.7f + 40;
//
//        try {
//            final List<Notification> notifications = dw.getApiMain().getNotificationManager();
//            if (notifications.stream().limit(10).toList().isEmpty()) {
//                if (mc.currentScreen instanceof ChatScreen) {
//                    dw.getApiMain().getNotificationManager().register(new Notification("Test ", "notification", Notification.Type.WARNING));
//                }
//                return;
//            }
//            final Notification notificationMax = Collections.max(notifications.stream().limit(10).toList(), Comparator.comparing(s -> FontManager.MEDIUM_16.getStringWidth(s.getText() + s.getHeader())));
//            float widthBrain = FontManager.MEDIUM_16.getStringWidth(notificationMax.getText() + notificationMax.getHeader());
//            this.widthBrain = AnimationService.animation(this.widthBrain, (float) (notifications.stream().limit(10).toList().size() * 20), (float) Timer.deltaTime());
//            widthBrain += this.widthBrain;
//            for (Notification notification : notifications.stream().limit(10).toList()) {
//                if ((System.currentTimeMillis() - notification.getStartTime()) > 3050) {
//                    notifications.remove(notification);
//                    continue;
//                }
//                RenderSystem.pushMatrix();
//                notification.getAnimation().animate(0, 1, 0.3f, EasingList.BACK_OUT, mc.getTimer().renderPartialTicks);
//                GLService.INSTANCE.scaleAnimation((width) - 132.7f - widthBrain / 2, height - 9.7f - offset, widthBrain, 36.4f, notification.getAnimation().getAnimationValue());
//                notification.setDirection(!((System.currentTimeMillis() - notification.getStartTime()) > 2800));
//                notification.getAnimation().animate(0, 1, 0.3f, EasingList.EXPO_OUT, mc.getTimer().renderPartialTicks);
//                RenderUtils.drawRoundedShadow((width) - 132.7f - widthBrain / 2, height - 9.7f - offset, widthBrain, 17f, 2, 4, ColorUtils.getColorWithAlpha(ClientColor.panelMain, 0.6f));
//                float finalWidthBrain = widthBrain;
//                int finalOffset1 = offset;
//                NORMAL_BLUR_RUNNABLES.add(() -> {
//                    GLService.INSTANCE.rescale(2);
//                    RenderSystem.pushMatrix();
//                    GLService.INSTANCE.scaleAnimation((width) - 132.7f - finalWidthBrain, height - 9.7f - finalOffset1, finalWidthBrain, 36.4f, notification.getAnimation().getAnimationValue());
//                    RenderUtils.drawRect((width) - 132.7f - finalWidthBrain / 2, height - 9.7f - finalOffset1, finalWidthBrain, 17f, ClientColor.panelMain);
//                    RenderSystem.popMatrix();
//                    GLService.INSTANCE.rescaleMC();
//                });
//                FontManager.MEDIUM_16.drawCenteredStringWithShadow(event.getMatrixStack(),notification.getHeader() + notification.getText(), (width) - 133.2f, height - 3.7f - offset, ColorUtils.rgbaFloat(242, 242, 247, 1 * notification.getAnimation().getAnimationValue()));
//                int color = ColorUtils.rgb(255, 194, 46);
//                if (notification.getType() == Notification.Type.CORRECT) {
//                    color = ColorUtils.rgb(41, 207, 66);
//                } else if (notification.getType() == Notification.Type.WRONG) {
//                    color = ColorUtils.rgb(255, 97, 89);
//                }
//                RenderUtils.drawRect((width) - 132f - widthBrain / 2 - 1, height - 9.7f - offset, 1, 16.7f, color);
//                RenderUtils.drawRect((width) - 133.4f - widthBrain / 2 + widthBrain , height - 9.7f - offset, 1, 16.7f, color);
//                offset += 21 * notification.getAnimation().getAnimationValue();
//                widthBrain -= 20 * notification.getAnimation().getAnimationValue();
//                RenderSystem.popMatrix();
//            }
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//
//        GLService.INSTANCE.rescaleMC();
    }

}
