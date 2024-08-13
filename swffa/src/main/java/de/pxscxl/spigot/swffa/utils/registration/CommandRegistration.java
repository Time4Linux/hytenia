package de.pxscxl.spigot.swffa.utils.registration;

import de.pxscxl.origin.utils.Reflections;
import de.pxscxl.spigot.swffa.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;

public class CommandRegistration {

    private final SimpleCommandMap simpleCommandMap = Reflections.getField(Bukkit.getServer(), "commandMap");

    public void registerAllCommands() {
        simpleCommandMap.register("swffa", new ForcemapCommand());
        simpleCommandMap.register("swffa", new NextMapCommand());
        simpleCommandMap.register("swffa", new SetupCommand());
        simpleCommandMap.register("swffa", new StatsCommand());
        simpleCommandMap.register("swffa", new TopCommand());
    }
}
