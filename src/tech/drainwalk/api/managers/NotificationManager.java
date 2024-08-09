package tech.drainwalk.api.managers;

import tech.drainwalk.api.impl.interfaces.IManager;
import tech.drainwalk.api.impl.models.Notification;

import java.util.ArrayList;

public class NotificationManager extends ArrayList<Notification> implements IManager<Notification> {

    @Override
    public void register(Notification notification) {
        this.add(notification);
    }

}
