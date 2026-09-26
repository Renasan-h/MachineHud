
package com.rsdvlp.machinehud.mekanism.provider;

import com.rsdvlp.machinehud.common.hud.provider.HudProvider;
import com.rsdvlp.machinehud.mekanism.client.MekanismHudClientData;
import com.rsdvlp.machinehud.mekanism.data.*;
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
        // 既存のReaderからクライアント側の機械データを取得する。
        MekanismMachineHudData data =
                MekanismMachineHudDataReader.read(blockEntity);

        if (data == null) {
            return List.of();
        }

        // 化学タンクの情報を取得する。
        MekanismChemicalHudData chemicalData =
                MekanismChemicalReader.read(blockEntity);

        // 現在見ている機械の同期済みデータを取得する。
        MekanismHudSyncPayload synced =
                MekanismHudClientData.get(blockEntity.getBlockPos());

        if (synced != null) {
            // エネルギーと加工進捗にはサーバーの最新値を使用する。
            data = new MekanismMachineHudData(
                    synced.storedEnergy(),
                    data.maxEnergy(),
                    synced.energyUsage(),
                    synced.progress(),
                    synced.maxProgress(),
                    data.state()
            );

            // 化学タンクの情報もサーバーの同期値を優先する。
            if (synced.chemicalCapacity() > 0) {
                chemicalData = new MekanismChemicalHudData(
                        synced.chemicalId(),
                        synced.chemicalAmount(),
                        synced.chemicalCapacity()
                );
            }
        }

        // サーバーから同期された複数タンク情報を取得する。
        // 同期前は空リストとして扱う。
        List<MekanismChemicalTankHudData> chemicalTanks =
                synced != null
                        ? synced.chemicalTanks()
                        : List.of();

        // 同期前はクライアント側のBlockEntityから読み取る。
        MekanismFluidHudData fluidData =
                PressurizedReactionFluidReader.read(blockEntity);

        // 同期済みデータがあればサーバー側の値を優先する。
        if (synced != null) {
            fluidData = synced.fluid();
        }

        return List.of(
                new MekanismHudProvider(
                        data,
                        chemicalData,
                        chemicalTanks,
                        fluidData
                )
        );
    }
}