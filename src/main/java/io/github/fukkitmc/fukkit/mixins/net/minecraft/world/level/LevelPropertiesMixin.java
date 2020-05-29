package io.github.fukkitmc.fukkit.mixins.net.minecraft.world.level;

import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LevelProperties.class)
public abstract class LevelPropertiesMixin{


    @Shadow public String levelName;

    public void checkName(String name) {
        if (!this.levelName.equals(name)) {
            this.levelName = name;
        }
    }
}
