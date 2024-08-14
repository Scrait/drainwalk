package tech.drainwalk.api.impl.models.script;

import com.darkmagician6.eventapi.EventTarget;
import org.openjdk.nashorn.api.scripting.JSObject;
import org.openjdk.nashorn.api.scripting.ScriptUtils;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.api.impl.events.movement.StrafeEvent;
import tech.drainwalk.api.impl.events.packet.EventReceivePacket;
import tech.drainwalk.api.impl.events.packet.EventSendPacket;
import tech.drainwalk.api.impl.events.player.EventEntitySync;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.api.impl.events.render.EventRender3D;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.client.option.Option;

import java.util.List;

public class ScriptModule extends Module {
    private final Script script;

    public ScriptModule(Script script) {
        super(script.getName(), Category.SCRIPTS);
        this.script = script;
        if(((JSObject)script.getEngine().get("script")).hasMember("options")) {
            super.register((Option<?>[]) ScriptUtils.convert(((JSObject) script.getEngine().get("script")).getMember("options"), Option[].class));
        }
        if(((JSObject)script.getEngine().get("script")).hasMember("type")) {
            super.addType((Type) ScriptUtils.convert(((JSObject) script.getEngine().get("script")).getMember("type"), Type.class));
        }
        if(((JSObject)script.getEngine().get("script")).hasMember("key")) {
            super.setCurrentKey((Integer) ScriptUtils.convert(((JSObject) script.getEngine().get("script")).getMember("key"), Integer.class));
        }
        if(((JSObject)script.getEngine().get("script")).hasMember("description")) {
            super.addDescription((String) ScriptUtils.convert(((JSObject) script.getEngine().get("script")).getMember("description"), String.class));
        }
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public Category getCategory() {
        return super.getCategory();
    }

    @Override
    public int getCurrentKey() {
        return super.getCurrentKey();
    }

    @Override
    public Type getType() {
        return super.getType();
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        script.invokeMethod("onEnable");
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        script.invokeMethod("onRender2D", event);
    }
    @EventTarget
    public void onRender3D(EventRender3D event) {
        script.invokeMethod("onRender3D", event);
    }
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        script.invokeMethod("onUpdate", event);
    }
    @EventTarget
    public void onEntitySync(EventEntitySync event) {
        script.invokeMethod("onEntitySync", event);
    }
    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        script.invokeMethod("onSendPacket", event);
    }
    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        script.invokeMethod("onReceivePacket", event);
    }
    @EventTarget
    public void onStrafe(StrafeEvent event) {
        script.invokeMethod("onStrafe", event);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        script.invokeMethod("onDisable");
    }
}