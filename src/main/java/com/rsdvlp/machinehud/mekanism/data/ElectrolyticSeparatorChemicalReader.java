
package com.rsdvlp.machinehud.mekanism.data;

import mekanism.common.tile.machine.TileEntityElectrolyticSeparator;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

/**
 * 電解分離機の左右2つの出力化学タンクを読み取る。
 */
public final class ElectrolyticSeparatorChemicalReader {

    private ElectrolyticSeparatorChemicalReader() {
    }

    public static MekanismChemicalTanksHudData read(
            BlockEntity blockEntity
    ) {
        if (!(blockEntity instanceof
                TileEntityElectrolyticSeparator separator)) {
            return null;
        }

        return new MekanismChemicalTanksHudData(
                List.of(
                        MekanismChemicalTankReader.read(
                                "left",
                                separator.leftTank
                        ),
                        MekanismChemicalTankReader.read(
                                "right",
                                separator.rightTank
                        )
                )
        );
    }
}