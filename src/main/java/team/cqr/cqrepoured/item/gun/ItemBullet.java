package team.cqr.cqrepoured.item.gun;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.ChatFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
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
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn) {
		tooltip.add((new TranslationTextComponent("item.cqrepoured.tooltip.bullet_damage", (double) this.getType().getAdditionalDamage()).withStyle(ChatFormatting.BLUE)));
		if(this.getType().fireDamage()) {
            tooltip.add(new TranslationTextComponent("item.cqrepoured.bullet_fire.tooltip").withStyle(ChatFormatting.DARK_RED));
		}
	}

}
