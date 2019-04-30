package com.teamcqr.chocolatequestrepoured.event;

import com.teamcqr.chocolatequestrepoured.dungeongen.protection.ProtectionHandler;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Copyright (c) 29.04.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class CQREventHandler {
    @SubscribeEvent
    public void blockBreak(BlockEvent.BreakEvent event) {
        ProtectionHandler.PROTECTION_HANDLER.check(event);
    }
}
