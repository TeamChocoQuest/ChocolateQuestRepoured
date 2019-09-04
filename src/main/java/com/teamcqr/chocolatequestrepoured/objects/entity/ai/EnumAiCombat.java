package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

public enum EnumAiCombat {
      OFFENSIVE("ai.offensive.name"),
      DEFENSIVE("ai.defensive.name"),
      EVASIVE("ai.evasive.name"),
      FLEE("ai.flee.name"),
      BACKSTAB("ai.backstab.name");

      public String ainame;

      private EnumAiCombat(String name) {
            this.ainame = name;
      }
}
