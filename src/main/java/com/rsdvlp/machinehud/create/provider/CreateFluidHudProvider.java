package com.rsdvlp.machinehud.create.provider;

import com.rsdvlp.machinehud.common.hud.HudGroup;
import com.rsdvlp.machinehud.common.hud.HudLine;
import com.rsdvlp.machinehud.common.hud.HudLineType;
import com.rsdvlp.machinehud.common.hud.element.HudElement;
import com.rsdvlp.machinehud.common.hud.provider.HudProvider;
import com.rsdvlp.machinehud.create.data.fluid.*;
import com.rsdvlp.machinehud.create.element.CreateHudElement;
import com.simibubi.create.content.fluids.pipes.SmartFluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;

public final class CreateFluidHudProvider implements HudProvider {

    private final CreateFluidHudData data;

    public CreateFluidHudProvider(
            BlockEntity blockEntity
    ) {

        if (blockEntity instanceof PumpBlockEntity pump) {
            this.data = CreatePumpHudData.create(pump);
        } else if (blockEntity instanceof FluidValveBlockEntity valve) {
            this.data = CreateFluidValveHudData.create(valve);
        } else if (blockEntity instanceof SmartFluidPipeBlockEntity pipe) {
            this.data = CreateSmartFluidPipeHudData.create(pipe);
        } else if (blockEntity instanceof FluidTankBlockEntity tank) {
            this.data = CreateFluidTankHudData.create(tank);
        } else {
            this.data = null;
        }
    }

    @Override
    public boolean supports(
            HudElement element
    ) {

        if (data == null) {
            return false;
        }

        if (!(element instanceof CreateHudElement createElement)) {
            return false;
        }

        if (data instanceof CreatePumpHudData) {
            return createElement == CreateHudElement.CREATE_FLUID_INPUT_CONNECTION
                    || createElement == CreateHudElement.CREATE_FLUID_OUTPUT_CONNECTION
                    || createElement == CreateHudElement.CREATE_FLUID_MAX_FLOW_RATE;
        }

        if (data instanceof CreateFluidValveHudData) {
            return createElement == CreateHudElement.CREATE_FLUID_VALVE_STATE;
        }

        if (data instanceof CreateSmartFluidPipeHudData) {
            return createElement == CreateHudElement.CREATE_FLUID_FILTER;
        }

        if (data instanceof CreateFluidTankHudData) {
            return createElement == CreateHudElement.CREATE_FLUID_CONTENT
                    || createElement == CreateHudElement.CREATE_FLUID_AMOUNT;
        }

        return false;
    }

    @Override
    public HudLine createLine(
            HudElement element
    ) {

        if (!(element instanceof CreateHudElement createElement)) {
            return null;
        }

        return switch (createElement) {

            case CREATE_FLUID_INPUT_CONNECTION -> createInputConnectionLine(createElement);

            case CREATE_FLUID_OUTPUT_CONNECTION -> createOutputConnectionLine(createElement);

            case CREATE_FLUID_MAX_FLOW_RATE -> createMaxFlowRateLine(createElement);

            case CREATE_FLUID_VALVE_STATE -> createValveStateLine(createElement);

            case CREATE_FLUID_FILTER -> createFilterLine(createElement);

            case CREATE_FLUID_CONTENT -> createFluidContentLine(createElement);

            case CREATE_FLUID_AMOUNT -> createFluidAmountLine(createElement);

            default -> null;
        };
    }

    private HudLine createInputConnectionLine(
            CreateHudElement element
    ) {

        if (!(data instanceof CreatePumpHudData pumpData)) {
            return null;
        }

        return new HudLine(
                Component.translatable(element.getDisplayName()),
                getConnectionName(pumpData.inputConnection()),
                1,
                0xFFFFFF,
                HudLineType.VALUE,
                HudGroup.CREATE_FLUID,
                null
        );
    }

    private HudLine createOutputConnectionLine(
            CreateHudElement element
    ) {

        if (!(data instanceof CreatePumpHudData pumpData)) {
            return null;
        }

        return new HudLine(
                Component.translatable(element.getDisplayName()),
                getConnectionName(pumpData.outputConnection()),
                1,
                0xFFFFFF,
                HudLineType.VALUE,
                HudGroup.CREATE_FLUID,
                null
        );
    }

    /**
     * Mechanical Pumpが現在の回転速度から出せる最大流量を表示する。
     * 実際に移動した流量ではなく、CreateのFluidNetworkが
     * Pumpのpressureから算出する転送能力を表す。
     */
    private HudLine createMaxFlowRateLine(
            CreateHudElement element
    ) {

        if (!(data instanceof CreatePumpHudData pumpData)) {
            return null;
        }

        return new HudLine(
                Component.translatable(element.getDisplayName()),
                Component.literal(
                        String.format("%,d mB/t", pumpData.maxFlowRate())
                ),
                1,
                0xFFFFFF,
                HudLineType.VALUE,
                HudGroup.CREATE_FLUID,
                null
        );
    }

    private HudLine createValveStateLine(
            CreateHudElement element
    ) {

        if (!(data instanceof CreateFluidValveHudData(CreateFluidValveHudData.State state))) {
            return null;
        }

        String key =
                state == CreateFluidValveHudData.State.OPEN
                        ? "machinehud.create.fluid.valve_state.open"
                        : "machinehud.create.fluid.valve_state.closed";

        return new HudLine(
                Component.translatable(element.getDisplayName()),
                Component.translatable(key),
                1,
                ChatFormatting.WHITE.getColor(),
                HudLineType.VALUE,
                HudGroup.CREATE_FLUID,
                null
        );
    }

    private HudLine createFilterLine(
            CreateHudElement element
    ) {

        if (!(data instanceof CreateSmartFluidPipeHudData(
                ItemStack filter
        ))) {
            return null;
        }

        Component value =
                filter.isEmpty()
                        ? Component.translatable(
                        "machinehud.create.fluid.filter.empty"
                )
                        : filter.getHoverName();

        return new HudLine(
                Component.translatable(element.getDisplayName()),
                value,
                1,
                ChatFormatting.WHITE.getColor(),
                HudLineType.VALUE,
                HudGroup.CREATE_FLUID,
                null
        );
    }

    private HudLine createFluidContentLine(
            CreateHudElement element
    ) {

        if (!(data instanceof CreateFluidTankHudData(
                FluidStack fluid,
                int ignoredAmount,
                int ignoredCapacity
        ))) {
            return null;
        }

        Component value =
                fluid.isEmpty()
                        ? Component.translatable(
                        "machinehud.create.fluid.empty"
                )
                        : fluid.getHoverName();

        return new HudLine(
                Component.translatable(element.getDisplayName()),
                value,
                1,
                ChatFormatting.WHITE.getColor(),
                HudLineType.VALUE,
                HudGroup.CREATE_FLUID,
                null
        );
    }

    private HudLine createFluidAmountLine(
            CreateHudElement element
    ) {

        if (!(data instanceof CreateFluidTankHudData(
                FluidStack ignoredFluid,
                int amount,
                int capacity
        ))) {
            return null;
        }

        return new HudLine(
                Component.translatable(element.getDisplayName()),
                Component.literal(
                        String.format(
                                "%,d / %,d mB",
                                amount,
                                capacity
                        )
                ),
                1,
                ChatFormatting.WHITE.getColor(),
                HudLineType.VALUE,
                HudGroup.CREATE_FLUID,
                null
        );
    }

    /**
     * Pumpの接続種別をHUD表示用Componentへ変換する。
     */
    private Component getConnectionName(
            CreatePumpHudData.ConnectionType type
    ) {
        return switch (type) {
            case PIPE -> Component.translatable("machinehud.create.fluid.connection.pipe");

            case MACHINE -> Component.translatable("machinehud.create.fluid.connection.machine");

            case OPEN -> Component.translatable("machinehud.create.fluid.connection.open");
        };
    }
}