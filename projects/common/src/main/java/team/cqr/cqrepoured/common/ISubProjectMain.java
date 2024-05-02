package team.cqr.cqrepoured.common;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

public interface ISubProjectMain {
	
	public List<Consumer<? extends Event>> getEventListenersToReigster();
	public void registerEventHandlerObjects(Consumer<Object> registerMethod);

	// Loading callbacks
	public void onModConstruction(final IEventBus eventBus);
	public void onConfigFolderInit(final Path folderPath);

}
