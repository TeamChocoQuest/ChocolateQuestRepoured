package com.example.chocolatequest.client.render;

import com.example.chocolatequest.block.entity.NexusCoreBlockEntity;
import com.example.chocolatequest.client.model.NexusCoreModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class NexusCoreRenderer extends GeoBlockRenderer<NexusCoreBlockEntity> {
    public NexusCoreRenderer() {
        super(new NexusCoreModel());
    }
}
