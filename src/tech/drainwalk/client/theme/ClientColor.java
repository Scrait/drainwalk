package tech.drainwalk.client.theme;

import tech.drainwalk.client.gui.modernui.MenuMain;
import tech.drainwalk.services.animation.Animation;

public class ClientColor {
    public static final Animation animation = new Animation();

    public static int prevPanelMain = MenuMain.getSelectedTheme().getValue().getPanelMain();
    public static int prevPanel = MenuMain.getSelectedTheme().getValue().getPanel();
    public static int prevPanelLines = MenuMain.getSelectedTheme().getValue().getPanelLines();
    public static int prevObject = MenuMain.getSelectedTheme().getValue().getObject();

    public static int prevTextMain = MenuMain.getSelectedTheme().getValue().getTextMain();
    public static int prevTextStay = MenuMain.getSelectedTheme().getValue().getTextStay();

    public static int prevMain = MenuMain.getSelectedTheme().getValue().getMain();
    public static int prevMainStay = MenuMain.getSelectedTheme().getValue().getMainStay();

    public static int prevCategory = MenuMain.getSelectedTheme().getValue().getCategory();

    public static int prevCheckBoxStayBG = MenuMain.getSelectedTheme().getValue().getCheckBoxStayBG();
    public static int prevCheckBoxStay = MenuMain.getSelectedTheme().getValue().getCheckBoxStay();

    public static int panelMain = MenuMain.getSelectedTheme().getValue().getPanelMain();
    public static int panel = MenuMain.getSelectedTheme().getValue().getPanel();
    public static int panelLines = MenuMain.getSelectedTheme().getValue().getPanelLines();

    public static int object = MenuMain.getSelectedTheme().getValue().getObject();

    public static int textMain = MenuMain.getSelectedTheme().getValue().getTextMain();
    public static int textStay = MenuMain.getSelectedTheme().getValue().getTextStay();

    public static int main = MenuMain.getSelectedTheme().getValue().getMain();
    public static int mainStay = MenuMain.getSelectedTheme().getValue().getMainStay();

    public static int category = MenuMain.getSelectedTheme().getValue().getCategory();


    public static int checkBoxStayBG = MenuMain.getSelectedTheme().getValue().getCheckBoxStayBG();
    public static int checkBoxStay = MenuMain.getSelectedTheme().getValue().getCheckBoxStay();
}
