package io.github.fukkitmc.fukkit.extras;

import net.minecraft.world.gen.feature.FeatureConfig;

public interface StructureFeatureExtra<C extends FeatureConfig> {

    net.minecraft.world.gen.feature.StructureFeature.StructureStartFactory<C> a();
}
