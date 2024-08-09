package tech.drainwalk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.drainwalk.api.ApiMain;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.models.DrainwalkResource;
import tech.drainwalk.api.managers.DraggableManager;
import tech.drainwalk.api.managers.ScriptManager;
import tech.drainwalk.client.draggables.DraggableController;
import tech.drainwalk.api.impl.models.ClientInfo;
import tech.drainwalk.client.gui.modernui.MenuMain;
import tech.drainwalk.client.profile.Profile;
import tech.drainwalk.client.font.FontManager;
import lombok.Getter;
import lombok.Setter;

public class Drainwalk implements IInstanceAccess {

    @Getter
    private static Drainwalk instance = new Drainwalk();
    @Getter
    @Setter
    private boolean roflMode = false;

    @Getter
    private final ClientInfo clientInfo = new ClientInfo(
            "drainwalk",
            "1 august 2023",
            "Nightly",
            "3.0"
    );
    @Getter
    private final ApiMain apiMain;
    @Getter
    private final ScriptManager scriptManager;
    @Getter
    private final DraggableManager draggableManager;
    @Getter
    private final DraggableController draggableController;
    @Getter
    private final MenuMain menuMain;
    @Getter
    private final Profile profile;

    private Drainwalk() {
        instance = this;
        DrainwalkResource.registerResources();
        final Logger LOGGER = LogManager.getLogger();
        LOGGER.info("Init instance");
        FontManager.initFonts();
        apiMain = new ApiMain();
        draggableManager = new DraggableManager();
        scriptManager = new ScriptManager();
        LOGGER.info("Init api");
        draggableController = new DraggableController();
        profile = new Profile("Scrait", 0, "999 days", "Шеф");
        menuMain = new MenuMain();
        LOGGER.info("End of init");
    }

}
