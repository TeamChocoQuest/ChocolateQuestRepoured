package team.cqr.cqrepoured.common.services.interfaces;

import java.util.Optional;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public interface FactionService {
	
	public boolean hasFactionCapabiltiy(Entity entity);

	public void setReputation(Entity entity, ResourceLocation faction, int value);
	
	public Optional<Integer> getReputation(Entity entity, ResourceLocation faction);
	
}
