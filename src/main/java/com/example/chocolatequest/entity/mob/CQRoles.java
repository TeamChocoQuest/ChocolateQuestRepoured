package com.example.chocolatequest.entity.mob;

public enum CQRoles {
    KNIGHT,
    HEALER,
    ARCHER,
    COMMANDER;

    public static CQRoles fromId(int id) {
        if (id < 0 || id >= values().length) return KNIGHT;
        return values()[id];
    }
}
