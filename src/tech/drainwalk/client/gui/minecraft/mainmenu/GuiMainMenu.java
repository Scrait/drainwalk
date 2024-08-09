package tech.drainwalk.client.gui.minecraft.mainmenu;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderSkybox;
import net.minecraft.client.renderer.RenderSkyboxCube;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import tech.drainwalk.client.gui.minecraft.Generator;
import tech.drainwalk.utils.render.RenderUtils;

public class GuiMainMenu extends Screen
{
    public static final RenderSkyboxCube PANORAMA_RESOURCES = new RenderSkyboxCube(new ResourceLocation("textures/gui/title/background/panorama"));
    private static final ResourceLocation PANORAMA_OVERLAY_TEXTURES = new ResourceLocation("textures/gui/title/background/panorama_overlay.png");
    @Nullable
    private String splashText;
    private final RenderSkybox panorama = new RenderSkybox(PANORAMA_RESOURCES);
    private final boolean showFadeInAnimation;
    private long firstRenderTime;

    public GuiMainMenu()
    {
        this(false);
    }

    public GuiMainMenu(boolean showFadeInAnimation)
    {
        super(new TranslationTextComponent("pizdaezda"));
        this.showFadeInAnimation = showFadeInAnimation;
    }

    public static CompletableFuture<Void> loadAsync(TextureManager texMngr, Executor backgroundExecutor)
    {
        return CompletableFuture.allOf(PANORAMA_RESOURCES.loadAsync(texMngr, backgroundExecutor));
    }

    public boolean isPauseScreen()
    {
        return false;
    }

    public boolean shouldCloseOnEsc()
    {
        return false;
    }

    protected void init()
    {
        if (this.splashText == null)
        {
            this.splashText = this.minecraft.getSplashes().getSplashText();
        }

        int j = this.height / 4 + 48;

        this.addSingleplayerMultiplayerButtons(j, 24);
        this.addButton(new Button(this.width / 2 + 32, j + 72 + 12, 20, 20, new TranslationTextComponent("menu.options"), (p_lambda$init$1_1_) ->
        {
            this.minecraft.displayGuiScreen(new OptionsScreen(this, this.minecraft.gameSettings));
        }));
        this.addButton(new Button(this.width / 2 + 64, j + 72 + 12, 20, 20, new TranslationTextComponent("menu.quit"), (p_lambda$init$2_1_) ->
        {
            this.minecraft.shutdown();
        }));
    }

    private void addSingleplayerMultiplayerButtons(int yIn, int rowHeightIn)
    {
        this.addButton(new Button(this.width / 2 - 64, yIn + 72 + 12, 20, 20, new TranslationTextComponent("menu.singleplayer"), (p_lambda$addSingleplayerMultiplayerButtons$4_1_) ->
        {
            this.minecraft.displayGuiScreen(new WorldSelectionScreen(this));
        }));
        boolean flag = this.minecraft.isMultiplayerEnabled();
        (this.addButton(new Button(this.width / 2 - 32, yIn + 72 + 12, 20, 20, new TranslationTextComponent("menu.multiplayer"), (p_lambda$addSingleplayerMultiplayerButtons$6_1_) ->
        {
            Screen screen = (Screen)(this.minecraft.gameSettings.skipMultiplayerWarning ? new MultiplayerScreen(this) : new MultiplayerWarningScreen(this));
            this.minecraft.displayGuiScreen(screen);
        }))).active = flag;
        (this.addButton(new Button(this.width / 2, yIn + 72 + 12, 20, 20, new TranslationTextComponent("Alt Manager"), (p_lambda$addSingleplayerMultiplayerButtons$7_1_) ->
        {
            Minecraft.getInstance().setSession(new Session(Generator.generateName(), "", "", "mojang"));
        }))).active = flag;
    }


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if (this.firstRenderTime == 0L && this.showFadeInAnimation)
        {
            this.firstRenderTime = Util.milliTime();
        }
        int j = this.height / 4 + 48;
        //RenderUtils.drawRect(0, 0, this.width, this.height, -1);
        //RenderUtils.drawRoundedRect(this.width / 2 - 100, j + 72 + 8, this.width / 2 + 100, 24, 4, -1);
        GlStateManager.disableDepthTest();
        RenderUtils.drawRoundedRect(this.width / 2 - 87, j + 72 + 8, 190, 28, 6, -1);
//        InstanceAccess.render2DRunnables(partialTicks, true);
//        NORMAL_BLUR_RUNNABLES.add(() -> RenderUtils.drawRoundedRect(this.width / 2 - 100, j + 72, this.width / 2 + 100, 24, 4, -1));

        float f = this.showFadeInAnimation ? (float)(Util.milliTime() - this.firstRenderTime) / 1000.0F : 1.0F;
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (super.mouseClicked(mouseX, mouseY, button))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
