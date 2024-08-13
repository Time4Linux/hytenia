package de.pxscxl.bungee.proxysystem.commands;

import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.bungee.api.manager.PartyManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PartyCommand extends Command {

    public PartyCommand() {
        super("party");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        switch (args.length) {
            case 1:
                if (args[0].equalsIgnoreCase("list")) {
                    PartyManager.Party party = PartyManager.getInstance().getParty(player);
                    if (party != null) {
                        player.sendMessage("");
                        player.sendMessage(
                                PartyManager.getInstance().getPrefix() + "§7Liste aller Party§8-§7Mitglieder§8:",
                                PartyManager.getInstance().getPrefix() + "§7List of all Party§8-§7Members§8:"
                        );
                        player.sendMessage(PartyManager.getInstance().getPrefix() + "§7Leader§8: " + party.getLeader().getDisplayName());
                        player.sendMessage("");
                        player.sendMessage(PartyManager.getInstance().getPrefix() + "§7" + player.language("Mitglieder", "Members") + "§8:");
                        if (!party.getMembers().isEmpty()) {
                            party.getMembers().forEach(players -> player.sendMessage(PartyManager.getInstance().getPrefix() + " §f- " + players.getDisplayName() + " §f| §e" + players.getServer().getName()));
                        } else {
                            player.sendMessage(PartyManager.getInstance().getPrefix() + " §f- §7" + player.language("Es gibt derzeit §ckeine §7Mitglieder", "There are currently §cno §7members"));
                        }
                        player.sendMessage("");
                    } else {
                        player.sendMessage(
                                PartyManager.getInstance().getPrefix() + "§7Du bist in §ckeiner §7Party!",
                                PartyManager.getInstance().getPrefix() + "§7You §caren't §7in a party!"
                        );
                    }
                    return;
                } else if (args[0].equalsIgnoreCase("leave")) {
                    PartyManager.Party party = PartyManager.getInstance().getParty(player);
                    if (party != null) {
                        party.leave(player);
                    } else {
                        player.sendMessage(
                                PartyManager.getInstance().getPrefix() + "§7Du bist in §ckeiner §7Party!",
                                PartyManager.getInstance().getPrefix() + "§7You §caren't §7in a party!"
                        );
                    }
                    return;
                }
                sendHelpMessage(player);
                break;
            case 2:
                if (args[0].equalsIgnoreCase("invite")) {
                    PartyManager.Party party = (PartyManager.getInstance().getParty(player) != null ? PartyManager.getInstance().getParty(player) : new PartyManager.Party(player));
                    if (party != null) {
                        if (party.getLeader() == player) {
                            OriginPlayer target = OriginManager.getInstance().getPlayer(args[1]);
                            if (target != null && target.isOnline()) {
                                if (player != target) {
                                    if (PartyManager.getInstance().getParty(target) == null) {
                                        if (!party.getInvitations().contains(target)) {
                                            party.invite(target);
                                        } else {
                                            player.sendMessage(
                                                    PartyManager.getInstance().getPrefix() + "§7Dieser Spieler wurde §ebereits §7eingeladen!",
                                                    PartyManager.getInstance().getPrefix() + "§7This player was §ealready §7invited!"
                                            );
                                        }
                                    } else {
                                        player.sendMessage(
                                                PartyManager.getInstance().getPrefix() + "§7Dieser Spieler ist §ebereits §7in einer Party!",
                                                PartyManager.getInstance().getPrefix() + "§7This player is §ealready §7in a party!"
                                        );
                                    }
                                } else {
                                    player.sendMessage(
                                            PartyManager.getInstance().getPrefix() + "§7Du darfst §cnicht §7mit dir selbst interagieren!",
                                            PartyManager.getInstance().getPrefix() + "§7You §ccan't §7interact with yourself!"
                                    );
                                }
                            } else {
                                player.sendMessage(
                                        PartyManager.getInstance().getPrefix() + "§7Dieser Spieler wurde §cnicht §7gefunden!",
                                        PartyManager.getInstance().getPrefix() + "§7This player was §cnot §7found!"
                                );
                            }
                        } else {
                            player.sendMessage(
                                    PartyManager.getInstance().getPrefix() + "§7Du bist §cnicht §7der Leiter der Party!",
                                    PartyManager.getInstance().getPrefix() + "§7You §caren't §7the leader of the party!"
                            );
                        }
                    } else {
                        player.sendMessage(
                                PartyManager.getInstance().getPrefix() + "§7Du bist in §ckeiner §7Party!",
                                PartyManager.getInstance().getPrefix() + "§7You §caren't §7in a party!"
                        );
                    }
                    return;
                } else if (args[0].equalsIgnoreCase("accept")) {
                    OriginPlayer target = OriginManager.getInstance().getPlayer(args[1]);
                    if (target != null && target.isOnline()) {
                        PartyManager.Party party = PartyManager.getInstance().getParty(target);
                        if (party != null) {
                            if (party.getInvitations().contains(player)) {
                                party.acceptInvite(player);
                            } else {
                                player.sendMessage(
                                        PartyManager.getInstance().getPrefix() + "§7Du hast §ckeine §7Einladung zu dieser Party erhalten!",
                                        PartyManager.getInstance().getPrefix() + "§7You §chaven't §7received an invite to this party!"
                                );
                            }
                        } else {
                            player.sendMessage(
                                    PartyManager.getInstance().getPrefix() + "§7Dieser Spieler ist in §ckeiner §7Party!",
                                    PartyManager.getInstance().getPrefix() + "§7This player §cisn't §7in a party!"
                            );
                        }
                    } else {
                        player.sendMessage(
                                PartyManager.getInstance().getPrefix() + "§7Dieser Spieler wurde §cnicht §7gefunden!",
                                PartyManager.getInstance().getPrefix() + "§7This player was §cnot §7found!"
                        );
                    }
                    return;
                } else if (args[0].equalsIgnoreCase("deny")) {
                    OriginPlayer target = OriginManager.getInstance().getPlayer(args[1]);
                    if (target != null && target.isOnline()) {
                        PartyManager.Party party = PartyManager.getInstance().getParty(target);
                        if (party != null) {
                            if (party.getInvitations().contains(player)) {
                                party.denyInvite(player);
                            } else {
                                player.sendMessage(
                                        PartyManager.getInstance().getPrefix() + "§7Dieser Spieler wurde §ebereits §7eingeladen!",
                                        PartyManager.getInstance().getPrefix() + "§7This player was §ealready §7invited!"
                                );
                            }
                        } else {
                            player.sendMessage(
                                    PartyManager.getInstance().getPrefix() + "§7Du hast §ckeine §7Einladung zu dieser Party erhalten!",
                                    PartyManager.getInstance().getPrefix() + "§7You §chaven't §7received an invite to this party!"
                            );
                        }
                    } else {
                        player.sendMessage(
                                PartyManager.getInstance().getPrefix() + "§7Dieser Spieler wurde §cnicht §7gefunden!",
                                PartyManager.getInstance().getPrefix() + "§7This player was §cnot §7found!"
                        );
                    }
                    return;
                } else if (args[0].equalsIgnoreCase("kick")) {
                    PartyManager.Party party = PartyManager.getInstance().getParty(player);
                    if (party != null) {
                        if (party.getLeader() == player) {
                            OriginPlayer target = OriginManager.getInstance().getPlayer(args[1]);
                            if (target != null && target.isOnline()) {
                                if (player != target) {
                                    if (PartyManager.getInstance().getParty(target) == party) {
                                        party.leave(player);
                                    } else {
                                        player.sendMessage(
                                                PartyManager.getInstance().getPrefix() + "§7Dieser Spieler ist §cnicht §7in deiner Party!",
                                                PartyManager.getInstance().getPrefix() + "§7This player §cisn't §7in your party!"
                                        );
                                    }
                                } else {
                                    player.sendMessage(
                                            PartyManager.getInstance().getPrefix() + "§7Du darfst §cnicht §7mit dir selbst interagieren!",
                                            PartyManager.getInstance().getPrefix() + "§7You §ccan't §7interact with yourself!"
                                    );
                                }
                            } else {
                                player.sendMessage(
                                        PartyManager.getInstance().getPrefix() + "§7Dieser Spieler wurde §cnicht §7gefunden!",
                                        PartyManager.getInstance().getPrefix() + "§7This player was §cnot §7found!"
                                );
                            }
                        } else {
                            player.sendMessage(
                                    PartyManager.getInstance().getPrefix() + "§7Du bist §cnicht §7der Leader der Party!",
                                    PartyManager.getInstance().getPrefix() + "§7You §caren't §7the leader of the party!"
                            );
                        }
                    } else {
                        player.sendMessage(
                                PartyManager.getInstance().getPrefix() + "§7Du bist in §ckeiner §7Party!",
                                PartyManager.getInstance().getPrefix() + "§7You §caren't §7in a party!"
                        );
                    }
                    return;
                } else if (args[0].equalsIgnoreCase("promote")) {
                    PartyManager.Party party = PartyManager.getInstance().getParty(player);
                    if (party != null) {
                        if (party.getLeader() == player) {
                            OriginPlayer target = OriginManager.getInstance().getPlayer(args[1]);
                            if (target != null && target.isOnline()) {
                                if (PartyManager.getInstance().getParty(target) == party) {
                                    party.promote(player);
                                } else {
                                    player.sendMessage(
                                            PartyManager.getInstance().getPrefix() + "§7Dieser Spieler ist §cnicht §7in deiner Party!",
                                            PartyManager.getInstance().getPrefix() + "§7This player §cisn't §7in your party!"
                                    );
                                }
                            } else {
                                player.sendMessage(
                                        PartyManager.getInstance().getPrefix() + "§7Dieser Spieler wurde §cnicht §7gefunden!",
                                        PartyManager.getInstance().getPrefix() + "§7This player was §cnot §7found!"
                                );
                            }
                        } else {
                            player.sendMessage(
                                    PartyManager.getInstance().getPrefix() + "§7Du bist §cnicht §7der Leader der Party!",
                                    PartyManager.getInstance().getPrefix() + "§7You §caren't §7the leader of the party!"
                            );
                        }
                    } else {
                        player.sendMessage(
                                PartyManager.getInstance().getPrefix() + "§7Du bist in §ckeiner §7Party!",
                                PartyManager.getInstance().getPrefix() + "§7You §caren't §7in a party!"
                        );
                    }
                    return;
                }
                sendHelpMessage(player);
                break;
            default:
                sendHelpMessage(player);
                break;
        }
    }

    private void sendHelpMessage(OriginPlayer player) {
        player.sendMessage(
                PartyManager.getInstance().getPrefix() + "§7Bitte nutze: §f/party invite [Name]",
                PartyManager.getInstance().getPrefix() + "§7Please use: §f/party invite [Name]"
        );
        player.sendMessage(
                PartyManager.getInstance().getPrefix() + "§7Bitte nutze: §f/party accept [Clan-Name]",
                PartyManager.getInstance().getPrefix() + "§7Please use: §f/party accept [Clan-Name]"
        );
        player.sendMessage(
                PartyManager.getInstance().getPrefix() + "§7Bitte nutze: §f/party deny [Name]",
                PartyManager.getInstance().getPrefix() + "§7Please use: §f/party deny [Name]"
        );
        player.sendMessage(
                PartyManager.getInstance().getPrefix() + "§7Bitte nutze: §f/party kick [Name]",
                PartyManager.getInstance().getPrefix() + "§7Please use: §f/party kick [Name]"
        );
        player.sendMessage(
                PartyManager.getInstance().getPrefix() + "§7Bitte nutze: §f/party promote [Name]",
                PartyManager.getInstance().getPrefix() + "§7Please use: §f/party promote [Name]"
        );
        player.sendMessage(
                PartyManager.getInstance().getPrefix() + "§7Bitte nutze: §f/party leave",
                PartyManager.getInstance().getPrefix() + "§7Please use: §f/party leave"
        );
        player.sendMessage(
                PartyManager.getInstance().getPrefix() + "§7Bitte nutze: §f/party list",
                PartyManager.getInstance().getPrefix() + "§7Please use: §f/party list"
        );
        player.sendMessage(
                PartyManager.getInstance().getPrefix() + "§7Bitte nutze: §f/p [Nachricht]",
                PartyManager.getInstance().getPrefix() + "§7Please use: §f/p [Message]"
        );
    }
}
