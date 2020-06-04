package io.github.fukkitmc.fukkit.mixins.net.minecraft.util.registry;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Registry.class)
public class RegistryMixin {

    @Inject(method = "register(Lnet/minecraft/util/registry/Registry;Lnet/minecraft/util/Identifier;Ljava/lang/Object;)Ljava/lang/Object;", at = @At("TAIL"))
    private static <T> void register(Registry<? super T> registry, Identifier id, T entry, CallbackInfoReturnable<T> cir){
        if(registry == Registry.ENCHANTMENT){
            Enchantment.registerEnchantment(new CraftEnchantment((net.minecraft.enchantment.Enchantment) entry));
        }
    }

}

