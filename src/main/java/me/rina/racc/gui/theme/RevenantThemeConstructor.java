package me.rina.racc.gui.theme;

// Revenant.
import me.rina.racc.Revenant;

/**
 *
 * @author Rina.
 * @since 2̃7/09/2020.
 *
 **/
public class RevenantThemeConstructor {
    /* Frame categories. */
    public int[] frameName;
    public int[] frameNameBackground;
    public int[] frameBackground;

    /* Module button. */
    public int[] moduleName;
    public int[] moduleNameBackground;
    public int[] moduleNameHighlight;

    /* Setting button. */
    public int[] settingName;
    public int[] settingNameBackground;
    public int[] settingNameHighlight;

    public boolean shadowFont;

    public RevenantThemeConstructor() {
        this.frameName           = new int[] {255, 255, 255};
        this.frameNameBackground = new int[] {255, 0, 255, 255};
        this.frameBackground     = new int[] {0, 0, 0, 100};

        this.moduleName           = new int[] {255, 255, 255};
        this.moduleNameBackground = new int[] {0, 0, 0, 0};
        this.moduleNameHighlight  = new int[] {255, 0, 255, 100};

        this.settingName           = new int[] {255, 255, 255};
        this.settingNameBackground = new int[] {0, 0, 0, 0};
        this.settingNameHighlight  = new int[] {255, 0, 255, 100};

        this.shadowFont = false;
    }

    public void refreshTheme() {
        this.frameName = new int[] {
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUINameColorRed").getInteger(),
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUINameColorGreen").getInteger(),
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUINameColorBlue").getInteger()
        };

        this.frameNameBackground = new int[] {
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBaseColorRed").getInteger(),
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBaseColorGreen").getInteger(),
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUINameColorBlue").getInteger(),
                255
        };

        this.frameBackground = new int[] {
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBackgroundColorRed").getInteger(),
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBackgroundColorGreen").getInteger(),
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBackgroundColorBlue").getInteger(),
                100
        };

        this.moduleName = new int[] {
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUINameColorRed").getInteger(),
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUINameColorGreen").getInteger(),
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUINameColorBlue").getInteger()
        };

        this.moduleNameBackground = new int[] {
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBackgroundColorRed").getInteger(),
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBackgroundColorGreen").getInteger(),
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBackgroundColorBlue").getInteger(),
                0
        };

        this.moduleNameHighlight = new int[] {
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBaseColorRed").getInteger(),
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBaseColorGreen").getInteger(),
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUINameColorBlue").getInteger(),
                100
        };

        this.settingName = new int[] {
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUINameColorRed").getInteger(),
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUINameColorGreen").getInteger(),
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUINameColorBlue").getInteger()
        };

        this.settingNameBackground = new int[] {
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBackgroundColorRed").getInteger(),
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBackgroundColorGreen").getInteger(),
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBackgroundColorBlue").getInteger(),
                0
        };

        this.settingNameHighlight = new int[] {
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBaseColorRed").getInteger(),
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBaseColorGreen").getInteger(),
                Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUINameColorBlue").getInteger(),
                100
        };

        this.shadowFont = Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIShadow").getBoolean();

        if (Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBaseRGB").getBoolean()) {
            Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBaseColorRed").setInteger(Revenant.getSystemController().getRGBEffect()[0]);
            Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBaseColorGreen").setInteger(Revenant.getSystemController().getRGBEffect()[1]);
            Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBaseColorBlue").setInteger(Revenant.getSystemController().getRGBEffect()[2]);
        }

        if (Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBackgroundRGB").getBoolean()) {
            Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBackgroundColorRed").setInteger(Revenant.getSystemController().getRGBEffect()[0]);
            Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBackgroundColorGreen").setInteger(Revenant.getSystemController().getRGBEffect()[1]);
            Revenant.getModuleManager().getModuleByTag("GUI").getSettingByTag("GUIBackgroundColorBlue").setInteger(Revenant.getSystemController().getRGBEffect()[2]);
        }
    }
}