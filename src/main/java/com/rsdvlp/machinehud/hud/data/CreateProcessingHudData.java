package com.rsdvlp.machinehud.hud.data;

import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;

/**
 * Createの加工機械から取得したHUD表示用データ。
 * 現在はMechanical Pressのみ対応する。
 * 将来的にMechanical Mixerなどもここへ統合する。
 */
public record CreateProcessingHudData(
        Mode mode,
        boolean running
) {

    /**
     * HUD側で扱う加工モード。
     * Create内部のModeをそのまま外へ公開せず、
     * MachineHUD側の意味として保持する。
     */
    public enum Mode {
        PRESSING,
        COMPACTING,
        MIXING
    }

    /**
     * Mechanical Pressから加工情報を取得する。
     */
    public static CreateProcessingHudData create(
            MechanicalPressBlockEntity press
    ) {

        PressingBehaviour behaviour = press.getPressingBehaviour();

        /*
         * Behaviourがまだ初期化されていない場合を考慮する。
         * 通常はaddBehaviours()で生成されるが、
         * HUD側ではBlockEntityのライフサイクルに
         * 依存しすぎないよう安全側に倒す。
         */
        if (behaviour == null) {
            return new CreateProcessingHudData(
                    Mode.PRESSING,
                    false
            );
        }

        /*
         * Basin上で動作している場合はCompacting。
         * それ以外は通常のPressingとして扱う。
         */
        Mode mode = behaviour.onBasin()
                ? Mode.COMPACTING
                : Mode.PRESSING;

        return new CreateProcessingHudData(
                mode,
                behaviour.running
        );
    }

    /**
     * Mechanical Mixerから加工情報を取得する。
     * MixerのProgressについては、
     * 正しい総加工時間の取得方法を確定するまで0.0とする。
     */
    public static CreateProcessingHudData create(
            MechanicalMixerBlockEntity mixer
    ) {
        return new CreateProcessingHudData(
                Mode.MIXING,
                mixer.running
        );
    }
}