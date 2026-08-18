package com.example.chocolatequest.entity.mob.enums;

public enum MobTier {
    DEFAULT("default"),
    LEATHER("leather"),
    CHAINMAIL("chainmail"),
    GOLD("gold"),
    IRON("iron"),
    DIAMOND("diamond"),
    HEALER("healer"),
    BOSS("boss");

    private final String name;

    MobTier(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static MobTier fromName(String name) {
        for (MobTier tier : values()) {
            if (tier.getName().equalsIgnoreCase(name)) {
                return tier;
            }
        }
        return DEFAULT;
    }
}
