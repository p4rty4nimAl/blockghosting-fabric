package com.p4.blockghosting.mixins;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import com.p4.blockghosting.ConfigInit;
import net.minecraft.block.*;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.p4.blockghosting.BlockGhosting.ySubtraction;
import static com.p4.blockghosting.ConfigInit.State.modAreaState;
import static com.p4.blockghosting.ConfigInit.State.modBlockState;
import static com.p4.blockghosting.randomUtils.isBetweenCoords;

@Mixin(AbstractBlockState.class)
public class AbstractBlockStateMixin extends State<Block, BlockState>
{
	private AbstractBlockStateMixin(Block object, ImmutableMap<Property<?>, Comparable<?>> immutableMap, MapCodec<BlockState> mapCodec) {
		super(object, immutableMap, mapCodec);
	}

	@Inject(at = {@At("HEAD")},
		method = {
			"getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"},
		cancellable = true)
	private void onGetOutlineShape(BlockView view, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
		if (context == ShapeContext.absent()) return;
		if (modBlockState && ConfigInit.blockList.contains(MinecraftClient.getInstance().world.getBlockState(pos).toString())) return;
		if (ConfigInit.State.breakCane) {
			if (MinecraftClient.getInstance().world.getBlockState(pos).isOf(Blocks.SUGAR_CANE) && !MinecraftClient.getInstance().world.getBlockState(pos.subtract(ySubtraction)).isOf(Blocks.SUGAR_CANE)) {
				cir.setReturnValue(VoxelShapes.empty());
				return;
			}
		}
		if (ConfigInit.State.breakBamboo) {
			if (MinecraftClient.getInstance().world.getBlockState(pos).isOf(Blocks.BAMBOO) && !MinecraftClient.getInstance().world.getBlockState(pos.subtract(ySubtraction)).isOf(Blocks.BAMBOO)) {
				cir.setReturnValue(VoxelShapes.empty());
				return;
			}
		}
		if (ConfigInit.State.modAreaState) {
			if (ConfigInit.areaBlackList.size() != 0) {
				for (int i = 0; i < ConfigInit.areaBlackList.size(); i++) {
					BlockPos pos1 = new BlockPos(ConfigInit.areaBlackList.get(i)[0][0], ConfigInit.areaBlackList.get(i)[0][1], ConfigInit.areaBlackList.get(i)[0][2]);
					BlockPos pos2 = new BlockPos(ConfigInit.areaBlackList.get(i)[1][0], ConfigInit.areaBlackList.get(i)[1][1], ConfigInit.areaBlackList.get(i)[1][2]);
					if (isBetweenCoords(pos1, pos2, pos)) {
						cir.setReturnValue(VoxelShapes.empty());
						return;
					}
				}
			}
			if (ConfigInit.areaWhiteList.size() != 0) {
				for (int i = 0; i < ConfigInit.areaWhiteList.size(); i++) {
					BlockPos pos1 = new BlockPos(ConfigInit.areaWhiteList.get(i)[0][0], ConfigInit.areaWhiteList.get(i)[0][1], ConfigInit.areaWhiteList.get(i)[0][2]);
					BlockPos pos2 = new BlockPos(ConfigInit.areaWhiteList.get(i)[1][0], ConfigInit.areaWhiteList.get(i)[1][1], ConfigInit.areaWhiteList.get(i)[1][2]);
					if (isBetweenCoords(pos1, pos2, pos)) return;
				}
			}
		}
		if (modBlockState || modAreaState) cir.setReturnValue(VoxelShapes.empty());
	}
}
