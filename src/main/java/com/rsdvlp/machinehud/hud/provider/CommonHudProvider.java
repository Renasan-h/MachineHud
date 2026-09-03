package com.rsdvlp.machinehud.hud.provider;

import com.rsdvlp.machinehud.hud.HudLine;
import com.rsdvlp.machinehud.hud.HudLineType;
import com.rsdvlp.machinehud.hud.element.CommonHudElement;
import com.rsdvlp.machinehud.hud.element.HudElement;
import net.minecraft.core.BlockPos;

/**
 * 特定MODに依存しない共通HUD情報を生成するProvider。
 */
public final class CommonHudProvider implements HudProvider{
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
                            "Position",
                            blockPos.getX()
                                    + ", "
                                    + blockPos.getY()
                                    + ", "
                                    + blockPos.getZ(),
                            0,
                            0xAAAAAA,
                            HudLineType.VALUE,
                            null
                    );
        };
    }
}
