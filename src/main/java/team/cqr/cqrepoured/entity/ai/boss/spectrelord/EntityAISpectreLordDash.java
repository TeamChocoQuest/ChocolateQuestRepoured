package team.cqr.cqrepoured.entity.ai.boss.spectrelord;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.BlockMode;
import net.minecraft.world.level.ClipContext.FluidMode;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.PacketDistributor;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.ai.spells.AbstractEntityAISpell;
import team.cqr.cqrepoured.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntityCQRSpectreLord;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateEntityPrevPos;
import team.cqr.cqrepoured.util.math.BoundingBox;

import java.util.List;

public class EntityAISpectreLordDash extends AbstractEntityAISpell<EntityCQRSpectreLord> implements IEntityAISpellAnimatedVanilla {

	private final double dashSpeed;
	private final double dashWidth;
	private LivingEntity target;
	private Vec3 targetDirection;
	private double yawRadian;
	private float yawDegree;

	public EntityAISpectreLordDash(EntityCQRSpectreLord entity, int cooldown, int chargeUpTicks, int dashTicks, double dashSpeed, double dashWidth) {
		super(entity, cooldown, chargeUpTicks, 60 + dashTicks);
		this.setup(true, false, false, false);
		this.dashSpeed = dashSpeed;
		this.dashWidth = dashWidth;
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.target = null;
	}

	@Override
	public void startChargingSpell() {
		super.startChargingSpell();
		Faction faction = this.entity.getFaction();
		if (faction == null) {
			this.target = this.entity.getTarget();
		} else {
			AABB aabb = new AABB(this.entity.getX() - 16.0D, this.entity.getY() - 2.0D, this.entity.getZ() - 16.0D, this.entity.getX() + 16.0D, this.entity.getY() + this.entity.getBbHeight() + 2.0D, this.entity.getZ() + 16.0D);
			List<LivingEntity> list = this.world.getEntitiesOfClass(LivingEntity.class, aabb, e -> TargetUtil.PREDICATE_ATTACK_TARGET.apply(e) && faction.isEnemy(e));
			if (list.isEmpty()) {
				this.target = this.entity.getTarget();
			} else {
				this.target = list.get(this.random.nextInt(list.size()));
				this.entity.setTarget(this.target);
			}
		}
	}

	@Override
	public void startCastingSpell() {
		super.startCastingSpell();
		this.entity.setInvisibility(25);
	}

	@Override
	public void castSpell() {
		if (this.tick == this.chargingTicks + 20 - 1) {
			double oldX = this.entity.getX();
			double oldY = this.entity.getY();
			double oldZ = this.entity.getZ();

			this.teleportBehindTarget();

			double dx = this.target.getX() - this.entity.getX();
			double dz = this.target.getZ() - this.entity.getZ();
			double dist = Math.sqrt(dx * dx + dz * dz);
			this.targetDirection = new Vec3(dx / dist * this.dashSpeed, 0.0D, dz / dist * this.dashSpeed);
			this.yawRadian = Math.atan2(-dx, dz);
			this.yawDegree = (float) Math.toDegrees(this.yawRadian);
			this.entity.yRot = this.yawDegree;

			CQRMain.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> this.entity), new SPacketUpdateEntityPrevPos(this.entity));

			this.entity.playSound(SoundEvents.EVOKER_PREPARE_WOLOLO, 1.0F, 0.9F + this.random.nextFloat() * 0.2F);
			((ServerLevel) this.world).addParticle(ParticleTypes.PORTAL, oldX, oldY + this.entity.getBbHeight() * 0.5D, oldZ, /*4,*/ 0.2D, 0.2D, 0.2D/*, 0.0D*/);
			((ServerLevel) this.world).addParticle(ParticleTypes.PORTAL, this.entity.getX(), this.entity.getY() + this.entity.getBbHeight() * 0.5D, this.entity.getZ(), /*4,*/ 0.2D, 0.2D, 0.2D/*, 0.0D*/);
		} else if (this.tick > this.chargingTicks + 20 - 1) {
			this.entity.yRot = this.yawDegree;

			if (this.tick > this.chargingTicks + 60 - 1) {
				this.dashToTarget();
			}
		}
	}

	private void teleportBehindTarget() {
		double dx = this.target.getX() - this.entity.getX();
		double dz = this.target.getZ() - this.entity.getZ();
		double dist = Math.sqrt(dx * dx + dz * dz);
		Vec3 start = this.target.position();
		Vec3 end = start.add(dx / dist * 3.0D, 0.0D, dz / dist * 3.0D);
		ClipContext rtc = new ClipContext(start, end, BlockMode.COLLIDER, FluidMode.ANY, null);
		HitResult result = this.world.clip(rtc);
		Vec3 vec = TargetUtil.getPositionNearTarget(this.world, this.entity, result != null ? result.getLocation() : end, start, 2.0D, 4.0D, 1.0D);
		if (vec != null) {
			this.entity.setPos(vec.x, vec.y, vec.z);
		}
	}

	private void dashToTarget() {
		Vec3 vec1 = this.entity.position();

		boolean noClip = this.entity.noPhysics;
		if (!noClip) {
			Vec3 start = new Vec3(this.entity.getX(), this.entity.getY() + this.entity.getEyeHeight(), this.entity.getZ());
			Vec3 end = start.add(this.targetDirection);
			ClipContext rtc = new ClipContext(start, end, BlockMode.COLLIDER, FluidMode.ANY, null);
			if (this.world.clip(rtc) == null && this.world.noCollision(this.entity.getBoundingBox().move(this.targetDirection))) {
				this.entity.noPhysics = true;
			}
		}
		this.entity.move(MoverType.SELF, this.targetDirection);
		this.entity.noPhysics = noClip;

		Vec3 vec2 = this.entity.position();
		Vec3 vec3 = new Vec3(-this.dashWidth, 0.0D, 0.0D);
		Vec3 vec4 = new Vec3(this.dashWidth, this.entity.getBbHeight(), vec2.subtract(vec1).length());
		BoundingBox bb = new BoundingBox(vec3, vec4, this.yawRadian, 0.0D, vec1);
		List<LivingEntity> list = BoundingBox.getEntitiesInsideBB(this.world, this.entity, LivingEntity.class, bb);
		Faction faction = this.entity.getFaction();
		for (LivingEntity entity : list) {
			if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(entity)) {
				continue;
			}
			if (faction != null && faction.isAlly(entity)) {
				continue;
			}
			entity.hurt(DamageSource.mobAttack(this.entity).bypassArmor(), 10.0F);
		}
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.EVOKER_PREPARE_ATTACK;
	}

	@Override
	public int getWeight() {
		return 10;
	}

	@Override
	public boolean ignoreWeight() {
		return false;
	}

	@Override
	public float getRed() {
		return 0.2F;
	}

	@Override
	public float getGreen() {
		return 0.1F;
	}

	@Override
	public float getBlue() {
		return 0.6F;
	}

}
