package uz.shahzod.poplar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.trees.Tree;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.server.ServerWorld;

public class PoplarTree extends Tree {

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random random, boolean largeHive) {
        return null;
    }

    @Override
    public boolean growTree(ServerWorld world, ChunkGenerator generator, BlockPos pos, BlockState state, Random rand) {
        int height = 7 + rand.nextInt(4);
        Block log = PoplarMod.LOG.get();

        for (int y = 1; y < height; y++) {
            if (!canPlace(world, pos.above(y))) {
                return false;
            }
        }

        Block leaves;
        switch (rand.nextInt(3)) {
            case 0:
                leaves = PoplarMod.LEAVES_RED.get();
                break;
            case 1:
                leaves = PoplarMod.LEAVES_ORANGE.get();
                break;
            default:
                leaves = PoplarMod.LEAVES_YELLOW.get();
                break;
        }

        Set<BlockPos> logs = new HashSet<>();
        for (int y = 0; y < height; y++) {
            logs.add(pos.above(y));
        }

        List<BlockPos> leafList = new ArrayList<>();
        int[][] layers = {
                {height, 0},
                {height - 1, 1},
                {height - 2, 1},
                {height - 3, 2},
                {height - 4, 2},
                {height - 5, 2},
                {height - 6, 1}
        };
        for (int[] layer : layers) {
            int y = layer[0];
            int r = layer[1];
            if (y < 3) continue;
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    boolean corner = Math.abs(dx) == r && Math.abs(dz) == r && r > 0;
                    if (corner && (r >= 2 || rand.nextInt(2) == 0)) continue;
                    BlockPos p = pos.offset(dx, y, dz);
                    if (logs.contains(p)) continue;
                    if (canPlace(world, p)) {
                        leafList.add(p);
                    }
                }
            }
        }

        Map<BlockPos, Integer> dist = new HashMap<>();
        for (BlockPos p : leafList) {
            dist.put(p, 7);
        }
        for (int pass = 0; pass < 7; pass++) {
            for (BlockPos p : leafList) {
                int best = dist.get(p);
                for (Direction d : Direction.values()) {
                    BlockPos n = p.relative(d);
                    if (logs.contains(n)) {
                        best = Math.min(best, 1);
                    } else if (dist.containsKey(n)) {
                        best = Math.min(best, dist.get(n) + 1);
                    }
                }
                dist.put(p, best);
            }
        }

        for (BlockPos p : logs) {
            world.setBlock(p, log.defaultBlockState(), 3);
        }
        for (BlockPos p : leafList) {
            int d = Math.min(7, dist.get(p));
            world.setBlock(p, leaves.defaultBlockState().setValue(LeavesBlock.DISTANCE, d), 3);
        }
        return true;
    }

    private static boolean canPlace(ServerWorld world, BlockPos p) {
        BlockState s = world.getBlockState(p);
        return s.isAir()
                || s.getBlock() instanceof LeavesBlock
                || s.getBlock() instanceof SaplingBlock
                || s.getMaterial().isReplaceable();
    }
}
