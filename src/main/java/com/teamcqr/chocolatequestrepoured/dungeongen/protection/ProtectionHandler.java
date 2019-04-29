package com.teamcqr.chocolatequestrepoured.dungeongen.protection;

import net.minecraftforge.event.world.BlockEvent;

import java.util.ArrayList;

/**
 * Copyright (c) 29.04.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ProtectionHandler {

    public static ProtectionHandler PROTECTION_HANDLER;

    private ArrayList<ProtectedRegion> regions;

    public ProtectionHandler() {
        this.regions = new ArrayList<>();
    }

    public void addRegion(ProtectedRegion region) {
        regions.add(region);
    }

    public void check(BlockEvent.BreakEvent e) {
        for (ProtectedRegion r: regions) {
            r.checkBreakEvent(e);
        }
    }

    public static void init() {
        PROTECTION_HANDLER = new ProtectionHandler();
    }
}
