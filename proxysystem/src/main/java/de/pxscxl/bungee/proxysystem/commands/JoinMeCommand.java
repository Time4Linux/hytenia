package de.pxscxl.bungee.proxysystem.commands;

import de.pxscxl.origin.bungee.Origin;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.utils.enums.Rank;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class JoinMeCommand extends Command {

    public JoinMeCommand() {
        super("joinme");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (player.hasPriorityAccess(Rank.PREMIUMPLUS.getPriority())) {
            if (args.length == 0) {
                if (!player.getServer().getName().toLowerCase().contains("lobby")) {
                    TextComponent german = new TextComponent("§7[§6✪§7] " + player.getDisplayName() + " §espielt derzeit auf §6§l" + player.getServer().getName() + "§e. §7§o(Betreten)");
                    german.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gtmysrvr " + player.getServer().getName()));

                    TextComponent english = new TextComponent("§7[§6✪§7] " + player.getDisplayName() + " §eis currently playing on §6§l" + player.getServer().getName() + "§e. §7§o(Join)");
                    english.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gtmysrvr " + player.getServer().getName()));

                    OriginManager.getInstance().getPlayers().forEach(players -> players.sendMessage(players.language(german, english)));
                } else {
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Du kannst §ckein §7JoinME auf der Lobby machen!",
                            Origin.getInstance().getPrefix() + "§7You §ccan't §7create a JoinME on a lobby!"
                    );
                }
            } else {
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/joinme",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/joinme"
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
