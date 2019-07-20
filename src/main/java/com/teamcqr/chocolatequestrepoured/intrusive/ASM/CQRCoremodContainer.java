package com.teamcqr.chocolatequestrepoured.intrusive.ASM;

import com.google.common.eventbus.EventBus;
import com.teamcqr.chocolatequestrepoured.util.Reference;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

import java.util.Collections;

/**
 * FML: For use in Forge mod list
 */
public class CQRCoremodContainer extends DummyModContainer {

    // Singleton setup
    private static CQRCoremodContainer COREMOD_CONTAINER = new CQRCoremodContainer();
    public static CQRCoremodContainer getInstance() { return COREMOD_CONTAINER; }

    /**
     * Configure metadata
     */
    private CQRCoremodContainer() {

        // Register using superclass
        super(new ModMetadata());
        ModMetadata meta = getMetadata();

        // Override where necessary
        meta.modId = Reference.MODID + "_coremod";
        meta.name = Reference.NAME + " Coremod";
        meta.description = "Coremod that enables certain functionality within the " + Reference.NAME + " mod";
        meta.version = Reference.VERSION;
        meta.authorList = Collections.singletonList( "jdawg3636 in cooperation with DerToater, ArloTheEpic, and the rest of the CQR team" );

    }

    /**
     * Register the event bus
     * @return successful
     */
    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

}