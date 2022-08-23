package team.cqr.cqrepoured.world.structure.generation;

import java.util.Collection;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.ICQRLevel;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableBossInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableForceFieldNexusInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableLootChestInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableSpawnerInfo;

public class GenerationUtil {

	private static DungeonPlacement placement;

	public static void init(DungeonPlacement placement) {
		GenerationUtil.placement = placement;
	}

	public static void reset() {
		placement = null;
	}

	public static void setBoss(ICQRLevel level, BlockPos pos) {
		new PreparableBossInfo((CompoundNBT) null).prepare(level, pos, placement);
	}

	public static void setNexus(ICQRLevel level, BlockPos pos) {
		new PreparableForceFieldNexusInfo().prepare(level, pos, placement);
	}

	public static void setLootChest(ICQRLevel level, BlockPos pos, ResourceLocation lootTable, Direction facing) {
		new PreparableLootChestInfo(lootTable, facing).prepare(level, pos, placement);
	}

	public static void setSpawner(ICQRLevel level, BlockPos pos, Entity... entities) {
		new PreparableSpawnerInfo(entities).prepare(level, pos, placement);
	}

	public static void setSpawner(ICQRLevel level, BlockPos pos, Collection<Entity> entities) {
		new PreparableSpawnerInfo(entities).prepare(level, pos, placement);
	}

}
