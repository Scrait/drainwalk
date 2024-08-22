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

    public MainMenuUI() {
        super(new StringTextComponent("MainMenuUI"));
    }

    @Override
    public void init() {
        final float factor = (float) mc.getMainWindow().getGuiScaleFactor();
        final float buttonBaseWidth = 250;
        final float buttonBaseHeight = 38;
        final float buttonWidth = buttonBaseWidth / factor;
        final float buttonHeight = buttonBaseHeight / factor;
        final float buttonsGap = 6 / factor;
        this.addButton(new CustomButton((int) (width / 2f - buttonWidth / 2), (int) (height / 2f - buttonHeight / 2 - ((buttonsGap + buttonHeight))), (int) buttonWidth, (int) buttonHeight,
                new TranslationTextComponent("menu.singleplayer"),
                (button) -> this.minecraft.displayGuiScreen(new WorldSelectionScreen(this)), false));
        this.addButton(new CustomButton((int) (width / 2f - buttonWidth / 2), (int) (height / 2f - buttonHeight / 2), (int) buttonWidth, (int) buttonHeight,
                new TranslationTextComponent("menu.multiplayer"),
                (button) -> this.minecraft.displayGuiScreen(new MultiplayerScreen(this)), false));
        this.addButton(new CustomButton((int) (width / 2f - buttonWidth / 2), (int) (height / 2f - buttonHeight / 2 + ((buttonsGap + buttonHeight))), (int) buttonWidth, (int) buttonHeight,
                new StringTextComponent("Alt Manager"),
                (button) -> this.minecraft.displayGuiScreen(new AltGui()), false));
        this.addButton(new CustomButton((int) (width / 2f - buttonWidth / 2), (int) (height / 2f - buttonHeight / 2 + ((buttonsGap + buttonHeight) * 2)), (int) buttonWidth, (int) buttonHeight,
                new TranslationTextComponent("menu.options"),
                (button) -> this.minecraft.displayGuiScreen(new OptionsScreen(this, mc.gameSettings)), false));
        this.addButton(new CustomButton((int) (width / 2f - buttonWidth / 2 / 2), (int) (height / 2f - buttonHeight / 2 + ((buttonsGap + buttonHeight) * 3 + buttonsGap * 2)), (int) buttonWidth / 2, (int) buttonHeight,
                new TranslationTextComponent("menu.quit"),
                (button) ->  mc.displayGuiScreen(new GuiImpl(EditorGui::new, mc.currentScreen)), true));
    }

    @Override
    public void render(@NonNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        final int SCALE_FACTOR = 1;
        final float width = mc.getMainWindow().getScaledWidthWithoutAutisticMojangIssue(SCALE_FACTOR);
        final float height = mc.getMainWindow().getScaledHeightWithoutAutisticMojangIssue(SCALE_FACTOR);

        GLService.INSTANCE.rescale(SCALE_FACTOR);
        RenderSystem.enableAlphaTest();
        RenderSystem.alphaFunc(GL11.GL_GREATER, 0.0f);

        // bg
        RenderService.drawRect(matrixStack, 0, 0, width, height, ColorService.hexToRgb("#090C11"));

        // starting pizdec
        final float baseWidth = 1920.0f;
        final float heightFactor = height / 1080.0f;
        final float adjustedWidth = baseWidth * heightFactor;
        final float glowWidth = Math.min(adjustedWidth, width);
        final float glowX = (width - glowWidth) / 2.0f;

        // glow
        RenderService.drawImage(matrixStack, new DrainwalkResource("textures/mainmenu/top_glow.png"), glowX, 0, glowWidth, height * 0.8f);

        final float circlesHeight = height * 0.6f;
        final float circlesWidth = glowWidth * 0.9f;
        final float circlesX = (width - circlesWidth) / 2.0f;
        final float circlesY = height - circlesHeight;

        // circles
        RenderService.drawImage(matrixStack, new DrainwalkResource("textures/mainmenu/circles.png"), circlesX, circlesY, circlesWidth, circlesHeight);

        // qr
        final float qrBaseWidth = 128;
        final float qrBaseHeight = 152;
        final float qrWidth = qrBaseWidth * heightFactor;
        final float qrHeight = qrBaseHeight * heightFactor;
        final float qrPadding = 36 * heightFactor;
        RenderService.drawImage(matrixStack, new DrainwalkResource("textures/mainmenu/qr.png"), width - qrWidth - qrPadding, height - qrHeight - qrPadding,
                qrWidth, qrHeight);

        // blots
        final float blotBaseWidth = 277;
        final float blotBaseHeight = 337;
        final float blotWidth = blotBaseWidth * heightFactor;
        final float blotHeight = blotBaseHeight * heightFactor;
        RenderService.drawImage(matrixStack, new DrainwalkResource("textures/mainmenu/blot1.png"), width / 2 - blotWidth, height / 2 - blotHeight / 2,
                blotWidth, blotHeight);
        RenderService.drawImage(matrixStack, new DrainwalkResource("textures/mainmenu/blot2.png"), width / 2, height / 2 - blotHeight,
                blotWidth, blotHeight);

        // logo
        final float logoWidth = 72;
        final float logoHeight = 100;
        final int logoColor = ColorService.hexToRgb("#9CB6DD");
        final int[] logoColors = getFadedColors(ColorService.getColorWithAlpha(logoColor, 0.1f), ColorService.getColorWithAlpha(logoColor, 0));
        RenderService.drawImage(matrixStack, new DrainwalkResource("textures/mainmenu/siriuswh.png"), width / 2 - logoWidth / 2, height / 2 - logoHeight - 38 * 2 - 10,
                logoWidth, logoHeight, logoColors[0], logoColors[1], logoColors[2], logoColors[3]);

        RenderSystem.disableAlphaTest();
        GLService.INSTANCE.rescaleMC();

        // buttons
        super.render(matrixStack, mouseX, mouseY, partialTicks);
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
