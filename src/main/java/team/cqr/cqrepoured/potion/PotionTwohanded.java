package team.cqr.cqrepoured.potion;

import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectType;

public class PotionTwohanded extends PotionCQR {

	public PotionTwohanded() {
		super("twohanded", EffectType.NEUTRAL, 0x363647);
		this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "984f5b02-5321-4638-a033-d27a36d64770", -0.1D, Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.ATTACK_SPEED, "627071a3-3a8f-42cf-810f-fb91c4bbe86c", -0.1D, Operation.MULTIPLY_TOTAL);
	}

}
