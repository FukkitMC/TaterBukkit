package io.github.fukkitmc.fukkit.mixins.net.minecraft.world.level;

import io.github.fukkitmc.fukkit.extras.LevelPropertiesExtra;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LevelProperties.class)
public abstract class LevelPropertiesMixin implements LevelPropertiesExtra {

    @Shadow public String levelName;

    public void checkName(String name) {
        if (!this.levelName.equals(name)) {
            this.levelName = name;
        }
    }


}
