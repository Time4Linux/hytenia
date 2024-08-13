package de.pxscxl.bungee.proxysystem.commands;

import de.pxscxl.origin.bungee.Origin;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.bungee.api.manager.PunishmentManager;
import de.pxscxl.origin.utils.enums.Rank;
import de.pxscxl.origin.utils.objects.origin.OriginPunishment;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Objects;

public class CheckIdCommand extends Command {

    public CheckIdCommand() {
        super("checkid");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (player.hasPriorityAccess(Rank.SUPPORTER.getPriority())) {
            if (args.length == 1) {
                OriginPunishment punishment = PunishmentManager.getInstance().getPunishments().stream().filter(originPunishment -> Objects.equals(originPunishment.getId(), args[0])).findFirst().orElse(null);
                if (punishment != null) {
                    player.sendMessage(Origin.getInstance().getPrefix() + "§7CheckID §9[" + OriginManager.getInstance().getName(punishment.getTarget()) + " - " + punishment.getId() + "]");
                    player.sendMessage("");
                    player.sendMessage(Origin.getInstance().getPrefix() + "§7" + (punishment.getType().equals(OriginPunishment.PunishmentType.BAN) ? player.language("Gebannt", "Banned") : player.language("Gemuted", "Muted")) + (punishment.isActive() ? "§a[✔]" : "§c[✘]"));
                    if (punishment.isActive()) {
                        player.sendMessage(Origin.getInstance().getPrefix() + "§7" + punishment.getReason() + " - " + punishment.getUntilAsDate());
                    }
                    player.sendMessage("");
                } else {
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Diese ID wurde §cnicht §7gefunden!",
                            Origin.getInstance().getPrefix() + "§7This id was §cnot §7found!"
                    );
                }
            } else {
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/checkid [ID]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/checkid [ID]"
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
