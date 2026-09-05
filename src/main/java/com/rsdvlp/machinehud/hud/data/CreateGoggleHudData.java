package com.rsdvlp.machinehud.hud.data;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Create標準のエンジニアゴーグル情報を取得する。
 *
 * Create側のIHaveGoggleInformationを利用することで、
 * MachineHUD側で各Create機械の標準情報を
 * 個別に再実装することを避ける。
 */
public final class CreateGoggleHudData {

    private final List<Component> toolTip;

    private CreateGoggleHudData(
            List<Component> toolTip
    ){
        this.toolTip = toolTip;
    }

    /**
     * BlockEntityからCreate標準ゴーグル情報を取得する。
     * ゴーグル情報を持たないBlockEntityの場合はnullを返す。
     */
    public static CreateGoggleHudData create(
            BlockEntity blockEntity,
            boolean isPlayerSneaking
    ){
        if(!(blockEntity instanceof IHaveGoggleInformation goggleInformation)) {
            return null;
        }

        List<Component> toolTip = new ArrayList<>();

        boolean added = goggleInformation.addToGoggleTooltip(
                toolTip, isPlayerSneaking
        );

        /*
         * Create側が情報を追加しなかった場合は
         * MachineHUD側でも標準ゴーグル情報なしとして扱う。
         */
        if(!added || toolTip.isEmpty()){
            return null;
        }

        return new CreateGoggleHudData(List.copyOf(toolTip));
    }

    /**
     * Createが生成したゴーグルTooltipを取得する。
     */
    public List<Component> getTooltip() {
        return Collections.unmodifiableList(this.toolTip);
    }
}
