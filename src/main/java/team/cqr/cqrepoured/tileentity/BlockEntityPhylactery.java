package team.cqr.cqrepoured.tileentity;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.util.WeakReferenceLazyLoadField;

public class BlockEntityPhylactery extends BlockEntity {

	private WeakReferenceLazyLoadField<Entity> currentOwner = new WeakReferenceLazyLoadField<>(this::findOwner);
	private UUID ownerUUID;
	
	
	public BlockEntityPhylactery(BlockPos pPos, BlockState pBlockState) {
		super(CQRBlockEntities.PHYLACTERY.get(), pPos, pBlockState);
	}
	
	@Nullable
	protected Entity findOwner() {
		if (this.level == null || this.ownerUUID == null) {
			return null;
		}
		
		if (this.level instanceof ServerLevel sl) {
			return sl.getEntity(this.ownerUUID);
		}
		
		return null;
	}
	
	public Optional<Entity> getOwner() {
		return Optional.ofNullable(this.currentOwner.get());
	}
	
	public Optional<BlockPos> getTargetPosition() {
		Optional<Entity> optOwner = this.getOwner();
		
		if (!optOwner.isPresent()) {
			return Optional.empty();
		}
		
		return Optional.ofNullable(optOwner.get().blockPosition());
	}

	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		
		if (this.ownerUUID != null) {
			pTag.putUUID("ownerUUID", this.ownerUUID);
		}
	}
	
	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		if (pTag.hasUUID("ownerUUID")) {
			this.ownerUUID = pTag.getUUID("ownerUUID");
			this.currentOwner.reset();
		}
	}
	

}
