package team.cqr.cqrepoured.entity.misc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraftforge.network.NetworkHooks;
import team.cqr.cqrepoured.entity.IDontRenderFire;
import team.cqr.cqrepoured.entity.IIsBeingRiddenHelper;

public class EntityBubble extends Entity implements IDontRenderFire, IIsBeingRiddenHelper {

	private static final int FLY_TIME_MAX = 160;

	private int flyTicks = 0;
	
	private float size = 1;

	public EntityBubble(EntityType<? extends EntityBubble> type, Level worldIn) {
		super(type, worldIn);
		this.setNoGravity(true);
	}
	
	@Override
	public EntitySize getDimensions(Pose p_213305_1_) {
		return super.getDimensions(p_213305_1_).scale(this.size);
	}

	protected static final Vec3 MOVEMENT_DIRECTION = new Vec3(0, 0.05, 0);

	@Override
	public void tick() {
		super.tick();

		if (!this.level.isClientSide) {
			if (this.isInLava() || (this.verticalCollision && !this.onGround) || this.flyTicks > FLY_TIME_MAX) {
				this.remove();
				return;
			}
			if (!this.getPassengers().isEmpty()) {
				Entity entity = this.getPassengers().get(0);
				//entity.unRide();
				Vec3 newPos = this.position().add(0, 0.5D * (this.getBbHeight() - entity.getBbHeight()), 0);
				entity.setPos(newPos.x, newPos.y, newPos.z);
				if (entity instanceof LivingEntity) {
					if (!((LivingEntity) entity).canBreatheUnderwater() && !((LivingEntity) entity).hasEffect(MobEffects.WATER_BREATHING)) {
						entity.setAirSupply(entity.getAirSupply() - 5);
					}
				} else {
					entity.setAirSupply(entity.getAirSupply() - 5);
				}
			}

			this.flyTicks++;
		}

		this.move(MoverType.SELF, MOVEMENT_DIRECTION);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		this.flyTicks += 40;
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public double getPassengersRidingOffset() {
		if (this.isBeingRidden()) {
			Entity entity = this.getPassengers().get(0);
			return 0.5D * (this.getBbHeight() - entity.getBbHeight()) - entity.getMyRidingOffset();
		}
		return 0.0D;
	}
	
	@Override
	protected void addPassenger(Entity passenger) {
		super.addPassenger(passenger);
		float size = Math.max(passenger.getBbWidth(), passenger.getBbHeight()) * 1.05F;
		this.setSize(size);
	}

	
	private void setSize(float sizeIn) {
		this.size = sizeIn;
		
		this.refreshDimensions();
	}

	@Override
	protected boolean canAddPassenger(Entity passenger) {
		return this.getPassengers().isEmpty();
	}

	@Override
	public boolean shouldRiderSit() {
		return false;
	}
	
	/*@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	} */

	@Override
	protected void defineSynchedData() {
		
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		this.flyTicks = compound.getInt("flyTicks");
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putInt("flyTicks", this.flyTicks);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
