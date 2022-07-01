package team.cqr.cqrepoured.entity.bases;

import javax.annotation.Nullable;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.BoostHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRideable;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public abstract class EntityCQRMountBase extends AnimalEntity implements IRideable {

	private static final DataParameter<Boolean> DATA_SADDLE_ID = EntityDataManager.defineId(EntityCQRMountBase.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> DATA_BOOST_TIME = EntityDataManager.defineId(EntityCQRMountBase.class, DataSerializers.INT);

	private final BoostHelper steering = new BoostHelper(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);

	public EntityCQRMountBase(EntityType<? extends EntityCQRMountBase> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public void travelWithInput(Vector3d pTravelVec) {
		super.travel(pTravelVec);
	}

	@Override
	public void travel(Vector3d pTravelVector) {
		this.travel(this, this.steering, pTravelVector);
	}

	@Override
	public boolean boost() {
		return this.steering.boost(this.getRandom());
	}

	@Override
	public float getSteeringSpeed() {
		return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.225F;
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
	protected boolean canRide(Entity pEntity) {
		return pEntity instanceof AbstractEntityCQR || pEntity instanceof PlayerEntity;
	}

	@Override
	public void spawnChildFromBreeding(ServerWorld pLevel, AnimalEntity p_234177_2_) {
		return;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_SADDLE_ID, true);
		this.entityData.define(DATA_BOOST_TIME, 0);
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

}
