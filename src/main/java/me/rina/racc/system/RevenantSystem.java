package me.rina.racc.system;

// Minecraft.
import me.rina.racc.event.render.RevenantEventRender2D;
import me.rina.racc.event.render.RevenantEventRender3D;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraftforge.client.event.*;
import net.minecraft.client.Minecraft;

// Java.
import java.awt.Color;

// OpenGL.
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

// Turok.
import me.rina.turok.render.TurokRenderGL;

// Client.
import me.rina.racc.client.RevenantCommand;

// Util.
import me.rina.racc.util.client.RevenantChatUtil;

// Revenant.
import me.rina.racc.Revenant;

/**
 *
 * @author Rina.
 * @since 25/09/2020.
 *
 **/
public class RevenantSystem {
    private final Minecraft mc = Minecraft.getMinecraft();

    private String systemTag;

    public static String CONTEXT = "event";

    public int colorRGBEffectRed;
    public int colorRGBEffectGreen;
    public int colorRGBEffectBlue;

    public RevenantSystem(String systemTag) {
        this.systemTag = systemTag;

        this.colorRGBEffectRed   = 0;
        this.colorRGBEffectGreen = 0;
        this.colorRGBEffectBlue  = 0;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.player == null) {
            return;
        }

        // We call all onUpdate method override from module manager.
        Revenant.getModuleManager().onUpdateModuleList();
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }

        RevenantEventRender3D eventRender = new RevenantEventRender3D(event.getPartialTicks());
        Revenant.getInstance().revEventManager.dispatch(eventRender);

        Revenant.getModuleManager().onRender3DModuleList(event);

        float[] tick_color = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };

        int colorInterpolized = Color.HSBtoRGB(tick_color[0], 1, 1);

        // We update.
        this.colorRGBEffectRed   = ((colorInterpolized >> 16) & 0xFF);
        this.colorRGBEffectGreen = ((colorInterpolized >> 8) & 0xFF);
        this.colorRGBEffectBlue  = ((colorInterpolized) & 0xFF);

        /* Refresh theme. */
        Revenant.getGUITheme().refreshTheme();
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (event.isCanceled()) {
            return;
        }

        RenderGameOverlayEvent.ElementType target = RenderGameOverlayEvent.ElementType.ALL;

        if (!mc.player.isCreative() && mc.player.getRidingEntity() instanceof AbstractHorse) {
            target = RenderGameOverlayEvent.ElementType.HEALTHMOUNT;
        }

        if (event.getType() == target) {
            Revenant.getInstance().revEventManager.dispatch(new RevenantEventRender2D(event.getPartialTicks()));

            // Prepare to render.
            TurokRenderGL.prepare2D();
            TurokRenderGL.release2D();
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            Revenant.getModuleManager().onKeyCodeModuleList(Keyboard.getEventKey());
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onChat(ClientChatEvent event) {
        String eventMessage = event.getMessage();

        String[] commandListMessage = Revenant.getCommandManager().verifyCommandAndGetArguments(eventMessage);

        if (commandListMessage.length > 0) {
            boolean commandReturnTrue = false;

            event.setCanceled(true);

            mc.ingameGUI.getChatGUI().addToSentMessages(eventMessage);

            for (RevenantCommand commands : Revenant.getCommandManager().getCommandList()) {
                try {
                    if (commandListMessage[0].equalsIgnoreCase(commands.getCommand())) {
                        commandReturnTrue = commands.onReceive(commandListMessage);
                    }
                } catch (Exception ignored) {}
            }

            if (!commandReturnTrue && Revenant.getCommandManager().hasPrefix(eventMessage)) {
                RevenantChatUtil.sendClientMessage(Revenant.CHAT + RevenantChatUtil.RED + "SyntaxError: Null pointer value at command");
            }
        }
    }

    public int[] getRGBEffect() {
        return new int[] {
                this.colorRGBEffectRed, this.colorRGBEffectGreen, this.colorRGBEffectBlue
        };
    }
}