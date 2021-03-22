package team.cqr.cqrepoured.objects.entity.ai.boss.spectrelord;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateEntityPrevPos;
import team.cqr.cqrepoured.objects.entity.ai.spells.AbstractEntityAISpell;
import team.cqr.cqrepoured.objects.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import team.cqr.cqrepoured.objects.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.objects.entity.boss.spectrelord.EntityCQRSpectreLord;
import team.cqr.cqrepoured.util.math.BoundingBox;

public class EntityAISpectreLordDash extends AbstractEntityAISpell<EntityCQRSpectreLord> implements IEntityAISpellAnimatedVanilla {

	private final double dashSpeed;
	private final double dashWidth;
	private EntityLivingBase target;
	private Vec3d targetDirection;
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
		CQRFaction faction = this.entity.getFaction();
		if (faction == null) {
			this.target = this.entity.getAttackTarget();
		} else {
			AxisAlignedBB aabb = new AxisAlignedBB(this.entity.posX - 16.0D, this.entity.posY - 2.0D, this.entity.posZ - 16.0D, this.entity.posX + 16.0D, this.entity.posY + this.entity.height + 2.0D, this.entity.posZ + 16.0D);
			List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb, e -> TargetUtil.PREDICATE_ATTACK_TARGET.apply(e) && faction.isEnemy(e));
			if (list.isEmpty()) {
				this.target = this.entity.getAttackTarget();
			} else {
				this.target = list.get(this.random.nextInt(list.size()));
				this.entity.setAttackTarget(this.target);
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
			double oldX = this.entity.posX;
			double oldY = this.entity.posY;
			double oldZ = this.entity.posZ;

			this.teleportBehindTarget();

			double dx = this.target.posX - this.entity.posX;
			double dz = this.target.posZ - this.entity.posZ;
			double dist = Math.sqrt(dx * dx + dz * dz);
			this.targetDirection = new Vec3d(dx / dist * this.dashSpeed, 0.0D, dz / dist * this.dashSpeed);
			this.yawRadian = Math.atan2(-dx, dz);
			this.yawDegree = (float) Math.toDegrees(this.yawRadian);
			this.entity.rotationYaw = this.yawDegree;

			CQRMain.NETWORK.sendToAllTracking(new SPacketUpdateEntityPrevPos(this.entity), this.entity);

			this.entity.playSound(SoundEvents.EVOCATION_ILLAGER_PREPARE_WOLOLO, 1.0F, 0.9F + this.random.nextFloat() * 0.2F);
			((WorldServer) this.world).spawnParticle(EnumParticleTypes.PORTAL, oldX, oldY + this.entity.height * 0.5D, oldZ, 4, 0.2D, 0.2D, 0.2D, 0.0D);
			((WorldServer) this.world).spawnParticle(EnumParticleTypes.PORTAL, this.entity.posX, this.entity.posY + this.entity.height * 0.5D, this.entity.posZ, 4, 0.2D, 0.2D, 0.2D, 0.0D);
		} else if (this.tick > this.chargingTicks + 20 - 1) {
			this.entity.rotationYaw = this.yawDegree;

			if (this.tick > this.chargingTicks + 60 - 1) {
				this.dashToTarget();
			}
		}
	}

	private void teleportBehindTarget() {
		double dx = this.target.posX - this.entity.posX;
		double dz = this.target.posZ - this.entity.posZ;
		double dist = Math.sqrt(dx * dx + dz * dz);
		Vec3d start = this.target.getPositionVector();
		Vec3d end = start.add(dx / dist * 3.0D, 0.0D, dz / dist * 3.0D);
		RayTraceResult result = this.world.rayTraceBlocks(start, end, false, true, false);
		Vec3d vec = TargetUtil.getPositionNearTarget(this.world, this.entity, result != null ? result.hitVec : end, start, 2.0D, 4.0D, 1.0D);
		if (vec != null) {
			this.entity.setPosition(vec.x, vec.y, vec.z);
		}
	}

	private void dashToTarget() {
		Vec3d vec1 = this.entity.getPositionVector();

		boolean noClip = this.entity.noClip;
		if (!noClip) {
			Vec3d start = new Vec3d(this.entity.posX, this.entity.posY + this.entity.getEyeHeight(), this.entity.posZ);
			Vec3d end = start.add(this.targetDirection);
			if (this.world.rayTraceBlocks(start, end, false, true, false) == null && !this.world.collidesWithAnyBlock(this.entity.getEntityBoundingBox().offset(this.targetDirection))) {
				this.entity.noClip = true;
			}
		}
		this.entity.move(MoverType.SELF, this.targetDirection.x, this.targetDirection.y, this.targetDirection.z);
		this.entity.noClip = noClip;

		Vec3d vec2 = this.entity.getPositionVector();
		Vec3d vec3 = new Vec3d(-this.dashWidth, 0.0D, 0.0D);
		Vec3d vec4 = new Vec3d(this.dashWidth, this.entity.height, vec2.subtract(vec1).length());
		BoundingBox bb = new BoundingBox(vec3, vec4, this.yawRadian, 0.0D, vec1);
		List<EntityLivingBase> list = BoundingBox.getEntitiesInsideBB(this.world, this.entity, EntityLivingBase.class, bb);
		CQRFaction faction = this.entity.getFaction();
		for (EntityLivingBase entity : list) {
			if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(entity)) {
				continue;
			}
			if (faction != null && faction.isAlly(entity)) {
				continue;
			}
			entity.attackEntityFrom(DamageSource.causeMobDamage(this.entity).setDamageBypassesArmor(), 10.0F);
		}
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
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
