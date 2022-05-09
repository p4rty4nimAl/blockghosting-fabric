package com.p4.blockghosting;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class RandomUtils {
    @SuppressWarnings("SpellCheckingInspection")
    public static boolean isBetweenCoords(BlockPos pos1, BlockPos pos2, BlockPos query) {
        int[] xCoords = {Math.min(pos1.getX(), pos2.getX()), Math.max(pos1.getX(), pos2.getX())};
        int[] yCoords = {Math.min(pos1.getY(), pos2.getY()), Math.max(pos1.getY(), pos2.getY())};
        int[] zCoords = {Math.min(pos1.getZ(), pos2.getZ()), Math.max(pos1.getZ(), pos2.getZ())};
        return ((query.getX() >= xCoords[0] && query.getX() <= xCoords[1]) &&
                (query.getY() >= yCoords[0] && query.getY() <= yCoords[1]) &&
                (query.getZ() >= zCoords[0] && query.getZ() <= zCoords[1]));
    }
    public static BlockPos getBlockPosFromVec3d(Vec3d vec) {
        return new BlockPos(vec.x, vec.y, vec.z);
    }
}