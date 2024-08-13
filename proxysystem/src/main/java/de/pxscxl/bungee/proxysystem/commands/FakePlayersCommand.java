package de.pxscxl.bungee.proxysystem.commands;

import de.pxscxl.bungee.proxysystem.ProxySystem;
import de.pxscxl.origin.bungee.Origin;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.bungee.api.manager.PingManager;
import de.pxscxl.origin.utils.enums.Rank;
import de.pxscxl.origin.utils.objects.origin.OriginServerPing;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class FakePlayersCommand extends Command {

    public FakePlayersCommand() {
        super("fakeplayers");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (player.hasPriorityAccess(Rank.ADMINISTRATOR.getPriority())) {
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("state")) {
                    if (args[1].equalsIgnoreCase("true")) {
                        player.sendMessage(
                                Origin.getInstance().getPrefix() + "§7Die FakePlayers wurden §aaktiviert",
                                Origin.getInstance().getPrefix() + "§7You have §aactivated §7the fake players"
                        );
                        OriginServerPing serverPing = PingManager.getInstance().getOriginServerPing();
                        serverPing.setFakePlayers(true);
                        ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> PingManager.getInstance().setOriginServerPing(serverPing));
                    } else if (args[1].equalsIgnoreCase("false")) {
                        player.sendMessage(
                                Origin.getInstance().getPrefix() + "§7Die FakePlayers wurden §cdeaktiviert",
                                Origin.getInstance().getPrefix() + "§7You have §cdeactivated §7the fake players"
                        );
                        OriginServerPing serverPing = PingManager.getInstance().getOriginServerPing();
                        serverPing.setFakePlayers(false);
                        ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> PingManager.getInstance().setOriginServerPing(serverPing));
                    } else {
                        player.sendMessage(
                                Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/fakeplayers state [true/false]",
                                Origin.getInstance().getPrefix() + "§7Please use: §f/fakeplayers state [true/false]"
                        );
                    }
                } else if (args[0].equalsIgnoreCase("count")) {
                    int count;
                    try {
                        count = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(
                                Origin.getInstance().getPrefix() + "§7Bitte gebe eine §cgültige §7Zahl an!",
                                Origin.getInstance().getPrefix() + "§7Please enter a §cvaild §7number!"
                        );
                        return;
                    }
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Du hast die FakePlayers-Spielerzahl §aaktualisiert",
                            Origin.getInstance().getPrefix() + "§7You have §aactualized §7the FakePlayers-Count"
                    );
                    player.sendMessage(Origin.getInstance().getPrefix() + " §f- §7" + player.language("Spieler", "Players") + "§8: §e" + count);

                    OriginServerPing serverPing = PingManager.getInstance().getOriginServerPing();
                    serverPing.setFakePlayersCount(count);
                    ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> PingManager.getInstance().setOriginServerPing(serverPing));
                } else {
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/fakeplayers state [true/false]",
                            Origin.getInstance().getPrefix() + "§7Please use: §f/fakeplayers state [true/false]"
                    );
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/fakeplayers count [Anzahl]",
                            Origin.getInstance().getPrefix() + "§7Please use: §f/fakeplayers count [Count]"
                    );
                }
            } else {
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/fakeplayers state [true/false]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/fakeplayers state [true/false]"
                );
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/fakeplayers count [Anzahl]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/fakeplayers count [Count]"
                );
            }
        } else {
            player.sendMessage(
                    Origin.getInstance().getPrefix() + "§7Du hast §ckeine §7Rechte diesen Befehl auszuführen!",
                    Origin.getInstance().getPrefix() + "§7You §cdon't §7have permission to perform this command!"
            );
        }
    }
}
