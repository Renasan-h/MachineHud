package com.rsdvlp.machinehud.hud.element;

import com.rsdvlp.machinehud.hud.HudGroup;

/**
 * Minecraft / MachineHUD共通のHUD項目。
 * Createなど、
 * 特定のMODに依存しない情報をここで管理する。
 */
public enum CommonHudElement implements HudElement {

    // 照準先ブロックのワールド座標。
    POSITION(
            "position",
            "Position",
            HudGroup.INFORMATION
    );

    private final String id;
    private final String displayName;
    private final HudGroup hudGroup;

    CommonHudElement(
            String id,
            String displayName,
            HudGroup hudGroup
    ) {
        this.id = id;
        this.displayName = displayName;
        this.hudGroup = hudGroup;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public HudGroup getHudGroup() {
        return hudGroup;
    }
}