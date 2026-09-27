
package com.rsdvlp.machinehud.mekanism.data;

import mekanism.common.tile.machine.TileEntityElectrolyticSeparator;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * 電解分離機のエネルギーと稼働状態を読み取る。
 */
public final class ElectrolyticSeparatorMachineReader
        implements MekanismMachineReader {

    @Override
    public boolean supports(BlockEntity blockEntity) {
        return blockEntity instanceof TileEntityElectrolyticSeparator;
    }

    @Override
    public MekanismMachineHudData read(BlockEntity blockEntity) {
        if (!(blockEntity instanceof
                TileEntityElectrolyticSeparator separator)) {
            return null;
        }

        var energy = separator.getEnergyContainer();

        if (energy == null) {
            return null;
        }

        boolean active = separator.getActive();

        // 電解分離機には通常の加工進捗カウンターがない。
        return new MekanismMachineHudData(
                energy.getEnergy(),
                energy.getMaxEnergy(),
                active ? separator.getEnergyUsed() : 0L,
                0,
                0,
                active
                        ? MekanismMachineHudData.State.RUNNING
                        : MekanismMachineHudData.State.IDLE
        );
    }
}