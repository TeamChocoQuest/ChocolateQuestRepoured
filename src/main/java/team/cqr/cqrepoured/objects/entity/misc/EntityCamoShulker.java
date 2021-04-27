package team.cqr.cqrepoured.objects.entity.misc;

import com.google.common.base.Optional;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityCamoShulker extends EntityShulker {

	protected static final DataParameter<Optional<IBlockState>> CAMO_BLOCK = EntityDataManager.<Optional<IBlockState>>createKey(EntityCamoShulker.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	
	public EntityCamoShulker(World worldIn) {
		super(worldIn);
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.set(CAMO_BLOCK, Optional.of(Blocks.PURPUR_BLOCK.getDefaultState()));
	}
	
	public IBlockState getCamoBlock() {
		if(this.dataManager.get(CAMO_BLOCK).isPresent()) {
			return this.dataManager.get(CAMO_BLOCK).get();
		}
		return Blocks.PURPUR_BLOCK.getDefaultState();
	}
	
	public void setCamoBlock(IBlockState block) {
		if(this.isServerWorld()) {
			this.dataManager.set(CAMO_BLOCK, Optional.of(block));
		}
	}

}
