package de.pxscxl.spigot.qsg.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplodeListener implements Listener {
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		event.blockList().clear();
	}
}