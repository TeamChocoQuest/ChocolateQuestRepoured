package team.cqr.cqrepoured.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.EntityPredicates;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

import java.util.EnumSet;
import java.util.List;

public class EntityAIIdleSit extends AbstractCQREntityAI<AbstractEntityCQR> {

	protected static final int COOLDOWN_BORDER = 50;
	protected static final int COOLDOWN_FOR_PARTNER_CYCLE_BORDER = 100;

	private Entity talkingPartner = null;
	private int cooldown = 0;
	private int cooldwonForPartnerCycle = 0;
	protected final Predicate<AbstractEntityCQR> predicate;

	public EntityAIIdleSit(AbstractEntityCQR entity) {
		super(entity);
		//this.setMutexBits(2);
		this.setFlags(EnumSet.of(Flag.LOOK));
		this.predicate = input -> {
			if (input == null) {
				return false;
			}
			if (!EntityPredicates.LIVING_ENTITY_STILL_ALIVE.test(input)) {
				return false;
			}
			return EntityAIIdleSit.this.isEntityAlly(input);
		};
	}

	@Override
	public boolean canUse() {
		if (!this.entity.isOnGround()) {
			return false;
		}
		if (this.entity.isOnFire()) {
			return false;
		}
		if (this.entity.getVehicle() != null) {
			return false;
		}
		if (this.isEntityMoving(this.entity)) {
			return false;
		}
		return this.entity.getTarget() == null;
	}

	@Override
	public void stop() {
		this.cooldown = 0;
		this.cooldwonForPartnerCycle = 0;
		this.talkingPartner = null;
		this.entity.setSitting(false);
		this.entity.setChatting(false);
	}

	@Override
	public void tick() {
		if (++this.cooldown > COOLDOWN_BORDER) {
			// Make entity sit
			this.entity.setSitting(true);

			if (this.entity.hasTrades()) {
				this.entity.setChatting(true);
				return;
			}

			// search for new talking partner
			if (++this.cooldwonForPartnerCycle > COOLDOWN_FOR_PARTNER_CYCLE_BORDER) {
				this.cooldwonForPartnerCycle = 0;
				double x = this.entity.position().x;
				double y = this.entity.position().y;
				double z = this.entity.position().z;
				double r = 6.0D;
				AABB aabb = new AABB(x - r, y - r * 0.5D, z - r, x + r, y + r * 0.5D, z + r);
				List<AbstractEntityCQR> friends = this.entity.level.getEntitiesOfClass(AbstractEntityCQR.class, aabb, this.predicate);
				if (!friends.isEmpty()) {
					this.talkingPartner = friends.get(this.random.nextInt(friends.size()));
				}
			}

			// check if talking partner is valid and either talk to him or stop talking
			if (this.talkingPartner != null) {
				if (this.talkingPartner.isAlive() && this.entity.distanceToSqr(this.talkingPartner) < 8.0D * 8.0D) {
					this.entity.setChatting(true);
					this.entity.getLookControl().setLookAt(this.talkingPartner, 15.0F, 15.0F);
					double dx = this.talkingPartner.position().x - this.entity.position().x;
					double dz = this.talkingPartner.position().z - this.entity.position().z;
					this.entity.yRot = (float) Math.toDegrees(Mth.atan2(dz, dx)) - 90.0F;
					this.entity.yBodyRot = this.entity.yRot;
				} else {
					this.talkingPartner = null;
					this.entity.setChatting(false);
				}
			} else {
				this.entity.setChatting(false);
			}
		}
	}

	private boolean isEntityAlly(AbstractEntityCQR possibleAlly) {
		if (possibleAlly == this.entity) {
			return false;
		}
		LivingEntity leader = TargetUtil.getLeaderOrOwnerRecursive(this.entity);
		LivingEntity targetLeader = TargetUtil.getLeaderOrOwnerRecursive(possibleAlly);
		if (!(leader instanceof Player) && targetLeader instanceof Player) {
			return false;
		}
		if (!TargetUtil.isAllyCheckingLeaders(leader, targetLeader)) {
			return false;
		}
		return this.entity.getSensing().canSee(possibleAlly);
	}

	private boolean isEntityMoving(Entity entity) {
		// motion <= 0.03 is set to 0 but gravity always pulls entities downwards
		return entity.getDeltaMovement().length() != 0.0D;
	}

}
