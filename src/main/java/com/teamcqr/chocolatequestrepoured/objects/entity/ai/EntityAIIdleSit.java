package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.List;

import com.google.common.base.Predicate;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIIdleSit extends AbstractCQREntityAI {

	protected static final int COOLDOWN_BORDER = 50;
	protected static final int COOLDOWN_FOR_PARTNER_CYCLE_BORDER = 100;

	private Entity talkingPartner = null;
	private int cooldown = 0;
	private int cooldwonForPartnerCycle = 0;
	protected final Predicate<AbstractEntityCQR> predicate;

	public EntityAIIdleSit(AbstractEntityCQR entity) {
		super(entity);
		this.predicate = new Predicate<AbstractEntityCQR>() {
			@Override
			public boolean apply(AbstractEntityCQR input) {
				if (input == null) {
					return false;
				}
				if (!EntitySelectors.IS_ALIVE.apply(input)) {
					return false;
				}
				return EntityAIIdleSit.this.isEntityAlly(input);
			}
		};
	}

	@Override
	public boolean shouldExecute() {
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
	public void resetTask() {
		this.cooldown = 0;
		this.cooldwonForPartnerCycle = 0;
		this.talkingPartner = null;
		this.entity.setSitting(false);
		this.entity.setChatting(false);
	}

	@Override
	public void updateTask() {
		if (this.cooldown < COOLDOWN_BORDER) {
			this.cooldown++;
		}

		if (this.cooldown >= COOLDOWN_BORDER) {
			if (this.cooldwonForPartnerCycle < COOLDOWN_FOR_PARTNER_CYCLE_BORDER) {
				this.cooldwonForPartnerCycle++;
			}

			// DONE: Make entity sit -> Renderer needs work for that
			if (!this.entity.isSitting()) {
				this.entity.setSitting(true);
			}

			// search for new talking partner
			if (this.talkingPartner == null || this.cooldwonForPartnerCycle >= COOLDOWN_FOR_PARTNER_CYCLE_BORDER) {
				if (this.entity.ticksExisted % 4 == 0) {
					AxisAlignedBB aabb = new AxisAlignedBB(this.entity.getPositionVector().subtract(6.0D, 3.0D, 6.0D), this.entity.getPositionVector().addVector(6.0D, 3.0D, 6.0D));
					List<AbstractEntityCQR> friends = this.entity.world.getEntitiesWithinAABB(AbstractEntityCQR.class, aabb, this.predicate);
					this.talkingPartner = friends.get(this.random.nextInt(friends.size()));
					this.cooldwonForPartnerCycle = 0;
				}
/**Before PR Meldex
				//DONE: Make entity sit -> Renderer needs work for that
				//int friendsFound = 0;
				List<Entity> friends = new ArrayList<>();
				List<Entity> nearbyEntities = entity.getEntityWorld().getEntitiesInAABBexcluding(entity, new AxisAlignedBB(entity.getPosition().subtract(new Vec3i(6,3,6)), entity.getPosition().add(6,3,6)), AbstractEntityCQR.MOB_SELECTOR);
				//System.out.println("NearbyEntities: " + nearbyEntities.size());
				if(!nearbyEntities.isEmpty()) {
					for(Entity ent : nearbyEntities) {
						if(ent instanceof AbstractEntityCQR) {
							if(entity.getFaction().isAlly((AbstractEntityCQR)ent)) {
								friends.add(ent);
								//friendsFound++;
							}
						}
					}
					//System.out.println("Friend list size: " + friends.size());
					if(!friends.isEmpty()) {
						cooldwonForPartnerCycle++;
						if(talkingPartner == null || cooldwonForPartnerCycle >= cooldownCyclePartnerBorder || talkingPartner.isDead || entity.getDistance(talkingPartner) > 8) {
							talkingPartner = friends.get(random.nextInt(friends.size()));
							//System.out.println("I found a partner!");
							cooldwonForPartnerCycle = 0;
						}
						if(talkingPartner != null && talkingPartner != entity && !talkingPartner.isDead) {
							//we have someone to talk to, yay :D
							//DONE: Orient entity to random friend
							//DONE: Make them "chat" / "play cards"
							entity.setChatting(true);
							entity.getLookHelper().setLookPosition(talkingPartner.posX, talkingPartner.posY + talkingPartner.getEyeHeight(), talkingPartner.posZ, (float)this.entity.getHorizontalFaceSpeed(), (float)this.entity.getVerticalFaceSpeed());
							
						} else {
							entity.setChatting(false);
						}
					} else {
						entity.setChatting(false);
					}
END_OF_OLD_STATE
**/
			}

			// check if talking partner is valid and either talk to him or stop talking
			if (this.talkingPartner != null) {
				if (this.talkingPartner.isEntityAlive() && this.entity.getDistance(this.talkingPartner) < 8.0D) {
					this.entity.setChatting(true);
					this.entity.getLookHelper().setLookPositionWithEntity(this.talkingPartner, 15.0F, 15.0F);
//PR Meldex
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
		return this.entity.getFaction().isEntityAlly(possibleAlly);
	}

	private boolean isEntityMoving(Entity entity) {
		// motion <= 0.03 is set to 0 but gravity always pulls entities downwards
		return entity.motionX != 0.0D || Math.abs(entity.motionY) > 0.1D || entity.motionZ != 0.0D;
	}

}
