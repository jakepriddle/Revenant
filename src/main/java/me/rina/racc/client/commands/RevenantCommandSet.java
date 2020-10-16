package me.rina.racc.client.commands;

import me.rina.racc.Revenant;
import me.rina.racc.client.RevenantCommand;
import me.rina.racc.util.client.RevenantChatUtil;

public class RevenantCommandSet extends RevenantCommand {
    public RevenantCommandSet() {
        super("set", "Sets a module's setting to a given value.");
    }

    @Override
    public boolean onReceive(String[] args) {
        if (args.length < 5) {
            RevenantChatUtil.sendClientMessage("Not enough arguments for command set.");
            return false;
        }

        // spaghetti code because of the current setting system :c
        switch (args[2].toLowerCase()) {
            case "string":
                Revenant.getModuleManager().getModuleByTag(args[1]).getSettingByTag(args[3]).setString(args[4]);
                return true;
            case "int":
                Revenant.getModuleManager().getModuleByTag(args[1]).getSettingByTag(args[3]).setInteger(Integer.parseInt(args[4]));
                return true;
            case "double":
                Revenant.getModuleManager().getModuleByTag(args[1]).getSettingByTag(args[3]).setDouble(Double.parseDouble(args[4]));
                return true;
            case "float":
                Revenant.getModuleManager().getModuleByTag(args[1]).getSettingByTag(args[3]).setFloat(Float.parseFloat(args[4]));
                return true;
            case "bool":
                Revenant.getModuleManager().getModuleByTag(args[1]).getSettingByTag(args[3]).setBoolean(Boolean.parseBoolean(args[4]));
                return true;
            case "enum":
                Revenant.getModuleManager().getModuleByTag(args[1]).getSettingByTag(args[3]).setEnum(args[4]);
                return true;
        }

        return false;
    }
}
