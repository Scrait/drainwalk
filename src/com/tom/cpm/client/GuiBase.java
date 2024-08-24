package com.tom.cpm.client;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.tom.cpm.shared.editor.gui.EditorGui;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.ChatVisibility;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import com.tom.cpl.gui.Frame;
import com.tom.cpl.gui.IGui;
import com.tom.cpl.gui.KeyCodes;
import com.tom.cpl.gui.KeyboardEvent;
import com.tom.cpl.gui.MouseEvent;
import com.tom.cpl.gui.NativeGuiComponents;
import com.tom.cpl.gui.NativeGuiComponents.NativeConstructor;
import com.tom.cpl.gui.UIColors;
import com.tom.cpl.gui.elements.FileChooserPopup;
import com.tom.cpl.gui.elements.TextField;
import com.tom.cpl.gui.elements.TextField.ITextField;
import com.tom.cpl.item.Stack;
import com.tom.cpl.math.Box;
import com.tom.cpl.math.Vec2i;
import com.tom.cpl.text.IText;
import com.tom.cpm.client.MinecraftObject.DynTexture;
import com.tom.cpm.common.ItemStackHandlerImpl;
import com.tom.cpm.shared.MinecraftCommonAccess;
import com.tom.cpm.shared.gui.panel.Panel3d;

public class GuiBase extends Screen implements IGui {
	protected static final KeyCodes CODES = new GLFWKeyCodes();
	protected static final NativeGuiComponents nativeComponents = new NativeGuiComponents();
	protected Frame gui;
	protected Screen parent;
	protected CtxStack stack;
	protected UIColors colors;
	protected Consumer<Runnable> closeListener;
	protected int keyModif;
	protected MatrixStack matrixStack;
	protected boolean noScissorTest;
	protected int vanillaScale = -1;

	static {
		nativeComponents.register(TextField.class, local(GuiBase::createTextField));
		nativeComponents.register(FileChooserPopup.class, TinyFDChooser::new);
		nativeComponents.register(Panel3d.class, Panel3dImpl::new);
	}

	public GuiBase(Function<IGui, Frame> creator, Screen parent) {
		super(new StringTextComponent(""));
		this.colors = new UIColors();
		this.parent = parent;
		try {
			this.gui = creator.apply(this);
		} catch (Throwable e) {
			onGuiException("Error creating gui", e, true);
		}
		noScissorTest = isCtrlDown();
	}

	private static <G extends Supplier<IGui>, N> NativeConstructor<G, N> local(Function<GuiBase, N> fac) {
		return f -> fac.apply((GuiBase) f.get());
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrixStack, 0);
		try {
			this.matrixStack = matrixStack;
			matrixStack.push();
			matrixStack.translate(0, 0, 800);
			stack = new CtxStack(width, height);
			RenderSystem.runAsFancy(() -> gui.draw(mouseX, mouseY, partialTicks));
		} catch (Throwable e) {
			onGuiException("Error drawing gui", e, true);
		} finally {
			if(!noScissorTest)
				RenderSystem.disableScissor();
			String modVer = MinecraftCommonAccess.get().getModVersion();
			String s = "Minecraft " + SharedConstants.getVersion().getName() + " (" + minecraft.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ("release".equalsIgnoreCase(minecraft.getVersionType()) ? "" : "/" + minecraft.getVersionType()) + ") " + modVer;
			font.drawString(matrixStack, s, width - font.getStringWidth(s) - 4, 2, 0xff000000);
			s = minecraft.debug;
			if(noScissorTest)s += " No Scissor";
			font.drawString(matrixStack, s, width - font.getStringWidth(s) - 4, 11, 0xff000000);
			this.matrixStack = null;
			matrixStack.pop();
		}
	}

	@Override
	public void onClose() {
//		this.minecraft.keyboardListener.enableRepeatEvents(false);
//		if(vanillaScale >= 0 && vanillaScale != minecraft.gameSettings.guiScale) {
//			minecraft.gameSettings.guiScale = vanillaScale;
//			vanillaScale = -999;
//			minecraft.updateWindowSize();
//		}
	}

	@Override
	public void closeScreen() {
		Screen p = parent;
		parent = null;
		minecraft.displayGuiScreen(p);
	}

	@Override
	public void drawBox(int x, int y, int w, int h, int color) {
		x += getOffset().x;
		y += getOffset().y;
		fill(matrixStack, x, y, x+w, y+h, color);
	}

	@Override
	public void drawBox(float x, float y, float w, float h, int color) {
		x += getOffset().x;
		y += getOffset().y;

		float minX = x;
		float minY = y;
		float maxX = x+w;
		float maxY = y+h;

		if (minX < maxX) {
			float i = minX;
			minX = maxX;
			maxX = i;
		}

		if (minY < maxY) {
			float j = minY;
			minY = maxY;
			maxY = j;
		}
		Matrix4f matrix = matrixStack.getLast().getMatrix();
		float f3 = (color >> 24 & 255) / 255.0F;
		float f = (color >> 16 & 255) / 255.0F;
		float f1 = (color >> 8 & 255) / 255.0F;
		float f2 = (color & 255) / 255.0F;
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		RenderSystem.defaultBlendFunc();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos(matrix, minX, maxY, 0.0F).color(f, f1, f2, f3).endVertex();
		bufferbuilder.pos(matrix, maxX, maxY, 0.0F).color(f, f1, f2, f3).endVertex();
		bufferbuilder.pos(matrix, maxX, minY, 0.0F).color(f, f1, f2, f3).endVertex();
		bufferbuilder.pos(matrix, minX, minY, 0.0F).color(f, f1, f2, f3).endVertex();
		bufferbuilder.finishDrawing();
		WorldVertexBufferUploader.draw(bufferbuilder);
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
	}

	@Override
	protected void init() {
		this.minecraft.keyboardListener.enableRepeatEvents(true);
		try {
			gui.init(width, height);
		} catch (Throwable e) {
			onGuiException("Error in init gui", e, true);
		}
	}
	@Override
	public void drawText(int x, int y, String text, int color) {
		x += getOffset().x;
		y += getOffset().y;
		matrixStack.push();
		matrixStack.translate(0, 0, 50);
		font.drawString(matrixStack, text, x, y, color);
		matrixStack.pop();
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		try {
			this.keyModif = modifiers;
			KeyboardEvent evt = new KeyboardEvent(keyCode, scanCode, (char) -1, GLFW.glfwGetKeyName(keyCode, scanCode));
			gui.keyPressed(evt);
			if(!evt.isConsumed() && gui.enableChat()) {
				if(gui.enableChat() && minecraft.player != null && minecraft.gameSettings.keyBindChat.matchesKey(keyCode, scanCode) && minecraft.gameSettings.chatVisibility != ChatVisibility.HIDDEN) {
					RenderSystem.recordRenderCall(() -> {
						int scale = vanillaScale;
						vanillaScale = -1;
						minecraft.displayGuiScreen(new Overlay());
						vanillaScale = scale;
					});
					return true;
				}
			}
			return true;
		} catch (Throwable e) {
			onGuiException("Error processing key event", e, false);
			return true;
		}
	}

	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		try {
			this.keyModif = modifiers;
			KeyboardEvent evt = new KeyboardEvent(-1, -1, codePoint, null);
			gui.keyPressed(evt);
			return evt.isConsumed();
		} catch (Throwable e) {
			onGuiException("Error processing key event", e, false);
			return true;
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		try {
			MouseEvent evt = new MouseEvent((int) mouseX, (int) mouseY, button);
			gui.mouseClick(evt);
			return evt.isConsumed();
		} catch (Throwable e) {
			onGuiException("Error processing mouse event", e, false);
			return true;
		}
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		try {
			MouseEvent evt = new MouseEvent((int) mouseX, (int) mouseY, button);
			gui.mouseDrag(evt);
			return evt.isConsumed();
		} catch (Throwable e) {
			onGuiException("Error processing mouse event", e, false);
			return true;
		}
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		try {
			MouseEvent evt = new MouseEvent((int) mouseX, (int) mouseY, button);
			gui.mouseRelease(evt);
			return evt.isConsumed();
		} catch (Throwable e) {
			onGuiException("Error processing mouse event", e, false);
			return true;
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		if(delta != 0) {
			try {
				MouseEvent evt = new MouseEvent((int) mouseX, (int) mouseY, (int) delta);
				gui.mouseWheel(evt);
				return evt.isConsumed();
			} catch (Throwable e) {
				onGuiException("Error processing mouse event", e, false);
				return true;
			}
		}
		return false;
	}

	@Override
	public void addPacks(List<Path> filesIn) {
		try {
			gui.filesDropped(filesIn.stream().map(Path::toFile).filter(File::exists).collect(Collectors.toList()));
		} catch (Throwable e) {
			onGuiException("Error processing mouse event", e, false);
		}
	}

	@Override
	public void displayError(String e) {
		Screen p = parent;
		parent = null;
		Minecraft.getInstance().displayGuiScreen(new CrashScreen(e, p));
	}

	private static class CrashScreen extends Screen {
		private String error;
		private Screen parent;

		public CrashScreen(String error, Screen p) {
			super(new StringTextComponent("Error"));
			this.error = error;
			parent = p;
		}

		@Override
		public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			this.renderBackground(matrixStack);
			String[] txt = IGui.wordWrap(I18n.format("error.cpm.crash", error), width - 200, font::getStringWidth).split("\\\\");
			for (int i = 0; i < txt.length; i++) {
				drawCenteredString(matrixStack, this.font, txt[i], this.width / 2, 15 + i * 10, 16777215);
			}
			super.render(matrixStack, mouseX, mouseY, partialTicks);
		}

		@Override
		protected void init() {
			super.init();
			this.addButton(new Button(this.width / 2 - 100, 140, 200, 20, DialogTexts.GUI_BACK, (p_213034_1_) -> {
				this.minecraft.displayGuiScreen((Screen)null);
			}));
		}

		@Override
		public void closeScreen() {
			if(parent != null) {
				Screen p = parent;
				parent = null;
				minecraft.displayGuiScreen(p);
			}
		}
	}

	@Override
	public void closeGui() {
		if(closeListener != null) {
			closeListener.accept(this::closeScreen);
		} else
			closeScreen();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void drawTexture(int x, int y, int w, int h, int u, int v, String texture) {
		minecraft.getTextureManager().bindTexture(new ResourceLocation("cpm", "textures/gui/" + texture + ".png"));
		x += getOffset().x;
		y += getOffset().y;
		RenderSystem.color4f(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		blit(matrixStack, x, y, u, v, w, h);
		RenderSystem.disableBlend();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void drawTexture(int x, int y, int w, int h, int u, int v, String texture, int color) {
		minecraft.getTextureManager().bindTexture(new ResourceLocation("cpm", "textures/gui/" + texture + ".png"));
		x += getOffset().x;
		y += getOffset().y;
		float a = (color >> 24 & 255) / 255.0F;
		float r = (color >> 16 & 255) / 255.0F;
		float g = (color >> 8 & 255) / 255.0F;
		float b = (color & 255) / 255.0F;
		RenderSystem.color4f(r, g, b, a);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		blit(matrixStack, x, y, u, v, w, h);
		RenderSystem.disableBlend();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void drawTexture(int x, int y, int width, int height, float u1, float v1, float u2, float v2) {
		x += getOffset().x;
		y += getOffset().y;
		RenderSystem.color4f(1, 1, 1, 1);
		minecraft.getTextureManager().bindTexture(DynTexture.getBoundLoc());
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		float bo = getBlitOffset();
		Matrix4f matrix = matrixStack.getLast().getMatrix();
		bufferbuilder.pos(matrix, x, y + height, bo).tex(u1, v2).endVertex();
		bufferbuilder.pos(matrix, x + width, y + height, bo).tex(u2, v2).endVertex();
		bufferbuilder.pos(matrix, x + width, y, bo).tex(u2, v1).endVertex();
		bufferbuilder.pos(matrix, x, y, bo).tex(u1, v1).endVertex();
		bufferbuilder.finishDrawing();
		RenderSystem.enableAlphaTest();
		WorldVertexBufferUploader.draw(bufferbuilder);
		RenderSystem.disableBlend();
	}

	@Override
	public String i18nFormat(String key, Object... obj) {
		return I18n.format(key, obj);
	}

	@Override
	public void setupCut() {
		if(!noScissorTest) {
			int dw = minecraft.getMainWindow().getWidth();
			int dh = minecraft.getMainWindow().getHeight();
			float multiplierX = dw / (float)width;
			float multiplierY = dh / (float)height;
			Box box = getContext().cutBox;
			RenderSystem.enableScissor((int) (box.x * multiplierX), dh - (int) ((box.y + box.h) * multiplierY),
					(int) (box.w * multiplierX), (int) (box.h * multiplierY));
		}
	}

	@Override
	public int textWidth(String text) {
		return font.getStringWidth(text);
	}

	private ITextField createTextField() {
		return new TxtField();
	}

	private class TxtField implements ITextField, Consumer<String> {
		private TextFieldWidget field;
		private Runnable eventListener;
		private Vec2i currentOff = new Vec2i(0, 0);
		private Box bounds = new Box(0, 0, 0, 0);
		private boolean settingText, updateField, enabled;
		public TxtField() {
			this.field = new TextFieldWidget(font, 0, 0, 0, 0, new TranslationTextComponent("narrator.cpm.field"));
			this.field.setMaxStringLength(1024*1024);
			this.field.setEnableBackgroundDrawing(false);
			this.field.setVisible(true);
			this.field.setTextColor(16777215);
			this.field.setResponder(this);
			this.enabled = true;
		}

		@Override
		public void draw(int mouseX, int mouseY, float partialTicks, Box bounds) {
			Vec2i off = getOffset();
			field.x = bounds.x + off.x + 4;
			field.y = bounds.y + off.y + 6;
			currentOff.x = off.x;
			currentOff.y = off.y;
			this.bounds.x = bounds.x;
			this.bounds.y = bounds.y;
			this.bounds.w = bounds.w;
			this.bounds.h = bounds.h;
			field.setWidth(bounds.w - 5);
			Platform.setHeight(field, bounds.h - 12);
			if(updateField) {
				settingText = true;
				field.setCursorPositionEnd();
				settingText = false;
				updateField = false;
			}
			field.render(matrixStack, mouseX, mouseY, partialTicks);
		}

		@Override
		public void keyPressed(KeyboardEvent evt) {
			if(evt.isConsumed())return;
			if(evt.keyCode == -1) {
				if(field.charTyped(evt.charTyped, keyModif))
					evt.consume();
			} else {
				if(field.keyPressed(evt.keyCode, evt.scancode, keyModif) || field.canWrite())
					evt.consume();
			}
		}

		@Override
		public void mouseClick(MouseEvent evt) {
			if(evt.isConsumed()) {
				field.mouseClicked(Integer.MIN_VALUE, Integer.MIN_VALUE, evt.btn);
				return;
			}
			field.x = bounds.x + currentOff.x;
			field.y = bounds.y + currentOff.y;
			field.setWidth(bounds.w);
			Platform.setHeight(field, bounds.h);
			if(field.mouseClicked(evt.x + currentOff.x, evt.y + currentOff.y, evt.btn))
				evt.consume();
		}

		@Override
		public String getText() {
			return field.getText();
		}

		@Override
		public void setText(String txt) {
			this.settingText = true;
			field.setText(txt);
			this.settingText = false;
			this.updateField = true;
		}

		@Override
		public void setEventListener(Runnable eventListener) {
			this.eventListener = eventListener;
		}

		@Override
		public void accept(String value) {
			if(eventListener != null && !settingText && enabled)eventListener.run();
		}

		@Override
		public void setEnabled(boolean enabled) {
			field.setEnabled(enabled);
			this.enabled = enabled;
		}

		@Override
		public boolean isFocused() {
			return field.isFocused();
		}

		@Override
		public void setFocused(boolean focused) {
			field.setFocused2(focused);
		}

		@Override
		public int getCursorPos() {
			return field.getCursorPosition();
		}

		@Override
		public void setCursorPos(int pos) {
			field.setCursorPosition(pos);
		}

		@Override
		public void setSelectionPos(int pos) {
			field.setSelectionPos(pos);
		}

		@Override
		public int getSelectionPos() {
			return field.getSelectionEnd();
		}
	}

	@Override
	public UIColors getColors() {
		return colors;
	}

	@Override
	public void setCloseListener(Consumer<Runnable> listener) {
		this.closeListener = listener;
	}

	@Override
	public boolean isShiftDown() {
		return hasShiftDown();
	}

	@Override
	public boolean isCtrlDown() {
		return hasControlDown();
	}

	@Override
	public boolean isAltDown() {
		return hasAltDown();
	}

	public class Overlay extends ChatScreen {

		public Overlay() {
			super("");
		}

		@Override
		public void render(MatrixStack st, int mouseX, int mouseY, float partialTicks) {
			st.push();
			st.translate(0, 0, -100);
			GuiBase.this.render(st, Integer.MIN_VALUE, Integer.MIN_VALUE, partialTicks);
			st.pop();
			st.push();
			st.translate(0, 0, 900);
			super.render(st, mouseX, mouseY, partialTicks);
			st.pop();
		}

		public Screen getGui() {
			return GuiBase.this;
		}
	}

	@Override
	public KeyCodes getKeyCodes() {
		return CODES;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void drawGradientBox(int x, int y, int w, int h, int topLeft, int topRight, int bottomLeft,
			int bottomRight) {
		x += getOffset().x;
		y += getOffset().y;
		int left = x;
		int top = y;
		int right = x + w;
		int bottom = y + h;
		float atr = (topRight >> 24 & 255) / 255.0F;
		float rtr = (topRight >> 16 & 255) / 255.0F;
		float gtr = (topRight >> 8 & 255) / 255.0F;
		float btr = (topRight & 255) / 255.0F;
		float atl = (topLeft >> 24 & 255) / 255.0F;
		float rtl = (topLeft >> 16 & 255) / 255.0F;
		float gtl = (topLeft >> 8 & 255) / 255.0F;
		float btl = (topLeft & 255) / 255.0F;
		float abl = (bottomLeft >> 24 & 255) / 255.0F;
		float rbl = (bottomLeft >> 16 & 255) / 255.0F;
		float gbl = (bottomLeft >> 8 & 255) / 255.0F;
		float bbl = (bottomLeft & 255) / 255.0F;
		float abr = (bottomRight >> 24 & 255) / 255.0F;
		float rbr = (bottomRight >> 16 & 255) / 255.0F;
		float gbr = (bottomRight >> 8 & 255) / 255.0F;
		float bbr = (bottomRight & 255) / 255.0F;
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.disableAlphaTest();
		RenderSystem.defaultBlendFunc();
		RenderSystem.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		Matrix4f mat = matrixStack.getLast().getMatrix();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos(mat, right, top, this.getBlitOffset()).color(rtr, gtr, btr, atr).endVertex();
		bufferbuilder.pos(mat, left, top, this.getBlitOffset()).color(rtl, gtl, btl, atl).endVertex();
		bufferbuilder.pos(mat, left, bottom, this.getBlitOffset()).color(rbl, gbl, bbl, abl).endVertex();
		bufferbuilder.pos(mat, right, bottom, this.getBlitOffset()).color(rbr, gbr, bbr, abr).endVertex();
		tessellator.draw();
		RenderSystem.shadeModel(7424);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableTexture();
	}

	@Override
	public NativeGuiComponents getNative() {
		return nativeComponents;
	}

	@Override
	public void setClipboardText(String text) {
		minecraft.keyboardListener.setClipboardString(text);
	}

	@Override
	public Frame getFrame() {
		return gui;
	}

	@Override
	public String getClipboardText() {
		return minecraft.keyboardListener.getClipboardString();
	}

	@Override
	public void setScale(int value) {
		if(vanillaScale == -999)return;
		if(value != minecraft.gameSettings.guiScale) {
			if(vanillaScale == -1)vanillaScale = minecraft.gameSettings.guiScale;
			if(value == -1) {
				if(minecraft.gameSettings.guiScale != vanillaScale) {
					minecraft.gameSettings.guiScale = vanillaScale;
					vanillaScale = -1;
					minecraft.updateWindowSize();
				}
			} else {
				minecraft.gameSettings.guiScale = value;
				minecraft.updateWindowSize();
			}
		}
	}

	@Override
	public int getScale() {
		return minecraft.gameSettings.guiScale;
	}

	@Override
	public int getMaxScale() {
		return minecraft.getMainWindow().calcGuiScale(0, minecraft.getForceUnicodeFont()) + 1;
	}

	@Override
	public CtxStack getStack() {
		return stack;
	}

	@Override
	public void tick() {
		try {
			gui.tick();
		} catch (Throwable e) {
			onGuiException("Error in tick gui", e, true);
		}
	}

	@Override
	public void drawFormattedText(float x, float y, IText text, int color, float scale) {
		x += getOffset().x;
		y += getOffset().y;
		matrixStack.push();
		matrixStack.translate(x, y, 50);
		matrixStack.scale(scale, scale, scale);
		font.func_243248_b(matrixStack, text.<ITextComponent>remap(), 0, 0, color);
		matrixStack.pop();
	}

	@Override
	public int textWidthFormatted(IText text) {
		return font.func_243245_a(text.<ITextComponent>remap().func_241878_f());
	}

	@Override
	public void openURL0(String url) {
		Util.getOSType().openURI(url);
	}

	public void onOpened() {
		vanillaScale = -1;
	}

	@Override
	public void drawStack(int x, int y, Stack stack) {
		x += getOffset().x;
		y += getOffset().y;
		ItemStack s = ItemStackHandlerImpl.impl.unwrap(stack);
		this.itemRenderer.zLevel = 600;
		this.itemRenderer.renderItemAndEffectIntoGUI(this.minecraft.player, s, x, y);
		this.itemRenderer.renderItemOverlayIntoGUI(font, s, x, y, null);
		this.itemRenderer.zLevel = 0;
	}

	@Override
	public void drawStackTooltip(int mx, int my, Stack stack) {
		matrixStack.push();
		matrixStack.translate(0, 0, -300);
		ItemStack s = ItemStackHandlerImpl.impl.unwrap(stack);
		renderTooltip(matrixStack, s, mx, my);
		matrixStack.pop();
		RenderSystem.disableRescaleNormal();
	}
}
