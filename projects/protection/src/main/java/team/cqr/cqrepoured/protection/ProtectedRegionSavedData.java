package team.cqr.cqrepoured.protection;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import team.cqr.cqrepoured.common.CQRepoured;

public class ProtectedRegionSavedData extends SavedData {
	
	public static final String KEY = CQRepoured.prefix("protectedregion_data").toString();
	
	// Important: Always provide the server so that we always return the same file for all sub-dimensions!
	@Nullable
	public static ProtectedRegionSavedData getGlobal(MinecraftServer server) {
		ServerLevel level = server.getLevel(Level.OVERWORLD);
		if (level == null) {
			return null;
		} else {
			return getLocal(level);
		}
	}
	
	public static ProtectedRegionSavedData getLocal(ServerLevel level) {
		DimensionDataStorage storage = level.getDataStorage();
		return storage.computeIfAbsent(ProtectedRegionSavedData::new, ProtectedRegionSavedData::new, KEY);
	}
	
	protected ProtectedRegionSavedData() {
		// Creating a new empty one => Nothign to do here!
	}
	
	protected ProtectedRegionSavedData(CompoundTag tag) {
		super();
		// TODO: Load
	}

	@Override
	public CompoundTag save(CompoundTag pCompoundTag) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void removeProtectedRegion(final UUID id) {
		
	}

}
