package me.rina.racc.client.modules.movement;

import me.rina.racc.client.RevenantModule;
import me.rina.racc.client.RevenantSetting;

/**
 * ollie module; sorry for the skid :( - linus
 **/

public class RevenantSprint extends RevenantModule {
    private RevenantSetting mode = newSetting(new String[] {"Mode", "SprintMode", "Mode"}, Mode.Rage);

    public RevenantSprint() {
        super("Sprint", "Sprint", "Allows player to sprint", Category.MOVEMENT);
    }

    public void onUpdate() {
        try {
            switch ((Mode) mode.getEnum()) {
                case Legit:
                    if (mc.gameSettings.keyBindForward.isKeyDown() && !(mc.player.collidedHorizontally)) {
                        if (!mc.player.isSprinting()) {
                            mc.player.setSprinting(true);
                        }
                    }
                    break;
                case Rage:
                    if (!mc.player.isSprinting()) {
                        mc.player.setSprinting(true);
                    }
            }

        } catch (Exception ignored) {}
    }

    enum Mode {
        Rage,
        Legit
    }
}
