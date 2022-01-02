package team.cqr.cqrepoured.entity.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.IDontRenderFire;

public class EntityBubble extends Entity implements IDontRenderFire {

	private static final int FLY_TIME_MAX = 160;

	private int flyTicks = 0;

	public EntityBubble(World worldIn) {
		super(worldIn);
		this.isImmuneToFire = true;
		this.setNoGravity(true);
	}

	@Override
	protected void entityInit() {

	}

	@Override
	public void onUpdate() {
		super.tick();

		if (!this.world.isRemote) {
			if (!this.isBeingRidden() || this.isInLava() || (this.collidedVertically && !this.onGround) || this.flyTicks > FLY_TIME_MAX) {
				if (this.isBeingRidden()) {
					Entity entity = this.getPassengers().get(0);
					entity.dismountRidingEntity();
					entity.setPositionAndUpdate(this.posX, this.posY + 0.5D * (this.height - entity.height), this.posZ);
					if (entity instanceof LivingEntity) {
						if (!((LivingEntity) entity).canBreatheUnderwater() && !((LivingEntity) entity).isPotionActive(Effects.WATER_BREATHING)) {
							entity.setAir(entity.getAir() - 5);
						}
					} else {
						entity.setAir(entity.getAir() - 5);
					}
				}
				this.setDead();
				return;
			}

			this.flyTicks++;
		}

		this.move(MoverType.SELF, 0.0D, 0.05D, 0.0D);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		this.flyTicks += 40;
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	protected void readEntityFromNBT(CompoundNBT compound) {
		this.flyTicks = compound.getInteger("flyTicks");
	}

	@Override
	protected void writeEntityToNBT(CompoundNBT compound) {
		compound.setInteger("flyTicks", this.flyTicks);
	}

	@Override
	public double getMountedYOffset() {
		if (this.isBeingRidden()) {
			Entity entity = this.getPassengers().get(0);
			return 0.5D * (this.height - entity.height) - entity.getYOffset();
		}
		return 0.0D;
	}

	@Override
	protected void addPassenger(Entity passenger) {
		super.addPassenger(passenger);
		float size = Math.max(passenger.width, passenger.height) + 0.1F;
		this.setSize(size, size);
	}

	@Override
	protected boolean canFitPassenger(Entity passenger) {
		return !this.isBeingRidden();
	}

	@Override
	public boolean shouldRiderSit() {
		return false;
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}

	@Override
	public boolean shouldDismountInWater(Entity rider) {
		return false;
	}

}
