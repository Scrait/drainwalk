package tech.drainwalk.api.impl.models.module;

import com.darkmagician6.eventapi.EventManager;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.services.animation.Animation;
import tech.drainwalk.api.impl.models.Notification;
import tech.drainwalk.client.modules.misc.ClientSounds;
import tech.drainwalk.client.option.Option;
import tech.drainwalk.client.option.options.*;
import tech.drainwalk.utils.sound.Sound;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Module implements IInstanceAccess {
    @Getter
    private final String name;
    @Setter
    private String suffix;
    @Getter
    private final Category category;
    @Getter
    private Type type = Type.MAIN;
    @Getter
    private String description = "Module haven't description";
    @Getter
    @Setter
    private int currentKey = GLFW.GLFW_KEY_UNKNOWN;
    @Setter
    @Getter
    private boolean enabled;
    @Getter
    private final Animation hoveredAnimation = new Animation();
    @Getter
    private final Animation animation = new Animation();
    @Getter
    private final Animation listAnimation = new Animation();
    @Getter
    private final Animation keyBindsAnimation = new Animation();
    @Getter
    @Setter
    private float inter2;
    private final Sound sound = new Sound();

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public String getSuffix() {
        return suffix == null ? name : name + TextFormatting.GRAY + " -> " + TextFormatting.WHITE + suffix;
    }

    public void addDescription(String description) {
        this.description = description;
    }

    public boolean hasBind() {
        return currentKey != GLFW.GLFW_KEY_UNKNOWN;
    }

    public Module addType(Type type) {
        this.type = type;
        return this;
    }


    public void onEnable() {
    }

    public void onDisable() {
    }

    public void toggle() {
        enabled = !enabled;
        if (enabled) {
            EventManager.register(this);
            System.out.println("ENABLED " + name);
//            dw.getApiMain().getNotificationManager().register(new Notification(getName() + " ", "was enabled", Notification.Type.WARNING));
            onEnable();
        } else {
            onDisable();
            System.out.println("DISABLED " + name);
//            dw.getApiMain().getNotificationManager().register(new Notification(getName() + " ", "was disabled", Notification.Type.WARNING));
            EventManager.unregister(this);
        }

//        if (dw.getApiMain().getModuleManager().findByClass(ClientSounds.class).isEnabled()) {
//            sound.playSound("buttonclick1.wav", 100);
//        }
    }

    public List<Option<?>> getSettingList() {
        List<Field> fields = new ArrayList<>();
        Class<?> clazz = this.getClass();

        // Collect fields from the class hierarchy
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                fields.add(field);
            }
            clazz = clazz.getSuperclass(); // Move to the superclass
        }

        return fields.stream().map(field -> {
                    try {
                        return field.get(this);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).filter(field -> field instanceof Option<?>)
                .map(field -> (Option<?>) field)
                .collect(Collectors.toList());
    }

    public JsonObject save() {
        final JsonObject object = new JsonObject();
        object.addProperty("state", isEnabled());
        if (hasBind()) {
            object.addProperty("keyIndex", currentKey);
        }
        final JsonObject propertiesObject = new JsonObject();
        for (Option option : this.getSettingList()) {
            if (this.getSettingList() != null) {
                if (option instanceof BooleanOption) {
                    propertiesObject.addProperty(option.getSettingName(), ((BooleanOption) option).getValue());
                } else if (option instanceof SelectOption) {
                    propertiesObject.addProperty(option.getSettingName(), ((SelectOption) option).getValue().getName());
                } else if (option instanceof FloatOption) {
                    propertiesObject.addProperty(option.getSettingName(), ((FloatOption) option).getValue());
                } else if (option instanceof MultiOption) {
                    propertiesObject.addProperty(option.getSettingName(), ((MultiOption) option).getSelected());
                } else if (option instanceof ColorOption) {
                    propertiesObject.addProperty(option.getSettingName(), ((ColorOption) option).getValue());
                } else if (option instanceof DraggableOption) {
                    propertiesObject.addProperty(option.getSettingName(), ((DraggableOption) option).getValue().x + ":" + ((DraggableOption) option).getValue().y);
                }
            }
            object.add("Options", propertiesObject);
        }
        return object;
    }

    public void load(JsonObject object) {
        if (object != null) {
            if (object.has("state")) {
                if (object.get("state").getAsBoolean()) {
                    this.toggle();
                }
            }
            currentKey = GLFW.GLFW_KEY_UNKNOWN;
            if (object.has("keyIndex")) {
                currentKey = object.get("keyIndex").getAsInt();
            }
            for (Option option : getSettingList()) {
                final JsonObject propertiesObject = object.getAsJsonObject("Options");
                if (option == null)
                    continue;
                if (propertiesObject == null)
                    continue;
                if (!propertiesObject.has(option.getSettingName()))
                    continue;
                if (option instanceof BooleanOption) {
                    ((BooleanOption) option).setValue(propertiesObject.get(option.getSettingName()).getAsBoolean());
                } else if (option instanceof SelectOption) {
                    for (SelectOptionValue value : ((SelectOption) option).getValues()) {
                        if (value.getName().equals(propertiesObject.get(option.getSettingName()).getAsString())) {
                            ((SelectOption) option).setValue(value);
                        }
                    }
                    //((SelectOption) option).setValue(((SelectOption) option).getValues()[propertiesObject.get(option.getSettingName()).getAsInt()]);
                } else if (option instanceof FloatOption) {
                    ((FloatOption) option).setValue(propertiesObject.get(option.getSettingName()).getAsFloat());
                } else if (option instanceof MultiOption) {
                    final String[] multiOptions = propertiesObject.get(option.getSettingName()).getAsString().split(":");
                    for (MultiOptionValue value : ((MultiOption) option).getValues()) {
                        value.setToggle(false);
                        for (int i = 0; i < multiOptions.length; i++) {
                            if (value.getName().equals(multiOptions[i])) {
                                value.setToggle(true);
                            }
                        }
                    }
                } else if (option instanceof ColorOption) {
                    ((ColorOption) option).setValue(propertiesObject.get(option.getSettingName()).getAsInt());
                } else if (option instanceof DraggableOption) {
                    final String[] coords = propertiesObject.get(option.getSettingName()).getAsString().split(":");
                    final Vector2f value = new Vector2f(Float.parseFloat(coords[0]), Float.parseFloat(coords[1]));
                    ((DraggableOption) option).setValue(value);
                }
            }
        }
    }

    public void resetAnimationKeybinds() {
        keyBindsAnimation.setAnimationValue(0);
        keyBindsAnimation.setValue(0);
        keyBindsAnimation.setPrevValue(0);
    }

    public void register(Option<?> ... settings) {
//        settingList.addAll(Arrays.asList(settings));
    }
}