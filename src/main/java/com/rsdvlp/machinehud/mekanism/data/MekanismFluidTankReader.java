
package com.rsdvlp.machinehud.mekanism.data;

import mekanism.api.fluid.IExtendedFluidTank;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Mekanismの液体タンクをHUD用データに変換する。
 * 機械の種類には依存せず、IExtendedFluidTankを受け取る。
 */
public final class MekanismFluidTankReader {

    private MekanismFluidTankReader() {
    }

    public static MekanismFluidHudData read(
            IExtendedFluidTank tank
    ) {
        FluidStack stack = tank.getFluid();
        int capacity = tank.getCapacity();

        // 空のタンクでも最大容量は取得する。
        if (stack.isEmpty()) {
            return new MekanismFluidHudData(
                    "",
                    0,
                    capacity
            );
        }

        String fluidId = BuiltInRegistries.FLUID
                .getKey(stack.getFluid())
                .toString();

        return new MekanismFluidHudData(
                fluidId,
                stack.getAmount(),
                capacity
        );
    }
}