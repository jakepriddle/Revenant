package me.rina.racc.client.modules.combat;

import me.rina.racc.client.RevenantModule;
import me.rina.racc.client.RevenantSetting;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

/**
 * @author Reap
 *
 * offhand :flushed: if there are any problems dm me pls
 */
public class RevenantOffhand extends RevenantModule {
    private RevenantSetting mode = newSetting(new String[] {"Mode", "ModuleMode", "Mode"}, Mode.Crystal);
    private RevenantSetting swordGap = newSetting(new String[] {"Sword Gap", "SwordGap", "Gapple on sword?"}, false);
    private RevenantSetting health = newSetting(new String[] {"Min Health", "MinHealth", "Minimum Health to switch for Item"}, 16.0, 0.0, 36.0);
    private RevenantSetting lookInHotbar = newSetting(new String[] {"Look in Hotbar", "LookInHotbar", "Should look in your hotbar?"}, false);

    public RevenantOffhand() {
        super("Offhand", "Offhand", "Switches items in the offhand", Category.COMBAT);
    }

    @Override public void onUpdate() {
        int itemToSwitch = getWantedItemSlot();
        if (itemToSwitch < 0) return;

        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, itemToSwitch, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, itemToSwitch, 0, ClickType.PICKUP, mc.player);
    }

    private int getWantedItemSlot() {
        Item toSwitch = Items.TOTEM_OF_UNDYING;

        if ((mc.player.getHealth() + mc.player.getAbsorptionAmount()) > health.getDouble()) {
            if (mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
                toSwitch = Items.GOLDEN_APPLE;
            } else {
                switch ((Mode) mode.getEnum()) {
                    case Gapple:
                        toSwitch = Items.GOLDEN_APPLE;
                    case Crystal:
                        toSwitch = Items.END_CRYSTAL;
                }
            }
        }

        if (mc.player.getHeldItemOffhand().getItem() == toSwitch) return -2; // right item is in offhand
        return findFirstItemSlot(toSwitch); // tries to find the item it should switch to
    }
    private int findFirstItemSlot(Item item) {
        if (lookInHotbar.getBoolean()) { // looks for wanted offhand item
            for (int i = 0; i < 45; i++)
                if (mc.player.inventory.getStackInSlot(i).getItem() == item) return i < 9? i + 36 : i; // item's slot id
        } else {
            for (int i = 9; i < 45; i++)
                if (mc.player.inventory.getStackInSlot(i).getItem() == item) return i < 9? i + 36 : i; // item's slot id
        }

        if (lookInHotbar.getBoolean()) { // looks for totem if wanted item wasn't found
            for (int i = 0; i < 45; i++)
                if (mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) return i < 9? i + 36 : i; // item's slot id
        } else {
            for (int i = 9; i < 45; i++)
                if (mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) return i < 9? i + 36 : i; // item's slot id
        }

        return -1; // item not found
    }

    public enum Mode {
        Crystal,
        Gapple
    }
}
