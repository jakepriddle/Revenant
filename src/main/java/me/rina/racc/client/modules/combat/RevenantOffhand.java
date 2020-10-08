package me.rina.racc.client.modules.combat;

import me.rina.racc.client.RevenantModule;
import me.rina.racc.client.RevenantSetting;
import me.rina.racc.client.modules.render.RevenantHoleESP;

public class RevenantOffhand extends RevenantModule {
    public RevenantOffhand() {
        super("Offhand", "Offhand", "Switches items in the offhand to totems when low on health", Category.COMBAT);
    }

    // basically just copy paste the autototem with some changes to the item to search for - linus

    private RevenantSetting mode = newSetting(new String[] {"Mode", "ModuleMode", "Mode"}, Mode.Crystal);

    public enum Mode{
        Crystal,
        Gapple
    }

}
