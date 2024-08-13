package de.pxscxl.bungee.proxysystem;

import de.pxscxl.bungee.proxysystem.registration.CommandRegistration;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

public class ProxySystem extends Plugin {

    @Getter
    private static ProxySystem instance;

    private CommandRegistration commandRegistration;

    @Override
    public void onLoad() {
        instance = this;

        commandRegistration = new CommandRegistration();
    }

    @Override
    public void onEnable() {
        commandRegistration.registerAllCommands();
    }
}
