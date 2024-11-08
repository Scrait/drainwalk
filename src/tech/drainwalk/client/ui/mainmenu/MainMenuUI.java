package tech.drainwalk.client.ui.mainmenu;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tom.cpm.client.GuiImpl;
import com.tom.cpm.shared.editor.gui.EditorGui;
import lombok.NonNull;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.models.DrainwalkResource;
import tech.drainwalk.client.gui.minecraft.altmanager.AltGui;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.services.render.GLService;
import tech.drainwalk.services.render.RenderService;

public class MainMenuUI extends Screen implements IInstanceAccess {

    private final int SCALE_FACTOR = 1;

    public MainMenuUI() {
        super(new StringTextComponent("MainMenuUI"));
    }

    @Override
    public void init() {
        final float width = mc.getMainWindow().getScaledWidthWithoutAutisticMojangIssue(SCALE_FACTOR);
        final float height = mc.getMainWindow().getScaledHeightWithoutAutisticMojangIssue(SCALE_FACTOR);
        final float factor = SCALE_FACTOR;
        final float buttonBaseWidth = 220;
        final float buttonBaseHeight = 38;
        final float buttonWidth = buttonBaseWidth / factor;
        final float buttonHeight = buttonBaseHeight / factor;
        final float buttonsGap = 8 / factor;

        this.addButton(new CustomButton((int) (width / 2f - buttonWidth / 2), (int) (height / 2f - buttonHeight / 2 - ((buttonsGap + buttonHeight))), (int) buttonWidth, (int) buttonHeight,
                new StringTextComponent("Singleplayer"),
                (button) -> this.minecraft.displayGuiScreen(new WorldSelectionScreen(this)), false));
        this.addButton(new CustomButton((int) (width / 2f - buttonWidth / 2), (int) (height / 2f - buttonHeight / 2), (int) buttonWidth, (int) buttonHeight,
                new StringTextComponent("Multiplayer"),
                (button) -> this.minecraft.displayGuiScreen(new MultiplayerScreen(this)), false));
        this.addButton(new CustomButton((int) (width / 2f - buttonWidth / 2), (int) (height / 2f - buttonHeight / 2 + ((buttonsGap + buttonHeight))), (int) buttonWidth, (int) buttonHeight,
                new StringTextComponent("Account Manager"),
                (button) -> this.minecraft.displayGuiScreen(new AltGui()), false));
        this.addButton(new CustomButton((int) (width / 2f - buttonWidth / 2), (int) (height / 2f - buttonHeight / 2 + ((buttonsGap + buttonHeight) * 2)), (int) buttonWidth, (int) buttonHeight,
                new StringTextComponent("Options"),
                (button) -> this.minecraft.displayGuiScreen(new OptionsScreen(this, mc.gameSettings)), false));
        this.addButton(new CustomButton((int) (width / 2f - buttonWidth / 2 / 2), (int) (height / 2f - buttonHeight / 2 + ((buttonsGap + buttonHeight) * 3 + buttonsGap * 2)), (int) buttonWidth / 2, (int) buttonHeight,
                new StringTextComponent("Quit"),
                (button) ->  mc.displayGuiScreen(new GuiImpl(EditorGui::new, mc.currentScreen)), true));
    }

    @Override
    public void render(@NonNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        final float width = mc.getMainWindow().getScaledWidthWithoutAutisticMojangIssue(SCALE_FACTOR);
        final float height = mc.getMainWindow().getScaledHeightWithoutAutisticMojangIssue(SCALE_FACTOR);

        // bg
        RenderService.drawMainMenuShader(width, height, mouseX, mouseY);

        GLService.INSTANCE.rescale(SCALE_FACTOR);
        RenderSystem.enableAlphaTest();
        RenderSystem.alphaFunc(GL11.GL_GREATER, 0.0f);

        // logo
        final float logoWidth = 96;
        final float logoHeight = 96;
        final int logoColor = ColorService.hexToRgb("#D4CDFF");
        final int[] logoColors = getFadedColors(logoColor, ColorService.getColorWithAlpha(logoColor, 0));
        RenderService.drawImage(matrixStack, new DrainwalkResource("textures/client/logo.png"), width / 2 - logoWidth / 2, height / 2 - logoHeight - 38 * 2 - 10,
                logoWidth, logoHeight, logoColors[0], logoColors[1], logoColors[2], logoColors[3]);

        // buttons
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        RenderSystem.disableAlphaTest();
        GLService.INSTANCE.rescaleMC();
    }

    private int[] getFadedColors(int firstColor, int secondColor) {
        int[] colors = new int[4];
        int speed = 15;
        for (int i = 0; i < 4; i++) {
            colors[i] = ColorService.gradient(speed, 1, firstColor, secondColor);
            speed += 5;
        }
        return colors;
    }

}
