package com.rsdvlp.machinehud.hud.data.fluid;

import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import net.minecraft.core.Direction;

public record CreatePumpHudData(
        Direction inputDirection,
        Direction outputDirection
) {

    public static CreatePumpHudData create(
            PumpBlockEntity pump
    ) {

        Direction outputDirection =
                pump.getBlockState()
                        .getValue(PumpBlock.FACING);

        Direction inputDirection =
                outputDirection.getOpposite();

        return new CreatePumpHudData(
                inputDirection,
                outputDirection
        );
    }
}