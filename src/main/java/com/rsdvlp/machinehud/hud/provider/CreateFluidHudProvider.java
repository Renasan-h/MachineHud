package com.rsdvlp.machinehud.hud.provider;

import com.rsdvlp.machinehud.hud.HudGroup;
import com.rsdvlp.machinehud.hud.HudLine;
import com.rsdvlp.machinehud.hud.HudLineType;
import com.rsdvlp.machinehud.hud.data.fluid.CreatePumpHudData;
import com.rsdvlp.machinehud.hud.element.CreateHudElement;
import com.rsdvlp.machinehud.hud.element.HudElement;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class CreateFluidHudProvider implements HudProvider {

    private final CreatePumpHudData data;

    public CreateFluidHudProvider(
            BlockEntity blockEntity
    ) {

        if (blockEntity instanceof PumpBlockEntity pump) {
            this.data = CreatePumpHudData.create(pump);
        } else {
            this.data = null;
        }
    }

    @Override
    public boolean supports(
            HudElement element
    ) {

        return data != null
                && element instanceof CreateHudElement
                && element.getHudGroup() == HudGroup.CREATE_FLUID;
    }

    @Override
    public HudLine createLine(
            HudElement element
    ) {

        if (!(element instanceof CreateHudElement createElement)) {
            return null;
        }

        return switch (createElement) {

            case FLUID_INPUT_DIRECTION ->
                    createInputDirectionLine();

            case FLUID_OUTPUT_DIRECTION ->
                    createOutputDirectionLine();

            default -> null;
        };
    }

    private HudLine createInputDirectionLine() {

        return new HudLine(
                Component.translatable(
                        CreateHudElement.FLUID_INPUT_DIRECTION.getDisplayName()
                ),
                Component.literal(
                        data.inputDirection()
                                .getName()
                                .toUpperCase()
                ),
                1,
                ChatFormatting.WHITE.getColor(),
                HudLineType.VALUE,
                HudGroup.CREATE_FLUID,
                null
        );
    }

    private HudLine createOutputDirectionLine() {

        return new HudLine(
                Component.translatable(
                        CreateHudElement.FLUID_OUTPUT_DIRECTION.getDisplayName()
                ),
                Component.literal(
                        data.outputDirection()
                                .getName()
                                .toUpperCase()
                ),
                1,
                ChatFormatting.WHITE.getColor(),
                HudLineType.VALUE,
                HudGroup.CREATE_FLUID,
                null
        );
    }
}