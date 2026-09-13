package com.rsdvlp.machinehud.hud.data;

import com.simibubi.create.content.kinetics.crusher.CrushingWheelBlockEntity;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;
import com.simibubi.create.content.kinetics.drill.DrillBlockEntity;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.content.kinetics.saw.SawBlock;
import com.simibubi.create.content.kinetics.saw.SawBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Createの加工機械から取得したHUD表示用データ。
 * 現在はMechanical Pressのみ対応する。
 * 将来的にMechanical Mixerなどもここへ統合する。
 */
public record CreateProcessingHudData(
        Mode mode,
        State state
) {

    public enum State {
        IDLE,
        RUNNING,
        OUTPUT_BLOCKED
    }

    /**
     * HUD側で扱う加工モード。
     * Create内部のModeをそのまま外へ公開せず、
     * MachineHUD側の意味として保持する。
     */
    public enum Mode {
        PRESSING,
        COMPACTING,
        MIXING,
        CUTTING,
        SAWING,
        DRILLING,
        CRUSHING,
        MILLING
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
                    State.IDLE
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
                behaviour.running ? State.RUNNING : State.IDLE
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
                mixer.running ? State.RUNNING : State.IDLE
        );
    }

    public static CreateProcessingHudData create(
            SawBlockEntity saw
    ) {

        Direction facing = saw.getBlockState().getValue(SawBlock.FACING);

        /*
         * 上向きSawはアイテム加工。
         * 横向きSawはブロック・樹木切断。
         */
        Mode mode = facing == Direction.UP ? Mode.CUTTING : Mode.SAWING;

        boolean running;

        if (mode == Mode.CUTTING) {
            /*
             * ProcessingInventoryでは
             * remainingTime == -1 が未処理状態。
             */
            running = saw.inventory.remainingTime >= 0;
        } else {
            /*
             * 横向きSawのBlockBreaking状態は親クラス内部で管理され、
             * 正確な「今まさに破壊中」を外部から直接取得できない。
             * そのため現段階では、
             * 回転しているかを稼働状態として扱う。
             */
            running = saw.getSpeed() != 0;
        }

        return new CreateProcessingHudData(
                mode,
                running ? State.RUNNING : State.IDLE
        );
    }

    // Drill
    public static CreateProcessingHudData create(
            DrillBlockEntity drill
    ) {
        /*
         * Drillの破壊進捗そのものは親クラス内部で管理されており、
         * MachineHUD側から直接参照できない。
         * そのため現段階では、回転しているかどうかを稼働状態として扱う。
         */
        boolean running = drill.getSpeed() != 0;

        return new CreateProcessingHudData(
                Mode.DRILLING,
                running ? State.RUNNING : State.IDLE
        );
    }

    // CrushingWheel
    public static CreateProcessingHudData create(
            CrushingWheelBlockEntity wheel
    ) {
        CrushingWheelControllerBlockEntity controller = findController(wheel);

        /*
         * Controllerが存在しない場合は、
         * Crushing Wheel単体なので加工状態ではない。
         */
        boolean running =
                controller != null
                        && controller.isOccupied()
                        && controller.crushingspeed != 0;

        return new CreateProcessingHudData(
                Mode.CRUSHING,
                running ? State.RUNNING : State.IDLE
        );
    }

    // Millstone
    public static CreateProcessingHudData create(
            MillstoneBlockEntity millstone
    ) {

        return new CreateProcessingHudData(
                Mode.MILLING,
                getMillstoneState(millstone)
        );
    }

    private static CrushingWheelControllerBlockEntity findController(
            CrushingWheelBlockEntity wheel
    ) {
        if (wheel.getLevel() == null) {
            return null;
        }

        BlockPos wheelPos = wheel.getBlockPos();
        Direction.Axis wheelAxis = wheel.getBlockState().getValue(
                RotatedPillarBlock.AXIS
        );

        /*
         * CrushingWheelControllerは、[Wheel]-[Controller]-[Wheel]の中央に生成される。
         * Wheel自身の回転軸方向にはControllerは生成されない。
         */
        for (Direction direction : Direction.values()) {

            if (direction.getAxis() == wheelAxis) {
                continue;
            }

            BlockEntity blockEntity = wheel.getLevel().getBlockEntity(
                    wheelPos.relative(direction)
            );

            if (blockEntity instanceof CrushingWheelControllerBlockEntity controller) {
                return controller;
            }
        }

        return null;
    }

    private static State getMillstoneState(
            MillstoneBlockEntity millstone
    ) {
        // 材料と動力はあるが、出力できない
        if (isMillstoneOutputBlocked(millstone)) {
            return State.OUTPUT_BLOCKED;
        }

        // 材料がないなら待機中
        if (millstone.inputInv
                .getStackInSlot(0)
                .isEmpty()) {

            return State.IDLE;
        }

        // 動力がないなら待機中
        if (millstone.getSpeed() == 0) {
            return State.IDLE;
        }

        return State.RUNNING;
    }

    private static boolean isMillstoneOutputBlocked(
            MillstoneBlockEntity millstone
    ) {

        for (int i = 0; i < millstone.outputInv.getSlots(); i++) {

            var stack = millstone.outputInv.getStackInSlot(i);

            if (stack.isEmpty()) {
                continue;
            }

            int effectiveLimit =
                    Math.min(
                            millstone.outputInv.getSlotLimit(i),
                            stack.getMaxStackSize()
                    );

            if (stack.getCount() >= effectiveLimit) {
                return true;
            }
        }

        return false;
    }
}