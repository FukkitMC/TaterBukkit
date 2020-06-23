package io.github.fukkitmc.fukkit.redirects;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import javax.annotation.Nullable;

public interface ItemFrameEntityRedirects {

    static Box calculateBoundingBox(@Nullable Entity entity, BlockPos blockPosition, Direction direction, int width, int height) {
        {
            double d0 = 0.46875D;
            double d1 = (double) blockPosition.getX() + 0.5D - (double) direction.getOffsetX() * 0.46875D;
            double d2 = (double) blockPosition.getY() + 0.5D - (double) direction.getOffsetY() * 0.46875D;
            double d3 = (double) blockPosition.getZ() + 0.5D - (double) direction.getOffsetZ() * 0.46875D;

            if (entity != null) {
                entity.setPos(d1, d2, d3);
            }
            double d4 = width;
            double d5 = height;
            double d6 = width;
            Direction.Axis enumdirection_enumaxis = direction.getAxis();

            switch (enumdirection_enumaxis) {
                case X:
                    d4 = 1.0D;
                    break;
                case Y:
                    d5 = 1.0D;
                    break;
                case Z:
                    d6 = 1.0D;
            }

            d4 /= 32.0D;
            d5 /= 32.0D;
            d6 /= 32.0D;
            return new Box(d1 - d4, d2 - d5, d3 - d6, d1 + d4, d2 + d5, d3 + d6);
        }
    }

}
