
package com.rsdvlp.machinehud.mekanism.provider;

import com.rsdvlp.machinehud.common.hud.provider.HudProvider;
import com.rsdvlp.machinehud.mekanism.client.MekanismHudClientData;
import com.rsdvlp.machinehud.mekanism.data.MekanismMachineHudData;
import com.rsdvlp.machinehud.mekanism.data.MekanismMachineHudDataReader;
import com.rsdvlp.machinehud.mekanism.network.MekanismHudSyncPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Mekanismの機械に対応するHUD Providerを生成する。
 * 共通側のHudProvidersから呼び出されるため、
 * CreateなどのほかのMODには依存しない。
 */
public final class MekanismHudProviders {

    private MekanismHudProviders() {
    }

    public static List<HudProvider> create(
            BlockState blockState,
            BlockEntity blockEntity
    ) {
        // 既存のReaderからクライアント側のデータを取得する。
        MekanismMachineHudData data =
                MekanismMachineHudDataReader.read(blockEntity);

        if (data == null) {
            return List.of();
        }

        // 現在見ている機械の同期済みデータを取得する。
        MekanismHudSyncPayload synced =
                MekanismHudClientData.get(blockEntity.getBlockPos());

        if (synced != null) {
            /*
             * 加工進捗とエネルギー消費量には、サーバーから受信した最新値を使用する。
             * 蓄積エネルギーなど、ほかの項目は既存のReaderで取得した値を維持する。
             */
            data = new MekanismMachineHudData(
                    synced.storedEnergy(),
                    data.maxEnergy(),
                    synced.energyUsage(),
                    synced.progress(),
                    synced.maxProgress(),
                    data.state()
            );
        }

        return List.of(new MekanismHudProvider(data));
    }
}