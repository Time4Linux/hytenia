package de.pxscxl.bungee.proxysystem.commands;

import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.FriendManager;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.utils.objects.friends.Friend;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.stream.IntStream;

public class ReplyCommand extends Command {

    public ReplyCommand() {
        super("reply", null, "r");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (args.length >= 1) {
            OriginPlayer target = FriendManager.getInstance().getReplies().get(player);
            if (FriendManager.getInstance().getReplies().get(player) == target && FriendManager.getInstance().getReplies().get(target) == player) {
                if (target != null && target.isOnline()) {
                    Friend friend = FriendManager.getInstance().getFriend(player.getUniqueId(), target.getUniqueId());
                    if (friend != null) {
                        if (friend.getSettings().isReceivingMessage()) {
                            StringBuilder message = new StringBuilder();
                            IntStream.range(0, args.length).forEach(value -> message.append(" §f").append(args[value]));

                            player.sendMessage(FriendManager.getInstance().getPrefix() + player.getDisplayName() + " §8» " + target.getDisplayName() + "§8:§f" + message);
                            target.sendMessage(FriendManager.getInstance().getPrefix() + player.getDisplayName() + " §8» " + target.getDisplayName() + "§8:§f" + message);

                            FriendManager.getInstance().getReplies().put(player, target);
                            FriendManager.getInstance().getReplies().put(target, player);
                        } else {
                            player.sendMessage(
                                    FriendManager.getInstance().getPrefix() + "§7Du kannst diesem Spieler §ckeine §7Nachricht senden!",
                                    FriendManager.getInstance().getPrefix() + "§7You §ccan't §7send a message to this player!"
                            );
                        }
                    } else {
                        player.sendMessage(
                                FriendManager.getInstance().getPrefix() + "§7Du bist §cnicht §7mit diesem Spieler befreundet!",
                                FriendManager.getInstance().getPrefix() + "§7This player §cisn't §7your friend!"
                        );
                    }
                } else {
                    player.sendMessage(
                            FriendManager.getInstance().getPrefix() + "§7Dieser Spieler wurde §cnicht §7gefunden!",
                            FriendManager.getInstance().getPrefix() + "§7This player was §cnot §7found!"
                    );
                }
            } else {
                player.sendMessage(
                        FriendManager.getInstance().getPrefix() + "§7Du hast §ckeine §7zu beantwortenden Nachrichten!",
                        FriendManager.getInstance().getPrefix() + "§7You §cdon't §7have message to answer!"
                );
            }
        } else {
            player.sendMessage(FriendManager.getInstance().getPrefix() + "§f/reply [" + player.language("Nachricht", "Message") + "]");
        }
    }
}
