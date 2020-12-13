package team.cqr.cqrepoured.objects.items.guns;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.init.CQRItems;

public class ItemCannonBall extends Item {

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.getItem() == CQRItems.CANNON_BALL) {
			tooltip.add(TextFormatting.BLUE + "+5 " + I18n.format("description.bullet_damage.name"));
		}
	}

}
