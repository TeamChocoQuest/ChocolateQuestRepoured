package team.cqr.cqrepoured.item.shield;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemShieldWalkerKing extends ItemShieldCQR {

	public ItemShieldWalkerKing(Properties props) {
		super(props, 2048, null);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		ItemLore.addHoverTextLogic(tooltip, flagIn, ForgeRegistries.ITEMS.getKey(this).getPath());
	}

}
