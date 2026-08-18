package com.example.chocolatequest.faction;

public enum EDefaultFaction {
    UNDEAD(true, "GOBLINS,ENDERMEN", "WALKERS,VILLAGERS,PLAYERS,TRITONS,ILLAGERS"),
    PIRATE(false, "ILLAGERS", "WALKERS,VILLAGERS,INQUISITION,PLAYERS,TRITONS,UNDEAD"),
    WALKERS(true, "", "UNDEAD,BEASTS,PIRATE,DWARVES_AND_GOLEMS,GOBLINS,ENDERMEN,PLAYERS,OGRES_AND_GREMLINS,INQUISITION,ILLAGERS,VILLAGERS,NPC,DRAGONS"),
    DWARVES_AND_GOLEMS(false, "VILLAGERS,INQUISITION,NPC", "WALKERS,ENDERMEN,ILLAGERS,UNDEAD"),
    GOBLINS(false, "TRITONS,VILLAGERS", "OGRES_AND_GREMLINS,WALKERS,INQUISITION"),
    ENDERMEN(false, "ILLAGERS,UNDEAD", "WALKERS,PLAYERS,DWARVES_AND_GOLEMS,VILLAGERS,NPC,PIRATE,TRITONS"),
    INQUISITION(false, "DWARVES_AND_GOLEMS,NPC,VILLAGERS", "WALKERS,ILLAGERS,UNDEAD,GOBLINS"),
    BEASTS(false, "ENDERMEN,PIRATE", "WALKERS,PLAYERS,VILLAGERS,NPC,TRITONS,UNDEAD"),
    VILLAGERS(false, "NPC,TRITONS,PLAYERS", "WALKERS,UNDEAD,ILLAGERS"),
    TRITONS(false, "NPC,VILLAGERS", "WALKERS,UNDEAD,PIRATE,ENDERMEN"),
    ILLAGERS(false, "GREMLINS,PIRATE,ENDERMEN", "TRITONS,VILLAGERS,DWARVES_AND_GOLEMS,UNDEAD"),
    GREMLINS(false, "ILLAGERS,PIRATE", "DWARVES_AND_GOLEMS,GOBLINS,UNDEAD,WALKERS,BEASTS,ENDERMEN"),

    OGRES_AND_GREMLINS(false, "ILLAGERS,PIRATE", "DWARVES_AND_GOLEMS,GOBLINS,UNDEAD,WALKERS,BEASTS,ENDERMEN"),
    DRAGONS(false, "", "WALKERS"),
    NPC(false, "VILLAGERS,INQUISITION,DWARVES_AND_GOLEMS", "WALKERS,UNDEAD,ILLAGERS"),
    PLAYERS(false, "VILLAGERS", "WALKERS,UNDEAD");

    private final boolean staticReputation;
    private final String[] allyNames;
    private final String[] enemyNames;
    private long allyMask;
    private long enemyMask;

    static {
        for (EDefaultFaction faction : values()) {
            faction.allyMask = buildMask(faction.allyNames);
            faction.enemyMask = buildMask(faction.enemyNames);
        }
    }

    EDefaultFaction(boolean staticReputation, String allies, String enemies) {
        this.staticReputation = staticReputation;
        this.allyNames = allies.isEmpty() ? new String[0] : allies.split(",");
        this.enemyNames = enemies.isEmpty() ? new String[0] : enemies.split(",");
    }

    public boolean isStaticReputation() { return staticReputation; }

    public boolean isAlly(EDefaultFaction other) {
        if (this == other) return true;
        return other != null && (this.allyMask & (1L << other.ordinal())) != 0L;
    }

    public boolean isEnemy(EDefaultFaction other) {
        return other != null && this != other && (this.enemyMask & (1L << other.ordinal())) != 0L;
    }

    private static long buildMask(String[] names) {
        long mask = 0L;
        for (String name : names) {
            try {
                mask |= 1L << EDefaultFaction.valueOf(name.trim()).ordinal();
            } catch (IllegalArgumentException ignored) {
                // Keep malformed optional relations non-fatal, matching the previous behavior.
            }
        }
        return mask;
    }
}
