package de.pxscxl.spigot.swffa.manager;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import lombok.Getter;
import org.bukkit.Location;

public class RegionManager {

    @Getter
    private static RegionManager instance;

    public RegionManager() {
        instance = this;
    }

    public boolean isInRegion(OriginPlayer player) {
        Location highest = MapManager.getInstance().getActiveMap().getHighest();
        Location lowest = MapManager.getInstance().getActiveMap().getLowest();

        double x1 = highest.getX();
        double y1 = highest.getY();
        double z1 = highest.getZ();

        double x2 = lowest.getX();
        double y2 = lowest.getY();
        double z2 = lowest.getZ();

        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();

        return isBetween(x, x1, x2) && isBetween(y, y1, y2) && isBetween(z, z1, z2);
    }

    public boolean isBetween(double first, double second, double third) {
        return second < first && first < third || second > first && first > third;
    }
}
