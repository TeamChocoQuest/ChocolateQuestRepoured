package team.cqr.cqrepoured.integration.jei;

import java.util.List;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import team.cqr.cqrepoured.entity.trade.RestockData;
import team.cqr.cqrepoured.entity.trade.StockData;
import team.cqr.cqrepoured.entity.trade.TradeData;
import team.cqr.cqrepoured.entity.trade.TradeInput;

public class NPCTradeRecipeCategory implements IRecipeCategory<TradeData> {

	public NPCTradeRecipeCategory(IGuiHelper guiHelper) {
		
	}
	
	@Override
	public RecipeType<TradeData> getRecipeType() {
		return JEIPluginCQR.NPC_TRADE_RECIPE_TYPE;
	}

	@Override
	public Component getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDrawable getBackground() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDrawable getIcon() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean isHandled(TradeData recipe) {
		return IRecipeCategory.super.isHandled(recipe);
	}
	
	@Override
	public void draw(TradeData recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
		// Draw items and checkboxes (meta, NBT, rules)
		
		// Top: Display rules for the customer => just a counter of how many rules, rules are rendered via tooltip!
		// Bottom: Stock and restock settings
	}
	
	@Override
	public List<Component> getTooltipStrings(TradeData recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		// TODO: Add the relevant rules to a certain item
		return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, TradeData recipe, IFocusGroup focuses) {
		int y = 0;
		for (TradeInput input : recipe) {
			builder.addSlot(RecipeIngredientRole.INPUT, 0, y).addIngredient(VanillaTypes.ITEM_STACK, input.stack());
		}
		// Displayed on the right
		builder.addSlot(RecipeIngredientRole.OUTPUT, 20, y / 2).addItemStack(recipe.getResultingItem());
		// Also display stockdata on the bottom if there is stockdata
		if (recipe.getStockData().isPresent()) {
			StockData stockData = recipe.getStockData().get();
			// If restocking is configured, display that too
			if (stockData.restockData().isPresent()) {
				RestockData restockData = stockData.restockData().get();
			}
		}
	}

}
