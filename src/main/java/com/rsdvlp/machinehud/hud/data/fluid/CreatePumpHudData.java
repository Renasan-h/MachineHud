package com.rsdvlp.machinehud.hud.data.fluid;

import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public record CreatePumpHudData(
        Direction inputDirection,
        Direction outputDirection,
        ConnectionType inputConnection,
        ConnectionType outputConnection,
        int maxFlowRate
) implements CreateFluidHudData {

    public enum ConnectionType {
        PIPE,
        MACHINE,
        OPEN
    }

    public static CreatePumpHudData create(
            PumpBlockEntity pump
    ) {

        Direction outputDirection =
                pump.getBlockState()
                        .getValue(PumpBlock.FACING);

        Direction inputDirection =
                outputDirection.getOpposite();

        ConnectionType inputConnection =
                getConnectionType(
                        pump,
                        inputDirection
                );

        ConnectionType outputConnection =
                getConnectionType(
                        pump,
                        outputDirection
                );

        /*
         * CreateのFluidNetworkではPumpから与えられたpressureの
         * 1/2を基本転送速度として使用している。
         *
         * Pumpのpressureは回転速度の絶対値なので、
         * 最大流量は |RPM| / 2 [mB/t] として求められる。
         */
        int maxFlowRate = pump.getSpeed() == 0
                ? 0
                : Math.max(1, (int) (Math.abs(pump.getSpeed()) / 2.0f));

        return new CreatePumpHudData(
                inputDirection,
                outputDirection,
                inputConnection,
                outputConnection,
                maxFlowRate
        );
    }

    /**
     * Pumpの指定方向に何が接続されているかを判定する。
     * Createの流体ネットワークと同様に、
     * FluidTransportBehaviourを持つものはPIPE、
     * FluidHandler Capabilityを持つBlockEntityはMACHINEとして扱う。
     */
    private static ConnectionType getConnectionType(
            PumpBlockEntity pump,
            Direction direction
    ) {

        Level level = pump.getLevel();

        if (level == null) {
            return ConnectionType.OPEN;
        }

        BlockPos targetPos = pump.getBlockPos().relative(direction);

        /*
         * CreateのPipe Networkに参加しているブロックか確認する。
         *
         * 通常のFluid Pipeだけでなく、
         * Smart Fluid Pipeなども同じ判定に入る。
         */
        FluidTransportBehaviour pipe =
                FluidPropagator.getPipe(
                        level,
                        targetPos
                );

        if (pipe != null) {
            return ConnectionType.PIPE;
        }

        /*
         * PipeではなくFluidHandlerを公開している場合は、
         * Pumpから見た流体Endpointとして扱う。
         *
         * Fluid Tankや他MODのTank/機械もここに入る。
         */
        IFluidHandler fluidHandler =
                level.getCapability(
                        Capabilities.FluidHandler.BLOCK,
                        targetPos,
                        direction.getOpposite()
                );

        if (fluidHandler != null) {
            return ConnectionType.MACHINE;
        }

        return ConnectionType.OPEN;
    }
}