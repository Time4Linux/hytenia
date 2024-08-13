package de.pxscxl.bungee.proxysystem.commands;

import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.bungee.api.manager.PartyManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.stream.IntStream;

public class PartyMessageCommand extends Command {

    public PartyMessageCommand() {
        super("p", null, "partymessage");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (args.length >= 1) {
            PartyManager.Party party = PartyManager.getInstance().getParty(player);
            if (party != null) {
                StringBuilder message = new StringBuilder();
                IntStream.range(0, args.length).forEach(value -> message.append(" §f").append(args[value]));

                party.getLeader().sendMessage(PartyManager.getInstance().getPrefix() + player.getDisplayName() + "§8:§f" + message);
                party.getMembers().forEach(players -> players.sendMessage(PartyManager.getInstance().getPrefix() + player.getDisplayName() + "§8:§f" + message));
            } else {
                player.sendMessage(
                        PartyManager.getInstance().getPrefix() + "§7Du bist in §ckeiner §7Party!",
                        PartyManager.getInstance().getPrefix() + "§7You §caren't §7in a party!"
                );
            }
        } else {
            player.sendMessage(
                    PartyManager.getInstance().getPrefix() + "§7Bitte nutze: §f/p [Nachricht]",
                    PartyManager.getInstance().getPrefix() + "§7Please use: §f/p [Message]"
            );
        }
    }
}
