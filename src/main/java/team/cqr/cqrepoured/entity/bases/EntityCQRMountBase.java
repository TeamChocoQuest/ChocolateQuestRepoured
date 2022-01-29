package team.cqr.cqrepoured.entity.bases;

import javax.annotation.Nullable;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRideable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public abstract class EntityCQRMountBase extends AnimalEntity implements IRideable {

	public EntityCQRMountBase(EntityType<? extends EntityCQRMountBase> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 0.9D));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 0.6D));
		this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
	}

	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return entityIn instanceof AbstractEntityCQR || entityIn instanceof PlayerEntity;
	}

	@Override
	public void spawnChildFromBreeding(ServerWorld pLevel, AnimalEntity p_234177_2_) {
		return;
	}

	@Override
	public boolean canFallInLove() {
		return false;
	}

	@Override
	@Nullable
	public Entity getControllingPassenger() {
		return this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
	}
	
	@Override
	public boolean canBeControlledByRider() {
		Entity entity = this.getControllingPassenger();

		return entity != null && (entity instanceof AbstractEntityCQR || entity instanceof PlayerEntity);
	}
	
	@Override
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		if (super.mobInteract(player, hand) != ActionResultType.SUCCESS) {
			if (!this.isVehicle()) {
				if (!this.level.isClientSide) {
					player.startRiding(this);
				}

				return ActionResultType.SUCCESS;
			}

		}
		return ActionResultType.FAIL;

	}
	
	@Override
	public AgeableEntity getBreedOffspring(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
		return null;
	}
	
	@Override
	public void travel(Vector3d direction) {
		if (this.isVehicle() && this.canBeControlledByRider()) {
			double vertical = direction.y();
			LivingEntity entity = (LivingEntity) this.getControllingPassenger();// this.getPassengers().isEmpty() ? null :
																						// (Entity)this.getPassengers().get(0);
			this.yRot = entity.yRot;
			this.yRotO = this.yRot;
			this.xRot = entity.xRot * 0.5F;
			this.setRot(this.yRot, this.xRot);
			this.yBodyRot = this.yRot;
			this.yHeadRot = this.yBodyRot;
			//this.stepHeight = 1.0F;
			//this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

			double v = 0.0;
			if (this.isInWater() || this.isInLava()) {
				v = vertical * 0.5;
			}
			if (this.isControlledByLocalInstance()) {
				float f = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.5F;

				this.setSpeed(f);
				super.travel(new Vector3d(direction.x * f, v, direction.z * f));
			} else {
				this.setDeltaMovement(Vector3d.ZERO);
			}

			this.prevLimbSwingAmount = this.limbSwingAmount;
			double d1 = this.posX - this.prevPosX;
			double d0 = this.posZ - this.prevPosZ;
			float f1 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

			if (f1 > 1.0F) {
				f1 = 1.0F;
			}

			this.limbSwingAmount += (f1 - this.limbSwingAmount) * 0.4F;
			this.limbSwing += this.limbSwingAmount;
		} else {
			this.stepHeight = 0.5F;
			this.jumpMovementFactor = 0.02F;
			super.travel(strafe, vertical, forward);
		}
	}
	
	@Override
	public void travelWithInput(Vector3d pTravelVec) {
		super.travel(pTravelVec);
	}

}
