
package com.rsdvlp.machinehud.mekanism.data;

import mekanism.api.chemical.IChemicalTank;
import mekanism.common.tile.machine.TileEntityMetallurgicInfuser;
import mekanism.common.tile.prefab.TileEntityAdvancedElectricMachine;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * 単一の化学タンクを持つMekanism機械から、
 * HUD表示用の化学素材データを取得する。
 */
public final class MekanismChemicalReader {

    private MekanismChemicalReader() {
    }

    public static MekanismChemicalHudData read(
            BlockEntity blockEntity
    ) {
        IChemicalTank tank;

        if (blockEntity instanceof TileEntityMetallurgicInfuser infuser) {
            // 冶金吹込機の注入素材タンク
            tank = infuser.infusionTank;

        } else if (blockEntity instanceof TileEntityAdvancedElectricMachine advanced) {
            // 浄化室・化学注入室・オスミウム圧縮機など
            tank = advanced.chemicalTank;

        } else {
            // 化学タンクを持たない機械は対象外
            return null;
        }

        // 化学タンクの読み取り処理を共通Readerに委譲する。
        MekanismChemicalTankHudData tankData =
                MekanismChemicalTankReader.read(
                        "main",
                        tank
                );

        // 既存の単一タンク用データ形式に変換する。
        return new MekanismChemicalHudData(
                tankData.chemicalId(),
                tankData.amount(),
                tankData.capacity()
        );
    }
}