package team.cqr.cqrepoured.entity.ai.boss.endercalamity;

import java.util.Optional;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SoundEvents;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity.E_CALAMITY_HAND;
import team.cqr.cqrepoured.entity.boss.endercalamity.phases.EEnderCalamityPhase;
import team.cqr.cqrepoured.entity.projectiles.ProjectileThrownBlock;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.VectorUtil;

public class BossAIBlockThrower extends AbstractBossAIEnderCalamity {

	enum E_HAND_STATE {
		NO_BLOCK, BLOCK, THROWING;

		public E_HAND_STATE getNextState() {
			switch (this) {
			case BLOCK:
				return THROWING;
			case NO_BLOCK:
				return BLOCK;
			case THROWING:
				return NO_BLOCK;
			}
			return BLOCK;
		}
	}

	protected static final int THROWING_TIME = 40; // Animation length is 1 second => 20 ticks

	private E_HAND_STATE[] handstates = new E_HAND_STATE[] { E_HAND_STATE.NO_BLOCK, E_HAND_STATE.NO_BLOCK, E_HAND_STATE.NO_BLOCK, E_HAND_STATE.NO_BLOCK, E_HAND_STATE.NO_BLOCK, E_HAND_STATE.NO_BLOCK };
	private int[] handCooldowns = new int[] { 100, 100, 100, 100, 100, 100 };
	protected static final int MAX_EQUIPPED_BLOCKS = 3;

	protected E_HAND_STATE getStateOfHand(EntityCQREnderCalamity.E_CALAMITY_HAND hand) {
		return this.handstates[hand.getIndex()];
	}

	protected int getCooldownOfHand(EntityCQREnderCalamity.E_CALAMITY_HAND hand) {
		return this.handCooldowns[hand.getIndex()];
	}

	protected int getCountOfEquippedHands() {
		int counter = 0;
		for (E_HAND_STATE state : this.handstates) {
			if (state == E_HAND_STATE.BLOCK) {
				counter++;
			}
		}
		return counter;
	}

	protected boolean blockLimitReached() {
		int counter = this.getCountOfEquippedHands();
		return counter >= MAX_EQUIPPED_BLOCKS;
	}

	public BossAIBlockThrower(EntityCQREnderCalamity entity) {
		super(entity);
		for (EntityCQREnderCalamity.E_CALAMITY_HAND hand : EntityCQREnderCalamity.E_CALAMITY_HAND.values()) {
			if (entity.getBlockFromHand(hand).isPresent()) {
				this.handstates[hand.getIndex()] = E_HAND_STATE.BLOCK;
			}
			this.handCooldowns[hand.getIndex()] = DungeonGenUtils.randomBetween(200, 400);
		}
	}

	@Override
	public boolean canUse() {
		if (this.entity != null && this.entity.hasAttackTarget()) {
			return super.canUse();
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return this.canUse();
	}

	protected void execHandStateBlockWhenDone(EntityCQREnderCalamity.E_CALAMITY_HAND hand) {
		this.throwBlockOfHand(hand);
	}

	protected void execHandStateThrowingWhenDone(EntityCQREnderCalamity.E_CALAMITY_HAND hand) {
		this.handCooldowns[hand.getIndex()] = DungeonGenUtils.randomBetween(80, 140, this.entity.getRandom());
	}

	protected void execHandStateNoBlockWhenDone(EntityCQREnderCalamity.E_CALAMITY_HAND hand) {
		BlockState block = DungeonGenUtils.percentageRandom(0.25) ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.END_STONE.defaultBlockState();
		this.entity.equipBlock(hand, block);
		this.handCooldowns[hand.getIndex()] = DungeonGenUtils.randomBetween(40, 200, this.entity.getRandom());
		this.handstates[hand.getIndex()] = E_HAND_STATE.BLOCK;
		// DONE: SPawn some particles
		this.spawnEquipParticlesForHand(hand);
	}

	@Override
	public void tick() {
		super.tick();

		for (EntityCQREnderCalamity.E_CALAMITY_HAND hand : EntityCQREnderCalamity.E_CALAMITY_HAND.values()) {
			switch (this.getStateOfHand(hand)) {
			case BLOCK:
				if (this.entity.getCurrentPhase().getPhaseObject().canThrowBlocksDuringPhase()) {
					this.handCooldowns[hand.getIndex()]--;
					if (this.getCooldownOfHand(hand) <= 0) {
						this.execHandStateBlockWhenDone(hand);
					}
				}
				break;
			case NO_BLOCK:
				if (this.entity.getCurrentPhase().getPhaseObject().canPickUpBlocksDuringPhase() && !this.blockLimitReached()) {
					this.handCooldowns[hand.getIndex()]--;
					if (this.getCooldownOfHand(hand) <= 0) {
						this.execHandStateNoBlockWhenDone(hand);
					}
				}
				break;
			case THROWING:
				this.handCooldowns[hand.getIndex()]--;
				if (this.getCooldownOfHand(hand) <= 0) {
					this.setStateOfHand(hand, E_HAND_STATE.NO_BLOCK);
					this.execHandStateThrowingWhenDone(hand);
				}

				break;
			}
		}

	}

	protected void spawnEquipParticlesForHand(EntityCQREnderCalamity.E_CALAMITY_HAND hand) {
		if (this.world instanceof ServerWorld && CQRConfig.bosses.calamityBlockEquipParticles) {
			ServerWorld ws = (ServerWorld) this.world;
			Vector3d pos = this.getPositionOfHand(hand);
			for (int i = 0; i < 50; i++) {
				double dx = -0.5 + this.entity.getRandom().nextDouble();
				dx *= 2;
				double dy = -0.5 + this.entity.getRandom().nextDouble();
				dy *= 2;
				double dz = -0.5 + this.entity.getRandom().nextDouble();
				dz *= 2;
				for(int j = 0; j < 10; j++) {
					ws.addParticle(ParticleTypes.ENCHANT, pos.x, pos.y, pos.z, dx * 0.05, dy * 0.05, dz * 0.05);
				}
				this.entity.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.5F, 1.25F);
			}
		}
	}

	@Override
	public void stop() {
		super.stop();
	}

	protected boolean throwBlockOfHand(EntityCQREnderCalamity.E_CALAMITY_HAND hand) {
		Vector3d v = this.entity.getLookAngle().normalize();
		if (this.entity.hasAttackTarget()) {
			v = this.entity.getTarget().position().subtract(this.entity.position());
			v = v.normalize();
			v = v.scale(0.5);
			v = v.add(0, 0.5, 0);
		}
		return this.throwBlockOfHand(hand, v);
	}

	protected Vector3d getPositionOfHand(EntityCQREnderCalamity.E_CALAMITY_HAND hand) {
		Vector3d offset = this.entity.getLookAngle().normalize().scale(1.25);
		offset = new Vector3d(offset.x, 0, offset.z);
		offset = VectorUtil.rotateVectorAroundY(offset, hand.isLeftSided() ? 90 : 270);
		switch (hand.name().split("_")[1].toUpperCase()) {
		case "LOWER":
			offset = offset.add(0, 0.5D, 0);
			break;
		case "MIDDLE":
			offset = offset.add(0, 1.0D, 0);
			break;
		case "UPPER":
			offset = offset.add(0, 1.5D, 0);
			break;
		default:
			break;
		}
		offset = offset.scale(this.entity.getSizeVariation());
		Vector3d position = this.entity.position().add(offset);
		return position;
	}

	protected boolean throwBlockOfHand(EntityCQREnderCalamity.E_CALAMITY_HAND hand, Vector3d velocity) {
		if (this.getStateOfHand(hand) == E_HAND_STATE.BLOCK) {
			// DONE: Implement
			/*
			 * Calculate offset vector to spawn the projectile
			 * Actually spawn the projectile and send it flying
			 */
			Optional<BlockState> handContent = this.entity.getBlockFromHand(hand);
			if (!handContent.isPresent()) {
				return false;
			}
			Vector3d position = this.getPositionOfHand(hand);
			BlockState block = handContent.get();
			ProjectileThrownBlock blockProj = new ProjectileThrownBlock(this.world, this.entity, block, true);
			blockProj.setPos(position.x, position.y, position.z);
			/*blockProj.motionX = velocity.x;
			blockProj.motionY = velocity.y;
			blockProj.motionZ = velocity.z;
			blockProj.velocityChanged = true;*/
			blockProj.setDeltaMovement(velocity);
			blockProj.hasImpulse = true;

			this.world.addFreshEntity(blockProj);

			this.setStateOfHand(hand, E_HAND_STATE.THROWING);
			this.handCooldowns[hand.getIndex()] = THROWING_TIME;
			this.entity.swingHand(hand);
			this.entity.removeHandBlock(hand);

			return true;
		}
		return false;
	}

	protected void setStateOfHand(E_CALAMITY_HAND hand, E_HAND_STATE state) {
		this.handstates[hand.getIndex()] = state;
	}

	public void forceDropAllBlocks() {
		for (EntityCQREnderCalamity.E_CALAMITY_HAND hand : EntityCQREnderCalamity.E_CALAMITY_HAND.values()) {
			this.throwBlockOfHand(hand, new Vector3d(0, -0.5, 0));
		}
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase currentPhase) {
		return currentPhase.getPhaseObject().canPickUpBlocksDuringPhase() || currentPhase.getPhaseObject().canThrowBlocksDuringPhase();
	}

}
