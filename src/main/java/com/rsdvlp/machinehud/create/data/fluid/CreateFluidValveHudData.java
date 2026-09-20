package com.rsdvlp.machinehud.create.data.fluid;

import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlock;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlockEntity;

public record CreateFluidValveHudData(
        State state
) implements CreateFluidHudData {

    public enum State {
        OPEN,
        CLOSED
    }

    public static CreateFluidValveHudData create(
            FluidValveBlockEntity valve
    ) {

        boolean open = valve.getBlockState()
                .getValue(FluidValveBlock.ENABLED);

        return new CreateFluidValveHudData(
                open
                        ? State.OPEN
                        : State.CLOSED
        );
    }
}