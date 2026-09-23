package com.rsdvlp.machinehud.common.config;

import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public enum ConfigTab {

    MACHINEHUD("MachineHUD", null),
    CREATE("Create", "create"),
    MEKANISM("Mekanism", "mekanism");

    private final String displayName;
    private final String requiredModId;

    ConfigTab(String displayName, String requiredModId) {
        this.displayName = displayName;
        this.requiredModId = requiredModId;
    }

    public String getDisplayName() {
        return displayName;
    }

    // 対応MODが導入されているタブだけを表示する。
    public boolean isAvailable() {
        return requiredModId == null
                || ModList.get().isLoaded(requiredModId);
    }

    // 設定画面に表示するタブ一覧を生成する。
    public static List<ConfigTab> getAvailableTabs() {
        List<ConfigTab> tabs = new ArrayList<>();

        for (ConfigTab tab : values()) {
            if (tab.isAvailable()) {
                tabs.add(tab);
            }
        }

        return tabs;
    }
}