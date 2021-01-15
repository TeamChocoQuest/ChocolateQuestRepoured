package team.cqr.cqrepoured.objects.entity.ai.boss.spectrelord;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.objects.entity.ai.spells.AbstractEntityAISpell;
import team.cqr.cqrepoured.objects.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import team.cqr.cqrepoured.objects.entity.boss.spectrelord.EntityCQRSpectreLord;
import team.cqr.cqrepoured.objects.entity.boss.spectrelord.EntitySpectreLordIllusion;

public class EntityAISpectreLordSummonIllusions extends AbstractEntityAISpell<EntityCQRSpectreLord> implements IEntityAISpellAnimatedVanilla {

	private final int amount;
	private final int lifeTime;

	public EntityAISpectreLordSummonIllusions(EntityCQRSpectreLord entity, int cooldown, int chargingTicks, int amount, int lifeTime) {
		super(entity, cooldown, chargingTicks, 1);
		this.setup(true, true, true, true);
		this.amount = Math.max(amount, 1);
		this.lifeTime = lifeTime;
	}

	@Override
	public void startCastingSpell() {
		super.startCastingSpell();

		if (this.entity.getSummonedEntities().isEmpty()) {
			this.summonIllusions();
		} else {
			this.absorbIllusions();
		}
	}

	private void summonIllusions() {
		Vec3d start = this.entity.getPositionEyes(1.0F);
		double d = this.random.nextDouble() * 360.0D;

		for (int i = 0; i < this.amount; i++) {
			double d1 = d + ((double) i / (double) this.amount + (this.random.nextDouble() - 0.5D) * 0.1D) * 360.0D;
			Vec3d look = Vec3d.fromPitchYaw(30.0F, (float) d1);
			Vec3d end = start.add(look.scale(8.0D));
			RayTraceResult result = this.world.rayTraceBlocks(start, end, false, true, true);
			if(result == null || result.hitVec == null) {
				return;
			}

			double x = result.hitVec.x;
			double y = result.hitVec.y;
			double z = result.hitVec.z;
			if (result.sideHit != EnumFacing.UP) {
				double dx = this.entity.posX - x;
				double dz = this.entity.posZ - z;
				double d2 = 0.5D / Math.sqrt(dx * dx + dz * dz);
				x += dx * d2;
				z += dz * d2;
			}

			EntitySpectreLordIllusion illusion = new EntitySpectreLordIllusion(this.world, this.entity, this.lifeTime, i == 0, i == 2);
			illusion.setPosition(x, y, z);
			illusion.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(illusion)), null);
			this.entity.addSummonedEntityToList(illusion);
			this.world.spawnEntity(illusion);
			((WorldServer) this.world).spawnParticle(EnumParticleTypes.SPELL, illusion.posX, illusion.posY + 0.5D * illusion.height, illusion.posZ, 8, 0.25D, 0.25D, 0.25D, 0.5D);
		}
	}

	private void absorbIllusions() {
		super.startCastingSpell();
		float heal = 0.05F;
		for (Entity e : this.entity.getSummonedEntities()) {
			if (e.getDistanceSq(this.entity) <= 32.0D * 32.0D) {
				heal += 0.05F;
				e.setDead();
				((WorldServer) this.world).spawnParticle(EnumParticleTypes.SPELL_INSTANT, e.posX, e.posY + e.height * 0.5D, e.posZ, 4, 0.25D, 0.25D, 0.25D, 0.5D);
			}
		}
		AxisAlignedBB aabb = new AxisAlignedBB(this.entity.posX - 8.0D, this.entity.posY - 0.5D, this.entity.posZ - 8.0D, this.entity.posX + 8.0D, this.entity.posY + this.entity.height + 0.5D, this.entity.posZ + 8.0D);
		CQRFaction faction = this.entity.getFaction();
		for (EntityLivingBase e : this.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb, e -> faction == null || !faction.isAlly(e))) {
			heal += 0.05F;
			e.attackEntityFrom(DamageSource.causeMobDamage(this.entity).setDamageBypassesArmor(), 4.0F);
			e.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 1, false, false));
		}
		this.entity.heal(this.entity.getMaxHealth() * heal);
		// TODO spawn shockwave entity
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.EVOCATION_ILLAGER_PREPARE_SUMMON;
	}

	@Override
	public int getWeight() {
		if (this.entity.getSummonedEntities().isEmpty()) {
			return 10;
		}
		return this.entity.getHealth() / this.entity.getMaxHealth() < 0.3334F ? 40 : 20;
	}

	@Override
	public boolean ignoreWeight() {
		return false;
	}

	@Override
	public float getRed() {
		return 0.5F;
	}

	@Override
	public float getGreen() {
		return 0.95F;
	}

	@Override
	public float getBlue() {
		return 1.0F;
	}

}
