package com.example.chocolatequest.item;

import com.example.chocolatequest.entity.projectile.CQRArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class CQRArrowItem extends ArrowItem {
    private final String elementType;

    public CQRArrowItem(String elementType, Item.Properties properties) {
        super(properties);
        this.elementType = elementType;
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        CQRArrowEntity arrow = new CQRArrowEntity(level, shooter, ammo.copyWithCount(1), weapon);
        arrow.setElementType(this.elementType);
        return arrow;
    }

    @Override
    public Projectile asProjectile(Level level, net.minecraft.core.Position pos, ItemStack itemStack, net.minecraft.core.Direction direction) {
        CQRArrowEntity arrow = new CQRArrowEntity(level, pos.x(), pos.y(), pos.z(), itemStack, null);
        arrow.setElementType(this.elementType);
        return arrow;
    }
}
