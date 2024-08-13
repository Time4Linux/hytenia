package de.pxscxl.bungee.proxysystem.commands;

import de.pxscxl.bungee.proxysystem.ProxySystem;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.FriendManager;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.utils.objects.friends.Friend;
import de.pxscxl.origin.utils.objects.friends.FriendRequest;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FriendCommand extends Command {

    public FriendCommand() {
        super("friend");
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
                    List<Friend> friends = FriendManager.getInstance().getFriends(player.getUniqueId());
                    if (!friends.isEmpty()) {
                        int maxPage = 0;
                        for (int i = 0; i < friends.size(); i += 10) {
                            maxPage++;
                        }
                        player.sendMessage(
                                FriendManager.getInstance().getPrefix() + "§7Liste deiner Freunde §8[§7Seite §e1§8/§e" + maxPage + "§8]",
                                FriendManager.getInstance().getPrefix() + "§7List of your friends §8[§7Page §e1§8/§e" + maxPage + "§8]");
                        friends.sort((o1, o2) -> {
                            OriginPlayer o1Player = OriginManager.getInstance().getPlayer(o1.getUuid());
                            OriginPlayer o2Player = OriginManager.getInstance().getPlayer(o2.getUuid());

                            String s1 = o1Player.isOnline() ? "a" : "b" + o1Player.getRank().getId() + o1Player.getName();
                            String s2 = o2Player.isOnline() ? "a" : "b" + o2Player.getRank().getId() + o2Player.getName();

                            return s1.compareTo(s2);
                        });
                        List<Friend> list = new ArrayList<>();
                        for (int i = 0; i < friends.size() && i < 10; i++) list.add(friends.get(i));
                        for (Friend target : list) {
                            player.sendMessage(sendFriendListEntry(player, target));
                        }
                    } else {
                        player.sendMessage(
                                FriendManager.getInstance().getPrefix() + "§7Du hast aktuell §ckeine §7Freunde",
                                FriendManager.getInstance().getPrefix() + "§7You currently have §cno §7friends");
                    }
                    return;
                } else if (args[0].equalsIgnoreCase("requests")) {
                    List<FriendRequest> requests = FriendManager.getInstance().getRequests(player.getUniqueId());
                    if (!requests.isEmpty()) {
                        int maxPage = 0;
                        for (int i = 0; i < requests.size(); i += 10) {
                            maxPage++;
                        }
                        player.sendMessage(
                                FriendManager.getInstance().getPrefix() + "§7Liste deiner Anfragen §8[§7Seite §e1§8/§e" + maxPage + "§8]",
                                FriendManager.getInstance().getPrefix() + "§7List of your requests §8[§7Page §e1§8/§e" + maxPage + "§8]");
                        List<FriendRequest> list = new ArrayList<>();
                        for (int i = 0; i < requests.size() && i < 10; i++) list.add(requests.get(i));
                        for (FriendRequest target : list) {
                            player.sendMessage(sendRequestListEntry(target));
                        }
                    } else {
                        player.sendMessage(
                                FriendManager.getInstance().getPrefix() + "§7Du hast aktuell §ckeine §7Anfragen",
                                FriendManager.getInstance().getPrefix() + "§7You currently have §cno §7requests");
                    }
                    return;
                }
                sendHelpMessage(player);
                break;
            case 2:
                if (args[0].equalsIgnoreCase("list")) {
                    List<Friend> friends = FriendManager.getInstance().getFriends(player.getUniqueId());
                    if (!friends.isEmpty()) {
                        int page;
                        try {
                            page = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            player.sendMessage(
                                    FriendManager.getInstance().getPrefix() + "§7Diese Seite wurde §cnicht §7gefunden!",
                                    FriendManager.getInstance().getPrefix() + "§7This page was §cnot §7found!");
                            return;
                        }
                        int maxPage = 0;
                        for (int i = 0; i < friends.size(); i += 10) {
                            maxPage++;
                        }
                        if (page > maxPage) page = maxPage;
                        player.sendMessage(
                                FriendManager.getInstance().getPrefix() + "§7Liste deiner Freunde §8[§7Seite §e1§8/§e" + maxPage + "§8]",
                                FriendManager.getInstance().getPrefix() + "§7List of your friends §8[§7Page §e1§8/§e" + maxPage + "§8]");
                        friends.sort((o1, o2) -> {
                            OriginPlayer o1Player = OriginManager.getInstance().getPlayer(o1.getUuid());
                            OriginPlayer o2Player = OriginManager.getInstance().getPlayer(o2.getUuid());

                            String s1 = o1Player.isOnline() ? "a" : "b" + o1Player.getRank().getId() + o1Player.getName();
                            String s2 = o2Player.isOnline() ? "a" : "b" + o2Player.getRank().getId() + o2Player.getName();

                            return s1.compareTo(s2);
                        });
                        List<Friend> list = new ArrayList<>();
                        for (int i = page * 10 - 10; i < friends.size() && i < page * 10; i++) list.add(friends.get(i));
                        for (Friend target : list) {
                            player.sendMessage(sendFriendListEntry(player, target));
                        }
                    } else {
                        player.sendMessage(
                                FriendManager.getInstance().getPrefix() + "§7Du hast aktuell §ckeine §7Freunde",
                                FriendManager.getInstance().getPrefix() + "§7You currently have §cno §7friends");
                    }
                    return;
                } else if (args[0].equalsIgnoreCase("requests")) {
                    List<FriendRequest> requests = FriendManager.getInstance().getRequests(player.getUniqueId());
                    if (!requests.isEmpty()) {
                        int page;
                        try {
                            page = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            player.sendMessage(
                                    FriendManager.getInstance().getPrefix() + "§7Diese Seite wurde §cnicht §7gefunden!",
                                    FriendManager.getInstance().getPrefix() + "§7This page was §cnot §7found!");
                            return;
                        }
                        int maxPage = 0;
                        for (int i = 0; i < requests.size(); i += 10) {
                            maxPage++;
                        }
                        if (page > maxPage) page = maxPage;
                        player.sendMessage(
                                FriendManager.getInstance().getPrefix() + "§7Liste deiner Anfragen §8[§7Seite §e1§8/§e" + maxPage + "§8]",
                                FriendManager.getInstance().getPrefix() + "§7List of your requests §8[§7Page §e1§8/§e" + maxPage + "§8]");
                        List<FriendRequest> list = new ArrayList<>();
                        for (int i = page * 10 - 10; i < requests.size() && i < page * 10; i++)
                            list.add(requests.get(i));
                        for (FriendRequest target : list) {
                            player.sendMessage(sendRequestListEntry(target));
                        }
                    } else {
                        player.sendMessage(
                                FriendManager.getInstance().getPrefix() + "§7Du hast aktuell §ckeine §7Anfragen",
                                FriendManager.getInstance().getPrefix() + "§7You currently have §cno §7requests");
                    }
                    return;
                } else if (args[0].equalsIgnoreCase("add")) {
                    OriginPlayer target = OriginManager.getInstance().getPlayer(args[1]);
                    if (target != null) {
                        if (player != target) {
                            Friend friend = FriendManager.getInstance().getFriend(player.getUniqueId(), target.getUniqueId());
                            if (friend == null) {
                                FriendRequest request = FriendManager.getInstance().getRequest(target.getUniqueId(), player.getUniqueId());
                                if (request == null) {
                                    if (FriendManager.getInstance().getSettings(target.getUniqueId()).isReceivingRequests()) {
                                        player.sendMessage(
                                                FriendManager.getInstance().getPrefix() + "§7Du hast " + target.getDisplayName() + " §7eine Anfrage gesendet",
                                                FriendManager.getInstance().getPrefix() + "§7You've sent a request to " + target.getDisplayName()
                                        );
                                        if (target.isOnline()) {
                                            TextComponent accept = new TextComponent(FriendManager.getInstance().getPrefix() + "§7" + player.language("Annehmen", "Accept") + "§8: §a/friend accept " + player.getName());
                                            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept " + player.getName()));

                                            TextComponent deny = new TextComponent(FriendManager.getInstance().getPrefix() + "§7" + player.language("Ablehnen", "Deny") + "§8: §c/friend deny " + player.getName());
                                            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend deny " + player.getName()));

                                            target.sendMessage(
                                                    FriendManager.getInstance().getPrefix() + player.getDisplayName() + " §7hat dir eine Anfrage gesendet",
                                                    FriendManager.getInstance().getPrefix() + player.getDisplayName() + " §7has sent you a request"
                                            );
                                            target.sendMessage(accept);
                                            target.sendMessage(deny);
                                        }
                                        ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> FriendManager.getInstance().insertRequests(target.getUniqueId(), player.getUniqueId(), new Date().getTime()));
                                    } else {
                                        player.sendMessage(
                                                FriendManager.getInstance().getPrefix() + "§7Du kannst diesem Spieler §ckeine §7Anfrage senden!",
                                                FriendManager.getInstance().getPrefix() + "§7You §ccan't §7send a request to this player!"
                                        );
                                    }
                                } else {
                                    player.sendMessage(
                                            FriendManager.getInstance().getPrefix() + "§7Du hast diesem Spieler §ebereits §7eine Anfrage gesendet!",
                                            FriendManager.getInstance().getPrefix() + "§7You've §ealready §7sent a request to this player!"
                                    );
                                }
                            } else {
                                player.sendMessage(
                                        FriendManager.getInstance().getPrefix() + "§7Du bist §ebereits §7mit diesem Spieler befreundet!",
                                        FriendManager.getInstance().getPrefix() + "§7This player is §ealready §7your friend!"
                                );
                            }
                        } else {
                            player.sendMessage(
                                    FriendManager.getInstance().getPrefix() + "§7Du darfst §cnicht §7mit dir selbst interagieren!",
                                    FriendManager.getInstance().getPrefix() + "§7You §ccan't §7interact with yourself!"
                            );
                        }
                    } else {
                        player.sendMessage(
                                FriendManager.getInstance().getPrefix() + "§7Dieser Spieler wurde §cnicht §7gefunden!",
                                FriendManager.getInstance().getPrefix() + "§7This player was §cnot §7found!"
                        );
                    }
                    return;
                } else if (args[0].equalsIgnoreCase("accept")) {
                    OriginPlayer target = OriginManager.getInstance().getPlayer(args[1]);
                    if (target != null) {
                        FriendRequest request = FriendManager.getInstance().getRequest(player.getUniqueId(), target.getUniqueId());
                        if (request != null) {
                            player.sendMessage(
                                    FriendManager.getInstance().getPrefix() + "§7Du hast die Anfrage von " + target.getDisplayName() + " §aangenommen",
                                    FriendManager.getInstance().getPrefix() + "§7You've §aaccepted §7the request of " + target.getDisplayName()
                            );
                            if (target.isOnline()) {
                                target.sendMessage(
                                        FriendManager.getInstance().getPrefix() + player.getDisplayName() + " §7hat deine Anfrage §aangenommen",
                                        FriendManager.getInstance().getPrefix() + player.getDisplayName() + " §7has §aaccepted §7your request"
                                );
                            }
                            ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> {
                                FriendManager.getInstance().deleteRequest(player.getUniqueId(), target.getUniqueId());

                                Date date = new Date();
                                FriendManager.getInstance().insertFriends(player.getUniqueId(), target.getUniqueId(), date.getTime());
                                FriendManager.getInstance().insertFriends(target.getUniqueId(), player.getUniqueId(), date.getTime());
                            });
                        } else {
                            player.sendMessage(
                                    FriendManager.getInstance().getPrefix() + "§7Dieser Spieler hat dir §ckeine §7Anfrage gesendet",
                                    FriendManager.getInstance().getPrefix() + "§7This player §chasn't §7sent you a request"
                            );
                        }
                    } else {
                        player.sendMessage(
                                FriendManager.getInstance().getPrefix() + "§7Dieser Spieler wurde §cnicht §7gefunden!",
                                FriendManager.getInstance().getPrefix() + "§7This player was §cnot §7found!"
                        );
                    }
                    return;
                } else if (args[0].equalsIgnoreCase("deny")) {
                    OriginPlayer target = OriginManager.getInstance().getPlayer(args[1]);
                    if (target != null) {
                        FriendRequest request = FriendManager.getInstance().getRequest(player.getUniqueId(), target.getUniqueId());
                        if (request != null) {
                            player.sendMessage(
                                    FriendManager.getInstance().getPrefix() + "§7Du hast die Anfrage von " + target.getDisplayName() + " §cabgelehnt",
                                    FriendManager.getInstance().getPrefix() + "§7You've §cdenied §7the request of " + target.getDisplayName()
                            );
                            if (target.isOnline()) {
                                target.sendMessage(
                                        FriendManager.getInstance().getPrefix() + player.getDisplayName() + " §7hat deine Anfrage §cabgelehnt",
                                        FriendManager.getInstance().getPrefix() + player.getDisplayName() + " §7has §cdenied §7your request"
                                );
                            }
                            ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> FriendManager.getInstance().deleteRequest(player.getUniqueId(), target.getUniqueId()));
                        } else {
                            player.sendMessage(
                                    FriendManager.getInstance().getPrefix() + "§7Dieser Spieler hat dir §ckeine §7Anfrage gesendet",
                                    FriendManager.getInstance().getPrefix() + "§7This player §chasn't §7sent you a request"
                            );
                        }
                    } else {
                        player.sendMessage(
                                FriendManager.getInstance().getPrefix() + "§7Dieser Spieler wurde §cnicht §7gefunden!",
                                FriendManager.getInstance().getPrefix() + "§7This player was §cnot §7found!"
                        );
                    }
                    return;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    OriginPlayer target = OriginManager.getInstance().getPlayer(args[1]);
                    if (target != null) {
                        Friend friend = FriendManager.getInstance().getFriend(player.getUniqueId(), target.getUniqueId());
                        if (friend != null) {
                            player.sendMessage(
                                    FriendManager.getInstance().getPrefix() + "§7Die Freundschaft mit " + target.getDisplayName() + " §7wurde §caufgelöst",
                                    FriendManager.getInstance().getPrefix() + "§7The friendship with " + target.getDisplayName() + " §7was §ccancelled"
                            );
                            if (target.isOnline()) {
                                target.sendMessage(
                                        FriendManager.getInstance().getPrefix() + "§7Die Freundschaft mit " + player.getDisplayName() + " §7wurde §caufgelöst",
                                        FriendManager.getInstance().getPrefix() + "§7The friendship with " + player.getDisplayName() + " §7was §ccancelled"
                                );
                            }
                            ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> {
                                FriendManager.getInstance().deleteFriend(player.getUniqueId(), target.getUniqueId());
                                FriendManager.getInstance().deleteFriend(target.getUniqueId(), player.getUniqueId());
                            });
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
                    return;
                } else if (args[0].equalsIgnoreCase("jump")) {
                    OriginPlayer target = OriginManager.getInstance().getPlayer(args[1]);
                    if (target != null) {
                        Friend friend = FriendManager.getInstance().getFriend(player.getUniqueId(), target.getUniqueId());
                        if (friend != null) {
                            if (target.isOnline()) {
                                player.connect(target.getServer());
                                player.sendMessage(
                                        FriendManager.getInstance().getPrefix() + "§7Du wirst mit " + target.getDisplayName() + "'s §7Server verbunden",
                                        FriendManager.getInstance().getPrefix() + "§7You will be connected with " + target.getDisplayName() + "'s §7server"
                                );
                            } else {
                                player.sendMessage(
                                        FriendManager.getInstance().getPrefix() + "§7Dieser Freund ist §cnicht §7online!",
                                        FriendManager.getInstance().getPrefix() + "§7This friend §cisn't §7online!"
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
                FriendManager.getInstance().getPrefix() + "§7Bitte nutze: §f/friend add [Name]",
                FriendManager.getInstance().getPrefix() + "§7Please use: §f/friend add [Name]"
        );
        player.sendMessage(
                FriendManager.getInstance().getPrefix() + "§7Bitte nutze: §f/friend accept [Name]",
                FriendManager.getInstance().getPrefix() + "§7Please use: §f/friend accept [Name]"
        );
        player.sendMessage(
                FriendManager.getInstance().getPrefix() + "§7Bitte nutze: §f/friend deny [Name]",
                FriendManager.getInstance().getPrefix() + "§7Please use: §f/friend deny [Name]"
        );
        player.sendMessage(
                FriendManager.getInstance().getPrefix() + "§7Bitte nutze: §f/friend remove [Name]",
                FriendManager.getInstance().getPrefix() + "§7Please use: §f/friend remove [Name]"
        );
        player.sendMessage(
                FriendManager.getInstance().getPrefix() + "§7Bitte nutze: §f/friend jump [Name]",
                FriendManager.getInstance().getPrefix() + "§7Please use: §f/friend jump [Name]"
        );
        player.sendMessage(
                FriendManager.getInstance().getPrefix() + "§7Bitte nutze: §f/friend list",
                FriendManager.getInstance().getPrefix() + "§7Please use: §f/friend list"
        );
        player.sendMessage(
                FriendManager.getInstance().getPrefix() + "§7Bitte nutze: §f/friend requests",
                FriendManager.getInstance().getPrefix() + "§7Please use: §f/friend requests"
        );
        player.sendMessage(
                FriendManager.getInstance().getPrefix() + "§7Bitte nutze: §f/msg [Name] [Nachricht]",
                FriendManager.getInstance().getPrefix() + "§7Please use: §f/msg [Name] [Message]"
        );
    }

    private TextComponent sendFriendListEntry(OriginPlayer player, Friend friend) {
        OriginPlayer target = OriginManager.getInstance().getPlayer(friend.getUuid());
        return new TextComponent(FriendManager.getInstance().getPrefix() + (target.isOnline() ? "§a" : "§c") + "-» " + target.getDisplayName() + " §8● §7" + (target.isOnline() ? player.language("Online auf", "§7Online on") + "§8: §e" + target.getServer().getName() : "Offline"));
    }

    private TextComponent sendRequestListEntry(FriendRequest request) {
        return new TextComponent(FriendManager.getInstance().getPrefix() + "§8-» " + OriginManager.getInstance().getPlayer(request.getUuid()).getDisplayName() + " §8● §7" + request.getCreation());
    }
}
