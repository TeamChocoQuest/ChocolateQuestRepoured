package team.cqr.cqrepoured.item.sword;

import java.util.List;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.item.IItemTier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemDaggerNinja extends ItemDagger {

	public ItemDaggerNinja(Properties props, IItemTier material, int cooldown) {
		super(props, material, cooldown);
	}
	

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (playerIn.isCrouching()) {
			playerIn.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
			playerIn.getCooldowns().addCooldown(stack.getItem(), 30);

			for (int i = 0; i < 6; i++) {
				worldIn.addParticle(ParticleTypes.PORTAL, playerIn.position().x + random.nextFloat() - 0.5D, playerIn.position().y + random.nextFloat() - 0.5D, playerIn.position().z + random.nextFloat() - 0.5D, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F);
			}

			double x = -Math.sin(Math.toRadians(playerIn.yRot));
			double z = Math.cos(Math.toRadians(playerIn.yRot));
			double y = -Math.sin(Math.toRadians(playerIn.xRot));
			x *= (1.0D - Math.abs(y));
			z *= (1.0D - Math.abs(y));
			int dist = 4;

			BlockPos pos = playerIn.blockPosition().offset(x * dist, y * dist + 1, z * dist);

			if (worldIn.getBlockState(pos).getBlock().getCollisionShape(worldIn.getBlockState(pos), worldIn, pos, CollisionContext.of(playerIn)).isEmpty() && pos.getY() > 0) {
				playerIn.setPos(playerIn.position().x + x * dist, playerIn.position().y + y * dist + 1, playerIn.position().z + z * dist);
				playerIn.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 40, 5, false, false));
			} else {
				return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
			}

			stack.hurtAndBreak(1, playerIn, (p_220040_1_) -> {
                p_220040_1_.broadcastBreakEvent(handIn);
             });

			for (int i = 0; i < 6; i++) {
				worldIn.addParticle(ParticleTypes.PORTAL, playerIn.position().x + random.nextFloat() - 0.5D, playerIn.position().y + random.nextFloat() - 0.5D, playerIn.position().z + random.nextFloat() - 0.5D, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F);
			}
		} else {
			return super.use(worldIn, playerIn, handIn);
		}
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("item.cqrepoured.rear_damage.tooltip", "200%").withStyle(ChatFormatting.BLUE));

		ItemLore.addHoverTextLogic(tooltip, flagIn, this.getRegistryName().getPath());
	}

}
