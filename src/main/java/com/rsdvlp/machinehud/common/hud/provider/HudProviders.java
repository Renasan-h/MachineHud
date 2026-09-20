package com.rsdvlp.machinehud.common.hud.provider;

import com.rsdvlp.machinehud.create.provider.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

/**
 * 照準先のブロックに応じて、
 * 使用可能なHudProviderを生成するFactory。
 * <p>
 * MachineHudRenderer側ではCreateの
 * MOD固有BlockEntityを判定せず、このクラスへ任せる。
 */
public final class HudProviders {
    private HudProviders() {
    }

    /**
     * 対象ブロックに対して使用するHudProvider一覧を生成する。
     */
    public static List<HudProvider> create(
            Level level,
            BlockPos blockPos,
            BlockState blockState,
            BlockEntity blockEntity
    ) {
        List<HudProvider> providers =
                new ArrayList<>(
                        CreateHudProviders.create(
                                blockState,
                                blockEntity
                        )
                );

        /*
         * =========================
         * Common
         * =========================
         *
         * 対応している機械Providerが存在するときだけPositionなどの共通情報を表示する。
         */
        if (!providers.isEmpty()) {

            providers.addFirst(
                    new CommonHudProvider(blockPos)
            );
        }

        return providers;
    }
}
