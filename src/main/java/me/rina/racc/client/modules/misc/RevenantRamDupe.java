package me.rina.racc.client.modules.misc;


import me.rina.racc.client.RevenantModule;
import me.rina.racc.client.RevenantSetting;
import me.rina.racc.util.client.RevenantChatUtil;

import java.util.concurrent.ThreadLocalRandom;

//NotGhostTypes (strong hack below)
public class RevenantRamDupe extends RevenantModule {
    public RevenantRamDupe() {
        super("RamDupe", "RevenantRamDupe", "RamDupe", Category.MISC);
    }
    private RevenantSetting dupeDelay = newSetting(new String[]{"Delay", "RamDupeDelay", "Delay"}, 2, 1, 100);
    private RevenantSetting minRam = newSetting(new String[]{"MinRam", "RamDupeMinRam", "MinRam"}, 10, 1, 100);
    private RevenantSetting maxRam = newSetting(new String[]{"MaxRam", "RamDupeMaxRam", "MaxRam"}, 64, 1, 100);
    private RevenantSetting ramMode = newSetting(new String[] {"RamMode", "RamDupeMode", "RamMode"}, ramType.MEGABYTE);

    public enum ramType {
        MEGABYTE,
        GIGABYTE,
        NIGGABYTE,
    }
    int delayTimer;

    @Override
    public void onUpdate() {
        if (delayTimer < dupeDelay.getDouble() * 100) {
            delayTimer++;
            return;
        } else {
            delayTimer = 0;
        }
        int dupedRam = ThreadLocalRandom.current().nextInt(minRam.getInteger(), maxRam.getInteger() + 1);
        if (ramMode.getEnum() == ramType.MEGABYTE) {
            RevenantChatUtil.sendClientMessage("I just duped " + dupedRam + " MBs of RAM thanks to Revenant!");
        }
        if (ramMode.getEnum() == ramType.GIGABYTE) {
            RevenantChatUtil.sendClientMessage("I just duped " + dupedRam + " GBs of RAM thanks to Revenant!");
        }
        if (ramMode.getEnum() == ramType.NIGGABYTE) {
            RevenantChatUtil.sendClientMessage("I just duped " + dupedRam + " niggabytes of RAM thanks to Revenant!");

        }
    }
}
