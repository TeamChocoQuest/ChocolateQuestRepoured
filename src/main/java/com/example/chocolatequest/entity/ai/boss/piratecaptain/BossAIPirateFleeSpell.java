package com.example.chocolatequest.entity.ai.boss.piratecaptain;

import com.example.chocolatequest.entity.ai.AbstractCQREntityAI;
import com.example.chocolatequest.entity.boss.PirateCaptainEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.function.Predicate;

public class BossAIPirateFleeSpell extends AbstractCQREntityAI<PirateCaptainEntity> {

    private final Predicate<LivingEntity> predicateAlly = input -> {
        if (!input.isAlive()) return false;
        if (input == this.entity) return false;
        if (!this.entity.getDefaultFaction().isAlly(((com.example.chocolatequest.faction.IFactionRelated) input).getDefaultFaction())) return false;
        // Simplified reachability check
        return true;
    };

    private int cooldown = 60;

    public BossAIPirateFleeSpell(PirateCaptainEntity entity) {
        super(entity);
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }

        if (this.entity.getHealth() / this.entity.getMaxHealth() <= 0.2) {
            // Flee radius 32
            return this.hasNearbyAllies();
        }
        return false;
    }

    private boolean hasNearbyAllies() {
        double radius = 32.0;
        AABB aabb = this.entity.getBoundingBox().inflate(radius, radius / 2.0, radius);
        List<Mob> allies = this.entity.level().getEntitiesOfClass(Mob.class, aabb, input -> input instanceof com.example.chocolatequest.faction.IFactionRelated && predicateAlly.test(input));
        return !allies.isEmpty();
    }

    private int getNearbyAllies(Mob ally) {
        AABB aabb = ally.getBoundingBox().inflate(4, 2, 4);
        return ally.level().getEntitiesOfClass(Mob.class, aabb, input -> input instanceof com.example.chocolatequest.faction.IFactionRelated && predicateAlly.test(input)).size();
    }

    @Override
    public void start() {
        double radius = 32.0;
        AABB aabb = this.entity.getBoundingBox().inflate(radius, radius / 2.0, radius);
        List<Mob> allies = this.entity.level().getEntitiesOfClass(Mob.class, aabb, input -> input instanceof com.example.chocolatequest.faction.IFactionRelated && predicateAlly.test(input));
        
        if (!allies.isEmpty()) {
            allies.sort((o1, o2) -> {
                int count1 = this.getNearbyAllies(o1);
                int count2 = this.getNearbyAllies(o2);
                return Integer.compare(count2, count1);
            });
            Vec3 pos = allies.get(0).position();
            this.entity.randomTeleport(pos.x, pos.y, pos.z, true);
        }
        
        this.cooldown = 1200; // 60 seconds cooldown on fleeing
    }
}
