package com.rsdvlp.machinehud.common.hud.provider;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * MOD固有のHudProviderを生成するFactory。
 * common側はCreateやMekanismを直接認識せず、
 * 各連携MOD側からこのインターフェースの実装を登録する。
 */
@FunctionalInterface
public interface HudProviderFactory {

    /**
     * 対象ブロックから、そのMODで使用するHudProviderを生成する。
     */
    List<HudProvider> create(
            BlockState blockState,
            BlockEntity blockEntity
    );
}