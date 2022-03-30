package team.cqr.cqrepoured.entity.boss.spectrelord;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntitySpectreLordCurse extends Entity {

	private LivingEntity caster;
	private LivingEntity target;
	private int lastTimeHit;

	public EntitySpectreLordCurse(EntityType<? extends EntitySpectreLordCurse> type, World worldIn) {
		this(type, worldIn, null, null);
	}

	public EntitySpectreLordCurse(EntityType<? extends EntitySpectreLordCurse> type, World worldIn, LivingEntity caster, LivingEntity target) {
		super(type, worldIn);
		this.caster = caster;
		this.target = target;
		this.noPhysics = true;
		//this.setSize(1.0F, 2.0F);
	}

	@Override
	public void tick() {
		if (!this.level.isClientSide && (this.caster == null || !this.caster.isAlive() || !this.target.isAlive())) {
			this.remove();
		}

		super.tick();

		if (!this.level.isClientSide) {
			Vector3d vec = this.target.position().subtract(this.position()).normalize().scale(0.12F);
			this.setDeltaMovement(vec);
		}

		this.move(MoverType.SELF, this.getDeltaMovement());

		if (!this.level.isClientSide && this.tickCount - this.lastTimeHit >= 10 && this.getBoundingBox().inflate(0.25D).intersects(this.target.getBoundingBox())) {
			this.target.hurt(new DamageSource("curse").bypassArmor(), 4.0F);
			this.lastTimeHit = this.tickCount;
		}
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	protected void defineSynchedData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT pCompound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT pCompound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		// TODO Auto-generated method stub
		return NetworkHooks.getEntitySpawningPacket(this);
	}


}
