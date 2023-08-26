package team.cqr.cqrepoured.init;

import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.item.crafting.RecipeCrownAttach;
import team.cqr.cqrepoured.item.crafting.RecipeCrownDetach;

public class CQRRecipeTypes {
	
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CQRConstants.MODID);
	
	public static final RegistryObject<RecipeCrownAttach.Serializer> CROWN_ATTACH_SERIALIZER = RECIPE_TYPES.register(RecipeCrownAttach.TYPE_ID.getPath(), RecipeCrownAttach.Serializer::new);
	public static final RegistryObject<RecipeCrownDetach.Serializer> CROWN_DETACH_SERIALIZER = RECIPE_TYPES.register(RecipeCrownDetach.TYPE_ID.getPath(), RecipeCrownDetach.Serializer::new);
	
	public static void register(IEventBus eventBus) {
		RECIPE_TYPES.register(eventBus);

        Registry.register(Registry.RECIPE_TYPE, RecipeCrownAttach.TYPE_ID, new RecipeCrownAttach.RecipeType());
        Registry.register(Registry.RECIPE_TYPE, RecipeCrownDetach.TYPE_ID, new RecipeCrownDetach.RecipeType());
    }

}
