package team.cqr.cqrepoured.entity.boss.spectrelord;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class EntitySpectreLordCurse extends Entity {

	private LivingEntity caster;
	private LivingEntity target;
	private int lastTimeHit;

	public EntitySpectreLordCurse(EntityType<? extends EntitySpectreLordCurse> type, Level worldIn) {
		this(type, worldIn, null, null);
	}
	
	public EntitySpectreLordCurse(Level worldIn, LivingEntity caster, LivingEntity target) {
		this(CQREntityTypes.SPECTRE_LORD_CURSE.get(), worldIn, caster, target);
	}

	public EntitySpectreLordCurse(EntityType<? extends EntitySpectreLordCurse> type, Level worldIn, LivingEntity caster, LivingEntity target) {
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
			Vec3 vec = this.target.position().subtract(this.position()).normalize().scale(0.12F);
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
	protected void readAdditionalSaveData(CompoundTag pCompound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag pCompound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		// TODO Auto-generated method stub
		return NetworkHooks.getEntitySpawningPacket(this);
	}


}
