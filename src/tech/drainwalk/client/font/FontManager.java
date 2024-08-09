package tech.drainwalk.client.font;

public class FontManager {
    public static SFontRenderer
            ICONS_26,
            ICONS_16,
            ICONS_12,
            ICONS_21,
            ICONS_14,
            ICONSWT_14,
            ICONSWT_12,
            ICONSWT_16,
            REGULAR_20,
            REGULAR_22,
            REGULAR_18,
            REGULAR_14,
            REGULAR_16,
            SEMI_BOLD_16,
            SEMI_BOLD_15,
            SEMI_BOLD_14,
            SEMI_BOLD_12,
            SEMI_BOLD_10,
            SEMI_BOLD_18,
            SEMI_BOLD_28,
            MEDIUM_16,
            MEDIUM_20,

            BOLD_16,

            BOLD_20,

            LIGHT_14,

            LIGHT_10,

            LIGHT_12,

            LIGHT_13,

            LIGHT_20,

            PIX_8,

            PIX_12,

            PIX_14;

    public static void initFonts() {
        ICONS_26 = new SFontRenderer(SFont.readFontFromFile("dwfont.ttf", 26), true, true);
        ICONS_16 = new SFontRenderer(SFont.readFontFromFile("dwfont.ttf", 16), true, true);
        //ICONS_50_LOGO = new SFontRenderer(SFont.readFontFromFile("iconstest.ttf", 80), true, true);
        ICONS_12 = new SFontRenderer(SFont.readFontFromFile("dwfont.ttf", 12), true, true);
        ICONS_21 = new SFontRenderer(SFont.readFontFromFile("dwfont.ttf", 21), true, true);
        ICONS_14 = new SFontRenderer(SFont.readFontFromFile("iconstest.ttf", 14), true, true);
        ICONSWT_14 = new SFontRenderer(SFont.readFontFromFile("wticons.ttf", 14), true, true);
        ICONSWT_16 = new SFontRenderer(SFont.readFontFromFile("wticons.ttf", 16), true, true);
        ICONSWT_12 = new SFontRenderer(SFont.readFontFromFile("wticons.ttf", 12), false, true);
        BOLD_20 = new SFontRenderer(SFont.readFontFromFile("sf_pro_display_bold.ttf", 20), true, true);
        BOLD_16 = new SFontRenderer(SFont.readFontFromFile("sf_pro_display_bold.ttf", 20), true, true);
        REGULAR_20 = new SFontRenderer(SFont.readFontFromFile("regular.ttf", 20), true, true);
        REGULAR_18 = new SFontRenderer(SFont.readFontFromFile("regular.ttf", 18), true, true);
        REGULAR_22 = new SFontRenderer(SFont.readFontFromFile("regular.ttf", 22), true, true);
        REGULAR_14 = new SFontRenderer(SFont.readFontFromFile("regular.ttf", 10), true, true);
        REGULAR_16 = new SFontRenderer(SFont.readFontFromFile("regular.ttf", 16), true, true);
        SEMI_BOLD_16 = new SFontRenderer(SFont.readFontFromFile("sf_pro_display_semibold.otf", 16), true, true);
        SEMI_BOLD_15 = new SFontRenderer(SFont.readFontFromFile("sf_pro_display_semibold.otf", 15), true, true);
        SEMI_BOLD_14 = new SFontRenderer(SFont.readFontFromFile("sf_pro_display_semibold.otf", 14), true, true);
        SEMI_BOLD_12 = new SFontRenderer(SFont.readFontFromFile("sf_pro_display_semibold.otf", 12), true, true);
        SEMI_BOLD_10 = new SFontRenderer(SFont.readFontFromFile("sf_pro_display_semibold.otf", 10), true, true);
        SEMI_BOLD_18 = new SFontRenderer(SFont.readFontFromFile("sf_pro_display_semibold.otf", 18), true, true);
        SEMI_BOLD_28 = new SFontRenderer(SFont.readFontFromFile("sf_pro_display_semibold.otf", 28), true, true);
        LIGHT_14 = new SFontRenderer(SFont.readFontFromFile("sf_pro_display_light.ttf", 14), true, true);
        LIGHT_10 = new SFontRenderer(SFont.readFontFromFile("sf_pro_display_light.ttf", 10), true, true);
        LIGHT_12 = new SFontRenderer(SFont.readFontFromFile("sf_pro_display_light.ttf", 12), false, true);
        LIGHT_13 = new SFontRenderer(SFont.readFontFromFile("sf_pro_display_light.ttf", 13), false, true);
        LIGHT_20 = new SFontRenderer(SFont.readFontFromFile("sf_pro_display_light.ttf", 20), false, true);
        MEDIUM_16 = new SFontRenderer(SFont.readFontFromFile("medium.ttf", 16), true, true);
        MEDIUM_20 = new SFontRenderer(SFont.readFontFromFile("medium.ttf", 20), true, true);
        PIX_8 = new SFontRenderer(SFont.readFontFromFile("04b03.ttf", 8), true, true);
        PIX_12 = new  SFontRenderer(SFont.readFontFromFile("04b03.ttf", 12), true, true);
        PIX_14 = new  SFontRenderer(SFont.readFontFromFile("04b03.ttf", 14), true, true);
    }

}
