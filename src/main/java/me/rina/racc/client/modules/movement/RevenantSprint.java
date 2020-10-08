package me.rina.racc.client.modules.movement;

import me.rina.racc.client.RevenantModule;

public class RevenantSprint extends RevenantModule {
    public RevenantSprint() {
        super("Sprint", "Sprint", "Allows player to sprint", Category.MOVEMENT);
    }

    public void onUpdate() {
        try {
            if (mc.gameSettings.keyBindForward.isKeyDown() && !(mc.player.collidedHorizontally)) {
                if (!mc.player.isSprinting()) {
                    mc.player.setSprinting(true);
                }
            }

        } catch (Exception ignored) {}
    }
}
