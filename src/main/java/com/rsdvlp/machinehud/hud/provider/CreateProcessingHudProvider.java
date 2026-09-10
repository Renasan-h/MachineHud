package com.rsdvlp.machinehud.hud.provider;

import com.rsdvlp.machinehud.hud.HudGroup;
import com.rsdvlp.machinehud.hud.HudLevel;
import com.rsdvlp.machinehud.hud.HudLine;
import com.rsdvlp.machinehud.hud.HudLineType;
import com.rsdvlp.machinehud.hud.data.CreateProcessingHudData;
import com.rsdvlp.machinehud.hud.element.CreateHudElement;
import com.rsdvlp.machinehud.hud.element.HudElement;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
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
            case PROCESSING_PROGRESS -> createProgressLine();
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
        };

        return new HudLine(
                Component.translatable(
                        CreateHudElement.PROCESSING_MODE.getDisplayName()
                ),
                value,
                0,
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

        Component value = Component.translatable(
                data.running()
                        ? "machinehud.processing.state.running"
                        : "machinehud.processing.state.idle"
        );

        return new HudLine(
                Component.translatable(
                        CreateHudElement.PROCESSING_STATE.getDisplayName()
                ),
                value,
                0,
                data.running()
                        ? ChatFormatting.GREEN.getColor()
                        : ChatFormatting.GRAY.getColor(),
                HudLineType.VALUE,
                HudGroup.CREATE_PROCESSING,
                null
        );
    }

    /**
     * 現在の加工進捗を表示する。
     * progressはData側で0.0～1.0へ正規化済みなので、
     * Providerでは表示用のHudLevelへ変換するだけにする。
     */
    private HudLine createProgressLine() {

        int percent = (int) Math.round(
                data.progress() * 100.0
        );

        return new HudLine(
                Component.translatable(
                        CreateHudElement.PROCESSING_PROGRESS.getDisplayName()
                ),
                Component.literal(percent + "%"),
                0,
                ChatFormatting.GRAY.getColor(),
                HudLineType.PROGRESS,
                HudGroup.CREATE_PROCESSING,

                /*
                 * RendererのVisual列へ進捗値を渡す。
                 *
                 * HudLevel自体は既存のHudLine構造上、
                 * Visual用データの入れ物としてここでは利用する。
                 * min=0 / max=100。
                 */
                new HudLevel(
                        percent,
                        100,
                        0
                )
        );
    }
}