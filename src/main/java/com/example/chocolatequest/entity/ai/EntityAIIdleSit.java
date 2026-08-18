package com.example.chocolatequest.entity.ai;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.phys.AABB;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;

public class EntityAIIdleSit extends AbstractCQREntityAI<AbstractEntityCQR> {

    protected static final int COOLDOWN_BORDER = 50;
    protected static final int COOLDOWN_FOR_PARTNER_CYCLE_BORDER = 100;

    private Entity talkingPartner = null;
    private int cooldown = 0;
    private int cooldownForPartnerCycle = 0;
    protected final Predicate<AbstractEntityCQR> predicate;

    public EntityAIIdleSit(AbstractEntityCQR entity) {
        super(entity);
        this.setFlags(EnumSet.of(Flag.LOOK));
        this.predicate = input -> {
            if (input == null) {
                return false;
            }
            if (!EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(input)) {
                return false;
            }
            return EntityAIIdleSit.this.isEntityAlly(input);
        };
    }

    @Override
    public boolean canUse() {
        if (!this.entity.onGround()) {
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
        this.cooldownForPartnerCycle = 0;
        this.talkingPartner = null;
        this.entity.setSitting(false);
    }

    public void tick() {
        if (++this.cooldown > COOLDOWN_BORDER) {
            this.entity.setSitting(true);

            if (++this.cooldownForPartnerCycle > COOLDOWN_FOR_PARTNER_CYCLE_BORDER) {
                this.cooldownForPartnerCycle = 0;
                double x = this.entity.position().x;
                double y = this.entity.position().y;
                double z = this.entity.position().z;
                double r = 6.0D;
                AABB aabb = new AABB(x - r, y - r * 0.5D, z - r, x + r, y + r * 0.5D, z + r);
                List<AbstractEntityCQR> friends = this.entity.level().getEntitiesOfClass(AbstractEntityCQR.class, aabb, this.predicate);
                if (!friends.isEmpty()) {
                    this.talkingPartner = friends.get(this.random.nextInt(friends.size()));
                }
            }

            if (this.talkingPartner != null) {
                if (this.talkingPartner.isAlive() && this.entity.distanceToSqr(this.talkingPartner) < 64.0D) {
                    this.entity.getLookControl().setLookAt(this.talkingPartner, 15.0F, 15.0F);
                    double dx = this.talkingPartner.position().x - this.entity.position().x;
                    double dz = this.talkingPartner.position().z - this.entity.position().z;
                    this.entity.setYRot((float) Math.toDegrees(Mth.atan2(dz, dx)) - 90.0F);
                    this.entity.yBodyRot = this.entity.getYRot();
                } else {
                    this.talkingPartner = null;
                }
            }
        }
    }

    private boolean isEntityAlly(AbstractEntityCQR possibleAlly) {
        if (possibleAlly == this.entity) {
            return false;
        }
        if (!this.entity.getDefaultFaction().isAlly(possibleAlly.getDefaultFaction())) {
            return false;
        }
        return this.entity.getSensing().hasLineOfSight(possibleAlly);
    }

    private boolean isEntityMoving(Entity entity) {
        return entity.getDeltaMovement().horizontalDistanceSqr() > 0.001D;
    }

}
