package com.teamcqr.chocolatequestrepoured.objects.banners;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.BannerPattern;

class BannerLayer {

	 private final BannerPattern pattern;
     private final EnumDyeColor color;

     public BannerLayer (BannerPattern pattern, EnumDyeColor color) {

         this.pattern = pattern;
         this.color = color;
     }

     public BannerPattern getPattern () {

         return this.pattern;
     }

     public EnumDyeColor getColor () {

         return this.color;
     }

}
