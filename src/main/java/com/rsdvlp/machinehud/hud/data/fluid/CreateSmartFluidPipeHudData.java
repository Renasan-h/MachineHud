package com.rsdvlp.machinehud.hud.data.fluid;

import com.simibubi.create.content.fluids.pipes.SmartFluidPipeBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.minecraft.world.item.ItemStack;

public record CreateSmartFluidPipeHudData(
        ItemStack filter
) implements CreateFluidHudData {

    public static CreateSmartFluidPipeHudData create(
            SmartFluidPipeBlockEntity pipe
    ) {

        FilteringBehaviour filtering =
                pipe.getBehaviour(FilteringBehaviour.TYPE);

        if (filtering == null) {
            return new CreateSmartFluidPipeHudData(
                    ItemStack.EMPTY
            );
        }

        return new CreateSmartFluidPipeHudData(
                filtering.getFilter().copy()
        );
    }
}