package com.rsdvlp.machinehud.hud.provider;

import com.rsdvlp.machinehud.hud.data.CreateBoilerHudData;
import com.rsdvlp.machinehud.hud.data.CreateHudData;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelBlockEntity;
import com.simibubi.create.content.kinetics.drill.DrillBlockEntity;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.kinetics.saw.SawBlockEntity;
import com.simibubi.create.content.kinetics.speedController.SpeedControllerBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlockEntity;
import com.simibubi.create.content.kinetics.transmission.ClutchBlockEntity;
import com.simibubi.create.content.kinetics.transmission.GearshiftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

/**
 * 照準先のブロックに応じて、
 * 使用可能なHudProviderを生成するFactory。
 * <p>
 * MachineHudRenderer側ではCreateの
 * MOD固有BlockEntityを判定せず、このクラスへ任せる。
 */
public final class HudProviders {
    private HudProviders() {
    }

    /**
     * 対象ブロックに対して使用するHudProvider一覧を生成する。
     */
    public static List<HudProvider> create(
            Level level,
            BlockPos blockPos,
            BlockState blockState,
            BlockEntity blockEntity
    ) {
        List<HudProvider> providers = new ArrayList<>();

        /*
         * =========================
         * Create
         * =========================
         */
        if (blockEntity instanceof KineticBlockEntity kineticBlockEntity) {

            CreateHudData createHudData = new CreateHudData(kineticBlockEntity, blockState);

            providers.add(
                    new CreateHudProvider(createHudData)
            );
        }

        /*
         * Powered ShaftのKinetic情報を、Steam Engineの動力情報として表示する。
         */
        if (blockEntity instanceof SteamEngineBlockEntity steamEngine) {

            PoweredShaftBlockEntity shaft = steamEngine.getShaft();

            if (shaft != null) {

                CreateHudData createHudData = new CreateHudData(shaft, shaft.getBlockState());

                providers.add(
                        new CreateHudProvider(createHudData)
                );
            }
        }

        /*
         * Create Processing
         * Mechanical Pressなど、加工機械固有の情報を提供する。
         * Kinetic情報とは別Providerにすることで、回転情報と加工情報の責務を分離する。
         */
        if (blockEntity instanceof MechanicalPressBlockEntity
                || blockEntity instanceof MechanicalMixerBlockEntity
                || blockEntity instanceof SawBlockEntity
                || blockEntity instanceof DrillBlockEntity
                || blockEntity instanceof CrushingWheelBlockEntity
                || blockEntity instanceof MillstoneBlockEntity) {

            providers.add(new CreateProcessingHudProvider(blockEntity));
        }

        /*
         * Create Power
         * Mechanical Clutchなどの動力固有の情報を表示する。
         */
        if (blockEntity instanceof ClutchBlockEntity
                || blockEntity instanceof GearshiftBlockEntity
                || blockEntity instanceof SpeedControllerBlockEntity) {

            providers.add(new CreatePowerHudProvider(blockEntity));
        }

        /*
         * Create Boiler
         */
        if (blockEntity instanceof FluidTankBlockEntity fluidTankBlockEntity) {

            CreateBoilerHudData boilerHudData = CreateBoilerHudData.create(fluidTankBlockEntity);

            // 通常のFluid Tankなど、
            // Boilerとして動作していない場合は追加しない。
            if (boilerHudData != null) {
                providers.add(new CreateBoilerHudProvider(boilerHudData));
            }
        }

        /*
         * =========================
         * Common
         * =========================
         *
         * 対応している機械Providerが存在するときだけPositionなどの共通情報を表示する。
         */
        if (!providers.isEmpty()) {

            providers.addFirst(
                    new CommonHudProvider(blockPos)
            );
        }

        return providers;
    }
}
