package com.rsdvlp.machinehud.hud.data;

/**
 * Createの回転ネットワーク全体の状態。
 */
public enum NetworkStatus {

    // Capacity内で正常に回転している。
    STABLE("Stable", 0x36c136),

    // StressがCapacityを超えてネットワークが停止している。
    OVERSTRESSED("OVERSTRESSED", 0xd03c41),

    // ネットワークは存在するが回転していない。
    STOPPED("Stopped", 0xc5c0c3);

    private final String status;
    private final int color;
    NetworkStatus(String status, int color) {
        this.status = status;
        this.color = color;
    }

    public String getStatus(){return this.status;}
    public int getColor(){return this.color;}
}