package com.teamcqr.chocolatequestrepoured.dungeonprot;

import java.util.ArrayList;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.intrusive.IntrusiveModificationHelper;

import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Central class responsible for handling all activities related to the protection of regions
 * Intended for use on dungeons but can easily be used for other areas
 *
 * @author jdawg3636
 * GitHub: https://github.com/jdawg3636
 *
 * @version 22.07.19
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
    public ArrayList<ProtectedRegion> getActiveRegions() {
        return activeRegions;
    }

    // Detect Dungeon Spawn Event
    @SubscribeEvent
    public void eventHandleDungeonSpawn(CQDungeonStructureGenerateEvent e) {
        ProtectedRegion toRegister = new ProtectedRegion(e.getPos(), new BlockPos(e.getPos().getX() + e.getSize().getX(), e.getPos().getY() + e.getSize().getY(), e.getPos().getZ() + e.getSize().getZ()), e.getWorld());
        registerRegion(toRegister);
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
    public void eventHandleExplosion(ExplosionEvent e) {

        // Check explosion pos against all active regions
        for( ProtectedRegion region : activeRegions ) {
            if( region.checkIfBlockPosInRegion( new BlockPos(e.getExplosion().getPosition().x, e.getExplosion().getPosition().y, e.getExplosion().getPosition().z), e.getWorld()) ) {

                // Allow TNT, cancel if any other exploder
                if ( !(IntrusiveModificationHelper.reflectGetFieldValue(e.getExplosion(), IntrusiveModificationHelper.reflectGetField( e.getExplosion(), new String[] {"exploder", "field_77283_e"} ) ) instanceof EntityTNTPrimed ) ) {
                    e.setCanceled(true);
                }

            }
        }

    }

    @SubscribeEvent
    public void eventHandleNaturalSpawn(LivingSpawnEvent.CheckSpawn e) {

        // Check spawn pos against all regions and cancel if overlapping
        for( ProtectedRegion region : activeRegions ) {
            if(region.checkIfBlockPosInRegion( new BlockPos(e.getX(), e.getY(), e.getZ()), e.getWorld() ) && !e.isSpawner()) {
                e.setResult(Event.Result.DENY);
            }
        }

    }

    /*
     * Util
     */

    public ArrayList<ProtectedRegion> getActiveRegionsContainingBlockPos(BlockPos position, World world) {

        ArrayList<ProtectedRegion> toReturn = new ArrayList<>();

        // Check all active regions
        for (ProtectedRegion region : activeRegions) {
            if(region.checkIfBlockPosInRegion(position, world)) toReturn.add(region);
        }

        // Return
        return toReturn;

    }

    public int getLargerRegion(ProtectedRegion a, ProtectedRegion b) {
        if(a.getRegionVolume() > b.getRegionVolume()) return 1;
        if(a.getRegionVolume() < b.getRegionVolume()) return 2;
        return 0; // 0 = Both sizes equal
    }

}
