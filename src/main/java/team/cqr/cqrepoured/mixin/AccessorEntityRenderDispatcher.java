package team.cqr.cqrepoured.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;

@Mixin(EntityRenderDispatcher.class)
public interface AccessorEntityRenderDispatcher {

	@Accessor
	public Font getFont();
	
	@Accessor
	public ItemRenderer getItemRenderer();
	
	@Accessor
	public BlockRenderDispatcher getBlockRenderDispatcher();
	
	@Accessor
	public EntityModelSet getEntityModels();
	
}
