package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.IRangedWeapon;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;

public class EntityAIAttackRanged extends AbstractCQREntityAI {

	public EntityAIAttackRanged(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if(entity == null || entity.isDead) return false;
		if(entity.getHeldEquipment() == null) return false;
		Item item = entity.getHeldItemMainhand().getItem();
		if(item instanceof ItemBow || item instanceof IRangedWeapon) {
			return true;
		}
		
		if(entity.getAttackTarget() == null || (entity.getAttackTarget() != null && !entity.getEntitySenses().canSee(entity.getAttackTarget())) ) {
			return false;
		}
		return false;
	}

}
