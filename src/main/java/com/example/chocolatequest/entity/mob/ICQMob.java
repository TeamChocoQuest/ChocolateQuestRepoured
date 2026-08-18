package com.example.chocolatequest.entity.mob;

public interface ICQMob {
    CQRoles getRole();
    void setRole(CQRoles role);
    CQRanks getRank();
    void setRank(CQRanks rank);
    
    boolean hasDrunkPotion();
    void setDrunkPotion(boolean drunk);
}
