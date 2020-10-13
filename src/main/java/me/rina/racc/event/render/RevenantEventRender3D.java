package me.rina.racc.event.render;

// Event.
import me.rina.racc.event.RevenantEvent;

/**
 *
 * @author Rina.
 * @since 03/10/2020 - 00:44.
 *
 **/
public class RevenantEventRender3D extends RevenantEvent {
    private float partialTicks;

    public RevenantEventRender3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}