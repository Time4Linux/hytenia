package de.pxscxl.spigot.lobby.utils.registration;

import de.pxscxl.origin.utils.Reflections;
import de.pxscxl.spigot.lobby.commands.BuildCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;

public class CommandRegistration {

    private final SimpleCommandMap simpleCommandMap = Reflections.getField(Bukkit.getServer(), "commandMap");

    public void registerAllCommands() {
        simpleCommandMap.register("lobby", new BuildCommand());
    }
}
