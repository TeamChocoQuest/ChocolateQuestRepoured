package team.cqr.cqrepoured.entity.misc;

import com.google.common.base.Optional;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityCamoShulker extends ShulkerEntity {

	protected static final DataParameter<Optional<BlockState>> CAMO_BLOCK = EntityDataManager.<Optional<BlockState>>createKey(EntityCamoShulker.class, DataSerializers.OPTIONAL_BLOCK_STATE);

	public EntityCamoShulker(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.set(CAMO_BLOCK, Optional.of(Blocks.PURPUR_BLOCK.getDefaultState()));
	}

	public BlockState getCamoBlock() {
		if (this.dataManager.get(CAMO_BLOCK).isPresent()) {
			return this.dataManager.get(CAMO_BLOCK).get();
		}
		return Blocks.PURPUR_BLOCK.getDefaultState();
	}

	public void setCamoBlock(BlockState block) {
		if (this.isServerWorld()) {
			this.dataManager.set(CAMO_BLOCK, Optional.of(block));
		}
	}

}
