package tech.drainwalk.client.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.interfaces.IManager;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.theme.Theme;
import tech.drainwalk.client.theme.ThemeSetting;
import tech.drainwalk.client.ui.components.Component;
import tech.drainwalk.client.ui.components.impl.LeftAreaComponent;
import tech.drainwalk.client.ui.components.impl.MainComponent;
import tech.drainwalk.services.render.GLService;

import java.util.ArrayList;
import java.util.List;

public class UIMain extends Screen implements IInstanceAccess, IManager<Component> {

    @Getter
    @Setter
    private static ThemeSetting<Theme> selectedTheme = new ThemeSetting<>("Themes", Theme.BLACK);
    @Getter
    @Setter
    protected Category selectedCategory = Category.COMBAT;

    private final List<Component> components = new ArrayList<>();
    private final float UI_WIDTH = 752;
    private final float UI_HEIGHT = 472;

    @Getter
    private final float ROUND = 8;

    public UIMain() {
        super(new StringTextComponent("DrainUI"));
    }

    @Override
    public void init() {
        final float width = mc.getMainWindow().getScaledWidthWithoutAutisticMojangIssue(1);
        final float height = mc.getMainWindow().getScaledHeightWithoutAutisticMojangIssue(1);
        final float x = width / 2 - UI_WIDTH / 2;
        final float y = height / 2 - UI_HEIGHT / 2;

        register(new MainComponent(x, y, UI_WIDTH, UI_HEIGHT, this));
        register(new LeftAreaComponent(x, y, 64, UI_HEIGHT, this));
    }

    @Override
    public void render(@NonNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        GLService.INSTANCE.rescale(1);
        components.forEach(component -> component.render(matrixStack, mouseX, mouseY, partialTicks));
        GLService.INSTANCE.rescaleMC();
    }

    @Override
    public void tick() {
        components.forEach(Component::tick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void register(Component component) {
        components.add(component);
    }

}
