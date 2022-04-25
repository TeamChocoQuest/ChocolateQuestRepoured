package team.cqr.cqrepoured.mixinutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import team.cqr.cqrepoured.entity.CQRPartEntity;

public class PartEntityCache {
	
	public static final WeakHashMap<World, List<CQRPartEntity<?>>> PART_ENTITY_CACHE_CQR = new WeakHashMap<>();

	public static void registerMultipartEvents(IEventBus bus) {
		bus.addListener((Consumer<EntityJoinWorldEvent>) event -> {
			if(event.getEntity().isMultipartEntity())
			synchronized (PART_ENTITY_CACHE_CQR) {
				PART_ENTITY_CACHE_CQR.computeIfAbsent(event.getWorld(), (w) -> new ArrayList<>());
				PART_ENTITY_CACHE_CQR.get(event.getWorld()).addAll(Arrays.stream(Objects.requireNonNull(event.getEntity().getParts())).
						filter(CQRPartEntity.class::isInstance).map(obj -> (CQRPartEntity<?>) obj).
						collect(Collectors.toList()));

			}
		});
		bus.addListener((Consumer<EntityLeaveWorldEvent>) event -> {
			if(event.getEntity().isMultipartEntity())
			synchronized (PART_ENTITY_CACHE_CQR) {
				PART_ENTITY_CACHE_CQR.computeIfPresent(event.getWorld(), (world, list) -> {
					list.removeAll(Arrays.stream(Objects.requireNonNull(event.getEntity().getParts())).
							filter(CQRPartEntity.class::isInstance).map(CQRPartEntity.class::cast).
							collect(Collectors.toList()));
					return list;
				});
				if(PART_ENTITY_CACHE_CQR.get(event.getWorld()).isEmpty()) {
					PART_ENTITY_CACHE_CQR.remove(event.getWorld());
				}
			}
		});
		bus.addListener((Consumer<WorldEvent.Unload>) event -> PART_ENTITY_CACHE_CQR.remove(event.getWorld()));
	}

}
