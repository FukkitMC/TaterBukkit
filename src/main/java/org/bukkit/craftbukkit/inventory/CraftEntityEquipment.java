package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.entity.mob.MobEntity;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CraftEntityEquipment implements EntityEquipment {

    private final CraftLivingEntity entity;

    public CraftEntityEquipment(CraftLivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public void setItem(EquipmentSlot slot, ItemStack item) {
        Preconditions.checkArgument(slot != null, "slot must not be null");
        net.minecraft.entity.EquipmentSlot nmsSlot = CraftEquipmentSlot.getNMS(slot);
        setEquipment(nmsSlot, item);
    }

    @Override
    public ItemStack getItem(EquipmentSlot slot) {
        Preconditions.checkArgument(slot != null, "slot must not be null");
        net.minecraft.entity.EquipmentSlot nmsSlot = CraftEquipmentSlot.getNMS(slot);
        return getEquipment(nmsSlot);
    }

    @Override
    public ItemStack getItemInMainHand() {
        return getEquipment(net.minecraft.entity.EquipmentSlot.MAINHAND);
    }

    @Override
    public void setItemInMainHand(ItemStack item) {
        setEquipment(net.minecraft.entity.EquipmentSlot.MAINHAND, item);
    }

    @Override
    public ItemStack getItemInOffHand() {
        return getEquipment(net.minecraft.entity.EquipmentSlot.OFFHAND);
    }

    @Override
    public void setItemInOffHand(ItemStack item) {
        setEquipment(net.minecraft.entity.EquipmentSlot.OFFHAND, item);
    }

    @Override
    public ItemStack getItemInHand() {
        return getItemInMainHand();
    }

    @Override
    public void setItemInHand(ItemStack stack) {
        setItemInMainHand(stack);
    }

    @Override
    public ItemStack getHelmet() {
        return getEquipment(net.minecraft.entity.EquipmentSlot.HEAD);
    }

    @Override
    public void setHelmet(ItemStack helmet) {
        setEquipment(net.minecraft.entity.EquipmentSlot.HEAD, helmet);
    }

    @Override
    public ItemStack getChestplate() {
        return getEquipment(net.minecraft.entity.EquipmentSlot.CHEST);
    }

    @Override
    public void setChestplate(ItemStack chestplate) {
        setEquipment(net.minecraft.entity.EquipmentSlot.CHEST, chestplate);
    }

    @Override
    public ItemStack getLeggings() {
        return getEquipment(net.minecraft.entity.EquipmentSlot.LEGS);
    }

    @Override
    public void setLeggings(ItemStack leggings) {
        setEquipment(net.minecraft.entity.EquipmentSlot.LEGS, leggings);
    }

    @Override
    public ItemStack getBoots() {
        return getEquipment(net.minecraft.entity.EquipmentSlot.FEET);
    }

    @Override
    public void setBoots(ItemStack boots) {
        setEquipment(net.minecraft.entity.EquipmentSlot.FEET, boots);
    }

    @Override
    public ItemStack[] getArmorContents() {
        ItemStack[] armor = new ItemStack[]{
                getEquipment(net.minecraft.entity.EquipmentSlot.FEET),
                getEquipment(net.minecraft.entity.EquipmentSlot.LEGS),
                getEquipment(net.minecraft.entity.EquipmentSlot.CHEST),
                getEquipment(net.minecraft.entity.EquipmentSlot.HEAD),
        };
        return armor;
    }

    @Override
    public void setArmorContents(ItemStack[] items) {
        setEquipment(net.minecraft.entity.EquipmentSlot.FEET, items.length >= 1 ? items[0] : null);
        setEquipment(net.minecraft.entity.EquipmentSlot.LEGS, items.length >= 2 ? items[1] : null);
        setEquipment(net.minecraft.entity.EquipmentSlot.CHEST, items.length >= 3 ? items[2] : null);
        setEquipment(net.minecraft.entity.EquipmentSlot.HEAD, items.length >= 4 ? items[3] : null);
    }

    private ItemStack getEquipment(net.minecraft.entity.EquipmentSlot slot) {
        return CraftItemStack.asBukkitCopy(entity.getHandle().getEquippedStack(slot));
    }

    private void setEquipment(net.minecraft.entity.EquipmentSlot slot, ItemStack stack) {
        entity.getHandle().a(slot, CraftItemStack.asNMSCopy(stack));
    }

    @Override
    public void clear() {
        for (net.minecraft.entity.EquipmentSlot slot : net.minecraft.entity.EquipmentSlot.values()) {
            setEquipment(slot, null);
        }
    }

    @Override
    public Entity getHolder() {
        return entity;
    }

    @Override
    public float getItemInHandDropChance() {
        return getItemInMainHandDropChance();
    }

    @Override
    public void setItemInHandDropChance(float chance) {
        setItemInMainHandDropChance(chance);
    }

    @Override
    public float getItemInMainHandDropChance() {
       return getDropChance(net.minecraft.entity.EquipmentSlot.MAINHAND);
    }

    @Override
    public void setItemInMainHandDropChance(float chance) {
        setDropChance(net.minecraft.entity.EquipmentSlot.MAINHAND, chance);
    }

    @Override
    public float getItemInOffHandDropChance() {
        return getDropChance(net.minecraft.entity.EquipmentSlot.OFFHAND);
    }

    @Override
    public void setItemInOffHandDropChance(float chance) {
        setDropChance(net.minecraft.entity.EquipmentSlot.OFFHAND, chance);
    }

    @Override
    public float getHelmetDropChance() {
        return getDropChance(net.minecraft.entity.EquipmentSlot.HEAD);
    }

    @Override
    public void setHelmetDropChance(float chance) {
        setDropChance(net.minecraft.entity.EquipmentSlot.HEAD, chance);
    }

    @Override
    public float getChestplateDropChance() {
        return getDropChance(net.minecraft.entity.EquipmentSlot.CHEST);
    }

    @Override
    public void setChestplateDropChance(float chance) {
        setDropChance(net.minecraft.entity.EquipmentSlot.CHEST, chance);
    }

    @Override
    public float getLeggingsDropChance() {
        return getDropChance(net.minecraft.entity.EquipmentSlot.LEGS);
    }

    @Override
    public void setLeggingsDropChance(float chance) {
        setDropChance(net.minecraft.entity.EquipmentSlot.LEGS, chance);
    }

    @Override
    public float getBootsDropChance() {
        return getDropChance(net.minecraft.entity.EquipmentSlot.FEET);
    }

    @Override
    public void setBootsDropChance(float chance) {
        setDropChance(net.minecraft.entity.EquipmentSlot.FEET, chance);
    }

    private void setDropChance(net.minecraft.entity.EquipmentSlot slot, float chance) {
        if (slot == net.minecraft.entity.EquipmentSlot.MAINHAND || slot == net.minecraft.entity.EquipmentSlot.OFFHAND) {
            ((MobEntity) entity.getHandle()).handDropChances[slot.getEntitySlotId()] = chance;
        } else {
            ((MobEntity) entity.getHandle()).armorDropChances[slot.getEntitySlotId()] = chance;
        }
    }

    private float getDropChance(net.minecraft.entity.EquipmentSlot slot) {
        if (slot == net.minecraft.entity.EquipmentSlot.MAINHAND || slot == net.minecraft.entity.EquipmentSlot.OFFHAND) {
            return ((MobEntity) entity.getHandle()).handDropChances[slot.getEntitySlotId()];
        } else {
            return ((MobEntity) entity.getHandle()).armorDropChances[slot.getEntitySlotId()];
        }
    }
}
