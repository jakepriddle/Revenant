package me.rina.racc.client.modules.client;

// Java.
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.*;

// Client.
import me.rina.racc.client.RevenantComponent;
import me.rina.racc.client.RevenantSetting;
import me.rina.racc.client.RevenantModule;

// Revenant.
import me.rina.racc.Revenant;

/**
 *
 * @author Rina.
 * @since 04/10/2020 - 00:48.
 *
 **/
public class RevenantModuleHUDEditor extends RevenantModule {
    private RevenantSetting componentColorStringR = newSetting(new String[] {"Component String Red", "ComponentColorStringRed", "Set color string red."}, 255, 0, 255);
    private RevenantSetting componentColorStringG = newSetting(new String[] {"Component String Green", "ComponentColorStringGreen", "Set color string green."}, 255, 0, 255);
    private RevenantSetting componentColorStringB = newSetting(new String[] {"Component String Blue", "ComponentColorStringBlue", "Set color string blue."}, 255, 0, 255);

    private boolean showGUI = true;

    public RevenantModuleHUDEditor() {
        super("HUD Editor", "HUDEditor", "HUD to dragg and set components overlay.", Category.CLIENT);
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

    /**
     * Classes to override in "guiscreen" overrides.
     **/
//  public void onKeyPressedHUD(char vChar, int keyCode) {
//  	for (RevenantComponent components : Revenant.getModuleManager().getOverlayList()) {
//  		components.onKeyPressed(vChar, keyCode);
//  	}
//  }

    public void onMouseClickedHUD(int mousePositionX, int mousePositionY, int mouseButtonDown) {
        for (RevenantComponent components : Revenant.getModuleManager().getOverlayList()) {
            components.onMouseClicked(mousePositionX, mousePositionY, mouseButtonDown);
        }
    }

    public void onMouseReleasedHUD(int mousePositionX, int mousePositionY, int mouseButtonUp) {
        for (RevenantComponent components : Revenant.getModuleManager().getOverlayList()) {
            components.onMouseReleased(mousePositionX, mousePositionY, mouseButtonUp);
        }
    }

    public void onRenderHUD(int mousePositionX, int mousePositionY, float partialTicks) {
        for (RevenantComponent components : Revenant.getModuleManager().getOverlayList()) {
            components.onRender(mousePositionX, mousePositionY, partialTicks);
        }
    }
}