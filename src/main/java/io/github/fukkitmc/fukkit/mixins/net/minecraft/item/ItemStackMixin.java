package io.github.fukkitmc.fukkit.mixins.net.minecraft.item;

import com.mojang.datafixers.Dynamic;
import io.github.fukkitmc.fukkit.extras.ItemStackExtra;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.datafixer.NbtOps;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.tag.RegistryTagManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackExtra {

    @Shadow
    public abstract CompoundTag toTag(CompoundTag tag);

    @Shadow
    public CompoundTag tag;

    @Shadow
    public abstract void setTag(@Nullable CompoundTag tag);

    @Shadow
    @Deprecated
    public Item item;

    @Shadow
    public int count;

    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract void setDamage(int damage);

    @Shadow
    public abstract int getDamage();

    @Shadow
    public abstract void decrement(int amount);

    @Shadow
    public abstract void setCount(int count);

    @Shadow
    public abstract int getCount();

    @Shadow
    public abstract boolean canPlaceOn(RegistryTagManager manager, CachedBlockPosition pos);

    @Override
    public void convertStack(int version) {
        if (0 < version && version < CraftMagicNumbers.INSTANCE.getDataVersion()) {
            CompoundTag savedStack = new CompoundTag();
            this.toTag(savedStack);
            savedStack = (CompoundTag) MinecraftServer.getServer().dataFixer.update(TypeReferences.ITEM_STACK, new Dynamic(NbtOps.INSTANCE, savedStack), version, CraftMagicNumbers.INSTANCE.getDataVersion()).getValue();
            this.load(savedStack);
        }
    }

    @Override
    public CompoundTag getTagClone() {
        return this.tag == null ? null : this.tag.copy();
    }

    @Override
    public void setTagClone(CompoundTag nbtTagCompound) {
        this.setTag(nbtTagCompound == null ? null : nbtTagCompound.copy());
    }

    @Override
    public void setItem(Item var0) {
        this.item = var0;
    }

    @Override
    public ActionResult placeItem(ItemUsageContext itemactioncontext, Hand enumhand) { // CraftBukkit - add hand
        PlayerEntity entityhuman = itemactioncontext.getPlayer();
        BlockPos blockposition = itemactioncontext.getBlockPos();
        CachedBlockPosition shapedetectorblock = new CachedBlockPosition(itemactioncontext.getWorld(), blockposition, false);

        if (entityhuman != null && !entityhuman.abilities.allowModifyWorld && !this.canPlaceOn(itemactioncontext.getWorld().getTagManager(), shapedetectorblock)) {
            return ActionResult.PASS;
        } else {
            // CraftBukkit start - handle all block place event logic here
            CompoundTag oldData = this.getTagClone();
            int oldCount = this.getCount();
            World world = itemactioncontext.getWorld();

            if (!(this.getItem() instanceof BucketItem)) { // if not bucket
                world.captureBlockStates = true;
                // special case bonemeal
                if (this.getItem() == Items.BONE_MEAL) {
                    world.captureTreeGeneration = true;
                }
            }
            Item item = this.getItem();
            ActionResult enuminteractionresult = item.useOnBlock(itemactioncontext);
            CompoundTag newData = this.getTagClone();
            int newCount = this.getCount();
            this.setCount(oldCount);
            this.setTagClone(oldData);
            world.captureBlockStates = false;
            if (enuminteractionresult == ActionResult.SUCCESS && world.captureTreeGeneration && world.capturedBlockStates.size() > 0) {
                world.captureTreeGeneration = false;
                Location location = new Location(world.getCraftWorld(), blockposition.getX(), blockposition.getY(), blockposition.getZ());
                TreeType treeType = SaplingBlock.treeType;
                SaplingBlock.treeType = null;
                List<BlockState> blocks = new java.util.ArrayList<>(world.capturedBlockStates.values());
                world.capturedBlockStates.clear();
                StructureGrowEvent structureEvent = null;
                if (treeType != null) {
                    boolean isBonemeal = getItem() == Items.BONE_MEAL;
                    structureEvent = new StructureGrowEvent(location, treeType, isBonemeal, (Player) entityhuman.getBukkitEntity(), blocks);
                    org.bukkit.Bukkit.getPluginManager().callEvent(structureEvent);
                }

                BlockFertilizeEvent fertilizeEvent = new BlockFertilizeEvent(CraftBlock.at(world, blockposition), (Player) entityhuman.getBukkitEntity(), blocks);
                fertilizeEvent.setCancelled(structureEvent != null && structureEvent.isCancelled());
                org.bukkit.Bukkit.getPluginManager().callEvent(fertilizeEvent);

                if (!fertilizeEvent.isCancelled()) {
                    // Change the stack to its new contents if it hasn't been tampered with.
                    if (this.getCount() == oldCount && Objects.equals(this.tag, oldData)) {
                        this.setTag(newData);
                        this.setCount(newCount);
                    }
                    for (BlockState blockstate : blocks) {
                        blockstate.update(true);
                    }
                }

                return enuminteractionresult;
            }
            world.captureTreeGeneration = false;

            if (entityhuman != null && enuminteractionresult == ActionResult.SUCCESS) {
                org.bukkit.event.block.BlockPlaceEvent placeEvent = null;
                List<BlockState> blocks = new java.util.ArrayList<>(world.capturedBlockStates.values());
                world.capturedBlockStates.clear();
                if (blocks.size() > 1) {
                    placeEvent = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockMultiPlaceEvent(world, entityhuman, enumhand, blocks, blockposition.getX(), blockposition.getY(), blockposition.getZ());
                } else if (blocks.size() == 1) {
                    placeEvent = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(world, entityhuman, enumhand, blocks.get(0), blockposition.getX(), blockposition.getY(), blockposition.getZ());
                }

                if (placeEvent != null && (placeEvent.isCancelled() || !placeEvent.canBuild())) {
                    enuminteractionresult = ActionResult.FAIL; // cancel placement
                    // PAIL: Remove this when MC-99075 fixed
                    placeEvent.getPlayer().updateInventory();
                    // revert back all captured blocks
                    for (BlockState blockstate : blocks) {
                        blockstate.update(true, false);
                    }

                    // Brute force all possible updates
                    BlockPos placedPos = ((CraftBlock) placeEvent.getBlock()).getPosition();
                    for (Direction dir : Direction.values()) {
                        ((ServerPlayerEntity) entityhuman).networkHandler.sendPacket(new BlockUpdateS2CPacket(world, placedPos.offset(dir)));
                    }
                } else {
                    // Change the stack to its new contents if it hasn't been tampered with.
                    if (this.getCount() == oldCount && Objects.equals(this.tag, oldData)) {
                        this.setTag(newData);
                        this.setCount(newCount);
                    }

                    for (Map.Entry<BlockPos, BlockEntity> e : world.capturedTileEntities.entrySet()) {
                        world.setBlockEntity(e.getKey(), e.getValue());
                    }

                    for (BlockState blockstate : blocks) {
                        int updateFlag = ((CraftBlockState) blockstate).getFlag();
                        net.minecraft.block.BlockState oldBlock = ((CraftBlockState) blockstate).getHandle();
                        BlockPos newblockposition = ((CraftBlockState) blockstate).getPosition();
                        net.minecraft.block.BlockState block = world.getBlockState(newblockposition);

                        if (!(block.getBlock() instanceof BlockWithEntity)) { // Containers get placed automatically
                            block.getBlock().onBlockAdded(block, world, newblockposition, oldBlock, true);
                        }

                        world.notifyAndUpdatePhysics(newblockposition, null, oldBlock, block, world.getBlockState(newblockposition), updateFlag); // send null chunk as chunk.k() returns false by this point
                    }

                    // Special case juke boxes as they update their tile entity. Copied from ItemRecord.
                    // PAIL: checkme on updates.
                    if (this.item instanceof MusicDiscItem) {
                        ((JukeboxBlock) Blocks.JUKEBOX).setRecord(world, blockposition, world.getBlockState(blockposition), ((ItemStack) (Object) this));
                        world.syncWorldEvent(null, 1010, blockposition, Item.getRawId(this.item));
                        this.decrement(1);
                        entityhuman.incrementStat(Stats.PLAY_RECORD);
                    }

                    if (this.item == Items.WITHER_SKELETON_SKULL) { // Special case skulls to allow wither spawns to be cancelled
                        BlockPos bp = blockposition;
                        if (!world.getBlockState(blockposition).getMaterial().isReplaceable()) {
                            if (!world.getBlockState(blockposition).getMaterial().isSolid()) {
                                bp = null;
                            } else {
                                bp = bp.offset(itemactioncontext.getSide());
                            }
                        }
                        if (bp != null) {
                            BlockEntity te = world.getBlockEntity(bp);
                            if (te instanceof SkullBlockEntity) {
                                WitherSkullBlock.onPlaced(world, bp, (SkullBlockEntity) te);
                            }
                        }
                    }

                    // SPIGOT-4678
                    if (this.item instanceof SignItem && SignItem.openSign != null) {
                        try {
                            entityhuman.openEditSignScreen((SignBlockEntity) world.getBlockEntity(SignItem.openSign));
                        } finally {
                            SignItem.openSign = null;
                        }
                    }

                    // SPIGOT-1288 - play sound stripped from ItemBlock
                    if (this.item instanceof BlockItem) {
                        BlockSoundGroup soundeffecttype = ((BlockItem) this.item).getBlock().soundGroup;
                        world.playSound(entityhuman, blockposition, soundeffecttype.getPlaceSound(), SoundCategory.BLOCKS, (soundeffecttype.getVolume() + 1.0F) / 2.0F, soundeffecttype.getPitch() * 0.8F);
                    }

                    entityhuman.incrementStat(Stats.USED.getOrCreateStat(item));
                }
            }
            world.capturedTileEntities.clear();
            world.capturedBlockStates.clear();
            // CraftBukkit end

            return enuminteractionresult;
        }
    }

    @Override
    public void load(CompoundTag nbttagcompound) {
        this.item = Registry.ITEM.get(new Identifier(nbttagcompound.getString("id")));
        this.count = nbttagcompound.getByte("Count");
        if (nbttagcompound.contains("tag", 10)) {
            // CraftBukkit start - make defensive copy as this data may be coming from the save thread
            this.tag = nbttagcompound.getCompound("tag").copy();
            this.getItem().postProcessTag(this.tag);
            // CraftBukkit end
        }

        if (this.getItem().isDamageable()) {
            this.setDamage(this.getDamage());
        }

    }
}
