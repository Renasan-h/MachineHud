package com.rsdvlp.machinehud.hud.data;

import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;

/**
 * Createの加工機械から取得したHUD表示用データ。
 * 現在はMechanical Pressのみ対応する。
 * 将来的にMechanical Mixerなどもここへ統合する。
 */
public record CreateProcessingHudData(
        Mode mode,
        boolean running,
        double progress
) {

    /**
     * HUD側で扱う加工モード。
     * Create内部のModeをそのまま外へ公開せず、
     * MachineHUD側の意味として保持する。
     */
    public enum Mode {
        PRESSING,
        COMPACTING
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
                    false,
                    0.0
            );
        }

        /*
         * Basin上で動作している場合はCompacting。
         * それ以外は通常のPressingとして扱う。
         */
        Mode mode = behaviour.onBasin()
                ? Mode.COMPACTING
                : Mode.PRESSING;

        /*
         * Mechanical Pressの1サイクルを0.0～1.0へ正規化する。
         * Create側ではクライアント同期の都合でrunningTicksが
         * 一時的に負数になるため、描画処理と同様に絶対値を使用する。
         */
        double progress = behaviour.running
                ? Math.min(
                Math.abs(behaviour.runningTicks)
                / (double) PressingBehaviour.CYCLE,
                1.0
        ) : 0.0;

        return new CreateProcessingHudData(
                mode,
                behaviour.running,
                progress
        );
    }
}