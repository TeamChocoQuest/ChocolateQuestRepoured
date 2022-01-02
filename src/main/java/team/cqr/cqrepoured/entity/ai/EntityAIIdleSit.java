package team.cqr.cqrepoured.entity.ai;

import java.util.List;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class EntityAIIdleSit extends AbstractCQREntityAI<AbstractEntityCQR> {

	protected static final int COOLDOWN_BORDER = 50;
	protected static final int COOLDOWN_FOR_PARTNER_CYCLE_BORDER = 100;

	private Entity talkingPartner = null;
	private int cooldown = 0;
	private int cooldwonForPartnerCycle = 0;
	protected final Predicate<AbstractEntityCQR> predicate;

	public EntityAIIdleSit(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(2);
		this.predicate = input -> {
			if (input == null) {
				return false;
			}
			if (!EntityPredicates.IS_ALIVE.apply(input)) {
				return false;
			}
			return EntityAIIdleSit.this.isEntityAlly(input);
		};
	}

	@Override
	public boolean canUse() {
		if (!this.entity.onGround) {
			return false;
		}
		if (this.entity.isBurning()) {
			return false;
		}
		if (this.entity.isRiding()) {
			return false;
		}
		if (this.isEntityMoving(this.entity)) {
			return false;
		}
		return this.entity.getAttackTarget() == null;
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
				double x = this.entity.posX;
				double y = this.entity.posY;
				double z = this.entity.posZ;
				double r = 6.0D;
				AxisAlignedBB aabb = new AxisAlignedBB(x - r, y - r * 0.5D, z - r, x + r, y + r * 0.5D, z + r);
				List<AbstractEntityCQR> friends = this.entity.world.getEntitiesWithinAABB(AbstractEntityCQR.class, aabb, this.predicate);
				if (!friends.isEmpty()) {
					this.talkingPartner = friends.get(this.random.nextInt(friends.size()));
				}
			}

			// check if talking partner is valid and either talk to him or stop talking
			if (this.talkingPartner != null) {
				if (this.talkingPartner.isEntityAlive() && this.entity.getDistanceSq(this.talkingPartner) < 8.0D * 8.0D) {
					this.entity.setChatting(true);
					this.entity.getLookHelper().setLookPositionWithEntity(this.talkingPartner, 15.0F, 15.0F);
					double dx = this.talkingPartner.posX - this.entity.posX;
					double dz = this.talkingPartner.posZ - this.entity.posZ;
					this.entity.rotationYaw = (float) Math.toDegrees(MathHelper.atan2(dz, dx)) - 90.0F;
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
		if (!(leader instanceof PlayerEntity) && targetLeader instanceof PlayerEntity) {
			return false;
		}
		if (!TargetUtil.isAllyCheckingLeaders(leader, targetLeader)) {
			return false;
		}
		return this.entity.getSensing().canSee(possibleAlly);
	}

	private boolean isEntityMoving(Entity entity) {
		// motion <= 0.03 is set to 0 but gravity always pulls entities downwards
		return entity.motionX != 0.0D || Math.abs(entity.motionY) > 0.1D || entity.motionZ != 0.0D;
	}

}
