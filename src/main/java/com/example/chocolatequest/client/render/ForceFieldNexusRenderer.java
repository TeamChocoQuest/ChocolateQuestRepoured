package com.example.chocolatequest.client.render;

import com.example.chocolatequest.block.entity.ForceFieldNexusBlockEntity;
import com.example.chocolatequest.client.model.ForceFieldNexusModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ForceFieldNexusRenderer extends GeoBlockRenderer<ForceFieldNexusBlockEntity> {
    public ForceFieldNexusRenderer() {
        super(new ForceFieldNexusModel());
        // addRenderLayer(new software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer<>(this));
    }
}
