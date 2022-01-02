package team.cqr.cqrepoured.entity.boss.spectrelord;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class EntitySpectreLordCurse extends Entity {

	private LivingEntity caster;
	private LivingEntity target;
	private int lastTimeHit;

	public EntitySpectreLordCurse(World worldIn) {
		this(worldIn, null, null);
	}

	public EntitySpectreLordCurse(World worldIn, LivingEntity caster, LivingEntity target) {
		super(worldIn);
		this.caster = caster;
		this.target = target;
		this.noClip = true;
		this.setSize(1.0F, 2.0F);
	}

	@Override
	public void onEntityUpdate() {
		if (!this.world.isRemote && (this.caster == null || !this.caster.isEntityAlive() || !this.target.isEntityAlive())) {
			this.setDead();
		}

		super.onEntityUpdate();

		if (!this.world.isRemote) {
			Vector3d vec = this.target.getPositionVector().subtract(this.getPositionVector()).normalize().scale(0.12F);
			this.motionX = vec.x;
			this.motionY = vec.y;
			this.motionZ = vec.z;
		}

		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

		if (!this.world.isRemote && this.ticksExisted - this.lastTimeHit >= 10 && this.getEntityBoundingBox().grow(0.25D).intersects(this.target.getEntityBoundingBox())) {
			this.target.attackEntityFrom(new DamageSource("curse").setDamageBypassesArmor(), 4.0F);
			this.lastTimeHit = this.ticksExisted;
		}
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(CompoundNBT compound) {

	}

	@Override
	protected void writeEntityToNBT(CompoundNBT compound) {

	}

	@Override
	public boolean writeToNBTAtomically(CompoundNBT compound) {
		return false;
	}

	@Override
	public boolean writeToNBTOptional(CompoundNBT compound) {
		return false;
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT compound) {
		return compound;
	}

}
