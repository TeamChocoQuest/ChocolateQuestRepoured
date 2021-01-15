package team.cqr.cqrepoured.objects.entity.boss.spectrelord;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntitySpectreLordCurse extends Entity {

	private EntityLivingBase caster;
	private EntityLivingBase target;
	private int lastTimeHit;

	public EntitySpectreLordCurse(World worldIn) {
		this(worldIn, null, null);
	}

	public EntitySpectreLordCurse(World worldIn, EntityLivingBase caster, EntityLivingBase target) {
		super(worldIn);
		this.caster = caster;
		this.target = target;
		this.noClip = true;
		this.setSize(1.0F, 2.0F);
	}

	@Override
	public void onEntityUpdate() {
		if (!this.world.isRemote && (!this.caster.isEntityAlive() || !this.target.isEntityAlive())) {
			this.setDead();
		}

		super.onEntityUpdate();

		if (!this.world.isRemote) {
			Vec3d vec = this.target.getPositionVector().subtract(this.getPositionVector()).normalize().scale(0.12F);
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
	protected void readEntityFromNBT(NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {

	}

	@Override
	public boolean writeToNBTAtomically(NBTTagCompound compound) {
		return false;
	}

	@Override
	public boolean writeToNBTOptional(NBTTagCompound compound) {
		return false;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		return compound;
	}

}
