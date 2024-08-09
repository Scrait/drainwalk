package tech.drainwalk.client.modules.misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.TextFormatting;
import tech.drainwalk.api.impl.models.Friend;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.client.configs.FriendConfig;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.api.impl.models.Notification;
import tech.drainwalk.api.impl.events.input.EventMouse;
import tech.drainwalk.utils.minecraft.ChatUtils;

import java.util.Date;

public class MiddleClickFriend extends Module {
    public MiddleClickFriend() {
        super("MiddleClickFriend", Category.MISC);
        addType(Type.SECONDARY);
    }

    @EventTarget
    public void onMouseEvent(EventMouse event) {
        if (event.getKey() == 2 && mc.pointedEntity instanceof LivingEntity) {
            if (dw.getApiMain().getFriendManager().existsByName(mc.pointedEntity.getName().getString())) {
                dw.getApiMain().getFriendManager().removeByName(mc.pointedEntity.getName().getString());
                try {
                    dw.getApiMain().getConfigManager().findByClass(FriendConfig.class).safeSave();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                ChatUtils.addChatMessage(TextFormatting.RED + "Removed " + TextFormatting.RESET + "'" + mc.pointedEntity.getName().getString() + "'" + " as Friend!");
                dw.getApiMain().getNotificationManager().register(new Notification("Removed ", mc.pointedEntity.getName().getString(), Notification.Type.WRONG));
            } else {
                dw.getApiMain().getFriendManager().register(new Friend (mc.pointedEntity.getName().getString(), new Date()));
                try {
                    dw.getApiMain().getConfigManager().findByClass(FriendConfig.class).safeSave();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                ChatUtils.addChatMessage(TextFormatting.GREEN + "Added " + TextFormatting.RESET + "'" + mc.pointedEntity.getName().getString() + "'" + " as Friend!");
                dw.getApiMain().getNotificationManager().register(new Notification("Added ", mc.pointedEntity.getName().getString(), Notification.Type.CORRECT));
            }
        }
    }
}
