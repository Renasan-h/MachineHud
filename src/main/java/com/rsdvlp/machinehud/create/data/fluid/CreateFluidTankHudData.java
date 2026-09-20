package com.rsdvlp.machinehud.create.data.fluid;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;

public record CreateFluidTankHudData(
        FluidStack fluid,
        int amount,
        int capacity
) implements CreateFluidHudData {

    public static CreateFluidTankHudData create(
            FluidTankBlockEntity tank
    ) {

        FluidTankBlockEntity controller = tank.getControllerBE();

        if (controller == null) {
            return null;
        }

        FluidStack fluid =
                controller.getTankInventory()
                        .getFluid()
                        .copy();

        return new CreateFluidTankHudData(
                fluid,
                controller.getTankInventory()
                        .getFluidAmount(),
                controller.getTankInventory()
                        .getCapacity()
        );
    }
}