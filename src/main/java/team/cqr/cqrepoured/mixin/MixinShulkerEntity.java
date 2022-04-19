package team.cqr.cqrepoured.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.world.World;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

@Mixin(ShulkerEntity.class)
public abstract class MixinShulkerEntity extends GolemEntity {

	protected MixinShulkerEntity(EntityType<? extends GolemEntity> p_i48569_1_, World p_i48569_2_) {
		super(p_i48569_1_, p_i48569_2_);
	}
	
	@Override
	public CreatureAttribute getMobType() {
		return CQRCreatureAttributes.VOID;
	}

	/*@Inject(at = @At("HEAD"), method = "getMobType()Lnet/minecraft/entity/CreatureAttribute;")
	private void mixinGetMobType(CallbackInfoReturnable<CreatureAttribute> cbir) {
		cbir.setReturnValue(CQRCreatureAttributes.VOID);
	}*/
	
}
