package com.rsdvlp.machinehud.hud.provider;

import com.rsdvlp.machinehud.hud.HudGroup;
import com.rsdvlp.machinehud.hud.HudLevel;
import com.rsdvlp.machinehud.hud.HudLine;
import com.rsdvlp.machinehud.hud.HudLineType;
import com.rsdvlp.machinehud.hud.element.CommonHudElement;
import com.rsdvlp.machinehud.hud.element.HudElement;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

/**
 * 特定MODに依存しない共通HUD情報を生成するProvider。
 */
public final class CommonHudProvider implements HudProvider{

    // 通常文字。
    private static final int TEXT_PRIMARY = 0xAAAAAA;

    private final BlockPos blockPos;

    public CommonHudProvider(BlockPos blockPos){
        this.blockPos = blockPos;
    }

    @Override
    public boolean supports(HudElement element) {
        return element instanceof CommonHudElement;
    }

    @Override
    public HudLine createLine(HudElement element) {

        if (!(element instanceof CommonHudElement commonElement)) {
            return null;
        }

        return switch (commonElement) {

            case POSITION ->
                    new HudLine(
                            Component.literal("Position"),
                            Component.literal(blockPos.getX()
                                    + ", "
                                    + blockPos.getY()
                                    + ", "
                                    + blockPos.getZ()),
                            0,
                            TEXT_PRIMARY,
                            HudLineType.VALUE,
                            HudGroup.INFORMATION,
                            null
                    );
        };
    }
}
