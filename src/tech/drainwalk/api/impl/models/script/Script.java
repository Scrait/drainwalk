package tech.drainwalk.api.impl.models.script;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.UseAction;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.openjdk.nashorn.api.scripting.JSObject;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.api.managers.ScriptManager;
import tech.drainwalk.Drainwalk;
import tech.drainwalk.client.option.options.*;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.api.impl.events.render.EventRender3D;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.client.option.Option;
import tech.drainwalk.client.font.FontManager;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.utils.minecraft.ChatUtils;
import tech.drainwalk.utils.render.RenderUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Script {
    @Getter
    ScriptEngine engine;
    public String name;

    public Script(String fileName) {
        this.engine = ScriptManager.getEngineManager().getEngineByName("js");
        try {
            //module
            createJSVariableFromClass(Module[].class);
            createJSVariableFromClass(Option[].class);
            createJSVariableFromClass(BooleanOption.class);
            createJSVariableFromClass(ColorOption.class);
            createJSVariableFromClass(SelectOption.class);
            createJSVariableFromClass(SelectOptionValue.class);
            createJSVariableFromClass(MultiOption.class);
            createJSVariableFromClass(MultiOptionValue.class);
            createJSVariableFromClass(FloatOption.class);
            createJSVariableFromClass(ScriptModule.class);
            createJSVariableFromClass(Type.class);
            createJSVariableFromClass(Module.class);
            createJSVariableFromClass(ClientColor.class);
            //utilities
            createJSVariableFromClass(RenderUtils.class);
            createJSVariableFromClass(ColorService.class);
            createJSVariableFromClass(ChatUtils.class);
            createJSVariableFromClass(MathHelper.class);
            createJSVariableFromClass(FontManager.class);
            //events
            createJSVariableFromClass(EventRender2D.class);
            createJSVariableFromClass(UpdateEvent.class);
            createJSVariableFromClass(EventRender3D.class);
            //minecraft
            createJSVariableFromClass(UseAction.class);

            //opengl
            createJSVariableFromClass(GL11.class);
            createJSVariableFromClass(RenderSystem.class);
            createJSVariableFromClass(GlStateManager.class);
            createJSVariableFromClass(InputMappings.class);

            engine.put("__js__", this);
            engine.eval("function register() { __js__.registerModule(); }");
            engine.put("dw", Drainwalk.getInstance());
            engine.put("mc", Minecraft.getInstance());
            engine.eval(new FileReader(fileName), engine.getContext());
        }
        catch (FileNotFoundException e) {
            System.out.println("Не можем найти скрипт, имя файла: " + fileName);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
    }


    public void registerModule() {
        Drainwalk.getInstance().getApiMain().getModuleManager().register(new ScriptModule(this));
    }

    public String getName() {
        try {
            return (String) engine.eval("script.name");
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    private void createJSVariableFromClass(Class<?> clazz) throws ScriptException {
        engine.eval(String.format("var %s = Java.type('%s');", clazz.getSimpleName().replace("[]", "s"), clazz.getCanonicalName()));
    }

    public void invokeMethod(String name, Object... args) {
        if(!((JSObject)engine.get("script")).hasMember(name)) {
            return;
        }
        try {
            ((Invocable)getEngine()).invokeMethod(getEngine().get("script"), name, args);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}