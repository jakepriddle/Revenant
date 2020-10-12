package me.rina.racc.client;

// Gson.
import com.google.gson.*;

// Java.
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

// Minecraft.
import me.rina.racc.event.Handler;
import me.rina.racc.event.render.RevenantEventRender2D;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.Gui;

// OpenGL.
import org.lwjgl.input.Mouse;

// Turok.
import me.rina.turok.font.TurokFontManager;
import me.rina.turok.render.TurokRenderGL;
import me.rina.turok.math.TurokRect;

// Event.
import me.rina.racc.event.render.RevenantEventRender3D;

// Revenant.
import me.rina.racc.Revenant;

/**
 *
 * @author Rina.
 * @since 03/10/2020.
 *
 **/
public class RevenantComponent extends RevenantModule {
    public Docking dock;

    private int mousePositionX;
    private int mousePositionY;

    private int scaledWidth;
    private int scaledHeight;

    public int[] hudColor;

    private int moveX;
    private int moveY;

    public TurokRect rect;

    private float eventPartialTicks;

    private RevenantSetting settingShadow;
    private RevenantSetting settingSmooth;

    /* Public events. */
    public boolean isMouseOver;
    public boolean isComponentDragging;
    public boolean isMouseClicked;
    public boolean isStarted;

    public RevenantComponent(String name, String tag, String description, boolean hasText, Docking defaultDock) {
        super(name, tag, description, Category.OVERLAY);

        this.dock = defaultDock;

        this.moveX = 0;
        this.moveY = 0;

        this.rect = new TurokRect(tag, 0, 0);

        this.isStarted = true;

        if (hasText) {
            this.settingSmooth = newSetting(new String[] {"Smooth", tag + "Smooth", "Smooth string font."}, true);
            this.settingShadow = newSetting(new String[] {"Shadow", tag + "Shadow", "Shadow string effect."}, false);
        } else {
            this.settingSmooth = new RevenantSetting(new String[] {"Smooth", tag + "Smooth", "Disabled smooth setting."}, true);
            this.settingShadow = new RevenantSetting(new String[] {"Shadow", tag + "shadow", "Disabled shadow setting."}, false);
        }
    }

    @Handler
    public void onRender3D(RevenantEventRender3D event) {
        if (mc.world == null || mc.player == null) {
            return;
        }

        this.eventPartialTicks = event.getPartialTicks();
    }

    @Handler
    public void onRender2D(RevenantEventRender2D event) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        this.scaledWidth  = scaledResolution.getScaledWidth();
        this.scaledHeight = scaledResolution.getScaledHeight();

        this.mousePositionX = Mouse.getX();
        this.mousePositionY = Mouse.getY();

        this.hudColor = new int[] {
                Revenant.getModuleManager().getModuleByTag("HUDEditor").getSettingByTag("ComponentColorStringRed").getInteger(),
                Revenant.getModuleManager().getModuleByTag("HUDEditor").getSettingByTag("ComponentColorStringGreen").getInteger(),
                Revenant.getModuleManager().getModuleByTag("HUDEditor").getSettingByTag("ComponentColorStringBlue").getInteger()
        };

        verifyDraggingHandler(0, 0);

        onRenderHUD(this.mousePositionX, this.mousePositionY, this.scaledWidth, this.scaledHeight, Revenant.getSystemController().getRGBEffect(), this.eventPartialTicks);
    }

    public void onRenderHUD(int mousePositionX, int mousePositionY, int scaledWidth, int scaledHeight, int[] hudColorRGB, float partialTicks) {}

    protected void drawRect(int x, int y, int width, int height, int r, int g, int b, int a) {
        Gui.drawRect(this.rect.getX() + x, this.rect.getY() + y, this.rect.getX() + x + width, this.rect.getY() + y + height, new java.awt.Color(r, g, b, a).getRGB());
    }

    protected void renderString(String string, int x, int y, int a) {
        this.renderString(string, x, y, this.hudColor[0], this.hudColor[1], this.hudColor[2], a);
    }

    protected void renderString(String string, int x, int y, int r, int g, int b, int a) {
        if (!this.settingSmooth.getBoolean()) {
            TurokRenderGL.prepareToRenderString();
            TurokRenderGL.color(r, g, b, a);

            if (this.settingShadow.getBoolean()) {
                mc.fontRenderer.drawStringWithShadow(string, this.rect.getX() + x, this.rect.getY() + y, new java.awt.Color(r, g, b, a).getRGB());
            } else {
                mc.fontRenderer.drawString(string, this.rect.getX() + verifyDocking(getStringWidth(string), x), this.rect.getY() + y, new java.awt.Color(r, g, b, a).getRGB());
            }

            TurokRenderGL.releaseRenderString();
        } else {
            TurokFontManager.renderString(TurokFontManager.CFONT_HUD, string, this.rect.getX() + x, this.rect.getY() + y, r, g, b, a, this.settingShadow.getBoolean());
        }
    }

    protected int getStringWidth(String string) {
        if (!this.settingSmooth.getBoolean()) {
            return (mc.fontRenderer.getStringWidth(string) * 1);
        }

        return TurokFontManager.getStringWidth(TurokFontManager.CFONT_HUD, string);
    }

    protected int getStringHeight(String string) {
        if (!this.settingSmooth.getBoolean()) {
            return (mc.fontRenderer.FONT_HEIGHT * 1);
        }

        return TurokFontManager.getStringHeight(TurokFontManager.CFONT_HUD, string) + 2;
    }

    protected int verifyDocking(int width, int x) {
        int finalPosition = x;

        if (this.dock == Docking.TOP_LEFT) {
            finalPosition = x;
        }

        if (this.dock == Docking.BOTTOM_LEFT) {
            finalPosition = x;
        }

        if (this.dock == Docking.TOP_RIGHT) {
            finalPosition = this.rect.getWidth() - width - x;
        }

        if (this.dock == Docking.BOTTOM_RIGHT) {
            finalPosition = this.rect.getWidth() - width - x;
        }

        return finalPosition;
    }

    public enum Docking {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT;
    }

    /* Overrides to class "guiscreen" overrides. */
    public void onMouseClicked(int mousePositionX, int mousePositionY, int mouseButtonDown) {
        if (this.isEnabled()) {
            if (mouseButtonDown == 0 && this.isMouseOver) {
                this.isMouseClicked = true;

                this.moveX = mousePositionX - this.rect.getX();
                this.moveY = mousePositionY - this.rect.getY();
            }
        }
    }

    public void onMouseReleased(int mousePositionX, int mousePositionY, int mouseButtonUp) {
        if (this.isEnabled()) {
            if (mouseButtonUp == 0 && this.isMouseClicked) {
                this.isMouseClicked = false;
            }
        }
    }

    public void onRender(int mousePositionX, int mousePositionY, float partialTicks) {
        if (this.isEnabled()) {
            this.isMouseOver = this.rect.collide(mousePositionX, mousePositionY);

            if (this.isMouseClicked) {
                this.isComponentDragging = true;
            } else {
                this.isComponentDragging = false;
            }

            if (this.isMouseOver) {
                drawRect(0, 0, this.rect.getWidth(), this.rect.getHeight(), 255, 255, 255, 100);
            } else {
                drawRect(0, 0, this.rect.getWidth(), this.rect.getHeight(), 0, 0, 0, 100);
            }

            if (this.isComponentDragging) {
                this.rect.setX(mousePositionX - this.moveX);
                this.rect.setY(mousePositionY - this.moveY);

                drawRect(0, 0, this.rect.getWidth(), this.rect.getHeight(), 0, 0, 50, 100);
            }

            this.onRenderHUD(mousePositionX - this.moveX, mousePositionY - this.moveY, this.scaledWidth, this.scaledHeight, Revenant.getSystemController().getRGBEffect(), this.eventPartialTicks);

            verifyDraggingHandler(mousePositionX, mousePositionY);
        }
    }

    public void verifyDraggingHandler(int realX, int realY) {
        if (this.rect.getX() <= 5) {
            this.rect.setX(1);

            if (this.dock == Docking.TOP_RIGHT) {
                this.dock = Docking.TOP_LEFT;
            } else if (this.dock == Docking.BOTTOM_RIGHT) {
                this.dock = Docking.BOTTOM_LEFT;
            }
        }

        if (this.rect.getY() <= 5) {
            this.rect.setY(1);

            if (this.dock == Docking.BOTTOM_LEFT) {
                this.dock = Docking.TOP_LEFT;
            } else if (this.dock == Docking.BOTTOM_RIGHT) {
                this.dock = Docking.TOP_RIGHT;
            }
        }

        if (this.rect.getX() + this.rect.getWidth() >= this.scaledWidth - 5) {
            this.rect.setX(this.scaledWidth - this.rect.getWidth() - 1);

            if (this.dock == Docking.TOP_LEFT) {
                this.dock = Docking.TOP_RIGHT;
            } else if (this.dock == Docking.BOTTOM_LEFT) {
                this.dock = Docking.BOTTOM_RIGHT;
            }
        }

        if (this.rect.getY() + this.rect.getHeight() >= this.scaledHeight - 5) {
            this.rect.setY(this.scaledHeight - this.rect.getHeight() - 1);

            if (this.dock == Docking.TOP_LEFT) {
                this.dock = Docking.BOTTOM_LEFT;
            } else if (this.dock == Docking.TOP_RIGHT) {
                this.dock = Docking.BOTTOM_RIGHT;
            }
        }
    }

    public void onSave() throws IOException {
        Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();

        String path = "revenant/client/components/";

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
        mainJson.add("x", new JsonPrimitive(this.rect.getX()));
        mainJson.add("y", new JsonPrimitive(this.rect.getY()));

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
        String path = "revenant/client/components/";

        if (!Files.exists(Paths.get(path + getTag() + ".json"))) {
            return;
        }

        InputStream file = Files.newInputStream(Paths.get(path + getTag() + ".json"));

        JsonObject mainJson = new JsonParser().parse(new InputStreamReader(file)).getAsJsonObject();

        if (mainJson.get("name") == null || mainJson.get("tag") == null || mainJson.get("settingList") == null) {
            return;
        }

        JsonObject settingListJson = mainJson.get("settingList").getAsJsonObject();

        if (mainJson.get("x") != null) {
            this.rect.setX(mainJson.get("x").getAsInt());
        }

        if (mainJson.get("y") != null) {
            this.rect.setY(mainJson.get("y").getAsInt());
        }

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