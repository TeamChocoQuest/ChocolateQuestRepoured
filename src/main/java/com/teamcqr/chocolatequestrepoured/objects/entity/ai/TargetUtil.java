package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.Comparator;

import com.google.common.base.Predicate;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.EntityCQRMountBase;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.EntitySelectors;

public class TargetUtil {

	public static final Predicate<EntityLivingBase> PREDICATE_ATTACK_TARGET = new Predicate<EntityLivingBase>() {
		@Override
		public boolean apply(EntityLivingBase input) {
			if (input == null) {
				return false;
			}
			if (!EntitySelectors.CAN_AI_TARGET.apply(input)) {
				return false;
			}
			return true;
		}
	};

	public static final Predicate<? super Entity> PREDICATE_MOUNTS = new Predicate<Entity>() {
		@Override
		public boolean apply(Entity input) {
			if (input == null) {
				return false;
			}
			if (!EntitySelectors.IS_ALIVE.apply(input)) {
				return false;
			}
			if (!(input instanceof EntityAnimal)) {
				return false;
			}
			return ((EntityAnimal) input).canBeSteered() || input instanceof EntityCQRMountBase || input instanceof EntityLlama || input instanceof AbstractHorse || input instanceof EntityPig;
		}
	};

	public static final Predicate<? super Entity> PREDICATE_PETS = new Predicate<Entity>() {
		@Override
		public boolean apply(Entity input) {
			if (input == null) {
				return false;
			}
			if (!EntitySelectors.IS_ALIVE.apply(input)) {
				return false;
			}
			if (!(input instanceof EntityTameable)) {
				return false;
			}
			return input instanceof EntityOcelot || input instanceof EntityWolf;
		}
	};

	public static class Sorter implements Comparator<Entity> {

		private final Entity entity;

		public Sorter(Entity entityIn) {
			this.entity = entityIn;
		}

		@Override
		public int compare(Entity p_compare_1_, Entity p_compare_2_) {
			double d0 = this.entity.getDistanceSq(p_compare_1_);
			double d1 = this.entity.getDistanceSq(p_compare_2_);

			if (d0 < d1) {
				return -1;
			} else {
				return d0 > d1 ? 1 : 0;
			}
		}
	}

}
