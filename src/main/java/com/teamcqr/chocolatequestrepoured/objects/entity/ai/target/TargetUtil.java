package com.teamcqr.chocolatequestrepoured.objects.entity.ai.target;

import java.util.Comparator;
import java.util.List;

import com.google.common.base.Predicate;
import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.EntityCQRMountBase;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.EntitySelectors;

public class TargetUtil {

	public static final Predicate<EntityLivingBase> PREDICATE_ATTACK_TARGET = input -> {
		if (input == null) {
			return false;
		}
		return EntitySelectors.CAN_AI_TARGET.apply(input);
	};

	public static final Predicate<EntityLiving> PREDICATE_MOUNTS = input -> {
		if (input == null) {
			return false;
		}
		if (!EntitySelectors.IS_ALIVE.apply(input)) {
			return false;
		}
		if (input.isBeingRidden()) {
			return false;
		}
		if (input instanceof AbstractHorse && ((AbstractHorse) input).isTame()) {
			return false;
		}
		return input.canBeSteered() || input instanceof EntityCQRMountBase || input instanceof AbstractHorse || input instanceof EntityPig;
	};

	public static final Predicate<EntityTameable> PREDICATE_PETS = input -> {
		if (input == null) {
			return false;
		}
		if (!EntitySelectors.IS_ALIVE.apply(input)) {
			return false;
		}
		if (input.getOwnerId() != null) {
			return false;
		}
		return input instanceof EntityOcelot || input instanceof EntityWolf;
	};

	public static final Predicate<Entity> PREDICATE_LIVING = input -> {
		if (input == null) {
			return false;
		}
		if (!EntitySelectors.IS_ALIVE.apply(input)) {
			return false;
		}
		return input instanceof EntityLivingBase;
	};

	public static final Predicate<Entity> createPredicateAlly(CQRFaction faction) {
		return input -> faction.isAlly(input);
	}

	public static final Predicate<Entity> createPredicateNonAlly(CQRFaction faction) {
		return input -> !faction.isAlly(input);
	}

	public static final <T extends Entity> T getNearestEntity(Entity entity, List<T> list) {
		if (!list.isEmpty()) {
			T nearestEntity = list.get(0);
			double min = entity.getDistanceSq(nearestEntity);
			int size = list.size();
			for (int i = 0; i < size; i++) {
				T otherEntity = list.get(i);
				double distance = entity.getDistanceSq(otherEntity);
				if (distance < min) {
					nearestEntity = otherEntity;
					min = distance;
				}
			}
			return nearestEntity;
		}
		return null;
	}

	public static class Sorter implements Comparator<Entity> {

		private final Entity entity;

		public Sorter(Entity entityIn) {
			this.entity = entityIn;
		}

		@Override
		public int compare(Entity entity1, Entity entity2) {
			double d1 = this.entity.getDistanceSq(entity1);
			double d2 = this.entity.getDistanceSq(entity2);

			if (d1 < d2) {
				return -1;
			} else if (d1 > d2) {
				return 1;
			} else {
				return 0;
			}
		}

	}

}
