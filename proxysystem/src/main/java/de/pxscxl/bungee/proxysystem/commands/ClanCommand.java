package de.pxscxl.bungee.proxysystem.commands;

import com.google.common.base.Joiner;
import de.pxscxl.bungee.proxysystem.ProxySystem;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.ClanManager;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.utils.enums.ClanRank;
import de.pxscxl.origin.utils.objects.clans.Clan;
import de.pxscxl.origin.utils.objects.clans.ClanRequest;
import de.pxscxl.origin.utils.objects.clans.ClanSettings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClanCommand extends Command {

    public ClanCommand() {
        super("clan");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (args.length == 0) {
            sendHelpMessage(player);
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("info")) {
                Clan clan = ClanManager.getInstance().getClanByUuid(player.getUniqueId());
                if (clan != null) {
                    sendClanInfoMessage(clan, player);
                } else {
                    player.sendMessage(
                            ClanManager.getInstance().getPrefix() + "§7Du bist in §ckeinem §7Clan!",
                            ClanManager.getInstance().getPrefix() + "§7You §caren't §7in a clan!"
                    );
                }
                return;
            } else if (args[0].equalsIgnoreCase("requests")) {
                List<ClanRequest> requests = ClanManager.getInstance().getRequests(player.getUniqueId());
                player.sendMessage("");
                player.sendMessage(
                        ClanManager.getInstance().getPrefix() + "§7Liste aller deiner Anfragen§8:",
                        ClanManager.getInstance().getPrefix() + "§7List of all your requests§8:"
                );
                if (!requests.isEmpty()) {
                    requests.forEach(request -> player.sendMessage(ClanManager.getInstance().getPrefix() + " §f- §e" + request.getClan().getName()));
                } else {
                    player.sendMessage(ClanManager.getInstance().getPrefix() + " §f- §7" + player.language("Du hast derzeit §ckeine §7Anfragen", "You §cdon't §7have any requests"));
                }
                player.sendMessage("");
                return;
            } else if (args[0].equalsIgnoreCase("leave")) {
                Clan clan = ClanManager.getInstance().getClanByUuid(player.getUniqueId());
                if (clan != null) {
                    if (OriginManager.getInstance().getPlayer(clan.getLeader()) != player) {
                        clan.getAllMembers().forEach(uuid -> {
                            OriginPlayer clanMember = OriginManager.getInstance().getPlayer(uuid);
                            if (clanMember != null && clanMember.isOnline()) {
                                clanMember.sendMessage(
                                        ClanManager.getInstance().getPrefix() + player.getDisplayName() + " §7hat den Clan §cverlassen",
                                        ClanManager.getInstance().getPrefix() + player.getDisplayName() + " §7has §cleft §7the clan"
                                );
                            }
                        });
                        ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> {
                            ClanSettings settings = ClanManager.getInstance().getSettings(player.getUniqueId());
                            settings.setClanName("-");
                            settings.setClanRank(ClanRank.NONE);
                            ClanManager.getInstance().setSettings(player.getUniqueId(), settings);
                        });
                    } else {
                        player.sendMessage(
                                ClanManager.getInstance().getPrefix() + "§7Du kannst den Clan §cnicht §7verlassen",
                                ClanManager.getInstance().getPrefix() + "§7You §ccan't §7leave the clan."
                        );
                    }
                } else {
                    player.sendMessage(
                            ClanManager.getInstance().getPrefix() + "§7Du bist in §ckeinem §7Clan!",
                            ClanManager.getInstance().getPrefix() + "§7You §caren't §7in a clan!"
                    );
                }
                return;
            } else if (args[0].equalsIgnoreCase("delete")) {
                Clan clan = ClanManager.getInstance().getClanByUuid(player.getUniqueId());
                if (clan != null) {
                    if (ClanManager.getInstance().getSettings(player.getUniqueId()).getClanRank().equals(ClanRank.LEADER)) {
                        player.sendMessage(
                                ClanManager.getInstance().getPrefix() + "§7Du hast den Clan erfolgreich §cgelöscht",
                                ClanManager.getInstance().getPrefix() + "§7You have §cdeleted §7the clan successfully"
                        );
                        clan.getAllMembers().forEach(uuid -> {
                            OriginPlayer clanMember = OriginManager.getInstance().getPlayer(uuid);
                            if (clanMember != null && clanMember.isOnline()) {
                                if (clanMember != player) {
                                    clanMember.sendMessage(
                                            ClanManager.getInstance().getPrefix() + "§7Der Clan wurde §caufgelöst",
                                            ClanManager.getInstance().getPrefix() + "§7The clan was §cdeleted"
                                    );
                                }
                            }
                        });
                        ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> ClanManager.getInstance().deleteClan(player.getUniqueId(), clan));
                    } else {
                        player.sendMessage(
                                ClanManager.getInstance().getPrefix() + "§7Du bist §cnicht §7der Leiter des Clans!",
                                ClanManager.getInstance().getPrefix() + "§7You §caren't §7the leader of the clan!"
                        );
                    }
                } else {
                    player.sendMessage(
                            ClanManager.getInstance().getPrefix() + "§7Du bist in §ckeinem §7Clan!",
                            ClanManager.getInstance().getPrefix() + "§7You §caren't §7in a clan!"
                    );
                }
                return;
            }
            sendHelpMessage(player);
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("description")) {
            Clan clan = ClanManager.getInstance().getClanByUuid(player.getUniqueId());
            if (clan != null) {
                if (ClanManager.getInstance().getSettings(player.getUniqueId()).getClanRank().equals(ClanRank.LEADER)) {
                    StringBuilder description = new StringBuilder();
                    IntStream.range(1, args.length).forEach(value -> description.append(value == 1 ? "" : " ").append(args[value]));
                    clan.setDescription(String.valueOf(description));

                    player.sendMessage(
                            ClanManager.getInstance().getPrefix() + "§7Die neue Beschreibung des Clans lautet§8:",
                            ClanManager.getInstance().getPrefix() + "§7The new description of the clan is§8:"
                    );
                    player.sendMessage(ClanManager.getInstance().getPrefix() + " §f- §e" + description);

                    ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> ClanManager.getInstance().updateClan(player.getUniqueId(), clan));
                } else {
                    player.sendMessage(
                            ClanManager.getInstance().getPrefix() + "§7Du bist §cnicht §7der Leiter des Clans!",
                            ClanManager.getInstance().getPrefix() + "§7You §caren't §7the leader of the clan!"
                    );
                }
            } else {
                player.sendMessage(
                        ClanManager.getInstance().getPrefix() + "§7Du bist in §ckeinem §7Clan!",
                        ClanManager.getInstance().getPrefix() + "§7You §caren't §7in a clan!"
                );
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("invite")) {
                Clan clan = ClanManager.getInstance().getClanByUuid(player.getUniqueId());
                if (clan != null) {
                    if (ClanManager.getInstance().getSettings(player.getUniqueId()).getClanRank().equals(ClanRank.LEADER)) {
                        OriginPlayer target = OriginManager.getInstance().getPlayer(args[1]);
                        if (target != null) {
                            if (player != target) {
                                ClanRequest request = ClanManager.getInstance().getRequest(target.getUniqueId(), clan);
                                if (request == null) {
                                    if (ClanManager.getInstance().getSettings(target.getUniqueId()).isReceivingRequests()) {
                                        player.sendMessage(
                                                ClanManager.getInstance().getPrefix() + "§7Du hast " + target.getDisplayName() + " §7eine Anfrage gesendet",
                                                ClanManager.getInstance().getPrefix() + "§7You've sent a request to " + target.getDisplayName()
                                        );
                                        if (target.isOnline()) {
                                            TextComponent accept = new TextComponent(ClanManager.getInstance().getPrefix() + "§7" + player.language("Annehmen", "Accept") + "§8: §a/clan accept " + clan.getName());
                                            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan accept " + clan.getName()));

                                            TextComponent deny = new TextComponent(ClanManager.getInstance().getPrefix() + "§7" + player.language("Ablehnen", "Deny") + "§8: §c/clan deny " + clan.getName());
                                            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan deny " + clan.getName()));

                                            target.sendMessage(
                                                    ClanManager.getInstance().getPrefix() + player.getDisplayName() + " §7hat dir eine Anfrage gesendet",
                                                    ClanManager.getInstance().getPrefix() + player.getDisplayName() + " §7has sent you a request"
                                            );
                                            target.sendMessage(accept);
                                            target.sendMessage(deny);
                                        }
                                        ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> ClanManager.getInstance().insertRequests(target.getUniqueId(), clan, new Date().getTime()));
                                    } else {
                                        player.sendMessage(
                                                ClanManager.getInstance().getPrefix() + "§7Du kannst diesem Spieler §ckeine §7Anfrage senden!",
                                                ClanManager.getInstance().getPrefix() + "§7You §ccan't §7send a request to this player!"
                                        );
                                    }
                                } else {
                                    player.sendMessage(
                                            ClanManager.getInstance().getPrefix() + "§7Du hast diesem Spieler §ebereits §7eine Anfrage gesendet!",
                                            ClanManager.getInstance().getPrefix() + "§7You've §ealready §7sent a request to this player!"
                                    );
                                }
                            } else {
                                player.sendMessage(
                                        ClanManager.getInstance().getPrefix() + "§7Du darfst §cnicht §7mit dir selbst interagieren!",
                                        ClanManager.getInstance().getPrefix() + "§7You §ccan't §7interact with yourself!"
                                );
                            }
                        } else {
                            player.sendMessage(
                                    ClanManager.getInstance().getPrefix() + "§7Dieser Spieler wurde §cnicht §7gefunden!",
                                    ClanManager.getInstance().getPrefix() + "§7This player was §cnot §7found!"
                            );
                        }
                    } else {
                        player.sendMessage(
                                ClanManager.getInstance().getPrefix() + "§7Du bist §cnicht §7der Leiter des Clans!",
                                ClanManager.getInstance().getPrefix() + "§7You §caren't §7the leader of the clan!"
                        );
                    }
                } else {
                    player.sendMessage(
                            ClanManager.getInstance().getPrefix() + "§7Du bist in §ckeinem §7Clan!",
                            ClanManager.getInstance().getPrefix() + "§7You §caren't §7in a clan!"
                    );
                }
                return;
            } else if (args[0].equalsIgnoreCase("accept")) {
                if (ClanManager.getInstance().getClanByUuid(player.getUniqueId()) == null) {
                    Clan clan = ClanManager.getInstance().getClans().stream().filter(clans -> clans.getName().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                    if (clan != null) {
                        ClanRequest request = ClanManager.getInstance().getRequest(player.getUniqueId(), clan);
                        if (request != null) {
                            player.sendMessage(
                                    ClanManager.getInstance().getPrefix() + "§7Du hast die Anfrage §aangenommen",
                                    ClanManager.getInstance().getPrefix() + "§7You §aaccepted §7the request"
                            );
                            clan.getAllMembers().forEach(uuid -> {
                                OriginPlayer clanMember = OriginManager.getInstance().getPlayer(uuid);
                                if (clanMember != null && clanMember.isOnline()) {
                                    clanMember.sendMessage(
                                            ClanManager.getInstance().getPrefix() + player.getDisplayName() + " §7hat den Clan §abetreten",
                                            ClanManager.getInstance().getPrefix() + player.getDisplayName() + " §7has §ajoined §7the clan"
                                    );
                                }
                            });
                            ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> {
                                ClanManager.getInstance().deleteRequest(player.getUniqueId(), clan);

                                ClanSettings settings = ClanManager.getInstance().getSettings(player.getUniqueId());
                                settings.setClanName(clan.getName());
                                settings.setClanRank(ClanRank.MEMBER);
                                ClanManager.getInstance().setSettings(player.getUniqueId(), settings);
                            });
                        } else {
                            player.sendMessage(
                                    ClanManager.getInstance().getPrefix() + "§7Dieser Clan hat dir §ckeine §7Anfrage gesendet",
                                    ClanManager.getInstance().getPrefix() + "§7This clan §chasn't §7sent you a request"
                            );
                        }
                    } else {
                        player.sendMessage(
                                ClanManager.getInstance().getPrefix() + "§7Dieser Clan wurde §cnicht §7gefunden!",
                                ClanManager.getInstance().getPrefix() + "§7This clan was §cnot §7found!"
                        );
                    }
                } else {
                    player.sendMessage(
                            ClanManager.getInstance().getPrefix() + "§7Du bist §ebereits §7in einem Clan!",
                            ClanManager.getInstance().getPrefix() + "§7You are §ealready §7in a clan!"
                    );
                }
                return;
            } else if (args[0].equalsIgnoreCase("deny")) {
                Clan clan = ClanManager.getInstance().getClans().stream().filter(clans -> clans.getName().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                if (clan != null) {
                    ClanRequest request = ClanManager.getInstance().getRequest(player.getUniqueId(), clan);
                    if (request != null) {
                        player.sendMessage(
                                ClanManager.getInstance().getPrefix() + "§7Du hast die Anfrage §cabgelehnt",
                                ClanManager.getInstance().getPrefix() + "§7You §cdenied §7the request"
                        );
                        ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> ClanManager.getInstance().deleteRequest(player.getUniqueId(), clan));
                    } else {
                        player.sendMessage(
                                ClanManager.getInstance().getPrefix() + "§7Dieser Clan hat dir §ckeine §7Anfrage gesendet",
                                ClanManager.getInstance().getPrefix() + "§7This clan §chasn't §7sent you a request"
                        );
                    }
                } else {
                    player.sendMessage(
                            ClanManager.getInstance().getPrefix() + "§7Dieser Clan wurde §cnicht §7gefunden!",
                            ClanManager.getInstance().getPrefix() + "§7This clan was §cnot §7found!"
                    );
                }
                return;
            } else if (args[0].equalsIgnoreCase("kick")) {
                Clan clan = ClanManager.getInstance().getClanByUuid(player.getUniqueId());
                if (clan != null) {
                    if (ClanManager.getInstance().getSettings(player.getUniqueId()).getClanRank().equals(ClanRank.LEADER)) {
                        OriginPlayer target = OriginManager.getInstance().getPlayer(args[1]);
                        if (target != null) {
                            if (player != target) {
                                clan.getAllMembers().forEach(uuid -> {
                                    OriginPlayer clanMember = OriginManager.getInstance().getPlayer(uuid);
                                    if (clanMember != null && clanMember.isOnline()) {
                                        clanMember.sendMessage(
                                                ClanManager.getInstance().getPrefix() + target.getDisplayName() + " §7hat den Clan §cverlassen",
                                                ClanManager.getInstance().getPrefix() + target.getDisplayName() + " §7has §cleft §7the clan"
                                        );
                                    }
                                });
                                ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> {
                                    ClanSettings settings = ClanManager.getInstance().getSettings(target.getUniqueId());
                                    settings.setClanName("-");
                                    settings.setClanRank(ClanRank.NONE);
                                    ClanManager.getInstance().setSettings(target.getUniqueId(), settings);
                                });
                            } else {
                                player.sendMessage(
                                        ClanManager.getInstance().getPrefix() + "§7Du darfst §cnicht §7mit dir selbst interagieren!",
                                        ClanManager.getInstance().getPrefix() + "§7You §ccan't §7interact with yourself!"
                                );
                            }
                        } else {
                            player.sendMessage(
                                    ClanManager.getInstance().getPrefix() + "§7Dieser Spieler wurde §cnicht §7gefunden!",
                                    ClanManager.getInstance().getPrefix() + "§7This player was §cnot §7found!"
                            );
                        }
                    } else {
                        player.sendMessage(
                                ClanManager.getInstance().getPrefix() + "§7Du bist §cnicht §7der Leiter des Clans!",
                                ClanManager.getInstance().getPrefix() + "§7You §caren't §7the leader of the clan!"
                        );
                    }
                } else {
                    player.sendMessage(
                            ClanManager.getInstance().getPrefix() + "§7Du bist in §ckeinem §7Clan!",
                            ClanManager.getInstance().getPrefix() + "§7You §caren't §7in a clan!"
                    );
                }
                return;
            } else if (args[0].equalsIgnoreCase("promote")) {
                Clan clan = ClanManager.getInstance().getClanByUuid(player.getUniqueId());
                if (clan != null) {
                    if (ClanManager.getInstance().getSettings(player.getUniqueId()).getClanRank().equals(ClanRank.LEADER)) {
                        OriginPlayer target = OriginManager.getInstance().getPlayer(args[1]);
                        if (target != null) {
                            if (player != target) {
                                if (clan.getMembers().contains(target.getUniqueId())) {
                                    player.sendMessage(
                                            ClanManager.getInstance().getPrefix() + target.getDisplayName() + " §7wurde zum §cModerator §7befördert",
                                            ClanManager.getInstance().getPrefix() + target.getDisplayName() + " §7was promoted to §cmoderator"
                                    );

                                    ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> {
                                        ClanSettings settings = ClanManager.getInstance().getSettings(target.getUniqueId());
                                        settings.setClanRank(ClanRank.MODERATOR);
                                        ClanManager.getInstance().setSettings(target.getUniqueId(), settings);
                                    });
                                } else if (clan.getModerators().contains(target.getUniqueId())) {
                                    player.sendMessage(
                                            ClanManager.getInstance().getPrefix() + target.getDisplayName() + " §7wurde zum §4Leiter §7befördert",
                                            ClanManager.getInstance().getPrefix() + target.getDisplayName() + " §7was promoted to §4leader"
                                    );

                                    ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> ClanManager.getInstance().setClanLeader(player.getUniqueId(), target.getUniqueId()));
                                } else {
                                    player.sendMessage(
                                            ClanManager.getInstance().getPrefix() + "§7Du kannst diesen Spieler §cnicht §7befördern!",
                                            ClanManager.getInstance().getPrefix() + "§7You §ccan't §7promote this player!"
                                    );
                                }
                            } else {
                                player.sendMessage(
                                        ClanManager.getInstance().getPrefix() + "§7Du darfst §cnicht §7mit dir selbst interagieren!",
                                        ClanManager.getInstance().getPrefix() + "§7You §ccan't §7interact with yourself!"
                                );
                            }
                        } else {
                            player.sendMessage(
                                    ClanManager.getInstance().getPrefix() + "§7Dieser Spieler wurde §cnicht §7gefunden!",
                                    ClanManager.getInstance().getPrefix() + "§7This player was §cnot §7found!"
                            );
                        }
                    } else {
                        player.sendMessage(
                                ClanManager.getInstance().getPrefix() + "§7Du bist §cnicht §7der Leiter des Clans!",
                                ClanManager.getInstance().getPrefix() + "§7You §caren't §7the leader of the clan!"
                        );
                    }
                } else {
                    player.sendMessage(
                            ClanManager.getInstance().getPrefix() + "§7Du bist in §ckeinem §7Clan!",
                            ClanManager.getInstance().getPrefix() + "§7You §caren't §7in a clan!"
                    );
                }
                return;
            } else if (args[0].equalsIgnoreCase("demote")) {
                Clan clan = ClanManager.getInstance().getClanByUuid(player.getUniqueId());
                if (clan != null) {
                    if (ClanManager.getInstance().getSettings(player.getUniqueId()).getClanRank().equals(ClanRank.LEADER)) {
                        OriginPlayer target = OriginManager.getInstance().getPlayer(args[1]);
                        if (target != null) {
                            if (player != target) {
                                if (clan.getModerators().contains(target.getUniqueId())) {
                                    clan.getModerators().remove(target.getUniqueId());
                                    clan.getMembers().add(target.getUniqueId());

                                    player.sendMessage(
                                            ClanManager.getInstance().getPrefix() + target.getDisplayName() + " §7wurde zum Mitglied degradiert",
                                            ClanManager.getInstance().getPrefix() + target.getDisplayName() + " §7was demoted to member"
                                    );

                                    ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> {
                                        ClanSettings settings = ClanManager.getInstance().getSettings(target.getUniqueId());
                                        settings.setClanRank(ClanRank.MEMBER);
                                        ClanManager.getInstance().setSettings(target.getUniqueId(), settings);
                                    });
                                } else {
                                    player.sendMessage(
                                            ClanManager.getInstance().getPrefix() + "§7Du kannst diesen Spieler §cnicht §7degradieren!",
                                            ClanManager.getInstance().getPrefix() + "§7You §ccan't §7demote this player!"
                                    );
                                }
                            } else {
                                player.sendMessage(
                                        ClanManager.getInstance().getPrefix() + "§7Du darfst §cnicht §7mit dir selbst interagieren!",
                                        ClanManager.getInstance().getPrefix() + "§7You §ccan't §7interact with yourself!"
                                );
                            }
                        } else {
                            player.sendMessage(
                                    ClanManager.getInstance().getPrefix() + "§7Dieser Spieler wurde §cnicht §7gefunden!",
                                    ClanManager.getInstance().getPrefix() + "§7This player was §cnot §7found!"
                            );
                        }
                    } else {
                        player.sendMessage(
                                ClanManager.getInstance().getPrefix() + "§7Du bist §cnicht §7der Leiter des Clans!",
                                ClanManager.getInstance().getPrefix() + "§7You §caren't §7the leader of the clan!"
                        );
                    }
                } else {
                    player.sendMessage(
                            ClanManager.getInstance().getPrefix() + "§7Du bist in §ckeinem §7Clan!",
                            ClanManager.getInstance().getPrefix() + "§7You §caren't §7in a clan!"
                    );
                }
                return;
            } else if (args[0].equalsIgnoreCase("color")) {
                Clan clan = ClanManager.getInstance().getClanByUuid(player.getUniqueId());
                if (clan != null) {
                    if (ClanManager.getInstance().getSettings(player.getUniqueId()).getClanRank().equals(ClanRank.LEADER)) {
                        if (getColors().stream().anyMatch(color -> color.getName().equalsIgnoreCase(args[1]))) {
                            ChatColor chatColor = getColors().stream().filter(color -> color.name().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                            if (chatColor != null) {
                                player.sendMessage(
                                        ClanManager.getInstance().getPrefix() + "§7Die neue Farbe des Tags ist§8: " + chatColor + chatColor.getName().toUpperCase(),
                                        ClanManager.getInstance().getPrefix() + "§7The new color of the tag is§8: " + chatColor + chatColor.getName().toUpperCase()
                                );
                                clan.setColor(chatColor);
                                ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> ClanManager.getInstance().updateClan(player.getUniqueId(), clan));
                            }
                        } else {
                            player.sendMessage(
                                    ClanManager.getInstance().getPrefix() + "§7Diese Farbe wurde §cnicht §7gefunden!",
                                    ClanManager.getInstance().getPrefix() + "§7This color was §cnot §7found!"
                            );
                            player.sendMessage(ClanManager.getInstance().getPrefix() + "§7" + player.language("Farben", "Colors") + "§8: §f" + Joiner.on(", ").join(getColors().stream().map(ChatColor::name).collect(Collectors.toList())));
                        }
                    } else {
                        player.sendMessage(
                                ClanManager.getInstance().getPrefix() + "§7Du bist §cnicht §7der Leiter des Clans!",
                                ClanManager.getInstance().getPrefix() + "§7You §caren't §7the leader of the clan!"
                        );
                    }
                } else {
                    player.sendMessage(
                            ClanManager.getInstance().getPrefix() + "§7Du bist in §ckeinem §7Clan!",
                            ClanManager.getInstance().getPrefix() + "§7You §caren't §7in a clan!"
                    );
                }
                return;
            } else if (args[0].equalsIgnoreCase("info")) {
                OriginPlayer target = OriginManager.getInstance().getPlayer(args[1]);
                if (target != null) {
                    Clan clan = ClanManager.getInstance().getClanByUuid(target.getUniqueId());
                    if (clan != null) {
                        sendClanInfoMessage(clan, player);
                    } else {
                        player.sendMessage(
                                ClanManager.getInstance().getPrefix() + "§7Dieser Spieler ist in §ckeinem §7Clan!",
                                ClanManager.getInstance().getPrefix() + "§7This player §cisn't §7in a clan!"
                        );
                    }
                } else {
                    player.sendMessage(
                            ClanManager.getInstance().getPrefix() + "§7Dieser Spieler wurde §cnicht §7gefunden!",
                            ClanManager.getInstance().getPrefix() + "§7This player was §cnot §7found!"
                    );
                }
                return;
            } else if (args[0].equalsIgnoreCase("tinfo")) {
                Clan clan = ClanManager.getInstance().getClans().stream().filter(clans -> clans.getTag().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                if (clan != null) {
                    sendClanInfoMessage(clan, player);
                } else {
                    player.sendMessage(
                            ClanManager.getInstance().getPrefix() + "§7Dieser Clan wurde §cnicht §7gefunden!",
                            ClanManager.getInstance().getPrefix() + "§7This clan was §cnot §7found!"
                    );
                }
                return;
            }
            sendHelpMessage(player);
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("create")) {
                Clan clan = ClanManager.getInstance().getClanByUuid(player.getUniqueId());
                if (clan == null) {
                    String name = args[1];
                    String tag = args[2];

                    if (name.length() >= 3 && name.length() <= 15) {
                        if (ClanManager.getInstance().getClans().stream().noneMatch(clans -> clans.getName().equalsIgnoreCase(name))) {
                            if (name.matches("^[a-zA-Z]*$")) {
                                if (tag.length() >= 2 && tag.length() <= 5) {
                                    if (ClanManager.getInstance().getClans().stream().noneMatch(clans -> clans.getTag().equalsIgnoreCase(tag))) {
                                        if (tag.matches("^[a-zA-Z]*$")) {
                                            player.sendMessage(
                                                    ClanManager.getInstance().getPrefix() + "§7Der Clan §e" + name + " §7mit dem Kürzel §8[§7" + tag + "§8] §7wurde erstellt",
                                                    ClanManager.getInstance().getPrefix() + "§7The clan §e" + name + " §7with the abbreviation §8[§7" + tag + "§8] §7was created"
                                            );
                                            ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> ClanManager.getInstance().insertClan(player.getUniqueId(), name, tag, "No description", 100, new Date().getTime()));
                                        } else {
                                            player.sendMessage(
                                                    ClanManager.getInstance().getPrefix() + "§7Das Kürzel enthält §cungültige §7Zeichen!",
                                                    ClanManager.getInstance().getPrefix() + "§7The abbreviation contains §cinvalid §7characters!"
                                            );
                                        }
                                    } else {
                                        player.sendMessage(
                                                ClanManager.getInstance().getPrefix() + "§7Das Kürzel ist §ebereits §7vergeben!",
                                                ClanManager.getInstance().getPrefix() + "§7The abbreviation is §ealready §7in use!"
                                        );
                                    }
                                } else {
                                    player.sendMessage(
                                            ClanManager.getInstance().getPrefix() + "§7Das Kürzel §emuss §72-5 Zeichen haben!",
                                            ClanManager.getInstance().getPrefix() + "§7The abbreviation §emust §7have 2-5 characters!"
                                    );
                                }
                            } else {
                                player.sendMessage(
                                        ClanManager.getInstance().getPrefix() + "§7Der Name enthält §cungültige §7Zeichen!",
                                        ClanManager.getInstance().getPrefix() + "§7The name contains §cinvalid §7characters!"
                                );
                            }
                        } else {
                            player.sendMessage(
                                    ClanManager.getInstance().getPrefix() + "§7Der Name ist §ebereits §7vergeben!",
                                    ClanManager.getInstance().getPrefix() + "§7The name is §ealready §7in use!"
                            );
                        }
                    } else {
                        player.sendMessage(
                                ClanManager.getInstance().getPrefix() + "§7Der Name §emuss §73-15 Zeichen haben!",
                                ClanManager.getInstance().getPrefix() + "§7The name §emust §7have 3-15 characters!"
                        );
                    }
                } else {
                    player.sendMessage(
                            ClanManager.getInstance().getPrefix() + "§7Du bist §ebereits §7in einem Clan!",
                            ClanManager.getInstance().getPrefix() + "§7You are §ealready §7in a clan!"
                    );
                }
                return;
            } else if (args[0].equalsIgnoreCase("rename")) {
                Clan clan = ClanManager.getInstance().getClanByUuid(player.getUniqueId());
                if (clan != null) {
                    String name = args[1];
                    String tag = args[2];

                    if (ClanManager.getInstance().getSettings(player.getUniqueId()).getClanRank().equals(ClanRank.LEADER)) {
                        if (name.length() >= 3 && name.length() <= 15) {
                            if (ClanManager.getInstance().getClans().stream().noneMatch(clans -> clans.getName().equalsIgnoreCase(name))) {
                                if (name.matches("^[a-zA-Z]*$")) {
                                    if (tag.length() >= 2 && tag.length() <= 5) {
                                        if (ClanManager.getInstance().getClans().stream().noneMatch(clans -> clans.getTag().equalsIgnoreCase(tag))) {
                                            if (tag.matches("^[a-zA-Z]*$")) {
                                                clan.setName(name);
                                                clan.setTag(tag);
                                                player.sendMessage(
                                                        ClanManager.getInstance().getPrefix() + "§7Der Clan wurde zu §e" + name + " §7mit dem Kürzel §8[§7" + tag + "§8] §7umbenannt",
                                                        ClanManager.getInstance().getPrefix() + "§7The clan was renamed to §e" + name + " §7with the abbreviation §8[§7" + tag + "§8]"
                                                );
                                                ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> ClanManager.getInstance().updateClan(player.getUniqueId(), clan));
                                            } else {
                                                player.sendMessage(
                                                        ClanManager.getInstance().getPrefix() + "§7Das Kürzel enthält §cungültige §7Zeichen!",
                                                        ClanManager.getInstance().getPrefix() + "§7The abbreviation contains §cinvalid §7characters!"
                                                );
                                            }
                                        } else {
                                            player.sendMessage(
                                                    ClanManager.getInstance().getPrefix() + "§7Das Kürzel ist §ebereits §7vergeben!",
                                                    ClanManager.getInstance().getPrefix() + "§7The abbreviation is §ealready §7in use!"
                                            );
                                        }
                                    } else {
                                        player.sendMessage(
                                                ClanManager.getInstance().getPrefix() + "§7Das Kürzel §emuss §72-5 Zeichen haben!",
                                                ClanManager.getInstance().getPrefix() + "§7The abbreviation §emust §7have 2-5 characters!"
                                        );
                                    }
                                } else {
                                    player.sendMessage(
                                            ClanManager.getInstance().getPrefix() + "§7Der Name enthält §cungültige §7Zeichen!",
                                            ClanManager.getInstance().getPrefix() + "§7The name contains §cinvalid §7characters!"
                                    );
                                }
                            } else {
                                player.sendMessage(
                                        ClanManager.getInstance().getPrefix() + "§7Der Name ist §ebereits §7vergeben!",
                                        ClanManager.getInstance().getPrefix() + "§7The name is §ealready §7in use!"
                                );
                            }
                        } else {
                            player.sendMessage(
                                    ClanManager.getInstance().getPrefix() + "§7Der Name §emuss §73-15 Zeichen haben!",
                                    ClanManager.getInstance().getPrefix() + "§7The name §emust §7have 3-15 characters!"
                            );
                        }
                    } else {
                        player.sendMessage(
                                ClanManager.getInstance().getPrefix() + "§7Du bist §cnicht §7der Leiter des Clans!",
                                ClanManager.getInstance().getPrefix() + "§7You §caren't §7the leader of the clan!"
                        );
                    }
                } else {
                    player.sendMessage(
                            ClanManager.getInstance().getPrefix() + "§7Du bist in §ckeinem §7Clan!",
                            ClanManager.getInstance().getPrefix() + "§7You §caren't §7in a clan!"
                    );
                }
                return;
            }
            sendHelpMessage(player);
        }
    }

    private void sendHelpMessage(OriginPlayer player) {
        player.sendMessage(
                ClanManager.getInstance().getPrefix() + "§7Bitte nutze: §f/clan create [Clan-Name] [Clan-Tag]",
                ClanManager.getInstance().getPrefix() + "§7Please use: §f/clan create [Clan-Name] [Clan-Tag]"
        );
        player.sendMessage(
                ClanManager.getInstance().getPrefix() + "§7Bitte nutze: §f/clan rename [Clan-Name] [Clan-Tag]",
                ClanManager.getInstance().getPrefix() + "§7Please use: §f/clan rename [Clan-Name] [Clan-Tag]"
        );
        player.sendMessage(
                ClanManager.getInstance().getPrefix() + "§7Bitte nutze: §f/clan description [Beschreibung]",
                ClanManager.getInstance().getPrefix() + "§7Please use: §f/clan description [Description]"
        );
        player.sendMessage(
                ClanManager.getInstance().getPrefix() + "§7Bitte nutze: §f/clan color [Farbe]",
                ClanManager.getInstance().getPrefix() + "§7Please use: §f/clan color [Color]"
        );
        player.sendMessage(
                ClanManager.getInstance().getPrefix() + "§7Bitte nutze: §f/clan invite [Name]",
                ClanManager.getInstance().getPrefix() + "§7Please use: §f/clan invite [Name]"
        );
        player.sendMessage(
                ClanManager.getInstance().getPrefix() + "§7Bitte nutze: §f/clan accept [Clan-Name]",
                ClanManager.getInstance().getPrefix() + "§7Please use: §f/clan accept [Clan-Name]"
        );
        player.sendMessage(
                ClanManager.getInstance().getPrefix() + "§7Bitte nutze: §f/clan deny [Name]",
                ClanManager.getInstance().getPrefix() + "§7Please use: §f/clan deny [Name]"
        );
        player.sendMessage(
                ClanManager.getInstance().getPrefix() + "§7Bitte nutze: §f/clan kick [Name]",
                ClanManager.getInstance().getPrefix() + "§7Please use: §f/clan kick [Name]"
        );
        player.sendMessage(
                ClanManager.getInstance().getPrefix() + "§7Bitte nutze: §f/clan promote [Name]",
                ClanManager.getInstance().getPrefix() + "§7Please use: §f/clan promote [Name]"
        );
        player.sendMessage(
                ClanManager.getInstance().getPrefix() + "§7Bitte nutze: §f/clan demote [Name]",
                ClanManager.getInstance().getPrefix() + "§7Please use: §f/clan demote [Name]"
        );
        player.sendMessage(
                ClanManager.getInstance().getPrefix() + "§7Bitte nutze: §f/clan info [Name]",
                ClanManager.getInstance().getPrefix() + "§7Please use: §f/clan info [Name]"
        );
        player.sendMessage(
                ClanManager.getInstance().getPrefix() + "§7Bitte nutze: §f/clan tinfo [Clan-Tag]",
                ClanManager.getInstance().getPrefix() + "§7Please use: §f/clan tinfo [Clan-Tag]"
        );
        player.sendMessage(
                ClanManager.getInstance().getPrefix() + "§7Bitte nutze: §f/clan requests",
                ClanManager.getInstance().getPrefix() + "§7Please use: §f/clan requests"
        );
        player.sendMessage(
                ClanManager.getInstance().getPrefix() + "§7Bitte nutze: §f/cc [Nachricht]",
                ClanManager.getInstance().getPrefix() + "§7Please use: §f/cc [Message]"
        );
    }

    private void sendClanInfoMessage(Clan clan, OriginPlayer player) {
        player.sendMessage("");
        player.sendMessage(ClanManager.getInstance().getPrefix() + "§7Name§8: §e" + clan.getName());
        player.sendMessage(ClanManager.getInstance().getPrefix() + "§7Tag§8: " + clan.getColor() + clan.getTag());
        player.sendMessage(ClanManager.getInstance().getPrefix() + "§7Elo§8: §e" + clan.getElo());
        player.sendMessage("");
        player.sendMessage(ClanManager.getInstance().getPrefix() + "§7" + player.language("Beschreibung", "Description"));
        player.sendMessage(ClanManager.getInstance().getPrefix() + " §f- §e" + clan.getDescription());
        player.sendMessage("");
        player.sendMessage(ClanManager.getInstance().getPrefix() + "§7Leader§8: " + OriginManager.getInstance().getPlayer(clan.getLeader()).getDisplayName());
        player.sendMessage("");
        player.sendMessage(ClanManager.getInstance().getPrefix() + "§7" + player.language("Moderatoren", "Moderators") + "§8:");
        if (!clan.getModerators().isEmpty()) {
            clan.getModerators().forEach(players -> {
                OriginPlayer target = OriginManager.getInstance().getPlayer(players);
                player.sendMessage(ClanManager.getInstance().getPrefix() + " §f- " + target.getDisplayName() + " §f| " + (target.isOnline() && ClanManager.getInstance().getSettings(target.getUniqueId()).isDisplayedAsOnline() ? "§e" + target.getServer().getName() : "§cOffline"));
            });
        } else {
            player.sendMessage(ClanManager.getInstance().getPrefix() + " §f- §7" + player.language("Es gibt derzeit §ckeine §7Moderatoren", "There are currently §cno §7moderators"));
        }
        player.sendMessage("");
        player.sendMessage(ClanManager.getInstance().getPrefix() + "§7" + player.language("Mitglieder", "Members") + "§8:");
        if (!clan.getMembers().isEmpty()) {
            clan.getMembers().forEach(players -> {
                OriginPlayer target = OriginManager.getInstance().getPlayer(players);
                player.sendMessage(ClanManager.getInstance().getPrefix() + " §f- " + target.getDisplayName() + " §f| " + (target.isOnline() && ClanManager.getInstance().getSettings(target.getUniqueId()).isDisplayedAsOnline() ? "§e" + target.getServer().getName() : "§cOffline"));
            });
        } else {
            player.sendMessage(ClanManager.getInstance().getPrefix() + " §f- §7" + player.language("Es gibt derzeit §ckeine §7Mitglieder", "There are currently §cno §7members"));
        }
        player.sendMessage("");
    }

    private List<ChatColor> getColors() {
        return Arrays.stream(ChatColor.values()).filter(color -> !color.equals(ChatColor.BLACK) && !color.equals(ChatColor.DARK_GRAY) && !color.equals(ChatColor.GRAY) && !color.equals(ChatColor.BOLD) && !color.equals(ChatColor.UNDERLINE) && !color.equals(ChatColor.STRIKETHROUGH) && !color.equals(ChatColor.ITALIC) && !color.equals(ChatColor.RESET) && !color.equals(ChatColor.MAGIC)).collect(Collectors.toList());
    }
}
