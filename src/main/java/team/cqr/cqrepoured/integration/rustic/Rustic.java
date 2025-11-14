package team.cqr.cqrepoured.integration.rustic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class Rustic {

	private static final String KEY_OIL_TAG = "vanta_oil";
	private static final String KEY_EFFECT = "Effect";
	private static final String KEY_DURATION = "Duration";
	private static final String KEY_AMPLIFIER = "Amplifier";
	private static final int MAX_DURATION_PER_HIT = 300;

	public static void onMeleeAttack(AbstractEntityCQR attacker, EnumHand hand, EntityLivingBase target) {
		ItemStack stack = attacker.getHeldItem(hand);
		if (stack.isEmpty())
			return;

		NBTTagCompound oil = stack.getSubCompound(KEY_OIL_TAG);
		if (oil == null)
			return;
		Potion effect = Potion.REGISTRY.getObject(new ResourceLocation(oil.getString(KEY_EFFECT)));
		if (effect == null)
			return;
		int charges = oil.getInteger(KEY_DURATION);
		int strength = oil.getInteger(KEY_AMPLIFIER);

		int usedCharges = Math.min(charges, MAX_DURATION_PER_HIT);
		int duration = usedCharges;

		PotionEffect active = target.getActivePotionEffect(effect);
		if (active != null) {
			if (active.getAmplifier() > strength) {
				return;
			}
			if (active.getAmplifier() == strength) {
				duration += active.getDuration();
			}
		}

		target.addPotionEffect(new PotionEffect(effect, duration, strength));
		((WorldServer) target.world).spawnParticle(EnumParticleTypes.SPELL_MOB, target.posX, target.posY + target.height * 0.5D, target.posZ, 20, target.width * 0.35D, target.height, target.width * 0.35D, 1.0D);

		charges -= usedCharges;
		if (charges <= 0) {
			stack.removeSubCompound(KEY_OIL_TAG);
		} else {
			oil.setInteger(KEY_DURATION, charges);
		}
	}

}
