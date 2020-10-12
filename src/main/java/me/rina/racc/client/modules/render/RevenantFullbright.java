package me.rina.racc.client.modules.render;

import me.rina.racc.client.RevenantModule;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.io.IOException;

public class RevenantFullbright extends RevenantModule {
    private PotionEffect effect = new PotionEffect(Potion.getPotionById(16));

    public RevenantFullbright() {
        super("Fullbright", "Fullbright", "Gives you a night vision potion effect", Category.RENDER);
    }

    @Override
    public void onLoad() throws IOException {
        effect.setPotionDurationMax(true);
    }

    @Override
    public void onEnable() {
        mc.player.addPotionEffect(effect);
    }

    @Override
    public void onDisable() {
        mc.player.removeActivePotionEffect(effect.getPotion());
    }
}
