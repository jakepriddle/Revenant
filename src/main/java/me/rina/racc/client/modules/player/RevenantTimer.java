package me.rina.racc.client.modules.player;

import me.rina.racc.client.RevenantModule;
import me.rina.racc.client.RevenantSetting;

public class RevenantTimer extends RevenantModule {
    public RevenantTimer() {
        super("Timer", "timer", "modifies client side ticks", Category.PLAYER);
    }

    //private RevenantSetting ticks = newSetting(new String[] {"Ticks"}, 4, 0, 20);
    private RevenantSetting ticks = newSetting(new String[] {"Ticks", "TimerTicks", "Ticks"}, 2, 0, 10);

    @Override
    public void onDisable() {
        super.onDisable();
        mc.timer.tickLength = 50;
    }

    @Override
    public void onUpdate() {
        mc.timer.tickLength = 50.0f / ticks.getFloat();
    }


}
