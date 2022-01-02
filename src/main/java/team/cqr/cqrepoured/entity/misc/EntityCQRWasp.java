package team.cqr.cqrepoured.entity.misc;

import net.minecraft.block.*;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityCQRWasp extends CreatureEntity implements IFlyingAnimal {

	public EntityCQRWasp(World worldIn) {
		super(worldIn);
		this.setSize(0.75F, 0.75F);
		this.moveHelper = new FlyingMovementController(this);
	}

	@Override
	protected void initEntityAI() {
		super.registerGoals();
		this.tasks.addTask(4, new PanicGoal(this, 1.25D));
		this.tasks.addTask(5, new WaterAvoidingRandomFlyingGoal(this, 1D));

		this.targetTasks.addTask(0, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 100, true, true, input -> (input instanceof PlayerEntity || input.getCreatureAttribute() == CreatureAttribute.ILLAGER || input.getCreatureAttribute() == CreatureAttribute.UNDEAD)));
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
	}

	@Override
	protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	@Override
	protected boolean canBeRidden(Entity entityIn) {
		// return entityIn instanceof EntityCQRGoblin;
		return false;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(Attributes.FLYING_SPEED);
		this.getEntityAttribute(Attributes.MAX_HEALTH).setBaseValue(12.0D);
		this.getEntityAttribute(Attributes.FLYING_SPEED).setBaseValue(0.4000000059604645D);
		this.getEntityAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
	}

	/**
	 * Returns new PathNavigateGround instance
	 */
	@Override
	protected PathNavigator createNavigator(World worldIn) {
		FlyingPathNavigator pathnavigateflying = new FlyingPathNavigator(this, worldIn);
		pathnavigateflying.setCanOpenDoors(false);
		pathnavigateflying.setCanFloat(true);
		pathnavigateflying.setCanEnterDoors(true);
		return pathnavigateflying;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		int i = MathHelper.floor(this.posX);
		int j = MathHelper.floor(this.getEntityBoundingBox().minY);
		int k = MathHelper.floor(this.posZ);
		BlockPos blockpos = new BlockPos(i, j, k);
		Block block = this.world.getBlockState(blockpos.down()).getBlock();
		return block instanceof LeavesBlock || block == Blocks.GRASS || block instanceof LogBlock || block == Blocks.AIR && this.world.getLight(blockpos) > 8 && super.getCanSpawnHere();
	}

}
