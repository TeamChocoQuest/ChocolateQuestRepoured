package team.cqr.cqrepoured.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

@Mixin(EnderDragon.class)
public abstract class MixinEnderdragonEntity extends Mob {

	protected MixinEnderdragonEntity(EntityType<? extends Mob> p_i48576_1_, Level p_i48576_2_) {
		super(p_i48576_1_, p_i48576_2_);
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
