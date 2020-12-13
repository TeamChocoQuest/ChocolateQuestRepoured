package team.cqr.cqrepoured.magic;

import net.minecraft.entity.EntityLivingBase;

public class MagicUtil {
	
	public static int getMana(EntityLivingBase caster) {
		//TODO: Implement, also integrate E-Blob wizwardry mana as well as botania mana
		return 100;
	}
	
	public static boolean subtractMana(EntityLivingBase caster, final int amount) {
		//TODO: Change value of the capability, can't go below zero
		return true;
	}
	
	public static boolean addMana(EntityLivingBase caster, final int amount) {
		//TODO: Change value of the capability, can't go above a certain limit (defined in config)
		return true;
	}

}
