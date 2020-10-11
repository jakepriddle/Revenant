package me.rina.racc.client.modules.client;

// Client.
import me.rina.racc.client.RevenantSetting;
import me.rina.racc.client.RevenantModule;

// Revenant.
import me.rina.racc.Revenant;

/**
 *
 * @author Rina.
 * @since 26/09/2020.
 *
 **/
public class RevenantModuleGUI extends RevenantModule {
    private boolean showGUI = true;

    private RevenantSetting settingShadow = newSetting(new String[] {"Shadow", "GUIShadow", "Shadow string."}, false);
    private RevenantSetting settingScroll = newSetting(new String[] {"Scroll Speed", "GUIScrollSpeed", "Frames scroll speed."}, 5, 0, 10);

    private RevenantSetting colorNameRed = newSetting(new String[] {"Name Color Red", "GUINameColorRed", "Color red."}, 255, 0, 255);
    private RevenantSetting colorNameGreen = newSetting(new String[] {"Name Color Green", "GUINameColorGreen", "Color green."}, 255, 0, 255);
    private RevenantSetting colorNameBlue = newSetting(new String[] {"Name Color Blue", "GUINameColorBlue", "Color blue."}, 255, 0, 255);

    private RevenantSetting colorBaseRGB = newSetting(new String[] {"Base RGB", "GUIBaseRGB", "RGB base buttons."}, false);
    private RevenantSetting colorBaseRed = newSetting(new String[] {"Base Color Red", "GUIBaseColorRed", "Color red."}, 83, 0, 255);
    private RevenantSetting colorBaseGreen = newSetting(new String[] {"Base Color Green", "GUIBaseColorGreen", "Color green."}, 164, 0, 255);
    private RevenantSetting colorBaseBlue = newSetting(new String[] {"Base Color Blue", "GUIBaseColorBlue", "Color blue."}, 255, 0, 255);

    private RevenantSetting colorBackgroundRGB = newSetting(new String[] {"Background RGB", "GUIBackgroundRGB", "RGB background frame."}, false);
    private RevenantSetting colorBackgroundRed = newSetting(new String[] {"Background Color Red", "GUIBackgroundColorRed", "Color red."}, 0, 0, 255);
    private RevenantSetting colorBackgroundGreen = newSetting(new String[] {"Background Color Green", "GUIBackgroundColorGreen", "Color green."}, 0, 0, 255);
    private RevenantSetting colorBackgroundBlue = newSetting(new String[] {"Background Color Blue", "GUIBackgroundColorBlue", "Color blue."}, 0, 0, 255);

    public RevenantModuleGUI() {
        super("GUI", "GUI", "GUI for manager modules state and settings values.", Category.CLIENT);

        setKeyBind(25);
    }

    @Override
    public void onEnable() {
        showGUI = true;
    }

    @Override
    public void onDisable() {
        showGUI = false;
    }

    @Override
    public void onUpdate() {
        if (showGUI) {
            mc.displayGuiScreen(Revenant.getGUIClient());

            showGUI = false;
        }
    }
}