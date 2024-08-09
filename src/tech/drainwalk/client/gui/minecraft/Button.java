package tech.drainwalk.client.gui.minecraft;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.client.font.FontManager;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.utils.render.RenderUtils;

public class Button extends AbstractButton
{
    public static final Button.ITooltip field_238486_s_ = (button, matrixStack, mouseX, mouseY) ->
    {
    };
    protected final Button.IPressable onPress;
    protected final Button.ITooltip onTooltip;

    public Button(int x, int y, int width, int height, ITextComponent title, Button.IPressable pressedAction)
    {
        this(x, y, width, height, title, pressedAction, field_238486_s_);
    }

    public Button(int x, int y, int width, int height, ITextComponent title, Button.IPressable pressedAction, Button.ITooltip onTooltip)
    {
        super(x, y, width, height, title);
        this.onPress = pressedAction;
        this.onTooltip = onTooltip;
    }

    public void onPress()
    {
        this.onPress.onPress(this);
    }

    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        RenderUtils.drawCircle(this.x, this.y, 0, 360, 5, ColorService.rgb(130, 130, 130), 2);
        Minecraft minecraft = Minecraft.getInstance();
//        FontRenderer fontrenderer = minecraft.fontRenderer;
//        minecraft.getTextureManager().bindTexture(WIDGETS_LOCATION);
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
//        int i = this.getYImage(this.isHovered());
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        RenderSystem.enableDepthTest();
//        this.blit(matrixStack, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
//        this.blit(matrixStack, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        this.renderBg(matrixStack, minecraft, mouseX, mouseY);
        int j = this.isHovered ? ClientColor.main : ClientColor.textMain;
        FontManager.LIGHT_14.drawCenteredString(matrixStack, this.getMessage().getString(), this.x + this.width / 2, this.y + (this.height - 4) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);

    }

    public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY)
    {
        this.onTooltip.onTooltip(this, matrixStack, mouseX, mouseY);
    }

    public interface IPressable
    {
        void onPress(Button p_onPress_1_);
    }

    public interface ITooltip
    {
        void onTooltip(Button p_onTooltip_1_, MatrixStack p_onTooltip_2_, int p_onTooltip_3_, int p_onTooltip_4_);
    }
}
