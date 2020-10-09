package me.rina.racc.client.components;

// Java.
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.*;

// Client.
import me.rina.racc.client.RevenantComponent;
import me.rina.racc.client.RevenantSetting;
import me.rina.racc.client.RevenantModule;

// Util.
import me.rina.racc.util.client.RevenantChatUtil;

// Revenant.
import me.rina.racc.Revenant;

/**
 *
 * @author Rina.
 * @since 04/10/2020.
 *
 * updated by linus on 08/09/2020
 *
 **/
public class RevenantComponentArrayList extends RevenantComponent {
    private RevenantSetting rgb_effect = newSetting(new String[] {"RGB", "ComponentArrayListHUDRGB", "ArrayList utils."}, false);
    private RevenantSetting hud_info_separator = newSetting(new String[] {"Module[HUDInfo]", "ArrayListHUDModuleHUDInfo", "ArrayList utils."}, false);
    private RevenantSetting category = newSetting(new String[] {"Category", "Category", "Category"}, true);

    List<RevenantModule> pretty_modules;

    public RevenantComponentArrayList() {
        super("ArrayList", "ComponentArrayList", "ArrayList", true, Docking.TOP_LEFT);
    }

    @Override
    public void onRenderHUD(int mousePositionX, int mousePositionY, int scaledWidth, int scaledHeight, int[] colorHudRGB, float partialTicks) {
        Comparator<RevenantModule> comparator = (module_1, module_2) -> {
            String module_1_string = module_1.getTag() + (module_1.getHUDInfo().equals("") == true ? "" : RevenantChatUtil.GRAY + " [" + RevenantChatUtil.WHITE + module_1.getHUDInfo() + RevenantChatUtil.GRAY + "]" + RevenantChatUtil.RESET);
            String module_2_string = module_2.getTag() + (module_2.getHUDInfo().equals("") == true ? "" : RevenantChatUtil.GRAY + " [" + RevenantChatUtil.WHITE + module_2.getHUDInfo() + RevenantChatUtil.GRAY + "]" + RevenantChatUtil.RESET);

            float diff = getStringWidth(module_2_string) - getStringWidth(module_1_string);

            if (this.dock == Docking.TOP_LEFT || this.dock == Docking.TOP_RIGHT) {
                return diff != 0 ? (int) diff : module_2_string.compareTo(module_1_string);
            } else {
                return (int) diff;
            }
        };

        if (this.dock == Docking.TOP_LEFT || this.dock == Docking.TOP_RIGHT) {
            pretty_modules = Revenant.getModuleManager().getModuleList().stream().filter(module -> module.isEnabled()).sorted(comparator).collect(Collectors.toList());
        } else if (this.dock == Docking.BOTTOM_LEFT || this.dock == Docking.BOTTOM_RIGHT) {
            pretty_modules = Revenant.getModuleManager().getModuleList().stream().filter(module -> module.isEnabled()).sorted(Comparator.comparing(module -> getStringWidth(module.getName() + (module.getHUDInfo().equals("") == true ? "" : RevenantChatUtil.GRAY + " [" + RevenantChatUtil.RESET + module.getHUDInfo() + RevenantChatUtil.GRAY + "]" + RevenantChatUtil.RESET)))).collect(Collectors.toList());
        }

        int position_update_y = 0;

        for (RevenantModule modules : pretty_modules) {
            if (modules.getCategory() == RevenantModule.Category.CLIENT || modules.getCategory() == RevenantModule.Category.OVERLAY) {
                continue;
            }

            String module_name = (
                    hud_info_separator.getBoolean() == true ? (modules.getTag() + (modules.getHUDInfo().equals("") == true ? "" : RevenantChatUtil.GRAY + " [" + RevenantChatUtil.RESET + modules.getHUDInfo() + RevenantChatUtil.GRAY + "]" + RevenantChatUtil.RESET)) :
                            modules.getTag() + (modules.getHUDInfo().equals("") == true ? "" : RevenantChatUtil.GRAY + " " + modules.getHUDInfo())
            );

            if (rgb_effect.getBoolean()) {
                renderString(module_name, 0, position_update_y, colorHudRGB[0], colorHudRGB[1], colorHudRGB[2], 255);
            } else {
                if (category.getBoolean()) {
                    if (modules.getCategory() == Category.CLIENT) {
                        renderString(module_name, 0, position_update_y, 50, 168, 82, 255);
                    }
                    if (modules.getCategory() == Category.COMBAT) {
                        renderString(module_name, 0, position_update_y, 90, 125, 214, 255);
                    }
                    if (modules.getCategory() == Category.EXPLOIT) {
                        renderString(module_name, 0, position_update_y, 222, 209, 73, 255);
                    }
                    if (modules.getCategory() == Category.MISC) {
                        renderString(module_name, 0, position_update_y, 100, 73, 222, 255);
                    }
                    if (modules.getCategory() == Category.PLAYER) {
                        renderString(module_name, 0, position_update_y, 73, 222, 78, 255);
                    }
                    if (modules.getCategory() == Category.RENDER) {
                        renderString(module_name, 0, position_update_y, 201, 81, 161, 255);
                    }
                    if (modules.getCategory() == Category.MOVEMENT) {
                        renderString(module_name, 0, position_update_y, 222, 91, 132, 255);
                    }
                }
                else {renderString(module_name, 0, position_update_y, 255);}
            };

            position_update_y += getStringHeight(module_name);

            if (getStringWidth(module_name) >= this.rect.getWidth()) {
                this.rect.setWidth(getStringWidth(module_name));
            }

            this.rect.setHeight(position_update_y);
        }
    }
}
