package tech.drainwalk.services.lootbeams;

import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.client.option.options.FloatOption;

public class LootBeamConfiguration {

    protected static FloatOption BEAM_ALPHA = new FloatOption("beam_alpha", 0.85f, 0f, 1f);
    protected static FloatOption BEAM_RADIUS = new FloatOption("beam_radius", 1, 0f, 5);
    protected static FloatOption BEAM_HEIGHT = new FloatOption("beam_height", 1, 0f, 10);
    protected static FloatOption BEAM_Y_OFFSET = new FloatOption("beam_y_offset", 0, -30f, 30);

    protected static BooleanOption RENDER_NAMETAGS = new BooleanOption("render_nametags", true);
    protected static BooleanOption RENDER_NAMETAGS_ONLOOK = new BooleanOption("render_nametags_onlook", true);
    protected static FloatOption NAMETAG_LOOK_SENSITIVITY = new FloatOption("nametag_look_sensitivity", 0.018f, 0f, 5f);
    protected static FloatOption NAMETAG_TEXT_ALPHA = new FloatOption("nametag_text_alpha", 1, 0f, 1);
    protected static FloatOption NAMETAG_BACKGROUND_ALPHA = new FloatOption("nametag_background_alpha", 0.5f, 0f, 1);
    protected static FloatOption NAMETAG_Y_OFFSET = new FloatOption("nametag_y_offset", 0.75f, -30f, 30);
    protected static FloatOption NAMETAG_SCALE = new FloatOption("nametag_scale", 1, -10f, 10);

    protected static BooleanOption RENDER_STACKCOUNT = new BooleanOption("render_stackcount", true);
    protected static BooleanOption BORDERS = new BooleanOption("borders", true);


}
