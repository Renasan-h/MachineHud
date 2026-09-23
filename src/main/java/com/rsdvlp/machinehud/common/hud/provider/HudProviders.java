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

    // MOD固有のHudProvider Factory一覧。
    private static final List<HudProviderFactory> FACTORIES = new ArrayList<>();

    private HudProviders() {
    }

    /**
     * MOD固有のHudProvider Factoryを登録する。
     */
    public static void register(HudProviderFactory factory) {
        FACTORIES.add(factory);
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
        List<HudProvider> providers = new ArrayList<>();

        /*
         * 登録されている各MODのFactoryへ問い合わせる。
         *
         * Create/Mekanismなどの具体的な種類は
         * HudProviders自身では認識しない。
         */
        for (HudProviderFactory factory : FACTORIES) {
            providers.addAll(
                    factory.create(
                            blockState,
                            blockEntity
                    )
            );
        }

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
