
package com.rsdvlp.machinehud.mekanism.data;

import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.tile.machine.TileEntityEnergizedSmelter;
import mekanism.common.tile.prefab.TileEntityElectricMachine;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Mekanismの機械からHUD表示用データを取得する。
 * データ取得処理をProviderから分離することで、
 * 将来的にほかのMekanism機械にも対応しやすくする。
 */
public final class MekanismMachineHudDataReader {

    private MekanismMachineHudDataReader() {
    }

    /**
     * Energized SmelterのHUD用データを取得する。
     *
     * @param blockEntity 対象のBlockEntity
     * @return 対応機械の場合はHUDデータ、それ以外はnull
     */
    public static MekanismMachineHudData read(
            BlockEntity blockEntity
    ) {

        // 今回はEnergized Smelterのみを対象にする。
        if (!(blockEntity instanceof TileEntityEnergizedSmelter smelter)) {
            return null;
        }

        // 親クラスからエネルギーコンテナを取得する。
        MachineEnergyContainer<TileEntityElectricMachine> energy =
                smelter.getEnergyContainer();

        // エネルギー消費量は稼働中のみ表示する。
        long energyUsage = smelter.getActive()
                ? energy.getEnergyPerTick()
                : 0L;

        // 現時点では稼働状態をRUNNING / IDLEの2種類で判定する。
        // OUTPUT_BLOCKEDは原因を正確に判定できるようになってから使用する。
        MekanismMachineHudData.State state =
                smelter.getActive()
                        ? MekanismMachineHudData.State.RUNNING
                        : MekanismMachineHudData.State.IDLE;

        return new MekanismMachineHudData(
                energy.getEnergy(),
                energy.getMaxEnergy(),
                energyUsage,
                smelter.getOperatingTicks(),
                smelter.getTicksRequired(),
                state
        );
    }
}