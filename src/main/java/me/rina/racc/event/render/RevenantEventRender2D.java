package me.rina.racc.event.render;

import me.rina.racc.event.RevenantEvent;

public class RevenantEventRender2D extends RevenantEvent {
    private final float partialTicks;

    public RevenantEventRender2D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
