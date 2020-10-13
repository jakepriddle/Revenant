package me.rina.racc.event;

import me.rina.racc.Revenant;
import me.rina.racc.client.RevenantComponent;
import me.rina.racc.client.RevenantModule;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class RevenantEventManager {
    private final ArrayList<RevenantModule> allModules;
    private final ArrayList<RevenantComponent> allComponents;

    public RevenantEventManager() {
        allModules = Revenant.getModuleManager().getModuleList();
        allComponents = Revenant.getModuleManager().getOverlayList();
    }

    /**
     * reflection invoking :flushed:
     * @param event event being dispatched
     */
    public void dispatch(RevenantEvent event) {
        for (RevenantModule module : allModules) {
            if (!module.isEnabled()) continue;
            for (Method method : module.getClass().getDeclaredMethods()) {
                if (
                        method.isAnnotationPresent(Handler.class) &&
                        method.getParameterCount() == 1 &&
                        method.getParameters()[0].getType() == event.getClass()
                ) {
                    try {
                        method.invoke(module, event);
                    } catch (Exception ignored) {}
                }
            }
        }

        for (RevenantComponent component : allComponents) {
            if (!component.isEnabled()) continue;
            for (Method method : component.getClass().getDeclaredMethods()) {
                if (
                        method.isAnnotationPresent(Handler.class) &&
                                method.getParameterCount() == 1 &&
                                method.getParameters()[0].getType() == event.getClass()
                ) {
                    try {
                        method.invoke(component, event);
                    } catch (Exception ignored) {}
                }
            }
        }
    }
}
