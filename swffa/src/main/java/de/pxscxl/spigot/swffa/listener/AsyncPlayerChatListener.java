package de.pxscxl.spigot.swffa.listener;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.swffa.manager.StatsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Random;
import java.util.UUID;

public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());

        event.setCancelled(true);

        OriginManager.getInstance().getPlayers().forEach(players -> {
            if (player.getNick() != null) {
                if (!StatsManager.getInstance().getFakeStats().containsKey(player.getNick())) {
                    StatsManager.getInstance().getFakeStats().put(player.getNick(), new StatsManager.Stats(UUID.randomUUID(), new Random().nextInt(5), new Random().nextInt(5), new Random().nextInt(200)));
                }
                StatsManager.Stats stats = StatsManager.getInstance().getFakeStats().get(player.getNick());
                players.sendMessage("§e" + stats.getElo() + " §8┃ " + player.getDisplayName() + "§8: §f" + event.getMessage());
            } else {
                StatsManager.Stats stats = StatsManager.getInstance().getStats(player.getUniqueId());
                players.sendMessage("§e" + stats.getElo() + " §8┃ " + player.getRealDisplayName() + "§8: §f" + event.getMessage());
            }
        });
    }
}
