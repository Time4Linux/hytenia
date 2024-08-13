package de.pxscxl.spigot.mlgrush.utils.registration;

import de.pxscxl.origin.utils.Reflections;
import de.pxscxl.spigot.mlgrush.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;

public class CommandRegistration {

    private final SimpleCommandMap simpleCommandMap = Reflections.getField(Bukkit.getServer(), "commandMap");

    public void registerAllCommands() {
        simpleCommandMap.register("mlgrush", new ForcemapCommand());
        simpleCommandMap.register("mlgrush", new SetupCommand());
        simpleCommandMap.register("mlgrush", new StartCommand());
        simpleCommandMap.register("mlgrush", new StatsCommand());
        simpleCommandMap.register("mlgrush", new TopCommand());
    }
}
