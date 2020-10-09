package me.rina.racc.client.modules.movement;

import me.rina.racc.client.RevenantModule;
import me.rina.racc.client.RevenantSetting;
import me.rina.racc.client.modules.render.RevenantHoleESP;

public class RevenantStep extends RevenantModule {
    public RevenantStep() {
        super("Step", "Step", "Modifies player step height", Category.MOVEMENT);
    }

    /**
     * linus module
     **/

    private RevenantSetting height = newSetting(new String[] {"Height", "Height", "Height"}, 2, 0, 8);
    private RevenantSetting mode = newSetting(new String[] {"Mode", "ModuleMode", "Mode"}, Mode.Reverse);

    @Override
    public void onUpdate() {
        switch ((Mode) mode.getEnum()) {
            case Reverse: {
                if (mc.player != null && mc.player.onGround && !mc.player.isInWater() && !mc.player.isOnLadder()) {
                    for (double y = 0.0; y < height.getDouble() + 0.5; y += 0.01) {
                        if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                            mc.player.motionY = -10.0;
                            break;
                        }
                    }
                }
            } case Upward: {
                if (mc.player != null || mc.world != null) {
                    mc.player.stepHeight = height.getFloat();
                }
            } case Both: {
                // todo
            }
        }
    }

    @Override
    public void onDisable(){
        if (mc.player != null || mc.world != null) {
            mc.player.stepHeight = 0.5f;
        }

        if (mc.player != null && mc.player.onGround && !mc.player.isInWater() && !mc.player.isOnLadder()) {
            for (double y = 0.0; y < height.getDouble() + 0.5; y += 0.01) {
                if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                    mc.player.motionY = 0;
                    break;
                }
            }
        }
    }

    public enum Mode {
        Reverse,
        Upward,
        Both
    }

    @Override
    public String getHUDInfo() {
        return String.valueOf(mode.getEnum());
    }
}
