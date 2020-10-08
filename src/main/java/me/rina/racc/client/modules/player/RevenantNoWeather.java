package me.rina.racc.client.modules.player;

import me.rina.racc.client.RevenantModule;

public class RevenantNoWeather extends RevenantModule {
    public RevenantNoWeather() {
        super("NoWeather", "NoRain", "Prevents rain", Category.PLAYER);
    }

    @Override
    public void onUpdate(){
        if (mc.world == null) return;
        if (mc.world.isRaining()) {
            mc.world.setRainStrength(0);
        }
    }
}
