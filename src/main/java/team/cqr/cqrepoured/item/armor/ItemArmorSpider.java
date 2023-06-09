package team.cqr.cqrepoured.item.armor;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.BlockRenderType;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.client.init.CQRArmorModels;
import team.cqr.cqrepoured.item.ItemLore;
import team.cqr.cqrepoured.util.ItemUtil;

//#TODO Needs tests
public class ItemArmorSpider extends ArmorItem {

	private final Multimap<Attribute, AttributeModifier> attributeModifier;

	public ItemArmorSpider(IArmorMaterial materialIn, EquipmentSlot slot, Item.Properties properties) {
		super(materialIn, slot, properties);

		Multimap<Attribute, AttributeModifier> attributeMap = getDefaultAttributeModifiers(EquipmentSlot.MAINHAND);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> modifierBuilder = ImmutableMultimap.builder();
		modifierBuilder.putAll(attributeMap);
		modifierBuilder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier("SpiderArmorModifier", 0.05D, AttributeModifier.Operation.MULTIPLY_TOTAL));
		this.attributeModifier = modifierBuilder.build();
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		if (slot == MobEntity.getEquipmentSlotForItem(stack)) {
			return this.attributeModifier;
		}
		return super.getAttributeModifiers(slot, stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn) {
		ItemLore.addHoverTextLogic(tooltip, flagIn, "spider_armor");
	}

	@Override
	public void onArmorTick(ItemStack stack, Level world, Player player) {
		if (ItemUtil.hasFullSet(player, ItemArmorSpider.class)) {
			if (player.isSpectator()) {
				return;
			}
			if (player.horizontalCollision) {
				if (world.isClientSide) {
					if (player.zza > 0) {
						player.setDeltaMovement(player.getDeltaMovement().x, 0.2D, player.getDeltaMovement().z); // .motionY = 0.2D;
						this.createClimbingParticles(player, world);
					} else if (player.isCrouching()) {
						player.setDeltaMovement(player.getDeltaMovement().x, 0.0D, player.getDeltaMovement().z);
						// player.motionY = 0.0D;
					} else {
						player.setDeltaMovement(player.getDeltaMovement().x, -0.2D, player.getDeltaMovement().z);

						// player.motionY = -0.2D;
					}
				}

				player.setOnGround(true); // should do
				// player.onGround = true;
			}
			player.fallDistance = 0F;
			player.flyingSpeed += 0.005;
			player.addEffect(new MobEffectInstance(MobEffects.JUMP, 0, 1, false, false));
		}
	}

	private void createClimbingParticles(Player player, Level world) {
		int i = (int) player.position().x;
		int j = Mth.floor(player.blockPosition().getY());
		int k = (int) player.position().z;

		int direction = Mth.floor((player.yRot * 4.0F / 360.0F) + 0.5D) & 3;

		if (direction == 0) // south
		{
			if (k > 0) {
				k += 1;
			}

			if (i < 0) {
				i -= 1;
			}

			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState iblockstate = world.getBlockState(blockpos);

			if (!iblockstate.getBlock().addRunningEffects(iblockstate, world, blockpos, player)) {
				if (iblockstate.getRenderShape() != BlockRenderType.INVISIBLE) {
					world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, iblockstate), player.position().x + (random.nextFloat() - 0.5D) * player.getBbWidth(), player.getBoundingBox().minY + 0.1D, (player.position().z + 0.3) + (random
							.nextFloat() - 0.5D) * player.getBbWidth(),
							// -player.motionX * 4.0D, 1.5D, -player.motionZ * 4.0D,
							player.getDeltaMovement().multiply(-4.0D, 1.0D, 1.0D).x, 1.5D, player.getDeltaMovement().multiply(1.0D, 1.0D, -4.0D).z);
				}
			}
		}

		if (direction == 1) // west
		{
			if (i > 0) {
				i -= 1;
			}

			if (k < 0) {
				k -= 1;
			}

			if (i < 0) {
				i -= 2;
			}

			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState iblockstate = world.getBlockState(blockpos);

			if (!iblockstate.getBlock().addRunningEffects(iblockstate, world, blockpos, player)) {
				if (iblockstate.getRenderShape() != BlockRenderType.INVISIBLE) {
					world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, iblockstate), (player.position().x - 0.3) + (random.nextFloat() - 0.5D) * player.getBbWidth(), player.getBoundingBox().minY + 0.1D, player.position().z + (random
							.nextFloat() - 0.5D) * player.getBbWidth(), player.getDeltaMovement().multiply(-4.0D, 1.0D, 1.0D).x, 1.5D, player.getDeltaMovement().multiply(1.0D, 1.0D, -4.0D).z);
					// -player.motionX * 4.0D, 1.5D, -player.motionZ * 4.0D);
				}
			}
		}

		if (direction == 2) // north
		{
			if (i < 0) {
				i -= 1;
			}

			if (k > 0) {
				k -= 1;
			}

			if ((i > 0 && k < 0) || (i < 0 && k < 0)) {
				k -= 2;
			}

			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState iblockstate = world.getBlockState(blockpos);

			if (!iblockstate.getBlock().addRunningEffects(iblockstate, world, blockpos, player)) {
				if (iblockstate.getRenderShape() != BlockRenderType.INVISIBLE) {
					world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, iblockstate), player.position().x + (random.nextFloat() - 0.5D) * player.getBbWidth(), player.getBoundingBox().minY + 0.1D, (player.position().z - 0.3) + (random
							.nextFloat() - 0.5D) * player.getBbWidth(), player.getDeltaMovement().multiply(-4.0D, 1.0D, 1.0D).x, 1.5D, player.getDeltaMovement().multiply(1.0D, 1.0D, -4.0D).z);

					// -player.motionX * 4.0D, 1.5D, -player.motionZ * 4.0D);
				}
			}
		}

		if (direction == 3) // east
		{
			if (i > 0) {
				i += 1;
			}

			if (k < 0) {
				k -= 1;
			}

			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState iblockstate = world.getBlockState(blockpos);

			if (!iblockstate.getBlock().addRunningEffects(iblockstate, world, blockpos, player)) {
				if (iblockstate.getRenderShape() != BlockRenderType.INVISIBLE) {
					world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, iblockstate), (player.position().x + 0.3) + (random.nextFloat() - 0.5D) * player.getBbWidth(), player.getBoundingBox().minY + 0.1D, player.position().z + (random
							.nextFloat() - 0.5D) * player.getBbWidth(), player.getDeltaMovement().multiply(-4.0D, 1.0D, 1.0D).x, 1.5D, player.getDeltaMovement().multiply(1.0D, 1.0D, -4.0D).z);

					// -player.motionX * 4.0D, 1.5D, -player.motionZ * 4.0D, );
				}
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	@Nullable
	public HumanoidModel getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel _default) {
		return armorSlot == EquipmentSlot.LEGS ? CQRArmorModels.SPIDER_ARMOR_LEGS : CQRArmorModels.SPIDER_ARMOR;
	}

}
