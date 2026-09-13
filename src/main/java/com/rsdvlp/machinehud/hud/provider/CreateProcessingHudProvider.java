package com.rsdvlp.machinehud.hud.provider;

import com.rsdvlp.machinehud.hud.HudGroup;
import com.rsdvlp.machinehud.hud.HudLine;
import com.rsdvlp.machinehud.hud.HudLineType;
import com.rsdvlp.machinehud.hud.data.CreateProcessingHudData;
import com.rsdvlp.machinehud.hud.element.CreateHudElement;
import com.rsdvlp.machinehud.hud.element.HudElement;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelBlockEntity;
import com.simibubi.create.content.kinetics.drill.DrillBlockEntity;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.kinetics.saw.SawBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Createの加工機械固有情報をHUDへ提供するProvider。
 * 現在はMechanical Pressのみ対応する。
 * 将来的にMixerなどの加工機械もここへ追加する。
 */
public class CreateProcessingHudProvider implements HudProvider {

    private final BlockEntity blockEntity;
    private final CreateProcessingHudData data;

    public CreateProcessingHudProvider(BlockEntity blockEntity) {

        this.blockEntity = blockEntity;

        /*
         * 現在対応している加工機械はMechanical Pressのみ。
         * 対応機械が増えた場合も、
         * CreateProcessingHudDataへ変換してから
         * Provider側で共通処理する。
         */
        if (blockEntity instanceof MechanicalPressBlockEntity press) {
            this.data = CreateProcessingHudData.create(press);
        } else if (blockEntity instanceof MechanicalMixerBlockEntity mixer) {
            this.data = CreateProcessingHudData.create(mixer);
        } else if (blockEntity instanceof SawBlockEntity saw) {
            this.data = CreateProcessingHudData.create(saw);
        } else if (blockEntity instanceof DrillBlockEntity drill) {
            this.data = CreateProcessingHudData.create(drill);
        } else if (blockEntity instanceof CrushingWheelBlockEntity crushingWheel) {
            this.data = CreateProcessingHudData.create(crushingWheel);
        } else if (blockEntity instanceof MillstoneBlockEntity millstone) {
            this.data = CreateProcessingHudData.create(millstone);
        } else {
            this.data = null;
        }
    }

    @Override
    public boolean supports(HudElement element) {

        /*
         * Processingデータを取得できた機械に対してのみ、
         * CREATE_PROCESSINGグループを担当する。
         * これによりPROCESSING_PROGRESSやRECIPEなどを
         * 後から追加してもsupports()の変更は不要。
         */
        return data != null
                && element instanceof CreateHudElement
                && element.getHudGroup() == HudGroup.CREATE_PROCESSING;
    }

    @Override
    public HudLine createLine(HudElement element) {

        if (!(element instanceof CreateHudElement createElement)) {
            return null;
        }

        return switch (createElement) {
            case PROCESSING_MODE -> createModeLine();
            case PROCESSING_STATE -> createStateLine();
            default -> null;
        };
    }

    /**
     * 現在の加工モードを表示する。
     */
    private HudLine createModeLine() {

        Component value = switch (data.mode()) {

            case PRESSING -> Component.translatable(
                    "machinehud.processing.mode.pressing"
            );
            case COMPACTING -> Component.translatable(
                    "machinehud.processing.mode.compacting"
            );
            case MIXING -> Component.translatable(
                    "machinehud.processing.mode.mixing"
            );
            case CUTTING -> Component.translatable(
                    "machinehud.processing.mode.cutting"
            );
            case SAWING -> Component.translatable(
                    "machinehud.processing.mode.sawing"
            );
            case DRILLING -> Component.translatable(
                    "machinehud.processing.mode.drilling"
            );
            case CRUSHING -> Component.translatable(
                    "machinehud.processing.mode.crushing"
            );
            case MILLING -> Component.translatable(
                    "machinehud.processing.mode.milling"
            );
        };

        return new HudLine(
                Component.translatable(
                        CreateHudElement.PROCESSING_MODE.getDisplayName()
                ),
                value,
                1,
                ChatFormatting.GRAY.getColor(),
                HudLineType.VALUE,
                HudGroup.CREATE_PROCESSING,
                null
        );
    }

    /**
     * 現在加工中かどうかを表示する。
     */
    private HudLine createStateLine() {

        Component value = switch (data.state()) {
            case IDLE -> Component.translatable(
                    "machinehud.processing.state.idle"
            );
            case RUNNING -> Component.translatable(
                    "machinehud.processing.state.running"
            );
            case OUTPUT_BLOCKED -> Component.translatable(
                    "machinehud.processing.state.output_blocked"
            );
        };

        int color = switch(data.state()){
            case IDLE -> ChatFormatting.GRAY.getColor();
            case RUNNING -> ChatFormatting.GREEN.getColor();
            case OUTPUT_BLOCKED -> ChatFormatting.YELLOW.getColor();
        };

        return new HudLine(
                Component.translatable(
                        CreateHudElement.PROCESSING_STATE.getDisplayName()
                ),
                value,
                1,
                color,
                HudLineType.VALUE,
                HudGroup.CREATE_PROCESSING,
                null
        );
    }
}