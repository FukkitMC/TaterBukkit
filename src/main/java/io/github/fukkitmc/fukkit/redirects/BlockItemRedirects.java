package io.github.fukkitmc.fukkit.redirects;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;

import java.util.Iterator;

public interface BlockItemRedirects {

    static BlockState getBlockState(BlockState BlockState, CompoundTag nbttagcompound1) {
        BlockState BlockState1 = BlockState;
        {
            // CraftBukkit end
            StateManager<Block, BlockState> blockstatelist = BlockState.getBlock().getStateManager();
            Iterator iterator = nbttagcompound1.getKeys().iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();
                Property<?> Property = blockstatelist.getProperty(s);

                if (Property != null) {
                    String s1 = nbttagcompound1.get(s).asString();

                    BlockState1 = BlockItem.with(BlockState1, Property, s1);
                }
            }
        }
        return BlockState1;
    }
    
}
