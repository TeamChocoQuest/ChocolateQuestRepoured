package team.cqr.cqrepoured.item.armor;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.AzureLibUtil;

public abstract class CQRGeoArmorBase extends ArmorItem implements GeoItem {
	
	public CQRGeoArmorBase(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
		super(pMaterial, pType, pProperties);
	}

	protected final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
	
	protected abstract GeoArmorRenderer<?> createRenderer();
	
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		super.initializeClient(consumer);
		
		consumer.accept(new IClientItemExtensions() {
			private GeoArmorRenderer<?> renderer;
			
			@Override
			public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
				if (this.renderer == null) {
					this.renderer = CQRGeoArmorBase.this.createRenderer();
				}
				this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
				
				return this.renderer;
			}
		});
	}


}
