package team.cqr.cqrepoured.integration.jei;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.entity.trade.TradeData;
import team.cqr.cqrepoured.entity.trade.TradeProfile;
import team.cqr.cqrepoured.init.CQRDatapackLoaders;

@JeiPlugin
public class JEIPluginCQR implements IModPlugin {

	@Override
	public ResourceLocation getPluginUid() {
		return CQRConstants.JEI.PLUGIN_ID;
	}
	
	public static RecipeType<TradeData> NPC_TRADE_RECIPE_TYPE = new RecipeType<>(CQRConstants.JEI.NPC_TRADE_CATEGORY_UID, TradeData.class);
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IModPlugin.super.registerCategories(registration);
		
		registration.addRecipeCategories(new NPCTradeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		IModPlugin.super.registerRecipes(registration);
		
		Set<TradeProfile> profiles = CQRDatapackLoaders.TRADE_PROFILES.getData().values().stream().collect(Collectors.toSet());
		final Set<TradeData> tradeData = new HashSet<>();
		profiles.forEach(profile -> {
			profile.forEach(trade -> {
				tradeData.add(trade);
			});
		});
		registration.addRecipes(NPC_TRADE_RECIPE_TYPE, new ArrayList<>(tradeData));
	}
	
}
