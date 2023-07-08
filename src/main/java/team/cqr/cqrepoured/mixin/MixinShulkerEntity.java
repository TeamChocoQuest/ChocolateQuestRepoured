package team.cqr.cqrepoured.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

@Mixin(Shulker.class)
public abstract class MixinShulkerEntity extends AbstractGolem {

	protected MixinShulkerEntity(EntityType<? extends AbstractGolem> p_i48569_1_, Level p_i48569_2_) {
		super(p_i48569_1_, p_i48569_2_);
	}
	
	@Override
	public MobType getMobType() {
		return CQRCreatureAttributes.VOID;
	}

	/*@Inject(at = @At("HEAD"), method = "getMobType()Lnet/minecraft/entity/CreatureAttribute;")
	private void mixinGetMobType(CallbackInfoReturnable<CreatureAttribute> cbir) {
		cbir.setReturnValue(CQRCreatureAttributes.VOID);
	}*/
	
}
