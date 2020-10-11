package me.rina.racc.client;

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
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.Minecraft;

// Client.
import me.rina.racc.client.RevenantSetting;

// Event.
import me.rina.racc.event.render.RevenantEventRender3D;

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
public class RevenantModule {
    protected static Minecraft mc = Minecraft.getMinecraft();

    /**
     * Details:
     * - Name, tag & description are details for module.
     **/
    private String name;
    private String tag;
    private String description;

    // Specified elements of module.
    private RevenantSetting settingModuleListener;
    private RevenantSetting settingModuleNotify;

    private String hudInfo;

    public ArrayList<RevenantSetting> settingList = new ArrayList<>();

    /**
     * Category:
     * - We use enum type to make category for client.
     **/
    private Category category;

    /* Public camera value. */
    public ICamera camera = new Frustum();

    public RevenantModule(String name, String tag, String description, Category category) {
        this.initializeModuleDetail(name, tag, description, false, category);
    }

    public RevenantModule(String name, String tag, String description, Category category, boolean defaultState) {
        this.initializeModuleDetail(name, tag, description, defaultState, category);
    }

    public void initializeModuleDetail(String name, String tag, String description, boolean defaultState, Category category) {
        // Details.
        this.name        = name;
        this.tag         = tag;
        this.description = description;
        this.category    = category;

        // Elements.
        this.settingModuleListener = newSetting(new String[] {"Macro", tag + "Macro", "Macro module."}, -1, defaultState);
        this.settingModuleNotify   = newSetting(new String[] {"Notify", tag + "Notify", "Chat notify"}, true);

        this.hudInfo = "";
    }

    /**
     * onUpdate:
     * - Tick listen to module.
     **/
    public void onUpdate() {}

    /**
     * onEnable:
     * - A unique tick when enable.
     **/
    public void onEnable() {}

    /**
     * onDisable:
     * - A unique tick when disable.
     **/
    public void onDisable() {}

    public void onRender2D() {}
    public void onRender3D(RevenantEventRender3D event) {}

    public void setEnabled() {
        this.settingModuleListener.setBoolean(true);

        onEnable();

        if (this.settingModuleNotify.getBoolean()) {
            RevenantChatUtil.sendClientMessage(Revenant.CHAT + RevenantChatUtil.GREEN + this.tag);
        }

        Revenant.getPomeloEventManager().addEventListener(this);
    }

    public void setDisabled() {
        this.settingModuleListener.setBoolean(false);

        onDisable();

        if (this.settingModuleNotify.getBoolean()) {
            RevenantChatUtil.sendClientMessage(Revenant.CHAT + RevenantChatUtil.RED + this.tag);
        }

        Revenant.getPomeloEventManager().removeEventListener(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    private void setTag(String tag) {
        this.tag = tag;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    private void setHUDInfo(String hudInfo) {
        this.hudInfo = hudInfo;
    }

    public void setModuleState(boolean moduleState) {
        if (this.settingModuleListener.getBoolean() != moduleState) {
            if (moduleState) {
                setEnabled();
            } else {
                setDisabled();
            }
        }
    }

    public void setKeyBind(int keyCode) {
        this.settingModuleListener.setInteger(keyCode);
    }

    public void toggle() {
        setModuleState(!isEnabled());
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public String getDescription() {
        return description;
    }

    public String getHUDInfo() {
        return hudInfo;
    }

    public boolean isEnabled() {
        return settingModuleListener.getBoolean();
    }

    public boolean isNotify() {
        return settingModuleNotify.getBoolean();
    }

    public int getKeyBind() {
        return settingModuleListener.getInteger();
    }

    public ArrayList<RevenantSetting> getSettingList() {
        if (settingList == null) {
            settingList = new ArrayList<>();
        }

        return settingList;
    }

    public RevenantSetting getSettingByTag(String tag) {
        for (RevenantSetting settings : getSettingList()) {
            if (settings.getTag().equalsIgnoreCase(tag)) {
                return settings;
            }
        }

        return null;
    }

    protected RevenantSetting newSetting(String[] details, String value) {
        if (settingList == null) {
            settingList = new ArrayList<>();
        }

        RevenantSetting setting = new RevenantSetting(details, value);

        setting.setType(RevenantSetting.Type.SETTING_ENTRY);

        settingList.add(setting);

        return setting;
    }

    protected RevenantSetting newSetting(String[] details, boolean value) {
        if (settingList == null) {
            settingList = new ArrayList<>();
        }

        RevenantSetting setting = new RevenantSetting(details, value);

        setting.setType(RevenantSetting.Type.SETTING_BUTTON);

        settingList.add(setting);

        return setting;
    }

    protected RevenantSetting newSetting(String[] details, Number value, Number minimum, Number maximum) {
        if (settingList == null) {
            settingList = new ArrayList<>();
        }

        RevenantSetting setting = new RevenantSetting(details, value, minimum, maximum);

        setting.setType(RevenantSetting.Type.SETTING_SLIDER);

        settingList.add(setting);

        return setting;
    }

    protected RevenantSetting newSetting(String[] details, Enum value) {
        if (settingList == null) {
            settingList = new ArrayList<>();
        }

        RevenantSetting setting = new RevenantSetting(details, value);

        setting.setType(RevenantSetting.Type.SETTING_SELECT_BOX);

        settingList.add(setting);

        return setting;
    }

    private RevenantSetting newSetting(String[] details, int valueInteger, boolean valueBoolean) {
        if (settingList == null) {
            settingList = new ArrayList<>();
        }

        RevenantSetting setting = new RevenantSetting(details, valueInteger, valueBoolean);

        setting.setType(RevenantSetting.Type.SETTING_BUTTON_MACRO_LISTENER);

        settingList.add(setting);

        return setting;
    }

    public Category getCategory() {
        return category;
    }

    public enum Category {
        COMBAT("Combat", "Combat", "Modules for combat."),
        PLAYER("Player", "Player", "Player modules."),
        MISC("Misc", "Misc", "Specified modules to use."),
        MOVEMENT("Movement", "Movement", "Modules for movementation."),
        EXPLOIT("Exploit", "Exploit", "Exploit, crash..."),
        RENDER("Render", "Render", "Render modules."),
        OVERLAY("Overlay", "Overlay", "Category for components HUD."),
        CLIENT("Client", "Client", "Client basic modules.");

        String name;
        String tag;
        String description;

        Category(String name, String tag, String description) {
            this.name = name;
            this.tag = tag;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getTag() {
            return tag;
        }

        public String gerDescription() {
            return description;
        }
    }

    /**
     * System to savement.
     **/
    public void syncModule() {
        if (this.settingModuleListener.getBoolean()) {
            setEnabled();
        } else {
            setDisabled();
        }
    }

    public void onSave() throws IOException {
        Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();

        String path = "revenant/client/modules/" + getCategory().getTag().toLowerCase() + "/";

        if (!Files.exists(Paths.get(path))) {
            Files.createDirectories(Paths.get(path));
        }

        if (!Files.exists(Paths.get(path + getTag() + ".json"))) {
            Files.createFile(Paths.get(path + getTag() + ".json"));
        } else {
            File file = new File(path + getTag() + ".json");

            file.delete();

            Files.createFile(Paths.get(path + getTag() + ".json"));
        }

        JsonObject mainJson = new JsonObject();

        mainJson.add("name", new JsonPrimitive(getName()));
        mainJson.add("tag", new JsonPrimitive(getTag()));

        JsonObject settingListJson = new JsonObject();

        for (RevenantSetting settings : settingList) {
            JsonObject settingJson = new JsonObject();

            settingJson.add("name", new JsonPrimitive(settings.getName()));
            settingJson.add("tag", new JsonPrimitive(settings.getTag()));

            if (settings.getType() == RevenantSetting.Type.SETTING_ENTRY) {
                settingJson.add("string", new JsonPrimitive(settings.getString()));
            }

            if (settings.getType() == RevenantSetting.Type.SETTING_BUTTON) {
                settingJson.add("boolean", new JsonPrimitive(settings.getBoolean()));
            }

            if (settings.getType() == RevenantSetting.Type.SETTING_SLIDER) {
                if (settings.isSlider("int")) {
                    settingJson.add("int", new JsonPrimitive(settings.getInteger()));
                } else if (settings.isSlider("double")) {
                    // Obvious double and float.
                    settingJson.add("double", new JsonPrimitive(settings.getDouble()));
                } else if (settings.isSlider("float")) {
                    settingJson.add("float", new JsonPrimitive(settings.getFloat()));
                }
            }

            if (settings.getType() == RevenantSetting.Type.SETTING_SELECT_BOX) {
                settingJson.add("enum", new JsonPrimitive(settings.getEnum().name()));
            }

            if (settings.getType() == RevenantSetting.Type.SETTING_BUTTON_MACRO_LISTENER) {
                settingJson.add("int", new JsonPrimitive(settings.getInteger()));
                settingJson.add("boolean", new JsonPrimitive(settings.getBoolean()));
            }

            settingListJson.add(settings.getTag(), settingJson);
        }

        mainJson.add("settingList", settingListJson);

        String jsonString = gsonBuilder.toJson(new JsonParser().parse(mainJson.toString()));

        OutputStreamWriter fileOutpotStream = new OutputStreamWriter(new FileOutputStream(path + getTag() + ".json"), "UTF-8");

        fileOutpotStream.write(jsonString);
        fileOutpotStream.close();
    }

    public void onLoad() throws IOException {
        String path = "revenant/client/modules/" + getCategory().getTag().toLowerCase() + "/";

        if (!Files.exists(Paths.get(path + getTag() + ".json"))) {
            return;
        }

        InputStream file = Files.newInputStream(Paths.get(path + getTag() + ".json"));

        JsonObject mainJson = new JsonParser().parse(new InputStreamReader(file)).getAsJsonObject();

        if (mainJson.get("name") == null || mainJson.get("tag") == null || mainJson.get("settingList") == null) {
            return;
        }

        JsonObject settingListJson = mainJson.get("settingList").getAsJsonObject();

        for (RevenantSetting settings : settingList) {
            if (settingListJson.get(settings.getTag()) == null) {
                continue;
            }

            JsonObject settingJson = settingListJson.get(settings.getTag()).getAsJsonObject();

            RevenantSetting settingRequested = settings;

            if (settingJson.get("string") != null) {
                settingRequested.setString(settingJson.get("string").getAsString());
            }

            if (settingJson.get("boolean") != null) {
                settingRequested.setBoolean(settingJson.get("boolean").getAsBoolean());
            }

            if (settingJson.get("int") != null) {
                settingRequested.setInteger(settingJson.get("int").getAsInt());
            }

            if (settingJson.get("double") != null) {
                settingRequested.setDouble(settingJson.get("double").getAsDouble());
            }

            if (settingJson.get("float") != null) {
                settingRequested.setFloat(settingJson.get("float").getAsFloat());
            }

            if (settingJson.get("enum") != null) {
                settingRequested.setEnum(settingJson.get("enum").getAsString());
            }
        }

        file.close();

        syncModule();
    }
}