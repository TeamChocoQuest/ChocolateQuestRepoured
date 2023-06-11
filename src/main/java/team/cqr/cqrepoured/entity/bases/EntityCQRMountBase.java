package team.cqr.cqrepoured.entity.bases;

import javax.annotation.Nullable;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.BoostHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.entity.IRideable;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class EntityCQRMountBase extends AnimalEntity implements IRideable {

	private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID = SynchedEntityData.defineId(EntityCQRMountBase.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(EntityCQRMountBase.class, EntityDataSerializers.INT);

	private final BoostHelper steering = new BoostHelper(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);

	public EntityCQRMountBase(EntityType<? extends EntityCQRMountBase> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void travelWithInput(Vec3 pTravelVec) {
		super.travel(pTravelVec);
	}

	@Override
	public void travel(Vec3 pTravelVector) {
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
		this.goalSelector.addGoal(0, new RandomSwimmingGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 0.9D));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 0.6D));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
	}

	@Override
	protected boolean canRide(Entity pEntity) {
		return pEntity instanceof AbstractEntityCQR || pEntity instanceof Player;
	}

	@Override
	public void spawnChildFromBreeding(ServerLevel pLevel, AnimalEntity p_234177_2_) {
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

		return entity != null && (entity instanceof AbstractEntityCQR || entity instanceof Player);
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (super.mobInteract(player, hand) != InteractionResult.SUCCESS) {
			if (!this.isVehicle()) {
				if (!this.level.isClientSide) {
					player.startRiding(this);
				}

				return InteractionResult.SUCCESS;
			}

		}
		return InteractionResult.FAIL;

	}

	@Override
	public AgeableEntity getBreedOffspring(ServerLevel p_241840_1_, AgeableEntity p_241840_2_) {
		return null;
	}

}
