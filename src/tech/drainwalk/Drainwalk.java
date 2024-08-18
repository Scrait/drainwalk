package tech.drainwalk;

import com.darkmagician6.eventapi.EventManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.drainwalk.api.ApiMain;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.models.DrainwalkResource;
import tech.drainwalk.api.managers.DraggableManager;
import tech.drainwalk.api.managers.ScriptManager;
import tech.drainwalk.client.draggables.DraggableController;
import tech.drainwalk.api.impl.models.ClientInfo;
import tech.drainwalk.client.profile.Profile;
import tech.drainwalk.client.font.FontManager;
import lombok.Getter;
import lombok.Setter;
import tech.drainwalk.client.ui.dwmenu.UIMain;
import tech.drainwalk.services.highlighter.Highlighter;

public class Drainwalk implements IInstanceAccess {

    @Getter
    private static Drainwalk instance = new Drainwalk();
    @Getter
    @Setter
    private boolean roflMode = false;

    @Getter
    private final ClientInfo clientInfo = new ClientInfo(
            "drainwalk",
            "Innovative",
            "1 august 2023",
            "Nightly",
            "3.0"
    );
    @Getter
    private final Profile profile = new Profile(
            1,
            "Scrait",
            "Beta",
            "Baron"
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
    private final UIMain uiMain;

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
        uiMain = new UIMain();
        LOGGER.info("End of init");

        // mods
        EventManager.register(new Highlighter());
    }

}
