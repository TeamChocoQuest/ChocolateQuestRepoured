package com.teamcqr.chocolatequestrepoured.structureprot;

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
 *         GitHub: https://github.com/jdawg3636
 *
 * @version 05.01.20
 */
public class ProtectedRegion implements Serializable {

	/*
	 * Variables
	 */

	// Region Data
	private String UUID;
	private transient BlockPos NWCorner;
	private transient BlockPos SECorner;

	// Dependencies (Things that will remove ProtectedRegion if all are killed/destroyed)
	private transient ArrayList<String> entityDependencies = new ArrayList<>(); // Stores UUIDs as Strings for ease of serialization
	private transient ArrayList<BlockPos> blockDependencies = new ArrayList<>();

	// Settings
	public transient HashMap<String, Boolean> settings = new HashMap<>();

	/*
	 * Constructors
	 */

	// Verbose
	public ProtectedRegion(String UUID, BlockPos NWCorner, BlockPos SECorner, ArrayList<String> entityDependenciesAsUUIDStrings, ArrayList<BlockPos> blockDependencies, HashMap<String, Boolean> settingsOverrides) {
		// Region Data
		this.UUID = UUID;
		this.NWCorner = NWCorner;
		this.SECorner = SECorner;
		// Dependencies
		if (entityDependenciesAsUUIDStrings != null) {
			for (String entry : entityDependenciesAsUUIDStrings) {
				if (entry != null) {
					this.entityDependencies.add(entry);
				}
			}
		}
		if (blockDependencies != null) {
			for (BlockPos entry : blockDependencies) {
				if (entry != null) {
					this.blockDependencies.add(entry);
				}
			}
		}
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
		if (settingsOverrides != null) {
			for (String setting : settingsOverrides.keySet()) {
				if (settingsOverrides.get(setting) != null) {
					this.settings.put(setting, settingsOverrides.get(setting));
				}
			}
		}
	}

	// Minimal
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
		} catch (Exception ignored) {
		}
		// Write NW Corner
		stream.writeInt(this.NWCorner.getX());
		stream.writeInt(this.NWCorner.getY());
		stream.writeInt(this.NWCorner.getZ());
		// Write SE Corner
		stream.writeInt(this.SECorner.getX());
		stream.writeInt(this.SECorner.getY());
		stream.writeInt(this.SECorner.getZ());
		// Write Entity Dependencies
		stream.writeInt(this.entityDependencies.size());
		for (String UUID : this.entityDependencies) {
			stream.writeObject(UUID);
		}
		// Write Block Dependencies
		stream.writeInt(this.blockDependencies.size());
		for (BlockPos position : this.blockDependencies) {
			stream.writeInt(position.getX());
			stream.writeInt(position.getY());
			stream.writeInt(position.getZ());
		}
		// Write Settings Values
		stream.writeInt(this.settings.keySet().size());
		for (String setting : this.settings.keySet()) {
			stream.writeObject(setting);
			stream.writeBoolean(this.settings.get(setting));
		}
	}

	private void readObject(ObjectInputStream stream) throws Exception {
		// Let default handle what it can
		try {
			stream.defaultReadObject();
		} catch (Exception ignored) {
		}
		// Read NW Corner
		this.NWCorner = new BlockPos(stream.readInt(), stream.readInt(), stream.readInt());
		// Read SE Corner
		this.SECorner = new BlockPos(stream.readInt(), stream.readInt(), stream.readInt());
		// Read Entity Dependencies
		this.entityDependencies = new ArrayList<>();
		int entityDependencyCount = stream.readInt();
		for (int i = 0; i < entityDependencyCount; i++) {
			this.entityDependencies.add((String) stream.readObject());
		}
		// Read Block Dependencies
		this.blockDependencies = new ArrayList<>();
		int blockDependencyCount = stream.readInt();
		for (int i = 0; i < blockDependencyCount; i++) {
			this.blockDependencies.add(new BlockPos(stream.readInt(), stream.readInt(), stream.readInt()));
		}
		// Read Settings Values
		this.settings = new HashMap<>();
		int settingCount = stream.readInt();
		for (int i = 0; i < settingCount; i++) {
			this.settings.put((String) stream.readObject(), stream.readBoolean());
		}
	}

	/*
	 * Private Field Accessors
	 */

	public String getUUIDString() {
		return this.UUID;
	}

	public BlockPos getNWCorner() {
		return this.NWCorner;
	}

	public BlockPos getSECorner() {
		return this.SECorner;
	}

	public void addEntityDependency(String entityUUID) {
		this.entityDependencies.add(entityUUID);
	}

	public void removeEntityDependency(String entityUUID) {
        entityDependencies.remove(entityUUID);
        if(entityDependencies.size() == 0 && blockDependencies.size() == 0) ProtectionHandler.getInstance().deregister(this);
    }

    public ArrayList<String> getEntityDependenciesAsUUIDs() {
        return this.entityDependencies;
	}

	public void addBlockDependency(BlockPos positionOfBlock) {
		this.blockDependencies.add(positionOfBlock);
	}

	public void removeBlockDependency(BlockPos toBeRemoved) {
        this.blockDependencies.remove(toBeRemoved);
        if(blockDependencies.size() == 0 && entityDependencies.size() == 0) ProtectionHandler.getInstance().deregister(this);
    }

    public ArrayList<BlockPos> getBlockDependencies() {
        return this.blockDependencies;
	}

	/*
	 * Util
	 */

	// Assumes correct world
	public boolean checkIfBlockPosInRegion(BlockPos toCheck) {

		// Check NW (min)
		if (toCheck.getX() < this.NWCorner.getX()) {
			return false;
		}
		if (toCheck.getY() < this.NWCorner.getY()) {
			return false;
		}
		if (toCheck.getZ() < this.NWCorner.getZ()) {
			return false;
		}

		// Check SE (max)
		if (toCheck.getX() > this.SECorner.getX()) {
			return false;
		}
		if (toCheck.getY() > this.SECorner.getY()) {
			return false;
		}
		if (toCheck.getZ() > this.SECorner.getZ()) {
			return false;
		}

		// Default (this means that all disqualifiers failed)
		return true;

	}

	public int getRegionVolume() {
		// Calculate deltas for x,y,z and multiply results
		return (this.SECorner.getX() - this.NWCorner.getX()) * (this.SECorner.getY() - this.NWCorner.getY()) * (this.SECorner.getZ() - this.NWCorner.getZ());
	}

}
