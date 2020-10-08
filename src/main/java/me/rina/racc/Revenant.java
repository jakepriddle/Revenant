package me.rina.racc;

// Gson.
import com.google.gson.*;

// Java.
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.time.*;
import java.util.*;
import java.awt.*;
import java.io.*;

// Minecraft.
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.Minecraft;

// Pomelo event manager.
import org.lwjgl.opengl.Display;
import team.stiff.pomelo.impl.annotated.AnnotatedEventManager;
import team.stiff.pomelo.EventManager;

// Client GUI stuff.
import me.rina.racc.gui.theme.RevenantThemeConstructor;
import me.rina.racc.gui.RevenantMainGUI;

// Managment client.
import me.rina.racc.manager.RevenantCommandManager;
import me.rina.racc.manager.RevenantModuleManager;
import me.rina.racc.manager.RevenantSocialManager;

// System.
import me.rina.racc.system.RevenantSystem;

// Util.
import me.rina.racc.util.client.RevenantChatUtil;

/**
 *
 * @author Rina.
 * @since 25/09/2020.
 *
 **/
@Mod(modid = "revenant", name = Revenant.NAME, version = Revenant.VERSION)
public class Revenant {
    /**
     * Details of client:
     * - You can change here for the version name and stuff!
     **/
    public static final String NAME    = "Revenant";
    public static final String VERSION = "0.4.0";

    /**
     * Chat variables:
     * - Are the variables to chat notify, so you can put anything here.
     **/
    public static final String CHAT_WATERMARK           = RevenantChatUtil.DARK_GRAY + "[" + RevenantChatUtil.DARK_RED + NAME + RevenantChatUtil.DARK_GRAY + "]";
    public static final String CHAT_SEPARATOR_WATERMARK = RevenantChatUtil.RESET + " ";
    public static final String CHAT                     = CHAT_WATERMARK + CHAT_SEPARATOR_WATERMARK;

    @Mod.Instance
    public static Revenant INSTANCE;

    // All classes managers of client.
    private RevenantModuleManager moduleManager;
    private RevenantCommandManager commandManager;
    private RevenantSocialManager socialUserManager;

    // Pomelo event manager.
    private EventManager eventManager;

    // GUI classes to client.
    private RevenantMainGUI revenantGUI;
    private RevenantThemeConstructor revenantGUITheme;

    /**
     * System:
     * - Make effective controller to all client.
     **/
    private RevenantSystem systemController;

    @Mod.EventHandler
    public void clientPreInitializer(FMLPreInitializationEvent event){
        Display.setTitle(NAME + " Utility Mod " + VERSION);
    }


    @Mod.EventHandler
    public void clientInitializer(FMLInitializationEvent event) {
        this.moduleManager    = new RevenantModuleManager("modules");
        this.systemController = new RevenantSystem("system");
        this.commandManager   = new RevenantCommandManager("commands");
        this.socialUserManager    = new RevenantSocialManager();

        // We initialize event maanger pomelo.
        this.eventManager = new AnnotatedEventManager();

        this.revenantGUI = new RevenantMainGUI();

        this.revenantGUITheme = new RevenantThemeConstructor();

        // Register events.
        MinecraftForge.EVENT_BUS.register(this.systemController);
        MinecraftForge.EVENT_BUS.register(this.commandManager);

        // We load config.
        this.moduleManager.onLoadModuleList();
        this.socialUserManager.onLoadSocialUserList();
        this.revenantGUI.onLoadFrameList();

        try {
            this.onLoad();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public static RevenantMainGUI getGUIClient() {
        return INSTANCE.revenantGUI;
    }

    public static RevenantThemeConstructor getGUITheme() {
        return INSTANCE.revenantGUITheme;
    }

    public static EventManager getPomeloEventManager() {
        return INSTANCE.eventManager;
    }

    public static RevenantCommandManager getCommandManager() {
        return INSTANCE.commandManager;
    }

    public static RevenantModuleManager getModuleManager() {
        return INSTANCE.moduleManager;
    }

    public static RevenantSocialManager getSocialUserManager() {
        return INSTANCE.socialUserManager;
    }

    public static RevenantSystem getSystemController() {
        return INSTANCE.systemController;
    }

    public static Revenant getInstance() {
        return INSTANCE;
    }

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    /**
     * System to savement.
     **/
    public void onSave() throws IOException {
        Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();

        String path = "revenant/";

        if (!Files.exists(Paths.get(path))) {
            Files.createDirectories(Paths.get(path));
        }

        if (!Files.exists(Paths.get(path + INSTANCE.NAME + ".json"))) {
            Files.createFile(Paths.get(path + INSTANCE.NAME + ".json"));
        } else {
            File file = new File(path + INSTANCE.NAME + ".json");

            file.delete();

            Files.createFile(Paths.get(path + INSTANCE.NAME + ".json"));
        }

        JsonObject mainJson = new JsonObject();

        mainJson.add("prefix", new JsonPrimitive(INSTANCE.commandManager.getPrefix()));

        String jsonString = gsonBuilder.toJson(new JsonParser().parse(mainJson.toString()));

        OutputStreamWriter fileOutpotStream = new OutputStreamWriter(new FileOutputStream(path + INSTANCE.NAME + ".json"), "UTF-8");

        fileOutpotStream.write(jsonString);
        fileOutpotStream.close();
    }

    public void onLoad() throws IOException {
        String path = "revenant/";

        if (!Files.exists(Paths.get(path + INSTANCE.NAME + ".json"))) {
            return;
        }

        InputStream file = Files.newInputStream(Paths.get(path + INSTANCE.NAME + ".json"));

        JsonObject mainJson = new JsonParser().parse(new InputStreamReader(file)).getAsJsonObject();

        if (mainJson.get("prefix") != null) {
            INSTANCE.commandManager.setPrefix(mainJson.get("prefix").getAsString());
        }

        file.close();
    }
}