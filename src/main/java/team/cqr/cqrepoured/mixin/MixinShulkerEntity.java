package team.cqr.cqrepoured.mixin;

import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.world.entity.EntityType;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.passive.GolemEntity;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

@Mixin(ShulkerEntity.class)
public abstract class MixinShulkerEntity extends GolemEntity {

	protected MixinShulkerEntity(EntityType<? extends GolemEntity> p_i48569_1_, Level p_i48569_2_) {
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
