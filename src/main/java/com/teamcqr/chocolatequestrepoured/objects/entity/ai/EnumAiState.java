package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

public enum EnumAiState {
      FOLLOW("ai.follow.name"),
      FORMATION("ai.formation.name"),
      WARD("ai.ward.name"),
      PATH("ai.path.name"),
      SIT("ai.sit.name"),
      WANDER("ai.wander.name");

      public String ainame;

      private EnumAiState(String name) {
            this.ainame = name;
      }

      public static String[] getNames() {
            EnumAiState[] states = values();
            String[] names = new String[states.length];

            /*for(int i = 0; i < states.length; ++i) {
                  names[i] = StatCollector.translateToLocal(states[i].ainame);
            }*/

            return names;
      }
}
