package com.example.chocolatequest.entity.mob;

public enum CQRanks {
    LEATHER,
    CHAIN,
    GOLD,
    IRON,
    DIAMOND,
    CQ_GEAR;

    public static CQRanks fromId(int id) {
        if (id < 0 || id >= values().length) return LEATHER;
        return values()[id];
    }
}
