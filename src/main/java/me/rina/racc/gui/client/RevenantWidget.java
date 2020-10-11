package me.rina.racc.gui.client;

// Turok.
import me.rina.turok.math.TurokRect;

// Main GUI.
import me.rina.racc.gui.RevenantMainGUI;

// Revenant.
import me.rina.racc.Revenant;

/**
 *
 * @author Rina.
 * @since 28/09/2020.
 *
 **/
public class RevenantWidget {
    public TurokRect rect;
//change for test
    /* Public events flag */
    public boolean isMouseOver;
    public boolean isMouseClicked;
    public boolean isRendering;

    public RevenantWidget(String widgetTag) {
        this.rect = new TurokRect(widgetTag, 0, 0);

        this.isMouseOver    = false;
        this.isMouseClicked = false;
        this.isRendering    = false;
    }

    public void resetAllEventBase() {
        this.isMouseOver = false;
    }

    public void resetAllCustomEventBase() {}
    public void disableAllCustomEventBase() {}

    public void onRefreshMouseClicked(int mousePositionX, int mousePositionY, int mouseButtonDown) {
        if (this.isRendering) {
            if (mouseButtonDown == 0) {
                this.isMouseClicked = false;
            }
        }
    }

    public void onRefreshMouseReleased(int mousePositionX, int mousePositionY, int mouseButtonUp) {
        if (this.isRendering) {
            if (mouseButtonUp == 0) {
                this.isMouseClicked = false;
            }
        }
    }

    public void onCustomRefreshKeyPressed(char vChar, int keyCode) {}
    public void onCustomRefreshMouseClicked(int mousePositionX, int mousePositionY, int mouseButtonDown) {}
    public void onCustomRefreshMouseReleased(int mousePositionX, int mousePositionY, int mouseButtonUp) {}
    public void onCustomRefreshRender(int mousePositionX, int mousePositionY, float partialTicks) {}

    /**
     * @param vChar   - Character typed.
     * @param keyCode - KeyCode listened.
     **/
    public void onKeyPressed(char vChar, int keyCode) {}

    /**
     * @param mousePositionX  - Mouse position x.
     * @param mousePositionY  - Mouse position y.
     * @param mouseButtonDown - Mouse button down key.
     **/
    public void onMouseClicked(int mousePositionX, int mousePositionY, int mouseButtonDown) {}

    /**
     * @param mousePositionX - Mouse position x.
     * @param mousePositionY - Mouse position y.
     * @param mouseButtonUp  - Mouse button up key.
     **/
    public void onMouseReleased(int mousePositionX, int mousePositionY, int mouseButtonUp) {}

    /**
     * @param mousePositionX - Mouse position x.
     * @param mousePositionY - Mouse position y.
     * @param partialTicks   - Render partial ticks.
     **/
    public void onRender(int mousePositionX, int mousePositionY, float partialTicks) {}
}