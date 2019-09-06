package com.teamcqr.chocolatequestrepoured.dungeonprot;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.util.math.BlockPos;

/**
 * Custom type for containing info about a protected region
 * Intended for use by dungeons but can theoretically be anything
 *
 * @author jdawg3636
 * GitHub: https://github.com/jdawg3636
 *
 * @version 23.07.19
 */
public class ProtectedRegion implements Serializable {

    /*
     * Variables
     */

    // Region Data
    private String UUID;
    private transient BlockPos NWCorner;
    private transient BlockPos SECorner;

    // Dependencies (Things that will remove ProtectedRegion if killed/destroyed)
    private transient ArrayList<String> entityDependencies = new ArrayList<>(); // Stores UUIDs as Strings for ease of serialization
    private transient ArrayList<BlockPos> blockDependencies = new ArrayList<>();

    // Settings
    public transient HashMap<String, Boolean> settings = new HashMap<>();

    /*
     * Constructors
     */

    // Contains all available region settings
    public ProtectedRegion(String UUID, BlockPos NWCorner, BlockPos SECorner, ArrayList<String> entityDependenciesAsUUIDStrings, ArrayList<BlockPos> blockDependencies, HashMap<String, Boolean> settingsOverrides) {
        // Region Data
        this.UUID = UUID;
        this.NWCorner = NWCorner;
        this.SECorner = SECorner;
        // Dependencies
        this.entityDependencies.addAll(entityDependenciesAsUUIDStrings);
        this.blockDependencies.addAll(blockDependencies);
        // Protection Settings Defaults
        this.settings.put("preventBlockBreak", true);
        this.settings.put("preventBlockBreakCreative", false);
        this.settings.put("preventBlockPlace", true);
        this.settings.put("preventBlockPlaceCreative", false);
        this.settings.put("preventExplosionTNT", false);
        this.settings.put("preventExplosionOther", true);
        this.settings.put("preventFireSpread", true);
        this.settings.put("preventNaturalMobSpawn", true);
        // Protection Settings Overrides
        for(String settingName : settings.keySet()) {
            this.settings.put(settingName, settingsOverrides.get(settingName));
        }
    }

    // Contains only region data and uses default values for all dependency and protection settings
    public ProtectedRegion(String UUID, BlockPos NWCorner, BlockPos SECorner) {
        this.UUID = UUID;
        this.NWCorner = NWCorner;
        this.SECorner = SECorner;
        this.settings.put("preventBlockBreak", true);
        this.settings.put("preventBlockBreakCreative", false);
        this.settings.put("preventBlockPlace", true);
        this.settings.put("preventBlockPlaceCreative", false);
        this.settings.put("preventExplosionTNT", false);
        this.settings.put("preventExplosionOther", true);
        this.settings.put("preventFireSpread", true);
        this.settings.put("preventNaturalMobSpawn", true);
    }

    /*
     * Serialization
     */

    private void writeObject(ObjectOutputStream stream) throws Exception {
        // Let default handle what it can
        try {
            stream.defaultWriteObject();
        } catch (Exception ignored) {}
        // Write NW Corner
        stream.writeInt(NWCorner.getX());
        stream.writeInt(NWCorner.getY());
        stream.writeInt(NWCorner.getZ());
        // Write SE Corner
        stream.writeInt(SECorner.getX());
        stream.writeInt(SECorner.getY());
        stream.writeInt(SECorner.getZ());
        // Write Entity Dependencies
        stream.writeInt(entityDependencies.size());
        for(String UUID : entityDependencies) {
            stream.writeObject(UUID);
        }
        // Write Block Dependencies
        stream.writeInt(blockDependencies.size());
        for(BlockPos position : blockDependencies) {
            stream.writeInt(position.getX());
            stream.writeInt(position.getY());
            stream.writeInt(position.getZ());
        }
        // Write Settings Values
        stream.writeInt(settings.keySet().size());
        for(String setting : settings.keySet()) {
            stream.writeObject(setting);
            stream.writeBoolean(settings.get(setting));
        }
    }

    private void readObject(ObjectInputStream stream) throws Exception {
        // Let default handle what it can
        try {
            stream.defaultReadObject();
        } catch (Exception ignored) {}
        // Read NW Corner
        NWCorner = new BlockPos(stream.readInt(), stream.readInt(), stream.readInt());
        // Read SE Corner
        SECorner = new BlockPos(stream.readInt(), stream.readInt(), stream.readInt());
        // Read Entity Dependencies
        entityDependencies = new ArrayList<>();
        int entityDependencyCount = stream.readInt();
        for(int i = 0; i < entityDependencyCount; i++) {
            entityDependencies.add((String)stream.readObject());
        }
        // Read Block Dependencies
        blockDependencies = new ArrayList<>();
        int blockDependencyCount = stream.readInt();
        for(int i = 0; i < blockDependencyCount; i++) {
            blockDependencies.add(new BlockPos(stream.readInt(), stream.readInt(), stream.readInt()));
        }
        // Read Settings Values
        settings = new HashMap<>();
        int settingCount = stream.readInt();
        for(int i = 0; i < settingCount; i++) {
            settings.put((String)stream.readObject(), stream.readBoolean());
        }
    }

    /*
     * Private Field Accessors
     */

    public String getUUIDString() {
        return UUID;
    }

    public BlockPos getNWCorner() {
        return NWCorner;
    }

    public BlockPos getSECorner() {
        return SECorner;
    }

    public void addEntityDependency(String entityUUID) {
        entityDependencies.add(entityUUID);
    }

    public ArrayList<String> getEntityDependenciesAsUUIDs() {
        return entityDependencies;
    }

    public void addBlockDependency(BlockPos positionOfBlock) {
        blockDependencies.add(positionOfBlock);
    }

    public ArrayList<BlockPos> getBlockDependencies() {
        return blockDependencies;
    }

    /*
     * Util
     */

    // Assumes correct world
    public boolean checkIfBlockPosInRegion(BlockPos toCheck) {

        // Check NW (min)
        if(toCheck.getX() < NWCorner.getX()) return false;
        if(toCheck.getY() < NWCorner.getY()) return false;
        if(toCheck.getZ() < NWCorner.getZ()) return false;

        // Check SE (max)
        if(toCheck.getX() > SECorner.getX()) return false;
        if(toCheck.getY() > SECorner.getY()) return false;
        if(toCheck.getZ() > SECorner.getZ()) return false;

        // Default (this means that all disqualifiers failed)
        return true;

    }

    public int getRegionVolume() {
        // Calculate deltas for x,y,z and multiply results
        return ( this.SECorner.getX() - this.NWCorner.getX() ) * ( this.SECorner.getY() - this.NWCorner.getY() ) * ( this.SECorner.getZ() - this.NWCorner.getZ() );
    }

}
