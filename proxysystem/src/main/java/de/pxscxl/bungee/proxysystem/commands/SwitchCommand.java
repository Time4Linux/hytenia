package de.pxscxl.bungee.proxysystem.commands;

import de.pxscxl.origin.bungee.Origin;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.ClanManager;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SwitchCommand extends Command {

    public SwitchCommand() {
        super("gtmysrvr");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (args.length == 1) {
            ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(args[0]);
            if (serverInfo != null) {
                player.connect(serverInfo);
            } else {
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Dieser Server wurde §cnicht §7gefunden!",
                        Origin.getInstance().getPrefix() + "§7This server was §cnot §7found!"
                );
            }
        } else {
            player.sendMessage(
                    Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/gtmysrvr [Server]",
                    Origin.getInstance().getPrefix() + "§7Please use: §f/gtmysrvr [Server]"
            );
        }
    }
}
