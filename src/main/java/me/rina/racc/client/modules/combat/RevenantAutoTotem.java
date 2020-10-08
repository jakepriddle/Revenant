package me.rina.racc.client.modules.combat;

import me.rina.racc.client.RevenantModule;
import me.rina.racc.client.RevenantSetting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RevenantAutoTotem extends RevenantModule {
    public RevenantAutoTotem() {
        super("AutoTotem", "AutoTotem", "Automatically replaces", Category.COMBAT);
    }

    int totems;
    boolean moving = false;
    boolean returnI = false;

    private RevenantSetting soft = newSetting(new String[] {"Soft", "Soft", "Soft"}, true);

    @Override
    public void onUpdate(){

        Item item = getItem();

        if (mc.currentScreen instanceof GuiContainer) return;
        if (returnI){
            int t = -1;
            for (int i = 0; i < 45; i++)
                if (mc.player.inventory.getStackInSlot(i).isEmpty()){
                    t = i;
                    break;
                }
            if (t == -1) return;
            mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player);
            returnI = false;
        }

        totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == item).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == item) totems++;
        else{
            if (soft.getBoolean() && !RevenantAutoTotem.mc.player.getHeldItemOffhand().isEmpty()) return;
            if (moving){
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                moving = false;
                if (!mc.player.inventory.getItemStack().isEmpty()) returnI = true;
                return;
            }
            if (mc.player.inventory.getItemStack().isEmpty()){
                if (totems == 0) return;
                int t = -1;
                for (int i = 0; i < 45; i++)
                    if (mc.player.inventory.getStackInSlot(i).getItem() == item){
                        t = i;
                        break;
                    }

                if (t == -1){ return;}
                mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player);
                moving = true;
            } else if (!soft.getBoolean()) {
                int t = -1;
                for (int i = 0; i < 45; i++)
                    if (mc.player.inventory.getStackInSlot(i).isEmpty()){
                        t = i;
                        break;
                    } if (t == -1){ return;}
                mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player);
            }
        }
    }

    public Item getItem(){
        return Items.TOTEM_OF_UNDYING;
    }

    @Override
    public String getHUDInfo(){
        return String.valueOf(totems);
    }
}




