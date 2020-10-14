package me.rina.racc.client.modules.combat;

import me.rina.racc.Revenant;
import me.rina.racc.client.RevenantModule;
import me.rina.racc.client.RevenantSetting;
import me.rina.racc.util.client.RevenantChatUtil;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

//NotGhostTypes
//TODO: Fix Speed loop add more settings maybe do a list of all effects ez
public class RevenantCombatInfo extends RevenantModule {
    public RevenantCombatInfo() {
        super("CombatInfo", "CombatInfo", "Combat Alerts", Category.COMBAT);
    }
    private RevenantSetting alertForStr = newSetting(new String[] {"Strength", "CombatInfoAlertForStr", "Strength"}, true);
    private RevenantSetting alertForWeak = newSetting(new String[] {"Weakness", "CombatInfoAlertForWeak", "Weakness"}, true);
    private RevenantSetting alertForSpeed = newSetting(new String[] {"Speed", "CombatInfoAlertForSpeed", "Speed"}, false);
    private RevenantSetting alertForInvis = newSetting(new String[] {"Invisibility", "CombatInfoAlertForInvis", "Invisibility"}, false);

    private boolean hasAlertedWeak = false;
    private final Set<EntityPlayer> strPlayers = Collections.newSetFromMap(new WeakHashMap());
    private final Set<EntityPlayer> spdPlayers = Collections.newSetFromMap(new WeakHashMap());
    private final Set<EntityPlayer> invisPlayers = Collections.newSetFromMap(new WeakHashMap());
    private final Set<EntityPlayer> weakPlayers = Collections.newSetFromMap(new WeakHashMap());
    public static final Minecraft mc = Minecraft.getMinecraft();


    @Override
    public void onUpdate() {
        if (this.alertForStr.getBoolean()) {
            for (EntityPlayer player : RevenantCombatInfo.mc.world.playerEntities) {
                if (player.equals(RevenantCombatInfo.mc.player)) continue;
                if (player.isPotionActive(MobEffects.STRENGTH) && !this.strPlayers.contains(player)) {
                    RevenantChatUtil.sendClientMessage(player.getDisplayNameString() + " has Strength!");
                    this.strPlayers.add(player);
                }
                if (!this.strPlayers.contains(player) || player.isPotionActive(MobEffects.STRENGTH)) continue;
                RevenantChatUtil.sendClientMessage(player.getDisplayNameString() + " ran out of Strength");
                this.strPlayers.remove(player);
            }
        }
        if (this.alertForWeak.getBoolean()) {
            for (EntityPlayer player : RevenantCombatInfo.mc.world.playerEntities) {
                if (player.equals(RevenantCombatInfo.mc.player)) continue;
                if (player.isPotionActive(MobEffects.WEAKNESS) && !this.weakPlayers.contains(player)) {
                    RevenantChatUtil.sendClientMessage(player.getDisplayNameString() + " has Weakness!");
                    this.weakPlayers.add(player);
                }
                if (!this.weakPlayers.contains(player) || player.isPotionActive(MobEffects.WEAKNESS)) continue;
                RevenantChatUtil.sendClientMessage(player.getDisplayNameString() + " no longer has Weakness");
                this.weakPlayers.remove(player);
            }

        }
        if (this.alertForSpeed.getBoolean()) {
            for (EntityPlayer player : RevenantCombatInfo.mc.world.playerEntities) {
                if (player.equals(RevenantCombatInfo.mc.player)) continue;
                if (player.isPotionActive(MobEffects.SPEED) && !this.spdPlayers.contains(player)) {
                    RevenantChatUtil.sendClientMessage(player.getDisplayNameString() + " has Speed!");
                    this.spdPlayers.add(player);
                }
                if (!this.spdPlayers.contains(player) || player.isPotionActive(MobEffects.SPEED)) continue;
                RevenantChatUtil.sendClientMessage(player.getDisplayNameString() + " ran out of Speed");
                this.spdPlayers.remove(player);
            }
        }
        if (this.alertForInvis.getBoolean()) {
            for (EntityPlayer player : RevenantCombatInfo.mc.world.playerEntities) {
                if (player.equals(RevenantCombatInfo.mc.player)) continue;
                if (player.isPotionActive(MobEffects.INVISIBILITY) && !this.invisPlayers.contains(player)) {
                    RevenantChatUtil.sendClientMessage(player.getDisplayNameString() + " is invisible!");
                    this.invisPlayers.add(player);
                }
                if (!this.invisPlayers.contains(player) || player.isPotionActive(MobEffects.INVISIBILITY)) continue;
                RevenantChatUtil.sendClientMessage(player.getDisplayNameString() + " is no longer invisible");
                this.invisPlayers.remove(player);
            }
        }
    }
}

