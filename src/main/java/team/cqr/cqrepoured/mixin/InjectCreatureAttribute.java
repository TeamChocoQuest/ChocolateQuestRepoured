package team.cqr.cqrepoured.mixin;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

import javax.annotation.Nonnull;

/**
 * @author ZZZank
 */
@Mixin({EntityDragon.class, EntityEnderman.class, EntityShulker.class})
public abstract class InjectCreatureAttribute extends EntityLiving {
    private InjectCreatureAttribute(World p_i1582_1_) {
        super(p_i1582_1_);
    }

    @Nonnull
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return CQRCreatureAttributes.VOID;
    }
}
