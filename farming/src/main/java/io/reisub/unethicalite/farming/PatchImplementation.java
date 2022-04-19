package io.reisub.unethicalite.farming;

import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.plugins.timetracking.Tab;
import net.runelite.client.plugins.timetracking.farming.CropState;
import net.runelite.client.plugins.timetracking.farming.Produce;

@AllArgsConstructor
@Getter
public enum PatchImplementation {
  BELLADONNA(Tab.SPECIAL, "", false) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value >= 4 && value <= 7) {
        return new PatchState(Produce.BELLADONNA, CropState.GROWING, value - 4);
      }
      if (value == 8) {
        return new PatchState(Produce.BELLADONNA, CropState.HARVESTABLE, 0);
      }
      if (value >= 9 && value <= 11) {
        return new PatchState(Produce.BELLADONNA, CropState.DISEASED, value - 8);
      }
      if (value >= 12 && value <= 14) {
        return new PatchState(Produce.BELLADONNA, CropState.DEAD, value - 11);
      }
      if (value >= 15 && value <= 255) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      return null;
    }
  },
  MUSHROOM(Tab.SPECIAL, "", false) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value >= 4 && value <= 9) {
        return new PatchState(Produce.MUSHROOM, CropState.GROWING, value - 4);
      }
      if (value >= 10 && value <= 15) {
        return new PatchState(Produce.MUSHROOM, CropState.HARVESTABLE, value - 10);
      }
      if (value >= 16 && value <= 20) {
        return new PatchState(Produce.MUSHROOM, CropState.DISEASED, value - 15);
      }
      if (value >= 21 && value <= 25) {
        return new PatchState(Produce.MUSHROOM, CropState.DEAD, value - 20);
      }
      if (value >= 26 && value <= 255) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      return null;
    }
  },
  HESPORI(Tab.SPECIAL, "", true) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value >= 4 && value <= 6) {
        return new PatchState(Produce.HESPORI, CropState.GROWING, value - 4);
      }
      if (value >= 7 && value <= 8) {
        return new PatchState(Produce.HESPORI, CropState.HARVESTABLE, value - 7);
      }
      if (value == 9) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      return null;
    }
  },
  ALLOTMENT(Tab.ALLOTMENT, "", false) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value >= 4 && value <= 5) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 6 && value <= 9) {
        return new PatchState(Produce.POTATO, CropState.GROWING, value - 6);
      }
      if (value >= 10 && value <= 12) {
        return new PatchState(Produce.POTATO, CropState.HARVESTABLE, value - 10);
      }
      if (value >= 13 && value <= 16) {
        return new PatchState(Produce.ONION, CropState.GROWING, value - 13);
      }
      if (value >= 17 && value <= 19) {
        return new PatchState(Produce.ONION, CropState.HARVESTABLE, value - 17);
      }
      if (value >= 20 && value <= 23) {
        return new PatchState(Produce.CABBAGE, CropState.GROWING, value - 20);
      }
      if (value >= 24 && value <= 26) {
        return new PatchState(Produce.CABBAGE, CropState.HARVESTABLE, value - 24);
      }
      if (value >= 27 && value <= 30) {
        return new PatchState(Produce.TOMATO, CropState.GROWING, value - 27);
      }
      if (value >= 31 && value <= 33) {
        return new PatchState(Produce.TOMATO, CropState.HARVESTABLE, value - 31);
      }
      if (value >= 34 && value <= 39) {
        return new PatchState(Produce.SWEETCORN, CropState.GROWING, value - 34);
      }
      if (value >= 40 && value <= 42) {
        return new PatchState(Produce.SWEETCORN, CropState.HARVESTABLE, value - 40);
      }
      if (value >= 43 && value <= 48) {
        return new PatchState(Produce.STRAWBERRY, CropState.GROWING, value - 43);
      }
      if (value >= 49 && value <= 51) {
        return new PatchState(Produce.STRAWBERRY, CropState.HARVESTABLE, value - 49);
      }
      if (value >= 52 && value <= 59) {
        return new PatchState(Produce.WATERMELON, CropState.GROWING, value - 52);
      }
      if (value >= 60 && value <= 62) {
        return new PatchState(Produce.WATERMELON, CropState.HARVESTABLE, value - 60);
      }
      if (value >= 63 && value <= 69) {
        return new PatchState(Produce.SNAPE_GRASS, CropState.GROWING, value - 63);
      }
      if (value >= 70 && value <= 73) {
        return new PatchState(Produce.POTATO, CropState.GROWING, value - 70);
      }
      if (value >= 74 && value <= 76) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 77 && value <= 80) {
        return new PatchState(Produce.ONION, CropState.GROWING, value - 77);
      }
      if (value >= 81 && value <= 83) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 84 && value <= 87) {
        return new PatchState(Produce.CABBAGE, CropState.GROWING, value - 84);
      }
      if (value >= 88 && value <= 90) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 91 && value <= 94) {
        return new PatchState(Produce.TOMATO, CropState.GROWING, value - 91);
      }
      if (value >= 95 && value <= 97) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 98 && value <= 103) {
        return new PatchState(Produce.SWEETCORN, CropState.GROWING, value - 98);
      }
      if (value >= 104 && value <= 106) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 107 && value <= 112) {
        return new PatchState(Produce.STRAWBERRY, CropState.GROWING, value - 107);
      }
      if (value >= 113 && value <= 115) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 116 && value <= 123) {
        return new PatchState(Produce.WATERMELON, CropState.GROWING, value - 116);
      }
      if (value >= 124 && value <= 127) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 128 && value <= 134) {
        return new PatchState(Produce.SNAPE_GRASS, CropState.GROWING, value - 128);
      }
      if (value >= 135 && value <= 137) {
        return new PatchState(Produce.POTATO, CropState.DISEASED, value - 134);
      }
      if (value >= 138 && value <= 140) {
        return new PatchState(Produce.SNAPE_GRASS, CropState.HARVESTABLE, value - 138);
      }
      if (value == 141) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 142 && value <= 144) {
        return new PatchState(Produce.ONION, CropState.DISEASED, value - 141);
      }
      if (value >= 145 && value <= 148) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 149 && value <= 151) {
        return new PatchState(Produce.CABBAGE, CropState.DISEASED, value - 148);
      }
      if (value >= 152 && value <= 155) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 156 && value <= 158) {
        return new PatchState(Produce.TOMATO, CropState.DISEASED, value - 155);
      }
      if (value >= 159 && value <= 162) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 163 && value <= 167) {
        return new PatchState(Produce.SWEETCORN, CropState.DISEASED, value - 162);
      }
      if (value >= 168 && value <= 171) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 172 && value <= 176) {
        return new PatchState(Produce.STRAWBERRY, CropState.DISEASED, value - 171);
      }
      if (value >= 177 && value <= 180) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 181 && value <= 187) {
        return new PatchState(Produce.WATERMELON, CropState.DISEASED, value - 180);
      }
      if (value >= 188 && value <= 192) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 193 && value <= 195) {
        return new PatchState(Produce.SNAPE_GRASS, CropState.DEAD, value - 192);
      }
      if (value >= 196 && value <= 198) {
        return new PatchState(Produce.SNAPE_GRASS, CropState.DISEASED, value - 195);
      }
      if (value >= 199 && value <= 201) {
        return new PatchState(Produce.POTATO, CropState.DEAD, value - 198);
      }
      if (value >= 202 && value <= 204) {
        return new PatchState(Produce.SNAPE_GRASS, CropState.DISEASED, 3 + value - 201);
      }
      if (value == 205) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 206 && value <= 208) {
        return new PatchState(Produce.ONION, CropState.DEAD, value - 205);
      }
      if (value >= 209 && value <= 211) {
        return new PatchState(Produce.SNAPE_GRASS, CropState.DEAD, 3 + value - 208);
      }
      if (value == 212) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 213 && value <= 215) {
        return new PatchState(Produce.CABBAGE, CropState.DEAD, value - 212);
      }
      if (value >= 216 && value <= 219) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 220 && value <= 222) {
        return new PatchState(Produce.TOMATO, CropState.DEAD, value - 219);
      }
      if (value >= 223 && value <= 226) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 227 && value <= 231) {
        return new PatchState(Produce.SWEETCORN, CropState.DEAD, value - 226);
      }
      if (value >= 232 && value <= 235) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 236 && value <= 240) {
        return new PatchState(Produce.STRAWBERRY, CropState.DEAD, value - 235);
      }
      if (value >= 241 && value <= 244) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 245 && value <= 251) {
        return new PatchState(Produce.WATERMELON, CropState.DEAD, value - 244);
      }
      if (value >= 252 && value <= 255) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      return null;
    }
  },
  HERB(Tab.HERB, "", false) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value >= 4 && value <= 7) {
        return new PatchState(Produce.GUAM, CropState.GROWING, value - 4);
      }
      if (value >= 8 && value <= 10) {
        return new PatchState(Produce.GUAM, CropState.HARVESTABLE, 10 - value);
      }
      if (value >= 11 && value <= 14) {
        return new PatchState(Produce.MARRENTILL, CropState.GROWING, value - 11);
      }
      if (value >= 15 && value <= 17) {
        return new PatchState(Produce.MARRENTILL, CropState.HARVESTABLE, 17 - value);
      }
      if (value >= 18 && value <= 21) {
        return new PatchState(Produce.TARROMIN, CropState.GROWING, value - 18);
      }
      if (value >= 22 && value <= 24) {
        return new PatchState(Produce.TARROMIN, CropState.HARVESTABLE, 24 - value);
      }
      if (value >= 25 && value <= 28) {
        return new PatchState(Produce.HARRALANDER, CropState.GROWING, value - 25);
      }
      if (value >= 29 && value <= 31) {
        return new PatchState(Produce.HARRALANDER, CropState.HARVESTABLE, 31 - value);
      }
      if (value >= 32 && value <= 35) {
        return new PatchState(Produce.RANARR, CropState.GROWING, value - 32);
      }
      if (value >= 36 && value <= 38) {
        return new PatchState(Produce.RANARR, CropState.HARVESTABLE, 38 - value);
      }
      if (value >= 39 && value <= 42) {
        return new PatchState(Produce.TOADFLAX, CropState.GROWING, value - 39);
      }
      if (value >= 43 && value <= 45) {
        return new PatchState(Produce.TOADFLAX, CropState.HARVESTABLE, 45 - value);
      }
      if (value >= 46 && value <= 49) {
        return new PatchState(Produce.IRIT, CropState.GROWING, value - 46);
      }
      if (value >= 50 && value <= 52) {
        return new PatchState(Produce.IRIT, CropState.HARVESTABLE, 52 - value);
      }
      if (value >= 53 && value <= 56) {
        return new PatchState(Produce.AVANTOE, CropState.GROWING, value - 53);
      }
      if (value >= 57 && value <= 59) {
        return new PatchState(Produce.AVANTOE, CropState.HARVESTABLE, 59 - value);
      }
      if (value >= 60 && value <= 67) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 68 && value <= 71) {
        return new PatchState(Produce.KWUARM, CropState.GROWING, value - 68);
      }
      if (value >= 72 && value <= 74) {
        return new PatchState(Produce.KWUARM, CropState.HARVESTABLE, 74 - value);
      }
      if (value >= 75 && value <= 78) {
        return new PatchState(Produce.SNAPDRAGON, CropState.GROWING, value - 75);
      }
      if (value >= 79 && value <= 81) {
        return new PatchState(Produce.SNAPDRAGON, CropState.HARVESTABLE, 81 - value);
      }
      if (value >= 82 && value <= 85) {
        return new PatchState(Produce.CADANTINE, CropState.GROWING, value - 82);
      }
      if (value >= 86 && value <= 88) {
        return new PatchState(Produce.CADANTINE, CropState.HARVESTABLE, 88 - value);
      }
      if (value >= 89 && value <= 92) {
        return new PatchState(Produce.LANTADYME, CropState.GROWING, value - 89);
      }
      if (value >= 93 && value <= 95) {
        return new PatchState(Produce.LANTADYME, CropState.HARVESTABLE, 95 - value);
      }
      if (value >= 96 && value <= 99) {
        return new PatchState(Produce.DWARF_WEED, CropState.GROWING, value - 96);
      }
      if (value >= 100 && value <= 102) {
        return new PatchState(Produce.DWARF_WEED, CropState.HARVESTABLE, 102 - value);
      }
      if (value >= 103 && value <= 106) {
        return new PatchState(Produce.TORSTOL, CropState.GROWING, value - 103);
      }
      if (value >= 107 && value <= 109) {
        return new PatchState(Produce.TORSTOL, CropState.HARVESTABLE, 109 - value);
      }
      if (value >= 128 && value <= 130) {
        return new PatchState(Produce.GUAM, CropState.DISEASED, value - 127);
      }
      if (value >= 131 && value <= 133) {
        return new PatchState(Produce.MARRENTILL, CropState.DISEASED, value - 130);
      }
      if (value >= 134 && value <= 136) {
        return new PatchState(Produce.TARROMIN, CropState.DISEASED, value - 133);
      }
      if (value >= 137 && value <= 139) {
        return new PatchState(Produce.HARRALANDER, CropState.DISEASED, value - 136);
      }
      if (value >= 140 && value <= 142) {
        return new PatchState(Produce.RANARR, CropState.DISEASED, value - 139);
      }
      if (value >= 143 && value <= 145) {
        return new PatchState(Produce.TOADFLAX, CropState.DISEASED, value - 142);
      }
      if (value >= 146 && value <= 148) {
        return new PatchState(Produce.IRIT, CropState.DISEASED, value - 145);
      }
      if (value >= 149 && value <= 151) {
        return new PatchState(Produce.AVANTOE, CropState.DISEASED, value - 148);
      }
      if (value >= 152 && value <= 154) {
        return new PatchState(Produce.KWUARM, CropState.DISEASED, value - 151);
      }
      if (value >= 155 && value <= 157) {
        return new PatchState(Produce.SNAPDRAGON, CropState.DISEASED, value - 154);
      }
      if (value >= 158 && value <= 160) {
        return new PatchState(Produce.CADANTINE, CropState.DISEASED, value - 157);
      }
      if (value >= 161 && value <= 163) {
        return new PatchState(Produce.LANTADYME, CropState.DISEASED, value - 160);
      }
      if (value >= 164 && value <= 166) {
        return new PatchState(Produce.DWARF_WEED, CropState.DISEASED, value - 163);
      }
      if (value >= 167 && value <= 169) {
        return new PatchState(Produce.TORSTOL, CropState.DISEASED, value - 166);
      }
      if (value >= 170 && value <= 172) {
        return new PatchState(Produce.ANYHERB, CropState.DEAD, value - 169);
      }
      if (value >= 173 && value <= 191) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 192 && value <= 195) {
        return new PatchState(Produce.GOUTWEED, CropState.GROWING, value - 192);
      }
      if (value >= 196 && value <= 197) {
        return new PatchState(Produce.GOUTWEED, CropState.HARVESTABLE, 197 - value);
      }
      if (value >= 198 && value <= 200) {
        return new PatchState(Produce.GOUTWEED, CropState.DISEASED, value - 197);
      }
      if (value >= 201 && value <= 203) {
        return new PatchState(Produce.GOUTWEED, CropState.DEAD, value - 200);
      }
      if (value >= 204 && value <= 219) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 221 && value <= 255) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      return null;
    }
  },
  FLOWER(Tab.FLOWER, "", false) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value >= 4 && value <= 7) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 8 && value <= 11) {
        return new PatchState(Produce.MARIGOLD, CropState.GROWING, value - 8);
      }
      if (value == 12) {
        return new PatchState(Produce.MARIGOLD, CropState.HARVESTABLE, 0);
      }
      if (value >= 13 && value <= 16) {
        return new PatchState(Produce.ROSEMARY, CropState.GROWING, value - 13);
      }
      if (value == 17) {
        return new PatchState(Produce.ROSEMARY, CropState.HARVESTABLE, 0);
      }
      if (value >= 18 && value <= 21) {
        return new PatchState(Produce.NASTURTIUM, CropState.GROWING, value - 18);
      }
      if (value == 22) {
        return new PatchState(Produce.NASTURTIUM, CropState.HARVESTABLE, 0);
      }
      if (value >= 23 && value <= 26) {
        return new PatchState(Produce.WOAD, CropState.GROWING, value - 23);
      }
      if (value == 27) {
        return new PatchState(Produce.WOAD, CropState.HARVESTABLE, 0);
      }
      if (value >= 28 && value <= 31) {
        return new PatchState(Produce.LIMPWURT, CropState.GROWING, value - 28);
      }
      if (value == 32) {
        return new PatchState(Produce.LIMPWURT, CropState.HARVESTABLE, 0);
      }
      if (value >= 33 && value <= 35) {
        return new PatchState(Produce.SCARECROW, CropState.GROWING, 35 - value);
      }
      if (value == 36) {
        return new PatchState(Produce.SCARECROW, CropState.GROWING, 0);
      }
      if (value >= 37 && value <= 40) {
        return new PatchState(Produce.WHITE_LILY, CropState.GROWING, value - 37);
      }
      if (value == 41) {
        return new PatchState(Produce.WHITE_LILY, CropState.HARVESTABLE, 0);
      }
      if (value >= 42 && value <= 71) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 72 && value <= 75) {
        return new PatchState(Produce.MARIGOLD, CropState.GROWING, value - 72);
      }
      if (value == 76) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 77 && value <= 80) {
        return new PatchState(Produce.ROSEMARY, CropState.GROWING, value - 77);
      }
      if (value == 81) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 82 && value <= 85) {
        return new PatchState(Produce.NASTURTIUM, CropState.GROWING, value - 82);
      }
      if (value == 86) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 87 && value <= 90) {
        return new PatchState(Produce.WOAD, CropState.GROWING, value - 87);
      }
      if (value == 91) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 92 && value <= 95) {
        return new PatchState(Produce.LIMPWURT, CropState.GROWING, value - 92);
      }
      if (value >= 96 && value <= 100) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 101 && value <= 104) {
        return new PatchState(Produce.WHITE_LILY, CropState.GROWING, value - 101);
      }
      if (value >= 105 && value <= 136) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 137 && value <= 139) {
        return new PatchState(Produce.MARIGOLD, CropState.DISEASED, value - 136);
      }
      if (value >= 140 && value <= 141) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 142 && value <= 144) {
        return new PatchState(Produce.ROSEMARY, CropState.DISEASED, value - 141);
      }
      if (value >= 145 && value <= 146) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 147 && value <= 149) {
        return new PatchState(Produce.NASTURTIUM, CropState.DISEASED, value - 146);
      }
      if (value >= 150 && value <= 151) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 152 && value <= 154) {
        return new PatchState(Produce.WOAD, CropState.DISEASED, value - 151);
      }
      if (value >= 155 && value <= 156) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 157 && value <= 159) {
        return new PatchState(Produce.LIMPWURT, CropState.DISEASED, value - 156);
      }
      if (value >= 160 && value <= 165) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 166 && value <= 168) {
        return new PatchState(Produce.WHITE_LILY, CropState.DISEASED, value - 165);
      }
      if (value >= 169 && value <= 200) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 201 && value <= 204) {
        return new PatchState(Produce.MARIGOLD, CropState.DEAD, value - 200);
      }
      if (value == 205) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 206 && value <= 209) {
        return new PatchState(Produce.ROSEMARY, CropState.DEAD, value - 205);
      }
      if (value == 210) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 211 && value <= 214) {
        return new PatchState(Produce.NASTURTIUM, CropState.DEAD, value - 210);
      }
      if (value == 215) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 216 && value <= 219) {
        return new PatchState(Produce.WOAD, CropState.DEAD, value - 215);
      }
      if (value == 220) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 221 && value <= 224) {
        return new PatchState(Produce.LIMPWURT, CropState.DEAD, value - 220);
      }
      if (value >= 225 && value <= 229) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 230 && value <= 233) {
        return new PatchState(Produce.WHITE_LILY, CropState.DEAD, value - 229);
      }
      if (value >= 234 && value <= 255) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      return null;
    }
  },
  BUSH(Tab.BUSH, "", true) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value == 4) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 5 && value <= 9) {
        return new PatchState(Produce.REDBERRIES, CropState.GROWING, value - 5);
      }
      if (value >= 10 && value <= 14) {
        return new PatchState(Produce.REDBERRIES, CropState.HARVESTABLE, value - 10);
      }
      if (value >= 15 && value <= 20) {
        return new PatchState(Produce.CADAVABERRIES, CropState.GROWING, value - 15);
      }
      if (value >= 21 && value <= 25) {
        return new PatchState(Produce.CADAVABERRIES, CropState.HARVESTABLE, value - 21);
      }
      if (value >= 26 && value <= 32) {
        return new PatchState(Produce.DWELLBERRIES, CropState.GROWING, value - 26);
      }
      if (value >= 33 && value <= 37) {
        return new PatchState(Produce.DWELLBERRIES, CropState.HARVESTABLE, value - 33);
      }
      if (value >= 38 && value <= 45) {
        return new PatchState(Produce.JANGERBERRIES, CropState.GROWING, value - 38);
      }
      if (value >= 46 && value <= 50) {
        return new PatchState(Produce.JANGERBERRIES, CropState.HARVESTABLE, value - 46);
      }
      if (value >= 51 && value <= 58) {
        return new PatchState(Produce.WHITEBERRIES, CropState.GROWING, value - 51);
      }
      if (value >= 59 && value <= 63) {
        return new PatchState(Produce.WHITEBERRIES, CropState.HARVESTABLE, value - 59);
      }
      if (value >= 64 && value <= 69) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 70 && value <= 74) {
        return new PatchState(Produce.REDBERRIES, CropState.DISEASED, value - 69);
      }
      if (value >= 75 && value <= 79) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 80 && value <= 85) {
        return new PatchState(Produce.CADAVABERRIES, CropState.DISEASED, value - 79);
      }
      if (value >= 86 && value <= 90) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 91 && value <= 97) {
        return new PatchState(Produce.DWELLBERRIES, CropState.DISEASED, value - 90);
      }
      if (value >= 98 && value <= 102) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 103 && value <= 110) {
        return new PatchState(Produce.JANGERBERRIES, CropState.DISEASED, value - 102);
      }
      if (value >= 111 && value <= 115) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 116 && value <= 123) {
        return new PatchState(Produce.WHITEBERRIES, CropState.DISEASED, value - 115);
      }
      if (value >= 124 && value <= 133) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 134 && value <= 138) {
        return new PatchState(Produce.REDBERRIES, CropState.DEAD, value - 133);
      }
      if (value >= 139 && value <= 143) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 144 && value <= 149) {
        return new PatchState(Produce.CADAVABERRIES, CropState.DEAD, value - 143);
      }
      if (value >= 150 && value <= 154) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 155 && value <= 161) {
        return new PatchState(Produce.DWELLBERRIES, CropState.DEAD, value - 154);
      }
      if (value >= 162 && value <= 166) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 167 && value <= 174) {
        return new PatchState(Produce.JANGERBERRIES, CropState.DEAD, value - 166);
      }
      if (value >= 175 && value <= 179) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 180 && value <= 187) {
        return new PatchState(Produce.WHITEBERRIES, CropState.DEAD, value - 179);
      }
      if (value >= 188 && value <= 196) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 197 && value <= 204) {
        return new PatchState(Produce.POISON_IVY, CropState.GROWING, value - 197);
      }
      if (value >= 205 && value <= 209) {
        return new PatchState(Produce.POISON_IVY, CropState.HARVESTABLE, value - 205);
      }
      if (value >= 210 && value <= 216) {
        return new PatchState(Produce.POISON_IVY, CropState.DISEASED, value - 209);
      }
      if (value >= 217 && value <= 224) {
        return new PatchState(Produce.POISON_IVY, CropState.DEAD, value - 216);
      }
      if (value == 225) {
        return new PatchState(Produce.POISON_IVY, CropState.DISEASED, 8);
      }
      if (value >= 226 && value <= 249) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value == 250) {
        return new PatchState(
            Produce.REDBERRIES, CropState.GROWING, Produce.REDBERRIES.getStages() - 1);
      }
      if (value == 251) {
        return new PatchState(
            Produce.CADAVABERRIES, CropState.GROWING, Produce.CADAVABERRIES.getStages() - 1);
      }
      if (value == 252) {
        return new PatchState(
            Produce.DWELLBERRIES, CropState.GROWING, Produce.DWELLBERRIES.getStages() - 1);
      }
      if (value == 253) {
        return new PatchState(
            Produce.JANGERBERRIES, CropState.GROWING, Produce.JANGERBERRIES.getStages() - 1);
      }
      if (value == 254) {
        return new PatchState(
            Produce.WHITEBERRIES, CropState.GROWING, Produce.WHITEBERRIES.getStages() - 1);
      }
      if (value == 255) {
        return new PatchState(
            Produce.POISON_IVY, CropState.GROWING, Produce.POISON_IVY.getStages() - 1);
      }
      return null;
    }
  },
  FRUIT_TREE(Tab.FRUIT_TREE, "", true) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value >= 4 && value <= 7) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 8 && value <= 13) {
        return new PatchState(Produce.APPLE, CropState.GROWING, value - 8);
      }
      if (value >= 14 && value <= 20) {
        return new PatchState(Produce.APPLE, CropState.HARVESTABLE, value - 14);
      }
      if (value >= 21 && value <= 26) {
        return new PatchState(Produce.APPLE, CropState.DISEASED, value - 20);
      }
      if (value >= 27 && value <= 32) {
        return new PatchState(Produce.APPLE, CropState.DEAD, value - 26);
      }
      if (value == 33) {
        return new PatchState(Produce.APPLE, CropState.HARVESTABLE, 0);
      }
      if (value == 34) {
        return new PatchState(Produce.APPLE, CropState.GROWING, Produce.APPLE.getStages() - 1);
      }
      if (value >= 35 && value <= 40) {
        return new PatchState(Produce.BANANA, CropState.GROWING, value - 35);
      }
      if (value >= 41 && value <= 47) {
        return new PatchState(Produce.BANANA, CropState.HARVESTABLE, value - 41);
      }
      if (value >= 48 && value <= 53) {
        return new PatchState(Produce.BANANA, CropState.DISEASED, value - 47);
      }
      if (value >= 54 && value <= 59) {
        return new PatchState(Produce.BANANA, CropState.DEAD, value - 53);
      }
      if (value == 60) {
        return new PatchState(Produce.BANANA, CropState.HARVESTABLE, 0);
      }
      if (value == 61) {
        return new PatchState(Produce.BANANA, CropState.GROWING, Produce.BANANA.getStages() - 1);
      }
      if (value >= 62 && value <= 71) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 72 && value <= 77) {
        return new PatchState(Produce.ORANGE, CropState.GROWING, value - 72);
      }
      if (value >= 78 && value <= 84) {
        return new PatchState(Produce.ORANGE, CropState.HARVESTABLE, value - 78);
      }
      if (value >= 85 && value <= 89) {
        return new PatchState(Produce.ORANGE, CropState.DISEASED, value - 84);
      }
      if (value == 90) {
        return new PatchState(Produce.ORANGE, CropState.DISEASED, 6);
      }
      if (value >= 91 && value <= 96) {
        return new PatchState(Produce.ORANGE, CropState.DEAD, value - 90);
      }
      if (value == 97) {
        return new PatchState(Produce.ORANGE, CropState.HARVESTABLE, 0);
      }
      if (value == 98) {
        return new PatchState(Produce.ORANGE, CropState.GROWING, Produce.ORANGE.getStages() - 1);
      }
      if (value >= 99 && value <= 104) {
        return new PatchState(Produce.CURRY, CropState.GROWING, value - 99);
      }
      if (value >= 105 && value <= 111) {
        return new PatchState(Produce.CURRY, CropState.HARVESTABLE, value - 105);
      }
      if (value >= 112 && value <= 117) {
        return new PatchState(Produce.CURRY, CropState.DISEASED, value - 111);
      }
      if (value >= 118 && value <= 123) {
        return new PatchState(Produce.CURRY, CropState.DEAD, value - 117);
      }
      if (value == 124) {
        return new PatchState(Produce.CURRY, CropState.HARVESTABLE, 0);
      }
      if (value == 125) {
        return new PatchState(Produce.CURRY, CropState.GROWING, Produce.CURRY.getStages() - 1);
      }
      if (value >= 126 && value <= 135) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 136 && value <= 141) {
        return new PatchState(Produce.PINEAPPLE, CropState.GROWING, value - 136);
      }
      if (value >= 142 && value <= 148) {
        return new PatchState(Produce.PINEAPPLE, CropState.HARVESTABLE, value - 142);
      }
      if (value >= 149 && value <= 154) {
        return new PatchState(Produce.PINEAPPLE, CropState.DISEASED, value - 148);
      }
      if (value >= 155 && value <= 160) {
        return new PatchState(Produce.PINEAPPLE, CropState.DEAD, value - 154);
      }
      if (value == 161) {
        return new PatchState(Produce.PINEAPPLE, CropState.HARVESTABLE, 0);
      }
      if (value == 162) {
        return new PatchState(
            Produce.PINEAPPLE, CropState.GROWING, Produce.PINEAPPLE.getStages() - 1);
      }
      if (value >= 163 && value <= 168) {
        return new PatchState(Produce.PAPAYA, CropState.GROWING, value - 163);
      }
      if (value >= 169 && value <= 175) {
        return new PatchState(Produce.PAPAYA, CropState.HARVESTABLE, value - 169);
      }
      if (value >= 176 && value <= 181) {
        return new PatchState(Produce.PAPAYA, CropState.DISEASED, value - 175);
      }
      if (value >= 182 && value <= 187) {
        return new PatchState(Produce.PAPAYA, CropState.DEAD, value - 181);
      }
      if (value == 188) {
        return new PatchState(Produce.PAPAYA, CropState.HARVESTABLE, 0);
      }
      if (value == 189) {
        return new PatchState(Produce.PAPAYA, CropState.GROWING, Produce.PAPAYA.getStages() - 1);
      }
      if (value >= 190 && value <= 199) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 200 && value <= 205) {
        return new PatchState(Produce.PALM, CropState.GROWING, value - 200);
      }
      if (value >= 206 && value <= 212) {
        return new PatchState(Produce.PALM, CropState.HARVESTABLE, value - 206);
      }
      if (value >= 213 && value <= 218) {
        return new PatchState(Produce.PALM, CropState.DISEASED, value - 212);
      }
      if (value >= 219 && value <= 224) {
        return new PatchState(Produce.PALM, CropState.DEAD, value - 218);
      }
      if (value == 225) {
        return new PatchState(Produce.PALM, CropState.HARVESTABLE, 0);
      }
      if (value == 226) {
        return new PatchState(Produce.PALM, CropState.GROWING, Produce.PALM.getStages() - 1);
      }
      if (value >= 227 && value <= 232) {
        return new PatchState(Produce.DRAGONFRUIT, CropState.GROWING, value - 227);
      }
      if (value >= 233 && value <= 239) {
        return new PatchState(Produce.DRAGONFRUIT, CropState.HARVESTABLE, value - 233);
      }
      if (value >= 240 && value <= 245) {
        return new PatchState(Produce.DRAGONFRUIT, CropState.DISEASED, value - 239);
      }
      if (value >= 246 && value <= 251) {
        return new PatchState(Produce.DRAGONFRUIT, CropState.DEAD, value - 245);
      }
      if (value == 252) {
        return new PatchState(Produce.DRAGONFRUIT, CropState.HARVESTABLE, 0);
      }
      if (value == 253) {
        return new PatchState(
            Produce.DRAGONFRUIT, CropState.GROWING, Produce.DRAGONFRUIT.getStages() - 1);
      }
      if (value >= 254 && value <= 255) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      return null;
    }
  },
  HOPS(Tab.HOPS, "", false) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value >= 4 && value <= 7) {
        return new PatchState(Produce.HAMMERSTONE, CropState.GROWING, value - 4);
      }
      if (value >= 8 && value <= 10) {
        return new PatchState(Produce.HAMMERSTONE, CropState.HARVESTABLE, value - 8);
      }
      if (value >= 11 && value <= 15) {
        return new PatchState(Produce.ASGARNIAN, CropState.GROWING, value - 11);
      }
      if (value >= 16 && value <= 18) {
        return new PatchState(Produce.ASGARNIAN, CropState.HARVESTABLE, value - 16);
      }
      if (value >= 19 && value <= 24) {
        return new PatchState(Produce.YANILLIAN, CropState.GROWING, value - 19);
      }
      if (value >= 25 && value <= 27) {
        return new PatchState(Produce.YANILLIAN, CropState.HARVESTABLE, value - 25);
      }
      if (value >= 28 && value <= 34) {
        return new PatchState(Produce.KRANDORIAN, CropState.GROWING, value - 28);
      }
      if (value >= 35 && value <= 37) {
        return new PatchState(Produce.KRANDORIAN, CropState.HARVESTABLE, value - 35);
      }
      if (value >= 38 && value <= 45) {
        return new PatchState(Produce.WILDBLOOD, CropState.GROWING, value - 38);
      }
      if (value >= 46 && value <= 48) {
        return new PatchState(Produce.WILDBLOOD, CropState.HARVESTABLE, value - 46);
      }
      if (value >= 49 && value <= 52) {
        return new PatchState(Produce.BARLEY, CropState.GROWING, value - 49);
      }
      if (value >= 53 && value <= 55) {
        return new PatchState(Produce.BARLEY, CropState.HARVESTABLE, value - 53);
      }
      if (value >= 56 && value <= 60) {
        return new PatchState(Produce.JUTE, CropState.GROWING, value - 56);
      }
      if (value >= 61 && value <= 63) {
        return new PatchState(Produce.JUTE, CropState.HARVESTABLE, value - 61);
      }
      if (value >= 64 && value <= 67) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 68 && value <= 71) {
        return new PatchState(Produce.HAMMERSTONE, CropState.GROWING, value - 68);
      }
      if (value >= 72 && value <= 74) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 75 && value <= 79) {
        return new PatchState(Produce.ASGARNIAN, CropState.GROWING, value - 75);
      }
      if (value >= 80 && value <= 82) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 83 && value <= 88) {
        return new PatchState(Produce.YANILLIAN, CropState.GROWING, value - 83);
      }
      if (value >= 89 && value <= 91) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 92 && value <= 98) {
        return new PatchState(Produce.KRANDORIAN, CropState.GROWING, value - 92);
      }
      if (value >= 99 && value <= 101) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 102 && value <= 109) {
        return new PatchState(Produce.WILDBLOOD, CropState.GROWING, value - 102);
      }
      if (value >= 110 && value <= 112) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 113 && value <= 116) {
        return new PatchState(Produce.BARLEY, CropState.GROWING, value - 113);
      }
      if (value >= 117 && value <= 119) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 120 && value <= 124) {
        return new PatchState(Produce.JUTE, CropState.GROWING, value - 120);
      }
      if (value >= 125 && value <= 132) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 133 && value <= 135) {
        return new PatchState(Produce.HAMMERSTONE, CropState.DISEASED, value - 132);
      }
      if (value >= 136 && value <= 139) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 140 && value <= 143) {
        return new PatchState(Produce.ASGARNIAN, CropState.DISEASED, value - 139);
      }
      if (value >= 144 && value <= 147) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 148 && value <= 152) {
        return new PatchState(Produce.YANILLIAN, CropState.DISEASED, value - 147);
      }
      if (value >= 153 && value <= 156) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 157 && value <= 162) {
        return new PatchState(Produce.KRANDORIAN, CropState.DISEASED, value - 156);
      }
      if (value >= 163 && value <= 166) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 167 && value <= 173) {
        return new PatchState(Produce.WILDBLOOD, CropState.DISEASED, value - 166);
      }
      if (value >= 174 && value <= 177) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 178 && value <= 180) {
        return new PatchState(Produce.BARLEY, CropState.DISEASED, value - 177);
      }
      if (value == 181) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 183 && value <= 184) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 185 && value <= 188) {
        return new PatchState(Produce.JUTE, CropState.DISEASED, value - 184);
      }
      if (value >= 189 && value <= 196) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 197 && value <= 199) {
        return new PatchState(Produce.HAMMERSTONE, CropState.DEAD, value - 196);
      }
      if (value >= 200 && value <= 203) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 204 && value <= 207) {
        return new PatchState(Produce.ASGARNIAN, CropState.DEAD, value - 203);
      }
      if (value >= 208 && value <= 211) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 212 && value <= 216) {
        return new PatchState(Produce.YANILLIAN, CropState.DEAD, value - 211);
      }
      if (value >= 217 && value <= 220) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 221 && value <= 226) {
        return new PatchState(Produce.KRANDORIAN, CropState.DEAD, value - 220);
      }
      if (value >= 227 && value <= 230) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 231 && value <= 237) {
        return new PatchState(Produce.WILDBLOOD, CropState.DEAD, value - 230);
      }
      if (value >= 238 && value <= 241) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 242 && value <= 244) {
        return new PatchState(Produce.BARLEY, CropState.DEAD, value - 241);
      }
      if (value >= 245 && value <= 248) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 249 && value <= 252) {
        return new PatchState(Produce.JUTE, CropState.DEAD, value - 248);
      }
      if (value >= 253 && value <= 255) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      return null;
    }
  },
  TREE(Tab.TREE, "", true) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value >= 4 && value <= 7) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 8 && value <= 11) {
        return new PatchState(Produce.OAK, CropState.GROWING, value - 8);
      }
      if (value == 12) {
        return new PatchState(Produce.OAK, CropState.GROWING, Produce.OAK.getStages() - 1);
      }
      if (value == 13) {
        return new PatchState(Produce.OAK, CropState.HARVESTABLE, 0);
      }
      if (value == 14) {
        return new PatchState(Produce.OAK, CropState.HARVESTABLE, 0);
      }
      if (value >= 15 && value <= 20) {
        return new PatchState(Produce.WILLOW, CropState.GROWING, value - 15);
      }
      if (value == 21) {
        return new PatchState(Produce.WILLOW, CropState.GROWING, Produce.WILLOW.getStages() - 1);
      }
      if (value == 22) {
        return new PatchState(Produce.WILLOW, CropState.HARVESTABLE, 0);
      }
      if (value == 23) {
        return new PatchState(Produce.WILLOW, CropState.HARVESTABLE, 0);
      }
      if (value >= 24 && value <= 31) {
        return new PatchState(Produce.MAPLE, CropState.GROWING, value - 24);
      }
      if (value == 32) {
        return new PatchState(Produce.MAPLE, CropState.GROWING, Produce.MAPLE.getStages() - 1);
      }
      if (value == 33) {
        return new PatchState(Produce.MAPLE, CropState.HARVESTABLE, 0);
      }
      if (value == 34) {
        return new PatchState(Produce.MAPLE, CropState.HARVESTABLE, 0);
      }
      if (value >= 35 && value <= 44) {
        return new PatchState(Produce.YEW, CropState.GROWING, value - 35);
      }
      if (value == 45) {
        return new PatchState(Produce.YEW, CropState.GROWING, Produce.YEW.getStages() - 1);
      }
      if (value == 46) {
        return new PatchState(Produce.YEW, CropState.HARVESTABLE, 0);
      }
      if (value == 47) {
        return new PatchState(Produce.YEW, CropState.HARVESTABLE, 0);
      }
      if (value >= 48 && value <= 59) {
        return new PatchState(Produce.MAGIC, CropState.GROWING, value - 48);
      }
      if (value == 60) {
        return new PatchState(Produce.MAGIC, CropState.GROWING, Produce.MAGIC.getStages() - 1);
      }
      if (value == 61) {
        return new PatchState(Produce.MAGIC, CropState.HARVESTABLE, 0);
      }
      if (value == 62) {
        return new PatchState(Produce.MAGIC, CropState.HARVESTABLE, 0);
      }
      if (value >= 63 && value <= 72) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 73 && value <= 75) {
        return new PatchState(Produce.OAK, CropState.DISEASED, value - 72);
      }
      if (value == 77) {
        return new PatchState(Produce.OAK, CropState.DISEASED, 4);
      }
      if (value >= 78 && value <= 79) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 80 && value <= 84) {
        return new PatchState(Produce.WILLOW, CropState.DISEASED, value - 79);
      }
      if (value == 86) {
        return new PatchState(Produce.WILLOW, CropState.DISEASED, 6);
      }
      if (value >= 87 && value <= 88) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 89 && value <= 95) {
        return new PatchState(Produce.MAPLE, CropState.DISEASED, value - 88);
      }
      if (value == 97) {
        return new PatchState(Produce.MAPLE, CropState.DISEASED, 8);
      }
      if (value >= 98 && value <= 99) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 100 && value <= 108) {
        return new PatchState(Produce.YEW, CropState.DISEASED, value - 99);
      }
      if (value == 110) {
        return new PatchState(Produce.YEW, CropState.DISEASED, 10);
      }
      if (value >= 111 && value <= 112) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 113 && value <= 123) {
        return new PatchState(Produce.MAGIC, CropState.DISEASED, value - 112);
      }
      if (value == 125) {
        return new PatchState(Produce.MAGIC, CropState.DISEASED, 12);
      }
      if (value >= 126 && value <= 136) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 137 && value <= 139) {
        return new PatchState(Produce.OAK, CropState.DEAD, value - 136);
      }
      if (value == 141) {
        return new PatchState(Produce.OAK, CropState.DEAD, 4);
      }
      if (value >= 142 && value <= 143) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 144 && value <= 148) {
        return new PatchState(Produce.WILLOW, CropState.DEAD, value - 143);
      }
      if (value == 150) {
        return new PatchState(Produce.WILLOW, CropState.DEAD, 6);
      }
      if (value >= 151 && value <= 152) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 153 && value <= 159) {
        return new PatchState(Produce.MAPLE, CropState.DEAD, value - 152);
      }
      if (value == 161) {
        return new PatchState(Produce.MAPLE, CropState.DEAD, 8);
      }
      if (value >= 162 && value <= 163) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 164 && value <= 172) {
        return new PatchState(Produce.YEW, CropState.DEAD, value - 163);
      }
      if (value == 174) {
        return new PatchState(Produce.YEW, CropState.DEAD, 10);
      }
      if (value >= 175 && value <= 176) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 177 && value <= 187) {
        return new PatchState(Produce.MAGIC, CropState.DEAD, value - 176);
      }
      if (value == 189) {
        return new PatchState(Produce.MAGIC, CropState.DEAD, 12);
      }
      if (value >= 190 && value <= 191) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 192 && value <= 197) {
        return new PatchState(Produce.WILLOW, CropState.HARVESTABLE, 0);
      }
      if (value >= 198 && value <= 255) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      return null;
    }
  },
  HARDWOOD_TREE(Tab.TREE, "Hardwood Trees", true) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value >= 4 && value <= 7) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 8 && value <= 14) {
        return new PatchState(Produce.TEAK, CropState.GROWING, value - 8);
      }
      if (value == 15) {
        return new PatchState(Produce.TEAK, CropState.GROWING, Produce.TEAK.getStages() - 1);
      }
      if (value == 16) {
        return new PatchState(Produce.TEAK, CropState.HARVESTABLE, 0);
      }
      if (value == 17) {
        return new PatchState(Produce.TEAK, CropState.HARVESTABLE, 0);
      }
      if (value >= 18 && value <= 23) {
        return new PatchState(Produce.TEAK, CropState.DISEASED, value - 17);
      }
      if (value >= 24 && value <= 29) {
        return new PatchState(Produce.TEAK, CropState.DEAD, value - 23);
      }
      if (value >= 30 && value <= 37) {
        return new PatchState(Produce.MAHOGANY, CropState.GROWING, value - 30);
      }
      if (value == 38) {
        return new PatchState(
            Produce.MAHOGANY, CropState.GROWING, Produce.MAHOGANY.getStages() - 1);
      }
      if (value == 39) {
        return new PatchState(Produce.MAHOGANY, CropState.HARVESTABLE, 0);
      }
      if (value == 40) {
        return new PatchState(Produce.MAHOGANY, CropState.HARVESTABLE, 0);
      }
      if (value >= 41 && value <= 47) {
        return new PatchState(Produce.MAHOGANY, CropState.DISEASED, value - 40);
      }
      if (value >= 48 && value <= 54) {
        return new PatchState(Produce.MAHOGANY, CropState.DEAD, value - 47);
      }
      if (value >= 55 && value <= 255) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      return null;
    }
  },
  REDWOOD(Tab.TREE, "Redwood Trees", true) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value >= 4 && value <= 7) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 8 && value <= 17) {
        return new PatchState(Produce.REDWOOD, CropState.GROWING, value - 8);
      }
      if (value == 18) {
        return new PatchState(Produce.REDWOOD, CropState.HARVESTABLE, 0);
      }
      if (value >= 19 && value <= 27) {
        return new PatchState(Produce.REDWOOD, CropState.DISEASED, value - 18);
      }
      if (value >= 28 && value <= 36) {
        return new PatchState(Produce.REDWOOD, CropState.DEAD, value - 27);
      }
      if (value == 37) {
        return new PatchState(Produce.REDWOOD, CropState.GROWING, Produce.REDWOOD.getStages() - 1);
      }
      if (value >= 41 && value <= 55) {
        return new PatchState(Produce.REDWOOD, CropState.HARVESTABLE, 0);
      }
      return null;
    }
  },
  SPIRIT_TREE(Tab.TREE, "Spirit Trees", true) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value >= 4 && value <= 7) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 8 && value <= 19) {
        return new PatchState(Produce.SPIRIT_TREE, CropState.GROWING, value - 8);
      }
      if (value == 20) {
        return new PatchState(Produce.SPIRIT_TREE, CropState.GROWING, 12);
      }
      if (value >= 21 && value <= 31) {
        return new PatchState(Produce.SPIRIT_TREE, CropState.DISEASED, value - 20);
      }
      if (value >= 32 && value <= 43) {
        return new PatchState(Produce.SPIRIT_TREE, CropState.DEAD, value - 31);
      }
      if (value == 44) {
        return new PatchState(
            Produce.SPIRIT_TREE, CropState.GROWING, Produce.SPIRIT_TREE.getStages() - 1);
      }
      if (value >= 45 && value <= 63) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      return null;
    }
  },
  ANIMA(Tab.SPECIAL, "", false) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value >= 4 && value <= 7) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 8 && value <= 16) {
        return new PatchState(Produce.ATTAS, CropState.GROWING, value - 8);
      }
      if (value >= 17 && value <= 25) {
        return new PatchState(Produce.IASOR, CropState.GROWING, value - 17);
      }
      if (value >= 26 && value <= 34) {
        return new PatchState(Produce.KRONOS, CropState.GROWING, value - 26);
      }
      if (value >= 35 && value <= 255) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      return null;
    }
  },
  CACTUS(Tab.SPECIAL, "Cactus", true) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value >= 4 && value <= 7) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 8 && value <= 14) {
        return new PatchState(Produce.CACTUS, CropState.GROWING, value - 8);
      }
      if (value >= 15 && value <= 18) {
        return new PatchState(Produce.CACTUS, CropState.HARVESTABLE, value - 15);
      }
      if (value >= 19 && value <= 24) {
        return new PatchState(Produce.CACTUS, CropState.DISEASED, value - 18);
      }
      if (value >= 25 && value <= 30) {
        return new PatchState(Produce.CACTUS, CropState.DEAD, value - 24);
      }
      if (value == 31) {
        return new PatchState(Produce.CACTUS, CropState.GROWING, Produce.CACTUS.getStages() - 1);
      }
      if (value >= 32 && value <= 38) {
        return new PatchState(Produce.POTATO_CACTUS, CropState.GROWING, value - 32);
      }
      if (value >= 39 && value <= 45) {
        return new PatchState(Produce.POTATO_CACTUS, CropState.HARVESTABLE, value - 39);
      }
      if (value >= 46 && value <= 51) {
        return new PatchState(Produce.POTATO_CACTUS, CropState.DISEASED, value - 45);
      }
      if (value >= 52 && value <= 57) {
        return new PatchState(Produce.POTATO_CACTUS, CropState.DEAD, value - 51);
      }
      if (value == 58) {
        return new PatchState(
            Produce.POTATO_CACTUS, CropState.GROWING, Produce.POTATO_CACTUS.getStages() - 1);
      }
      if (value >= 59 && value <= 255) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      return null;
    }
  },
  SEAWEED(Tab.SPECIAL, "Seaweed", false) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value >= 4 && value <= 7) {
        return new PatchState(Produce.SEAWEED, CropState.GROWING, value - 4);
      }
      if (value >= 8 && value <= 10) {
        return new PatchState(Produce.SEAWEED, CropState.HARVESTABLE, value - 8);
      }
      if (value >= 11 && value <= 13) {
        return new PatchState(Produce.SEAWEED, CropState.DISEASED, value - 10);
      }
      if (value >= 14 && value <= 16) {
        return new PatchState(Produce.SEAWEED, CropState.DEAD, value - 13);
      }
      if (value >= 17 && value <= 255) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      return null;
    }
  },
  CALQUAT(Tab.FRUIT_TREE, "Calquat", true) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value >= 4 && value <= 11) {
        return new PatchState(Produce.CALQUAT, CropState.GROWING, value - 4);
      }
      if (value >= 12 && value <= 18) {
        return new PatchState(Produce.CALQUAT, CropState.HARVESTABLE, value - 12);
      }
      if (value >= 19 && value <= 25) {
        return new PatchState(Produce.CALQUAT, CropState.DISEASED, value - 18);
      }
      if (value >= 26 && value <= 33) {
        return new PatchState(Produce.CALQUAT, CropState.DEAD, value - 25);
      }
      if (value == 34) {
        return new PatchState(Produce.CALQUAT, CropState.GROWING, Produce.CALQUAT.getStages() - 1);
      }
      if (value >= 35 && value <= 255) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      return null;
    }
  },
  CELASTRUS(Tab.FRUIT_TREE, "Celastrus", true) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value >= 4 && value <= 7) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 8 && value <= 12) {
        return new PatchState(Produce.CELASTRUS, CropState.GROWING, value - 8);
      }
      if (value == 13) {
        return new PatchState(
            Produce.CELASTRUS, CropState.GROWING, Produce.CELASTRUS.getStages() - 1);
      }
      if (value >= 14 && value <= 16) {
        return new PatchState(Produce.CELASTRUS, CropState.HARVESTABLE, value - 14);
      }
      if (value == 17) {
        return new PatchState(Produce.CELASTRUS, CropState.HARVESTABLE, 0);
      }
      if (value >= 18 && value <= 22) {
        return new PatchState(Produce.CELASTRUS, CropState.DISEASED, value - 17);
      }
      if (value >= 23 && value <= 27) {
        return new PatchState(Produce.CELASTRUS, CropState.DEAD, value - 22);
      }
      if (value == 28) {
        return new PatchState(Produce.CELASTRUS, CropState.HARVESTABLE, 0);
      }
      if (value >= 29 && value <= 255) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      return null;
    }
  },
  GRAPES(Tab.GRAPE, "", true) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 1) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
      }
      if (value >= 2 && value <= 9) {
        return new PatchState(Produce.GRAPE, CropState.GROWING, value - 2);
      }
      if (value == 10) {
        return new PatchState(Produce.GRAPE, CropState.GROWING, 7);
      }
      if (value >= 11 && value <= 15) {
        return new PatchState(Produce.GRAPE, CropState.HARVESTABLE, value - 11);
      }
      return null;
    }
  },
  CRYSTAL_TREE(Tab.FRUIT_TREE, "Crystal Tree", true) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value >= 0 && value <= 3) {
        return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
      }
      if (value >= 8 && value <= 13) {
        return new PatchState(Produce.CRYSTAL_TREE, CropState.GROWING, value - 8);
      }
      if (value == 14) {
        return new PatchState(
            Produce.CRYSTAL_TREE, CropState.GROWING, Produce.CRYSTAL_TREE.getStages() - 1);
      }
      if (value == 15) {
        return new PatchState(Produce.CRYSTAL_TREE, CropState.HARVESTABLE, 0);
      }
      return null;
    }
  },
  COMPOST(Tab.SPECIAL, "Compost Bin", true) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value == 0) {
        return new PatchState(Produce.EMPTY_COMPOST_BIN, CropState.EMPTY, 0);
      }
      if (value >= 1 && value <= 15) {
        return new PatchState(Produce.COMPOST, CropState.FILLING, value - 1);
      }
      if (value >= 16 && value <= 30) {
        return new PatchState(Produce.COMPOST, CropState.HARVESTABLE, value - 16);
      }
      if (value == 31 || value == 32) {
        return new PatchState(Produce.COMPOST, CropState.GROWING, value - 31);
      }
      if (value >= 33 && value <= 47) {
        return new PatchState(Produce.SUPERCOMPOST, CropState.FILLING, value - 33);
      }
      if (value >= 48 && value <= 62) {
        return new PatchState(Produce.SUPERCOMPOST, CropState.HARVESTABLE, value - 48);
      }
      if (value == 94) {
        return new PatchState(Produce.COMPOST, CropState.GROWING, Produce.COMPOST.getStages() - 1);
      }
      if (value == 95 || value == 96) {
        return new PatchState(Produce.SUPERCOMPOST, CropState.GROWING, value - 95);
      }
      if (value == 126) {
        return new PatchState(
            Produce.SUPERCOMPOST, CropState.GROWING, Produce.SUPERCOMPOST.getStages() - 1);
      }
      if (value >= 129 && value <= 143) {
        return new PatchState(Produce.ROTTEN_TOMATO, CropState.FILLING, value - 129);
      }
      if (value >= 144 && value <= 158) {
        return new PatchState(Produce.ROTTEN_TOMATO, CropState.HARVESTABLE, value - 144);
      }
      if (value >= 159 && value <= 160) {
        return new PatchState(Produce.ROTTEN_TOMATO, CropState.GROWING, value - 159);
      }
      if (value >= 176 && value <= 190) {
        return new PatchState(Produce.ULTRACOMPOST, CropState.HARVESTABLE, value - 176);
      }
      return null;
    }
  },
  GIANT_COMPOST(Tab.SPECIAL, "Giant Compost Bin", true) {
    @Override
    public PatchState forVarbitValue(int value) {
      if (value == 0) {
        return new PatchState(Produce.EMPTY_GIANT_COMPOST_BIN, CropState.EMPTY, 0);
      }
      if (value >= 1 && value <= 15) {
        return new PatchState(Produce.GIANT_COMPOST, CropState.FILLING, value - 1);
      }
      if (value >= 16 && value <= 30) {
        return new PatchState(Produce.GIANT_COMPOST, CropState.HARVESTABLE, value - 16);
      }
      if (value >= 33 && value <= 47) {
        return new PatchState(Produce.GIANT_SUPERCOMPOST, CropState.FILLING, value - 33);
      }
      if (value >= 48 && value <= 62) {
        return new PatchState(Produce.GIANT_SUPERCOMPOST, CropState.HARVESTABLE, value - 48);
      }
      if (value >= 63 && value <= 77) {
        return new PatchState(Produce.GIANT_COMPOST, CropState.FILLING, 15 + value - 63);
      }
      if (value >= 78 && value <= 92) {
        return new PatchState(Produce.GIANT_COMPOST, CropState.HARVESTABLE, 15 + value - 78);
      }
      if (value == 93) {
        return new PatchState(
            Produce.GIANT_COMPOST, CropState.GROWING, Produce.GIANT_COMPOST.getStages() - 1);
      }
      if (value >= 97 && value <= 99) {
        return new PatchState(Produce.GIANT_SUPERCOMPOST, CropState.GROWING, value - 97);
      }
      if (value >= 100 && value <= 114) {
        return new PatchState(Produce.GIANT_SUPERCOMPOST, CropState.HARVESTABLE, 15 + value - 100);
      }
      if (value >= 127 && value <= 128) {
        return new PatchState(Produce.GIANT_COMPOST, CropState.GROWING, value - 127);
      }
      if (value >= 129 && value <= 143) {
        return new PatchState(Produce.GIANT_ROTTEN_TOMATO, CropState.FILLING, value - 129);
      }
      if (value >= 144 && value <= 158) {
        return new PatchState(Produce.GIANT_ROTTEN_TOMATO, CropState.HARVESTABLE, value - 144);
      }
      if (value >= 159 && value <= 160) {
        return new PatchState(Produce.GIANT_ROTTEN_TOMATO, CropState.GROWING, value - 159);
      }
      if (value >= 161 && value <= 175) {
        return new PatchState(Produce.GIANT_SUPERCOMPOST, CropState.FILLING, 15 + value - 161);
      }
      if (value >= 176 && value <= 205) {
        return new PatchState(Produce.GIANT_ULTRACOMPOST, CropState.HARVESTABLE, value - 176);
      }
      if (value >= 207 && value <= 221) {
        return new PatchState(Produce.GIANT_ROTTEN_TOMATO, CropState.HARVESTABLE, 15 + value - 207);
      }
      if (value == 222) {
        return new PatchState(
            Produce.GIANT_ROTTEN_TOMATO,
            CropState.GROWING,
            Produce.GIANT_ROTTEN_TOMATO.getStages() - 1);
      }
      if (value >= 223 && value <= 237) {
        return new PatchState(Produce.GIANT_ROTTEN_TOMATO, CropState.FILLING, 15 + value - 223);
      }
      return null;
    }
  };

  private final Tab tab;
  private final String name;
  private final boolean healthCheckRequired;

  @Nullable
  public abstract PatchState forVarbitValue(int value);
}
