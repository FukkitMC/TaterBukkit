package io.github.fukkitmc.fukkit.extras;

public interface AbstractMinecartEntityExtra {

	org.bukkit.util.Vector getDerailedVelocityMod();

	void setFlyingVelocityMod(org.bukkit.util.Vector var0);

	void setDerailedVelocityMod(org.bukkit.util.Vector var0);

	org.bukkit.util.Vector getFlyingVelocityMod();
}
