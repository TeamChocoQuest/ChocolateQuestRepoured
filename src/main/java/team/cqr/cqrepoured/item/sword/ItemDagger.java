package team.cqr.cqrepoured.item.sword;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.item.IItemTier;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.item.ItemLore;
import team.cqr.cqrepoured.util.EntityUtil;
import team.cqr.cqrepoured.util.ItemUtil;

public class ItemDagger extends ItemCQRWeapon {

	private final int specialAttackCooldown;

	public ItemDagger(Properties props, IItemTier material, int cooldown) {
		super(material, props);
		this.addAttributeModifiers(CQRConfig.SERVER_CONFIG.materials.itemTiers.dagger);
		this.specialAttackCooldown = cooldown;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		boolean flag = ItemUtil.compareRotations(player.yRot, entity.yRot, 50.0D);
		ItemUtil.attackTarget(stack, player, entity, flag, 0.0F, flag ? 2.0F : 1.0F, true, 1.0F, 0.0F, 0.25D, 0.25D, 0.3F);
		return true;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (playerIn.isOnGround() && !playerIn.swinging) {
			EntityUtil.move2D(playerIn, playerIn.xxa, playerIn.zza, 1.0D, playerIn.yRot);

			playerIn.setDeltaMovement(playerIn.getDeltaMovement().add(0, 0.2, 0));
			playerIn.getCooldowns().addCooldown(stack.getItem(), this.specialAttackCooldown);
		}
		return super.use(worldIn, playerIn, handIn);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("item.cqrepoured.rear_damage.tooltip", "200%").withStyle(ChatFormatting.BLUE));

		ItemLore.addHoverTextLogic(tooltip, flagIn, "leap");
	}

}
