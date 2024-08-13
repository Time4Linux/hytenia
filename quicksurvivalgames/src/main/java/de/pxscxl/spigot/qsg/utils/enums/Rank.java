package de.pxscxl.spigot.qsg.utils.enums;

import lombok.Getter;

@Getter
public enum Rank {

    UNRANKED("Unplatziert", "Unranked","§7", 0, 4999),
    BRONZE("Bronze", "Bronze", "§c", 5000, 9999),
    SILVER("Silber", "Silver", "§7", 10000, 14999),
    GOLD("Gold", "Gold", "§6", 15000, 19999),
    DIAMOND("Diamant", "Diamond", "§b", 20000, -1);

    final String german;
    final String english;
    final String color;

    final int minElo;
    final int maxElo;

    Rank(String german, String english, String color, int minElo, int maxElo) {
        this.german = german;
        this.english = english;
        this.color = color;
        this.minElo = minElo;
        this.maxElo = maxElo;
    }
}
