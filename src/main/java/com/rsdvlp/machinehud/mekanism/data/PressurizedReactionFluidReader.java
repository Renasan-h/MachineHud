
package com.rsdvlp.machinehud.mekanism.data;

import mekanism.common.tile.machine.TileEntityPressurizedReactionChamber;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * 加圧反応室の入力液体タンクを読み取る。
 */
public final class PressurizedReactionFluidReader {

    private PressurizedReactionFluidReader() {
    }

    public static MekanismFluidHudData read(
            BlockEntity blockEntity
    ) {
        if (!(blockEntity instanceof
                TileEntityPressurizedReactionChamber chamber)) {
            return null;
        }

        // 機械固有のReaderは対象タンクの選択だけを担当する。
        return MekanismFluidTankReader.read(
                chamber.inputFluidTank
        );
    }
}