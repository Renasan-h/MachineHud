package com.rsdvlp.machinehud.hud.data;

import net.minecraft.core.Direction;

public record CreateFluidHudData(
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