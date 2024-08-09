package tech.drainwalk.client.theme;

import lombok.Getter;
import tech.drainwalk.services.render.ColorService;

public enum Theme {
    WHITE(
            ColorService.rgb(240, 240, 240),
            ColorService.rgb(255, 255, 255),
            ColorService.rgb(217, 217, 217),

            ColorService.rgb(255, 255, 255),

            ColorService.rgb(0, 0, 0),
            ColorService.rgb(122, 122, 122),

            ColorService.rgb(3, 168, 245),
            ColorService.rgb(104, 197, 255),
            ColorService.rgb(217, 217, 217),

            ColorService.rgb(230, 230, 230),
            ColorService.rgb(154, 154, 154)
    ),
    BLACK(
            ColorService.rgb(16, 16, 16),
            ColorService.rgb(33, 33, 33),
            ColorService.rgb(242, 242, 242),

            ColorService.rgb(12, 12, 12),

            ColorService.rgb(242, 242, 242),
            ColorService.rgb(137, 137, 137),

            ColorService.rgb(3, 168, 245),
            ColorService.rgb(0, 88, 129),
            ColorService.rgb(44, 44, 44),


            ColorService.rgb(24, 25, 25),
            ColorService.rgb(75, 88, 98)
    ),
    BLUE(
            ColorService.rgb(27, 28, 44),
            ColorService.rgb(30, 31, 48),
            ColorService.rgb(38, 39, 59),

            ColorService.rgb(30, 31, 48),

            ColorService.rgb(222, 221, 255),
            ColorService.rgb(133, 138, 185),

            ColorService.rgb(55, 100, 255),
            ColorService.rgb(36, 63, 158),
            ColorService.rgb(53, 55, 86),

            ColorService.rgb(51, 51, 81),
            ColorService.rgb(133, 138, 185)
    ),
    FATALITY(
            ColorService.rgb(25, 20, 50),
            ColorService.rgb(30, 20, 55),
            ColorService.rgb(60, 55, 95),

            ColorService.rgb(35, 25, 65),

            ColorService.rgb(232, 231, 243),
            ColorService.rgb(161, 145, 200),

            ColorService.rgb(215, 10, 90),
            ColorService.rgb(139, 21, 78),
            ColorService.rgb(92, 17, 67),

            ColorService.rgb(30 - 2, 25 - 2, 65 - 2),
            ColorService.rgb(45, 40, 100)

    ),
    DRAIN(
            ColorService.rgb(23, 29, 32),
            ColorService.rgb(28, 29, 37),
            ColorService.rgb(164, 198, 216),

            ColorService.rgb(30, 37, 41),

            ColorService.rgb(189, 230, 251),
            ColorService.rgb(161, 179, 188),

            ColorService.rgb(229, 241, 246),
            ColorService.rgb(72, 90, 100),
            ColorService.rgb(56, 76, 86),

            ColorService.rgb(62, 86, 98),
            ColorService.rgb(164, 198, 216)

    );
    @Getter
    private final int panelMain;
    @Getter
    private final int panel;
    @Getter
    private final int panelLines;

    @Getter
    private final int object;

    @Getter
    private final int textMain;
    @Getter
    private final int textStay;

    @Getter
    private final int main;
    @Getter
    private final int mainStay;

    @Getter
    private final int category;

    @Getter
    private final int checkBoxStayBG;
    @Getter
    private final int checkBoxStay;
    Theme(
            int panelMain,
            int panel,
            int panelLines,

            int object,

            int textMain,
            int textStay,

            int main,
            int mainStay,
            int category,
            int checkBoxStayBG,
            int checkBoxStay
    ) {
        this.panelMain = panelMain;
        this.panel = panel;
        this.panelLines = panelLines;

        this.object = object;

        this.textMain = textMain;
        this.textStay = textStay;

        this.main = main;
        this.mainStay = mainStay;

        this.category = category;
        this.checkBoxStayBG = checkBoxStayBG;
        this.checkBoxStay = checkBoxStay;

    }
}
