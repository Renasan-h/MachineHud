
package com.rsdvlp.machinehud.mekanism.data;

import mekanism.common.tile.machine.TileEntityPressurizedReactionChamber;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

/**
 * 加圧反応室の入力・出力化学タンクを読み取る。
 */
public final class PressurizedReactionChemicalReader {

    private PressurizedReactionChemicalReader() {
    }

    public static MekanismChemicalTanksHudData read(
            BlockEntity blockEntity
    ) {
        if (!(blockEntity instanceof
                TileEntityPressurizedReactionChamber chamber)) {
            return null;
        }

        return new MekanismChemicalTanksHudData(
                List.of(
                        MekanismChemicalTankReader.read(
                                "input",
                                chamber.inputGasTank
                        ),
                        MekanismChemicalTankReader.read(
                                "output",
                                chamber.outputGasTank
                        )
                )
        );
    }
}