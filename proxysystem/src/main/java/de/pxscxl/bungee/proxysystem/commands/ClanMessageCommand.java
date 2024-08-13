package de.pxscxl.bungee.proxysystem.commands;

import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.ClanManager;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.utils.objects.clans.Clan;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.stream.IntStream;

public class ClanMessageCommand extends Command {

    public ClanMessageCommand() {
        super("cc", null, "clanmessage");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (args.length >= 1) {
            Clan clan = ClanManager.getInstance().getClanByUuid(player.getUniqueId());
            if (clan != null) {
                StringBuilder message = new StringBuilder();
                IntStream.range(0, args.length).forEach(value -> message.append(" §f").append(args[value]));

                clan.getAllMembers().stream().map(uuid -> OriginManager.getInstance().getPlayer(uuid)).filter(OriginPlayer::isOnline).filter(players -> ClanManager.getInstance().getSettings(players.getUniqueId()).isReceivingMessage()).forEach(players -> players.sendMessage(ClanManager.getInstance().getPrefix() + player.getDisplayName() + "§8:§f" + message));
            } else {
                player.sendMessage(
                        ClanManager.getInstance().getPrefix() + "§7Du bist in §ckeinem §7Clan!",
                        ClanManager.getInstance().getPrefix() + "§7You §caren't §7in a clan!"
                );
            }
        } else {
            player.sendMessage(
                    ClanManager.getInstance().getPrefix() + "§7Bitte nutze: §f/cc [Nachricht]",
                    ClanManager.getInstance().getPrefix() + "§7Please use: §f/cc [Message]"
            );
        }
    }
}
