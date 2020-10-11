package me.rina.racc.client.modules.render;

import me.rina.racc.client.RevenantModule;

/**
 * linus module
 **/

public class RevenantNoWeather extends RevenantModule {
    public RevenantNoWeather() {
        super("NoWeather", "NoWeather", "Prevents rain", Category.RENDER);
    }

    @Override
    public void onUpdate(){
        if (mc.world == null) return;
        if (mc.world.isRaining()) {
            mc.world.setRainStrength(0);
        }
    }
}
