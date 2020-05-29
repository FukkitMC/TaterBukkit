package io.github.fukkitmc.fukkit.mixins.net.minecraft.world.dimension;

import io.github.fukkitmc.fukkit.extras.DimensionTypeExtra;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DimensionType.class)
public class DimensionTypeMixin implements DimensionTypeExtra {

    @Override
    public DimensionType getType() {
        return (((DimensionType)(Object)this).type == null) ? ((DimensionType)(Object)this) : ((DimensionType)(Object)this).type;
    }

}
