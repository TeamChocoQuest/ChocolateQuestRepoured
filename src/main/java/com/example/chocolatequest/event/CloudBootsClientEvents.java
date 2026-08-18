package com.example.chocolatequest.event;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = ChocolateQuestReDone.MODID, value = Dist.CLIENT)
public class CloudBootsClientEvents {

    @SubscribeEvent
    public static void onClientPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof LocalPlayer player) {
            // Check if player has Cloud Boots equipped
            if (player.getItemBySlot(EquipmentSlot.FEET).is(ModItems.CLOUD_BOOTS.get())) {
                // Apply custom air physics if the player is airborne and not flying via Creative mode
                if (!player.onGround() && !player.getAbilities().flying) {
                    
                    // Read movement inputs (W/A/S/D)
                    boolean forward = player.input.up;
                    boolean backward = player.input.down;
                    boolean left = player.input.left;
                    boolean right = player.input.right;
                    
                    float forwardImpulse = forward ? 1.0F : (backward ? -1.0F : 0.0F);
                    float strafeImpulse = left ? 1.0F : (right ? -1.0F : 0.0F);
                    
                    Vec3 delta = player.getDeltaMovement();
                    double motionX = delta.x;
                    double motionY = delta.y;
                    double motionZ = delta.z;
                    
                    if (forwardImpulse != 0 || strafeImpulse != 0) {
                        // Increase horizontal speed significantly in the air
                        float yaw = player.getYRot();
                        float f = strafeImpulse * 0.72F;
                        float f1 = forwardImpulse * 0.72F;
                        
                        float f2 = (float) Math.sin(yaw * ((float)Math.PI / 180F));
                        float f3 = (float) Math.cos(yaw * ((float)Math.PI / 180F));
                        
                        double boostX = (f * f3 - f1 * f2) * 0.035D;
                        double boostZ = (f1 * f3 + f * f2) * 0.035D;
                        
                        motionX += boostX;
                        motionZ += boostZ;
                        
                        // Cap maximum horizontal speed so it doesn't get out of control
                        double hSpeed = Math.sqrt(motionX * motionX + motionZ * motionZ);
                        double maxSpeed = player.isSprinting() ? 0.38D : 0.28D;
                        if (hSpeed > maxSpeed) {
                            double ratio = maxSpeed / hSpeed;
                            motionX *= ratio;
                            motionZ *= ratio;
                        }
                    } else {
                        // Apply strong air friction when not pressing keys for precise stopping
                        motionX *= 0.8D;
                        motionZ *= 0.8D;
                    }
                    
                    // Slow down falling to create a "gliding" / stabilized effect
                    if (motionY < -0.9D) {
                        motionY = -0.9D;
                    }
                    
                    // Apply the new physics
                    player.setDeltaMovement(motionX, motionY, motionZ);
                }
            }
        }
    }
}
