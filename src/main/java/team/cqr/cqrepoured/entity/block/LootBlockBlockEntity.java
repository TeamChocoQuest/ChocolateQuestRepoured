package team.cqr.cqrepoured.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class LootBlockBlockEntity extends BlockEntity {
	
	protected ResourceLocation lootTableToUse = BuiltInLootTables.EMPTY;
	protected BlockState blockStateToUse = Blocks.CHEST.defaultBlockState();

	public LootBlockBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
		super(pType, pPos, pBlockState);
	}
	
	public ResourceLocation getLootTableToUse() {
		return this.lootTableToUse;
	}
	
	public BlockState getBlockStateToUse() {
		return this.blockStateToUse;
	}
	
	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
	
		CompoundTag data = new CompoundTag();
		data.put("blockstate", NbtUtils.writeBlockState(this.blockStateToUse));
		data.putString("loottable", this.lootTableToUse.toString());
		
		pTag.put("lootblockdata", data);
	}
	
	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		
		if (pTag.contains("lootblockdata", Tag.TAG_COMPOUND)) {
			CompoundTag data = pTag.getCompound("lootblockdata");
			this.lootTableToUse = new ResourceLocation(data.getString("loottable"));
			this.blockStateToUse = NbtUtils.readBlockState(this.level.holderLookup(Registries.BLOCK), data.getCompound("blockstate"));
		}
	}

}
