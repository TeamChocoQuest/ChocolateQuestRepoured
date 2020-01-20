package com.teamcqr.chocolatequestrepoured.objects.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class KilledByCQRMobTrigger implements ICriterionTrigger<KilledByCQRMobTrigger.Instance> {
	
	private static final ResourceLocation ID = new ResourceLocation(Reference.MODID, "cqr_killed_by_cqr_mob");
	
	public static class Instance extends AbstractCriterionInstance {

		public Instance(ResourceLocation criterionIn) {
			super(criterionIn);
		}
		
		public boolean test(Entity killer) {
			return killer instanceof AbstractEntityCQR;
		}
		
	}

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
		// TODO Auto-generated method stub
		return null;
	}

}
