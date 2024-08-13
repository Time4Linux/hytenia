package de.pxscxl.spigot.qsg.utils.registration;

import de.pxscxl.origin.utils.Reflections;
import de.pxscxl.spigot.qsg.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;

public class CommandRegistration {

    private final SimpleCommandMap simpleCommandMap = Reflections.getField(Bukkit.getServer(), "commandMap");

    public void registerAllCommands() {
        simpleCommandMap.register("qsg", new StartCommand());
        simpleCommandMap.register("qsg", new ForcemapCommand());
        simpleCommandMap.register("qsg", new SetupCommand());
        simpleCommandMap.register("qsg", new StatsCommand());
        simpleCommandMap.register("qsg", new TopCommand());
    }
}
