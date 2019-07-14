package com.teamcqr.chocolatequestrepoured.dungeongen.protection;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.util.ForgeReflectionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Written 12.07.2019 by jdawg3636
 * GitHub: https://github.com/jdawg3636
 *
 * Original (Old) Version Copyright (c) 29.04.2019 MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ProtectionHandler {

    // Singleton setup
    private static ProtectionHandler PROTECTION_HANDLER = new ProtectionHandler();
    private ProtectionHandler() { this.activeRegions = new ArrayList<>(); }
    public static ProtectionHandler getInstance() { return PROTECTION_HANDLER; }

    // Region Data
    private ArrayList<ProtectedRegion> activeRegions;

    // Accessors
    public void registerRegion(ProtectedRegion region) {
        activeRegions.add(region);
    }

    public ProtectedRegion getProtectedRegionFromUUID(UUID uuidToFind) {

        // Search
        for( ProtectedRegion protectedRegion : activeRegions ) {
            if(protectedRegion.getUUID() == uuidToFind) return protectedRegion;
        }

        // Default
        return null;

    }

    // Detect Dungeon Spawn Event
    @SubscribeEvent
    public void eventHandleDungeonSpawn(CQDungeonStructureGenerateEvent e) {

        activeRegions.add(new ProtectedRegion(e.getPos(), new BlockPos(e.getPos().getX() + e.getSize().getX(), e.getPos().getY() + e.getSize().getY(), e.getPos().getZ() + e.getSize().getZ()), e.getWorld()));

    }

    // Handle Protection-Related Events
    @SubscribeEvent
    public void eventHandleBlockBreak(BlockEvent.BreakEvent e) {

        // Check break pos against all active regions and cancel if overlapping
        for( ProtectedRegion region : activeRegions ) {
            if(region.checkIfBlockPosInRegion( e.getPos(), e.getWorld() )) {
                e.setCanceled(true);
            }
        }

    }

    @SubscribeEvent
    public void eventHandleBlockPlace(BlockEvent.PlaceEvent e) {

        // Check place pos against all active regions and cancel if overlapping
        for( ProtectedRegion region : activeRegions ) {
            if(region.checkIfBlockPosInRegion( e.getPos(), e.getWorld() )) {
                e.setCanceled(true);
            }
        }

    }

    @SubscribeEvent
    public void eventHandleExplosion(ExplosionEvent.Start e) {

        // Check explosion pos against all active regions and cancel if overlapping
        for( ProtectedRegion region : activeRegions ) {

            if( region.checkIfBlockPosInRegion( new BlockPos(e.getExplosion().getPosition().x, e.getExplosion().getPosition().y, e.getExplosion().getPosition().z), e.getWorld()) ) {

                // Allow TNT, block all other exploders
                if ( !(ForgeReflectionHelper.safeGetFieldValue(e.getExplosion(), "exploder", "field_77283_e") instanceof EntityTNTPrimed ) ) {
                    e.setCanceled(true);
                }

            }
        }

    }

    /*
    // Not preventing for the time being due to a limitation in Forge
    // https://www.minecraftforge.net/forum/topic/72896-1122-fire-tick-event/
    @SubscribeEvent
    public void eventHandleFireSpread(Event e) {

        e.doStuff();

    }
    */

    @SubscribeEvent
    public void eventHandleNaturalSpawn(LivingSpawnEvent.CheckSpawn e) {

        // Check spawn pos against all regions and cancel if overlapping
        for( ProtectedRegion region : activeRegions ) {
            if(region.checkIfBlockPosInRegion( new BlockPos(e.getX(), e.getY(), e.getZ()), e.getWorld() )) {
                e.setResult(Event.Result.DENY);
            }
        }

    }

}
