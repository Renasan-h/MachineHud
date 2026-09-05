package com.rsdvlp.machinehud.hud.data;

import com.simibubi.create.content.fluids.tank.BoilerData;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;

/**
 * CreateのBoilerDataを、
 * MachineHUDで扱いやすい形へ変換したデータ。
 * Create内部の表示用Componentには依存せず、
 * 公開されている数値情報から構築する。
 */
public record CreateBoilerHudData(
        // MachineHUD上で表示する実効ボイラーレベル。
        int boilerLevel,

        // ボイラーサイズによって決まる上限。
        int sizeLevel,

        // 水供給量によって決まる上限。
        int waterLevel,

        // 熱源によって決まるレベル。
        int heatLevel,

        // Size / Water / Heatの最小値。
        int minLevel,

        // Size / Water / Heatの最大値。
        int maxLevel,

        // Createが計測している現在の水供給量。
        float waterSupply,

        // 接続されているSteam Engine数。
        int attachedEngines,

        // 接続されているSteam Whistle数。
        int attachedWhistles

) {

    /**
     * Fluid Tankからボイラー情報を取得する。
     *
     * マルチブロックTankの一部を見ている場合でも、
     * Controllerへ正規化してから情報を取得する。
     */
    public static CreateBoilerHudData create(
            FluidTankBlockEntity tank
    ) {
        // マルチブロック構造のControllerを取得する。
        FluidTankBlockEntity controller = tank.getControllerBE();

        if(controller == null){
            return null;
        }

        BoilerData boilerData = controller.boiler;

        if(boilerData == null){
            return null;
        }

        // 通常のFluid Tankであり、
        // Boilerとして動作していない場合は対象外。
        if(!boilerData.isActive()){
            return null;
        }

        int boilerSize = controller.getTotalTankSize();

        /*
         * ボイラーサイズによって決まる最大Heat Level
         */
        int sizeLevel = boilerData.getMaxHeatLevelForBoilerSize(boilerSize);

        /*
         * 現在の水供給量によって決まる最大Heat Level。
         */
        int waterLevel = boilerData.getMaxHeatLevelForWaterSupply();

        /*
         * Heat SourceによるLevel。
         * Passive Heatの場合はCreate標準表示と同様に
         * Level 1として扱う。
         */
        int heatLevel = boilerData.passiveHeat ? 1 : boilerData.activeHeat;

        /*
         * Size / Water / Heatの中で最も低い値を取得する。
         * これがボイラー性能を制限している値になる。
         */
        int minLevel =
                Math.min(
                        heatLevel,
                        Math.min(waterLevel, sizeLevel)
                );

        /*
         * 3要素の最大値。
         * Create標準ゴーグルのバー表示でも最大値の基準として使用される。
         * 最大側の値。
         */
        int maxLevel =
                Math.max(
                        heatLevel,
                        Math.max(waterLevel, sizeLevel)
                );

        /*
         * MachineHUDで表示するボイラーレベル。
         */
        int boilerLevel = minLevel;

        return new CreateBoilerHudData(
                boilerLevel,
                sizeLevel,
                waterLevel,
                heatLevel,
                minLevel,
                maxLevel,
                boilerData.waterSupply,
                boilerData.attachedEngines,
                boilerData.attachedWhistles
        );
    }

    /**
     * ボイラーサイズが現在の性能制限になっているか。
     */
    public boolean isSizeLimited() {
        return sizeLevel == boilerLevel;
    }

    /**
     * 水供給量が現在の性能制限になっているか。
     */
    public boolean isWaterLimited() {
        return waterLevel == boilerLevel;
    }

    /**
     * 熱源が現在の性能制限になっているか。
     */
    public boolean isHeatLimited() {
        return heatLevel == boilerLevel;
    }
}