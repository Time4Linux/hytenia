package de.pxscxl.bungee.proxysystem.commands;

import com.google.common.base.Joiner;
import com.google.gson.JsonObject;
import de.pxscxl.origin.bungee.Origin;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.NotifyManager;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.utils.enums.Rank;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotifyCommand extends Command {

    private final List<String> channels = new ArrayList<>();

    public NotifyCommand() {
        super("notify");
        channels.add("Ban");
        channels.add("Mute");
        channels.add("Kick");
        channels.add("Report");
        channels.add("Appeals");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (player.hasPriorityAccess(Rank.BUILDER.getPriority())) {
            if (args.length == 0) {
                OriginManager.getInstance().sendPluginChannel(player, "notify");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("sub") || args[0].equalsIgnoreCase("subscribe")) {
                    if (channels.stream().anyMatch(name -> Objects.equals(name.toLowerCase(), args[1].toLowerCase()))) {
                        String channel = channels.stream().filter(name -> Objects.equals(name.toLowerCase(), args[1].toLowerCase())).findFirst().orElse(null);
                        if (channel != null) {
                            JsonObject object = NotifyManager.getInstance().getObject(player.getUniqueId());

                            boolean state = !object.get(channel.toLowerCase()).getAsBoolean();
                            object.addProperty(channel.toLowerCase(), state);
                            NotifyManager.getInstance().setObject(player.getUniqueId(), object);

                            player.sendMessage(NotifyManager.getInstance().getPrefix() + "§7Channel§8: §b" + (channel.equals("Appeals") ? player.language("Entbannungsanträge", "Appeals") : channel) + " §8- §7Hooked§8: §a" + (state ? "§a✔" : "§c✘"));
                        }
                    } else {
                        player.sendMessage(
                                NotifyManager.getInstance().getPrefix() + "§7Dieser Channel wurde §cnicht §7gefunden!",
                                NotifyManager.getInstance().getPrefix() + "§7This channel was §cnot §7found!"
                        );
                        player.sendMessage(NotifyManager.getInstance().getPrefix() + "§7Channels§8: §f" + Joiner.on(", ").join(channels));
                    }
                }
            } else {
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/notify",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/notify"
                );
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/notify subscribe [Channel]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/notify subscribe [Channel]"
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
