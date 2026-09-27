
package com.rsdvlp.machinehud.mekanism.data;

import mekanism.common.tile.machine.TileEntityElectrolyticSeparator;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * 電解分離機の入力液体タンクを読み取る。
 */
public final class ElectrolyticSeparatorFluidReader {

    private ElectrolyticSeparatorFluidReader() {
    }

    public static MekanismFluidHudData read(
            BlockEntity blockEntity
    ) {
        if (!(blockEntity instanceof
                TileEntityElectrolyticSeparator separator)) {
            return null;
        }

        return MekanismFluidTankReader.read(
                separator.fluidTank
        );
    }
}