package team.cqr.cqrepoured.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.extensions.IForgeItem;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface ICustomReachItem extends IForgeItem {

    //Needs to be unique
    public static final UUID REACH_DISTANCE_MODIFIER = UUID.fromString("b7d3df52-d360-491b-9bb5-2e8e3b5b279a");

    //Setter and getter for custom multimap
    public Multimap<Attribute, AttributeModifier> getCustomAttributesField();

    public void setCustomAttributesField(Multimap<Attribute, AttributeModifier> value);

    //Getter for the actual reach bonus value
    public double getReachDistanceBonus();

    @Nonnull
    Multimap<Attribute, AttributeModifier> execSuperGetAttributeModifiers(EquipmentSlot slot, ItemStack stack);

    public default Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            if (this.getCustomAttributesField() == null) {
                Builder<Attribute, AttributeModifier> attributeBuilder = ImmutableMultimap.builder();

                //Add in all the other attributes...
                this.execSuperGetAttributeModifiers(slot, stack).forEach((Attribute attribute, AttributeModifier attributeMod) -> {
                    attributeBuilder.put(attribute, attributeMod);
                });

                //!!ONLY call get when you need it according to "dieSieben07"...
                attributeBuilder.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(ICustomReachItem.REACH_DISTANCE_MODIFIER, "Weapon modifier", this.getReachDistanceBonus(), Operation.ADDITION));

                this.setCustomAttributesField(attributeBuilder.build());
            }
            return this.getCustomAttributesField();
        }

        return this.execSuperGetAttributeModifiers(slot, stack);
    }

}
