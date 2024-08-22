package com.tom.cpm.client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;

import com.tom.cpl.gui.IKeybind;
import com.tom.cpl.gui.KeyboardEvent;

public class KeyBindings implements IKeybind {
	public static KeyBinding gestureMenuBinding, renderToggleBinding;
	public static IKeybind[] quickAccess = new IKeybind[IKeybind.QUICK_ACCESS_KEYBINDS_COUNT];

	public static void init() {
		gestureMenuBinding = new KeyBinding("key.cpm.gestureMenu", InputMappings.Type.KEYSYM.getOrMakeInput(GLFW.GLFW_KEY_G).getKeyCode(), "key.cpm.category");
		renderToggleBinding = new KeyBinding("key.cpm.renderToggle", InputMappings.INPUT_INVALID.getKeyCode(), "key.cpm.category");
		kbs.add(new KeyBindings(gestureMenuBinding, "gestureMenu"));
		kbs.add(new KeyBindings(renderToggleBinding, "renderToggle"));

		for(int i = 1;i<=IKeybind.QUICK_ACCESS_KEYBINDS_COUNT;i++)
			createQA(i);
	}


	private static void createQA(int id) {
		KeyBinding kb = new KeyBinding("key.cpm.qa_" + id, InputMappings.INPUT_INVALID.getKeyCode(), "key.cpm.category");
		KeyBindings kbs = new KeyBindings(kb, "qa_" + id);
		KeyBindings.kbs.add(kbs);
		quickAccess[id - 1] = kbs;
	}

	public static List<IKeybind> kbs = new ArrayList<>();
	private final KeyBinding kb;
	private final String name;

	private KeyBindings(KeyBinding kb, String name) {
		this.kb = kb;
		this.name = name;
//		ClientRegistry.registerKeyBinding(kb);
	}

	@Override
	public boolean isPressed(KeyboardEvent evt) {
		InputMappings.Input mouseKey = InputMappings.getInputByCode(evt.keyCode, evt.scancode);
		return kb.matchesMouseKey(mouseKey.getKeyCode());
	}

	@Override
	public String getBoundKey() {
		return kb.func_238171_j_().getString();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isPressed() {
		return kb.isPressed();
	}
}
