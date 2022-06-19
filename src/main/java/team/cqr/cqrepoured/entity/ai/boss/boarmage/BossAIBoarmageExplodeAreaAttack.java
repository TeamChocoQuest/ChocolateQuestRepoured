package team.cqr.cqrepoured.entity.ai.boss.boarmage;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.boss.EntityCQRBoarmage;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class BossAIBoarmageExplodeAreaAttack extends AbstractCQREntityAI<EntityCQRBoarmage> {

	private static final int MIN_EXPLOSIONS = 6;
	private static final int MAX_EXPLOSIONS = 12;

	private static final long TIMEDIV = 20;
	private long lastExplodeTechTick = 0;

	private int explosionCount = 0;
	private List<BlockPos> explosions = new ArrayList<>();

	private static final int MIN_COOLDOWN = 20;
	private static final int MAX_COOLDOWN = 60;
	private int cooldown = 40;

	public BossAIBoarmageExplodeAreaAttack(EntityCQRBoarmage entity) {
		super(entity);
	}

	@Override
	public boolean canUse() {
		if (this.cooldown > 0) {
			this.cooldown--;
			return false;
		}
		return this.entity != null && this.entity.isAlive() && this.entity.isExecutingExplodeAreaAttack();
	}

	@Override
	public boolean canContinueToUse() {
		return this.entity != null && this.entity.isAlive() && this.entity.isExecutingExplodeAreaAttack();
	}

	@Override
	public void start() {
		this.explosionCount = DungeonGenUtils.randomBetween(MIN_EXPLOSIONS, MAX_EXPLOSIONS, this.entity.getRandom());
		this.lastExplodeTechTick = this.entity.tickCount;
		this.addExplosionLoc();
	}

	private void addExplosionLoc() {
		if (this.entity.getTarget() != null) {
			this.explosions.add(this.entity.getTarget().blockPosition());
		}
	}

	@Override
	public void tick() {
		super.tick();
		// Particles on positions
		if (this.entity.tickCount % 5 == 0) {
			for (BlockPos p : this.explosions) {
				if(!this.world.isClientSide) {
					ServerWorld sw = (ServerWorld)this.world;
					sw.sendParticles(ParticleTypes.FLAME, p.getX(), p.getY(), p.getZ(), 3, -0.125, 0.125, -0.125, 0.05);
					sw.sendParticles(ParticleTypes.FLAME, p.getX(), p.getY(), p.getZ(), 3, -0.125, 0.125, 0.125, 0.05);
					sw.sendParticles(ParticleTypes.FLAME, p.getX(), p.getY(), p.getZ(), 3, 0.125, 0.125, -0.125, 0.05);
					sw.sendParticles(ParticleTypes.FLAME, p.getX(), p.getY(), p.getZ(), 3, 0.125, 0.125, 0.125, 0.05);
				}
			}
		}

		if (this.explosions.size() >= this.explosionCount) {
			// EXPLOSION!!!!

			for (BlockPos p : this.explosions) {
				this.world.explode(this.entity, p.getX(), p.getY(), p.getZ(), 3, this.entity.getRandom().nextBoolean(), CQRConfig.SERVER_CONFIG.bosses.boarmageExplosionAreaDestroysTerrain.get() ? Mode.DESTROY : Mode.NONE);
			}

			this.stop();
		} else if (Math.abs(this.entity.tickCount - this.lastExplodeTechTick) > TIMEDIV) {
			this.entity.swing(Hand.OFF_HAND);
			this.lastExplodeTechTick = this.entity.tickCount;
			this.addExplosionLoc();
		}
	}

	@Override
	public void stop() {
		super.stop();
		this.explosionCount = 0;
		this.explosions.clear();
		this.cooldown = DungeonGenUtils.randomBetween(MIN_COOLDOWN, MAX_COOLDOWN, this.entity.getRandom());
		this.entity.stopExplodeAreaAttack();
	}

}
