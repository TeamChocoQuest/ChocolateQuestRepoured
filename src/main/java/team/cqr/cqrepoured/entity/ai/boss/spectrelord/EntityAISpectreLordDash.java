package team.cqr.cqrepoured.entity.ai.boss.spectrelord;

import java.util.List;

import org.joml.Vector3d;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.network.PacketDistributor;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.ai.spells.AbstractEntityAISpell;
import team.cqr.cqrepoured.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntityCQRSpectreLord;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateEntityPrevPos;
import team.cqr.cqrepoured.util.math.BoundingBox;

public class EntityAISpectreLordDash extends AbstractEntityAISpell<EntityCQRSpectreLord> implements IEntityAISpellAnimatedVanilla {

	private final double dashSpeed;
	private final double dashWidth;
	private LivingEntity target;
	private Vector3d targetDirection;
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
			AxisAlignedBB aabb = new AxisAlignedBB(this.entity.getX() - 16.0D, this.entity.getY() - 2.0D, this.entity.getZ() - 16.0D, this.entity.getX() + 16.0D, this.entity.getY() + this.entity.getBbHeight() + 2.0D, this.entity.getZ() + 16.0D);
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
			this.targetDirection = new Vector3d(dx / dist * this.dashSpeed, 0.0D, dz / dist * this.dashSpeed);
			this.yawRadian = Math.atan2(-dx, dz);
			this.yawDegree = (float) Math.toDegrees(this.yawRadian);
			this.entity.yRot = this.yawDegree;

			CQRMain.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> this.entity), new SPacketUpdateEntityPrevPos(this.entity));

			this.entity.playSound(SoundEvents.EVOKER_PREPARE_WOLOLO, 1.0F, 0.9F + this.random.nextFloat() * 0.2F);
			((ServerWorld) this.world).addParticle(ParticleTypes.PORTAL, oldX, oldY + this.entity.getBbHeight() * 0.5D, oldZ, /*4,*/ 0.2D, 0.2D, 0.2D/*, 0.0D*/);
			((ServerWorld) this.world).addParticle(ParticleTypes.PORTAL, this.entity.getX(), this.entity.getY() + this.entity.getBbHeight() * 0.5D, this.entity.getZ(), /*4,*/ 0.2D, 0.2D, 0.2D/*, 0.0D*/);
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
		Vector3d start = this.target.position();
		Vector3d end = start.add(dx / dist * 3.0D, 0.0D, dz / dist * 3.0D);
		RayTraceContext rtc = new RayTraceContext(start, end, BlockMode.COLLIDER, FluidMode.ANY, null);
		RayTraceResult result = this.world.clip(rtc);
		Vector3d vec = TargetUtil.getPositionNearTarget(this.world, this.entity, result != null ? result.getLocation() : end, start, 2.0D, 4.0D, 1.0D);
		if (vec != null) {
			this.entity.setPos(vec.x, vec.y, vec.z);
		}
	}

	private void dashToTarget() {
		Vector3d vec1 = this.entity.position();

		boolean noClip = this.entity.noPhysics;
		if (!noClip) {
			Vector3d start = new Vector3d(this.entity.getX(), this.entity.getY() + this.entity.getEyeHeight(), this.entity.getZ());
			Vector3d end = start.add(this.targetDirection);
			RayTraceContext rtc = new RayTraceContext(start, end, BlockMode.COLLIDER, FluidMode.ANY, null);
			if (this.world.clip(rtc) == null && this.world.noCollision(this.entity.getBoundingBox().move(this.targetDirection))) {
				this.entity.noPhysics = true;
			}
		}
		this.entity.move(MoverType.SELF, this.targetDirection);
		this.entity.noPhysics = noClip;

		Vector3d vec2 = this.entity.position();
		Vector3d vec3 = new Vector3d(-this.dashWidth, 0.0D, 0.0D);
		Vector3d vec4 = new Vector3d(this.dashWidth, this.entity.getBbHeight(), vec2.subtract(vec1).length());
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
