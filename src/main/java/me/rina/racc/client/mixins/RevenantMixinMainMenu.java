package me.rina.racc.client.mixins;

import me.rina.racc.Revenant;
import me.rina.turok.font.TurokFontManager;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class RevenantMixinMainMenu extends GuiScreen {

    @Inject(method = "drawScreen", at = @At("TAIL"), cancellable = true)
    public void drawText(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        TurokFontManager.renderString(TurokFontManager.CFONT_MODULE, Revenant.NAME + " Utility Mod " + Revenant.VERSION, 2, 2, 255, 255, 255, 255, true);
    }
}