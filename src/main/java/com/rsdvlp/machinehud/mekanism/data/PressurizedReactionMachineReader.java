
package com.rsdvlp.machinehud.mekanism.data;

import mekanism.common.capabilities.energy.PRCEnergyContainer;
import mekanism.common.tile.machine.TileEntityPressurizedReactionChamber;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * 加圧反応室のエネルギー・加工進捗を読み取るReader。
 */
public final class PressurizedReactionMachineReader
        implements MekanismMachineReader {

    @Override
    public boolean supports(BlockEntity blockEntity) {
        return blockEntity
                instanceof TileEntityPressurizedReactionChamber;
    }

    @Override
    public MekanismMachineHudData read(
            BlockEntity blockEntity
    ) {
        if (!(blockEntity instanceof
                TileEntityPressurizedReactionChamber chamber)) {
            return null;
        }

        PRCEnergyContainer energy =
                chamber.getEnergyContainer();

        if (energy == null) {
            return null;
        }

        boolean active = chamber.getActive();

        // 稼働中のみ、1tickあたりの消費量を表示する。
        long energyUsage = active
                ? energy.getEnergyPerTick()
                : 0L;

        MekanismMachineHudData.State state = active
                ? MekanismMachineHudData.State.RUNNING
                : MekanismMachineHudData.State.IDLE;

        return new MekanismMachineHudData(
                energy.getEnergy(),
                energy.getMaxEnergy(),
                energyUsage,
                chamber.getOperatingTicks(),
                chamber.getTicksRequired(),
                state
        );
    }
}