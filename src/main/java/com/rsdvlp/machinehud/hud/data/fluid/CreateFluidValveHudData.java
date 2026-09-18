package com.rsdvlp.machinehud.hud.data.fluid;

import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlock;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlockEntity;
import net.minecraft.core.Direction;

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

    public static record CreateFluidHudData(
            Type type,
            Direction inputDirection,
            Direction outputDirection,
            State state
    ) {

        public enum Type {
            PUMP,
            VALVE
        }

        public enum State {
            OPEN,
            CLOSED,
            NONE
        }
    }
}