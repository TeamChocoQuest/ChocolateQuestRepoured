package team.cqr.cqrepoured.item;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.item.IItemTier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import team.cqr.cqrepoured.entity.projectiles.ProjectileEarthQuake;
import team.cqr.cqrepoured.item.sword.ItemCQRWeapon;

public class ItemBullBattleAxe extends ItemCQRWeapon {

	private static final Set<Material> DIGGABLE_MATERIALS = Sets.newHashSet(Material.WOOD, Material.NETHER_WOOD, Material.PLANT, Material.REPLACEABLE_PLANT, Material.BAMBOO, Material.VEGETABLE);
	private static final Set<Block> OTHER_DIGGABLE_BLOCKS = Sets.newHashSet(Blocks.LADDER, Blocks.SCAFFOLDING, Blocks.OAK_BUTTON, Blocks.SPRUCE_BUTTON, Blocks.BIRCH_BUTTON, Blocks.JUNGLE_BUTTON, Blocks.DARK_OAK_BUTTON, Blocks.ACACIA_BUTTON, Blocks.CRIMSON_BUTTON, Blocks.WARPED_BUTTON);

	public ItemBullBattleAxe(IItemTier material, Item.Properties props) {
		super(material, props);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn)
	{
		if (!playerIn.swinging && playerIn.isOnGround())
		{
			playerIn.setPos(playerIn.position().x, playerIn.position().y + 0.1D, playerIn.position().z);
			playerIn.setDeltaMovement(playerIn.getDeltaMovement().x, playerIn.getDeltaMovement().y + 0.35D, playerIn.getDeltaMovement().z);
			//playerIn.posY += 0.1D;
			//playerIn.motionY += 0.35D;

			if (!worldIn.isClientSide) {
				ProjectileEarthQuake quake = new ProjectileEarthQuake(worldIn, playerIn);
				quake.shootFromRotation(playerIn, playerIn.xRot, playerIn.yRot, 0.0F, 0.25F, 1.0F);
				worldIn.addFreshEntity(quake);

				playerIn.getCooldowns().addCooldown(playerIn.getItemInHand(handIn).getItem(), 20);
				return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
			}
			playerIn.swing(handIn);
		}
		return InteractionResultHolder.fail(playerIn.getItemInHand(handIn));
	}

	@Override
	public InteractionResult useOn(UseOnContext pContext)
	{
		Level world = pContext.getLevel();
		BlockPos blockpos = pContext.getClickedPos();
		BlockState blockstate = world.getBlockState(blockpos);
		BlockState block = blockstate.getToolModifiedState(world, blockpos, pContext.getPlayer(), pContext.getItemInHand(), ToolType.AXE);

		if(block != null)
		{
			Player playerentity = pContext.getPlayer();
			world.playSound(playerentity, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
			if(!world.isClientSide)
			{
				world.setBlock(blockpos, block, 11);
				if(playerentity != null)
				{
					pContext.getItemInHand().hurtAndBreak(1, playerentity, (p_220040_1_) -> {
						p_220040_1_.broadcastBreakEvent(pContext.getHand());
					});
				}
			}

			return InteractionResult.sidedSuccess(world.isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);

		if (slot == EquipmentSlot.MAINHAND) {
			this.replaceModifier(modifiers, Attributes.ATTACK_SPEED, BASE_ATTACK_SPEED_UUID, 0.6);
		}

		return modifiers;
	}

	//Note1: modifiers is immutable
	//Note2: modifierMultimap is also immutable!
	protected void replaceModifier(Multimap<Attribute, AttributeModifier> modifierMultimap, Attribute attribute, UUID id, double multiplier) {
		/*Collection<AttributeModifier> modifiers = modifierMultimap.get(attribute);
		Optional<AttributeModifier> modifierOptional = modifiers.stream().filter(attributeModifier -> attributeModifier.getId().equals(id)).findFirst();

		if (modifierOptional.isPresent()) {
			final AttributeModifier modifier = modifierOptional.get();
			modifiers.remove(modifier);
			modifiers.add(new AttributeModifier(modifier.getId(), modifier.getName(), modifier.getAmount() - multiplier, modifier.getOperation()));
		}*/
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn)
	{
		ItemLore.addHoverTextLogic(tooltip, flagIn, "bull_battle_axe");
	}

	@Override
	public float getDestroySpeed(ItemStack pStack, BlockState pState)
	{
		Material material = pState.getMaterial();
		if(pState.isToolEffective(ToolType.AXE))
		{
			return this.getTier().getSpeed();
		}
		return DIGGABLE_MATERIALS.contains(material) || OTHER_DIGGABLE_BLOCKS.contains(pState.getBlock()) ? this.getTier().getSpeed() : 1.0F;
	}
}