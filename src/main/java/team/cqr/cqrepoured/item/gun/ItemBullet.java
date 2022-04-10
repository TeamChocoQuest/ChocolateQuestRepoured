package team.cqr.cqrepoured.item.gun;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBullet.EBulletType;

public class ItemBullet extends Item
{
	
	protected final EBulletType type;
	
	public ItemBullet(Properties properties, final EBulletType type)
	{
		super(properties);
		this.type = type;
	}
	
	public EBulletType getType() {
		return this.type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add((new TranslationTextComponent("description.bullet_damage", (double) this.getType().getAdditionalDamage())).withStyle(TextFormatting.BLUE));
		if(this.getType().fireDamage()) {
            tooltip.add(new TranslationTextComponent("description.bullet_fire").withStyle(TextFormatting.DARK_RED));
		}
	}

}
