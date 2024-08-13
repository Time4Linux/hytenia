package de.pxscxl.bungee.proxysystem.registration;

import de.pxscxl.bungee.proxysystem.ProxySystem;
import de.pxscxl.bungee.proxysystem.commands.*;
import net.md_5.bungee.api.ProxyServer;

public class CommandRegistration {

    public void registerAllCommands() {
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new CheckAccountsCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new CheckIdCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new CheckPlayerCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new ClanCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new ClanMessageCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new CoinsCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new FakePlayersCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new HubCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new FriendCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new JoinMeCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new KickCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new MaintenanceCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new MessageCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new MotdCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new NotifyCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new OnlineTimeCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new PartyCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new PartyMessageCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new PingCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new PointsCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new PunishCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new RankCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new ReducePunishCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new ReplyCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new ReportCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new SecurityBanCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new SlotsCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new StaffchatCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new SwitchCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(ProxySystem.getInstance(), new UnpunishCommand());
    }
}
