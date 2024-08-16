package tech.drainwalk.client.gui.minecraft.altmanager;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.Session;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.client.font.FontManager;
import tech.drainwalk.client.gui.TextField;
import tech.drainwalk.client.gui.minecraft.Generator;
import tech.drainwalk.client.ui.mainmenu.MainMenuUI;
import tech.drainwalk.services.render.ScreenService;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.utils.render.RenderUtils;

public class AltGui extends Screen {

    public TextField field;
    private boolean textFieldActive = false;

    public AltGui() {
        super(new TranslationTextComponent("pon"));
    }

    @Override
    protected void init() {
        final MainWindow mW = Minecraft.getInstance().getMainWindow();

        final int radius = 100;

        this.addButton(new Button(mW.getScaledWidth() / 2 - 120, mW.getScaledHeight() / 2 - 70 + radius, 80, 20, new StringTextComponent("Войти"), (p) -> {
            if (field.getText().isEmpty()) {
                return;
            }
            textFieldActive = false;
            minecraft.setSession(new Session(field.getText().replace(" ", ""), "", "", "mojang"));
            this.field.setText("");
        }));

        this.addButton(new Button(mW.getScaledWidth() / 2 - 35, mW.getScaledHeight() / 2 - 70 + radius, 80, 20, new StringTextComponent("Сгенерировать"), (p) -> {
            textFieldActive = false;
            minecraft.setSession(new Session(Generator.generateName(), "", "", "mojang"));
            this.field.setText("");
        }));

        this.addButton(new Button(mW.getScaledWidth() / 2 + 52, mW.getScaledHeight() / 2 - 70 + radius, 80, 20, new StringTextComponent("Вернуться"), (p) -> Minecraft.getInstance().displayGuiScreen(new MainMenuUI())));

        this.field = new TextField(FontManager.LIGHT_14, mW.getScaledWidth() / 2 - 43, mW.getScaledHeight() / 3 + 33, (int) (width / 4 - 30), 16, ITextComponent.getTextComponentOrEmpty("PanelComponentTextField"));
        this.field.setMaxStringLength(16);
        this.field.setEnableBackgroundDrawing(false);
        this.field.setFocused2(true);
        this.field.setText("");
        this.field.setCanLoseFocus(true);

        super.init();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (textFieldActive) {
            field.keyPressed(keyCode, scanCode, modifiers);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (textFieldActive) {
            field.charTyped(codePoint, modifiers);
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
//        RenderUtils.drawRoundedRect(0, 0, width, height, 0, ColorService.rgb(24, 22, 22));

        GL11.glPushMatrix();
        GL11.glColor4f(1, 1, 1, 1);

        GL11.glPopMatrix();

//        RenderUtils.drawRoundedGradientRect(field.x - 5 + 2, field.y - 5, 100, 14, 2, ClientColor.panelMain, ClientColor.main,ClientColor.main, ClientColor.panelMain);
//        RenderUtils.drawRoundedRect(field.x - 5 + 2, field.y - 5, 100, 14, 2, ColorService.rgb(7, 7, 7));

        if(textFieldActive) {
            field.renderButton(matrixStack);
        } else {
            FontManager.LIGHT_13.drawString(matrixStack, "Введите никнейм...", field.x, field.y + 1, -1);
        }

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (ScreenService.isHovered((int) mouseX, (int) mouseY, field.x + 2, field.y - 1, 100, 14) && button == 0) {
            field.mouseClicked(mouseX, mouseY, button);
            textFieldActive = true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void tick() {
        if (textFieldActive) {
            field.tick();
        }
    }
}