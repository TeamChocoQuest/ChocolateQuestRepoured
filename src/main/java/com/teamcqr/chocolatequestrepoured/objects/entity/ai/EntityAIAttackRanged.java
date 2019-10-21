package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.IRangedWeapon;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;

public class EntityAIAttackRanged extends AbstractCQREntityAI {

	public EntityAIAttackRanged(AbstractEntityCQR entity) {
		super(entity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean shouldExecute() {
		if(entity.getHeldEquipment() == null) return false;
		Item item = entity.getHeldItemMainhand().getItem();
		if(item instanceof ItemBow || item instanceof IRangedWeapon) {
			
		}
		return false;
	}

}
