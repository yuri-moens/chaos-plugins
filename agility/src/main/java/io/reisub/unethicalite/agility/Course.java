package io.reisub.unethicalite.agility;

import net.runelite.api.Locatable;
import net.runelite.api.ObjectID;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.Players;

public enum Course {
  TREE_GNOME_STRONGHOLD(
      new ObstacleArea(
          new WorldPoint(2470, 3435, 0), new WorldPoint(2489, 3447, 0), ObjectID.LOG_BALANCE_23145),
      new ObstacleArea(
          new WorldPoint(2470, 3423, 0),
          new WorldPoint(2477, 3430, 0),
          ObjectID.OBSTACLE_NET_23134),
      new ObstacleArea(
          new WorldPoint(2470, 3421, 1), new WorldPoint(2476, 3425, 1), ObjectID.TREE_BRANCH_23559),
      new ObstacleArea(
          new WorldPoint(2469, 3416, 2),
          new WorldPoint(2479, 3423, 2),
          ObjectID.BALANCING_ROPE_23557),
      new ObstacleArea(
          new WorldPoint(2482, 3416, 2), new WorldPoint(2489, 3423, 2), ObjectID.TREE_BRANCH_23560),
      new ObstacleArea(
          new WorldPoint(2482, 3418, 0),
          new WorldPoint(2489, 3427, 0),
          ObjectID.OBSTACLE_NET_23135),
      new ObstacleArea(
          new WorldPoint(2482, 3427, 0),
          new WorldPoint(2489, 3433, 0),
          ObjectID.OBSTACLE_PIPE_23139)),
  DRAYNOR(
      new ObstacleArea(
          new WorldPoint(3082, 3238, 0), new WorldPoint(3105, 3293, 0), ObjectID.ROUGH_WALL),
      new ObstacleArea(
          new WorldPoint(3096, 3275, 3), new WorldPoint(3103, 3282, 3), ObjectID.TIGHTROPE),
      new ObstacleArea(
          new WorldPoint(3086, 3271, 3), new WorldPoint(3093, 3279, 3), ObjectID.TIGHTROPE_11406),
      new ObstacleArea(
          new WorldPoint(3087, 3263, 3), new WorldPoint(3095, 3269, 3), ObjectID.NARROW_WALL),
      new ObstacleArea(
          new WorldPoint(3082, 3256, 3), new WorldPoint(3089, 3262, 3), ObjectID.WALL_11630),
      new ObstacleArea(
          new WorldPoint(3087, 3254, 3), new WorldPoint(3095, 3256, 3), ObjectID.GAP_11631),
      new ObstacleArea(
          new WorldPoint(3095, 3255, 3), new WorldPoint(3102, 3262, 3), ObjectID.CRATE_11632)),
  AL_KHARID(
      new ObstacleArea(
          new WorldPoint(3268, 3159, 0), new WorldPoint(3322, 3200, 0), ObjectID.ROUGH_WALL_11633),
      new ObstacleArea(
          new WorldPoint(3270, 3179, 3), new WorldPoint(3278, 3193, 3), ObjectID.TIGHTROPE_14398),
      new ObstacleArea(
          new WorldPoint(3263, 3160, 3), new WorldPoint(3274, 3174, 3), ObjectID.CABLE),
      new ObstacleArea(
          new WorldPoint(3282, 3159, 3), new WorldPoint(3303, 3176, 3), ObjectID.ZIP_LINE_14403),
      new ObstacleArea(
          new WorldPoint(3312, 3159, 1),
          new WorldPoint(3319, 3166, 1),
          ObjectID.TROPICAL_TREE_14404),
      new ObstacleArea(
          new WorldPoint(3311, 3172, 2), new WorldPoint(3319, 3180, 2), ObjectID.ROOF_TOP_BEAMS),
      new ObstacleArea(
          new WorldPoint(3311, 3180, 3), new WorldPoint(3319, 3187, 3), ObjectID.TIGHTROPE_14409),
      new ObstacleArea(
          new WorldPoint(3296, 3184, 3), new WorldPoint(3306, 3194, 3), ObjectID.GAP_14399)),
  VARROCK(
      new ObstacleArea(
          new WorldPoint(3184, 3386, 0), new WorldPoint(3258, 3428, 0), ObjectID.ROUGH_WALL_14412),
      new ObstacleArea(
          new WorldPoint(3213, 3409, 3), new WorldPoint(3220, 3420, 3), ObjectID.CLOTHES_LINE),
      new ObstacleArea(
          new WorldPoint(3200, 3412, 3), new WorldPoint(3209, 3420, 3), ObjectID.GAP_14414),
      new ObstacleArea(
          new WorldPoint(3192, 3415, 1), new WorldPoint(3198, 3417, 1), ObjectID.WALL_14832),
      new ObstacleArea(
          new WorldPoint(3191, 3401, 3), new WorldPoint(3198, 3407, 3), ObjectID.GAP_14833),
      new ObstacleArea(
          new WorldPoint(3181, 3393, 3), new WorldPoint(3209, 3401, 3), ObjectID.GAP_14834),
      new ObstacleArea(
          new WorldPoint(3217, 3392, 3), new WorldPoint(3233, 3404, 3), ObjectID.GAP_14835),
      new ObstacleArea(
          new WorldPoint(3235, 3402, 3), new WorldPoint(3240, 3409, 3), ObjectID.LEDGE_14836),
      new ObstacleArea(
          new WorldPoint(3235, 3410, 3), new WorldPoint(3240, 3416, 3), ObjectID.EDGE)),
  CANIFIS(
      new ObstacleArea(
          new WorldPoint(3459, 3464, 0), new WorldPoint(3519, 3514, 0), ObjectID.TALL_TREE_14843),
      new ObstacleArea(
          new WorldPoint(3504, 3491, 2), new WorldPoint(3512, 3499, 2), ObjectID.GAP_14844),
      new ObstacleArea(
          new WorldPoint(3495, 3503, 2), new WorldPoint(3505, 3508, 2), ObjectID.GAP_14845),
      new ObstacleArea(
          new WorldPoint(3484, 3498, 2), new WorldPoint(3494, 3506, 2), ObjectID.GAP_14848),
      new ObstacleArea(
          new WorldPoint(3474, 3491, 3), new WorldPoint(3481, 3501, 3), ObjectID.GAP_14846),
      new ObstacleArea(
          new WorldPoint(3477, 3481, 2), new WorldPoint(3485, 3488, 2), ObjectID.POLEVAULT),
      new ObstacleArea(
          new WorldPoint(3488, 3468, 3), new WorldPoint(3505, 3480, 3), ObjectID.GAP_14847),
      new ObstacleArea(
          new WorldPoint(3508, 3474, 2), new WorldPoint(3517, 3484, 2), ObjectID.GAP_14897)),
  APE_ATOLL(
      new ObstacleArea(
          new WorldPoint(2754, 2741, 0),
          new WorldPoint(2784, 2751, 0),
          ObjectID.STEPPING_STONE_15412),
      new ObstacleArea(
          new WorldPoint(2753, 2742, 0),
          new WorldPoint(2751, 2739, 0),
          ObjectID.TROPICAL_TREE_15414),
      new ObstacleArea(
          new WorldPoint(2753, 2742, 2), new WorldPoint(2752, 2741, 2), ObjectID.MONKEYBARS_15417),
      new ObstacleArea(
          new WorldPoint(2747, 2741, 0), new WorldPoint(2746, 2741, 0), ObjectID.SKULL_SLOPE_15483),
      new ObstacleArea(
          new WorldPoint(2735, 2726, 0), new WorldPoint(2754, 2742, 0), ObjectID.ROPE_15487),
      new ObstacleArea(
          new WorldPoint(2755, 2726, 0),
          new WorldPoint(2760, 2737, 0),
          ObjectID.TROPICAL_TREE_16062)),
  FALADOR(
      new ObstacleArea(
          new WorldPoint(3008, 3328, 0), new WorldPoint(3071, 3391, 0), ObjectID.ROUGH_WALL_14898),
      new ObstacleArea(
          new WorldPoint(3034, 3342, 3), new WorldPoint(3040, 3347, 3), ObjectID.TIGHTROPE_14899),
      new ObstacleArea(
          new WorldPoint(3043, 3341, 3), new WorldPoint(3051, 3350, 3), ObjectID.HAND_HOLDS_14901),
      new ObstacleArea(
          new WorldPoint(3047, 3356, 3), new WorldPoint(3051, 3359, 3), ObjectID.GAP_14903),
      new ObstacleArea(
          new WorldPoint(3044, 3360, 3), new WorldPoint(3049, 3367, 3), ObjectID.GAP_14904),
      new ObstacleArea(
          new WorldPoint(3033, 3360, 3), new WorldPoint(3042, 3364, 3), ObjectID.TIGHTROPE_14905),
      new ObstacleArea(
          new WorldPoint(3025, 3352, 3), new WorldPoint(3029, 3355, 3), ObjectID.TIGHTROPE_14911),
      new ObstacleArea(
          new WorldPoint(3008, 3352, 3), new WorldPoint(3021, 3358, 3), ObjectID.GAP_14919),
      new ObstacleArea(
          new WorldPoint(3015, 3343, 3), new WorldPoint(3022, 3350, 3), ObjectID.LEDGE_14920),
      new ObstacleArea(
          new WorldPoint(3010, 3343, 3), new WorldPoint(3015, 3347, 3), ObjectID.LEDGE_14921),
      new ObstacleArea(
          new WorldPoint(3008, 3335, 3), new WorldPoint(3014, 3343, 3), ObjectID.LEDGE_14922),
      new ObstacleArea(
          new WorldPoint(3013, 3331, 3), new WorldPoint(3018, 3335, 3), ObjectID.LEDGE_14924),
      new ObstacleArea(
          new WorldPoint(3019, 3331, 3), new WorldPoint(3027, 3335, 3), ObjectID.EDGE_14925)),
  SEERS(
      new ObstacleArea(
          new WorldPoint(2689, 3457, 0), new WorldPoint(2750, 3517, 0), ObjectID.WALL_14927),
      new ObstacleArea(
          new WorldPoint(2720, 3489, 3), new WorldPoint(2731, 3498, 3), ObjectID.GAP_14928),
      new ObstacleArea(
          new WorldPoint(2702, 3486, 2), new WorldPoint(2714, 3499, 2), ObjectID.TIGHTROPE_14932),
      new ObstacleArea(
          new WorldPoint(2707, 3475, 2), new WorldPoint(2717, 3483, 2), ObjectID.GAP_14929),
      new ObstacleArea(
          new WorldPoint(2697, 3468, 3), new WorldPoint(2718, 3478, 3), ObjectID.GAP_14930),
      new ObstacleArea(
          new WorldPoint(2689, 3458, 2), new WorldPoint(2704, 3467, 2), ObjectID.EDGE_14931)),
  POLLNIVNEACH(
      new ObstacleArea(
          new WorldPoint(3328, 2944, 0), new WorldPoint(3392, 3008, 0), ObjectID.BASKET_14935),
      new ObstacleArea(
          new WorldPoint(3346, 2963, 1),
          new WorldPoint(3352, 2969, 1),
          ObjectID.MARKET_STALL_14936),
      new ObstacleArea(
          new WorldPoint(3352, 2973, 1), new WorldPoint(3356, 2977, 1), ObjectID.BANNER_14937),
      new ObstacleArea(
          new WorldPoint(3360, 2977, 1), new WorldPoint(3363, 2980, 1), ObjectID.GAP_14938),
      new ObstacleArea(
          new WorldPoint(3366, 2976, 1), new WorldPoint(3372, 2975, 1), ObjectID.TREE_14939),
      new ObstacleArea(
          new WorldPoint(3365, 2982, 1), new WorldPoint(3370, 2987, 1), ObjectID.ROUGH_WALL_14940),
      new ObstacleArea(
          new WorldPoint(3355, 2980, 2), new WorldPoint(3366, 2986, 2), ObjectID.MONKEYBARS),
      new ObstacleArea(
          new WorldPoint(3357, 2991, 2), new WorldPoint(3367, 2996, 2), ObjectID.TREE_14944),
      new ObstacleArea(
          new WorldPoint(3356, 3000, 2), new WorldPoint(3363, 3005, 2), ObjectID.DRYING_LINE)),
  PRIFDDINAS(
      new ObstacleArea(
          new WorldPoint(3237, 6099, 0), new WorldPoint(3275, 6114, 0), ObjectID.LADDER_36221),
      new ObstacleArea(
          new WorldPoint(3254, 6102, 2), new WorldPoint(3259, 6112, 2), ObjectID.TIGHTROPE_36225),
      new ObstacleArea(
          new WorldPoint(3271, 6104, 2), new WorldPoint(3276, 6107, 2), ObjectID.CHIMNEY_36227),
      new ObstacleArea(
          new WorldPoint(3268, 6111, 2), new WorldPoint(3270, 6116, 2), ObjectID.ROOF_EDGE),
      new ObstacleArea(
          new WorldPoint(3267, 6115, 0), new WorldPoint(3271, 6119, 0), ObjectID.DARK_HOLE_36229),
      new ObstacleArea(
          new WorldPoint(2239, 3386, 0), new WorldPoint(2272, 3410, 0), ObjectID.LADDER_36231),
      new ObstacleArea(
          new WorldPoint(3265, 6138, 0), new WorldPoint(3276, 6150, 0), ObjectID.LADDER_36232),
      new ObstacleArea(
          new WorldPoint(2264, 3388, 2), new WorldPoint(2270, 3394, 2), ObjectID.ROPE_BRIDGE_36233),
      new ObstacleArea(
          new WorldPoint(2252, 3386, 2), new WorldPoint(2259, 3391, 2), ObjectID.TIGHTROPE_36234),
      new ObstacleArea(
          new WorldPoint(2242, 3393, 2), new WorldPoint(2248, 3399, 2), ObjectID.ROPE_BRIDGE_36235),
      new ObstacleArea(
          new WorldPoint(2243, 3404, 2), new WorldPoint(2249, 3411, 2), ObjectID.TIGHTROPE_36236),
      new ObstacleArea(
          new WorldPoint(2248, 3414, 2), new WorldPoint(2254, 3420, 2), ObjectID.TIGHTROPE_36237),
      new ObstacleArea(
          new WorldPoint(2255, 3424, 0), new WorldPoint(2263, 3436, 0), ObjectID.DARK_HOLE_36238)),
  RELLEKKA(
      new ObstacleArea(
          new WorldPoint(2612, 3654, 0), new WorldPoint(2672, 3687, 0), ObjectID.ROUGH_WALL_14946),
      new ObstacleArea(
          new WorldPoint(2621, 3671, 3), new WorldPoint(2627, 3677, 3), ObjectID.GAP_14947),
      new ObstacleArea(
          new WorldPoint(2614, 3657, 3), new WorldPoint(2623, 3669, 3), ObjectID.TIGHTROPE_14987),
      new ObstacleArea(
          new WorldPoint(2625, 3649, 3), new WorldPoint(2631, 3656, 3), ObjectID.GAP_14990),
      new ObstacleArea(
          new WorldPoint(2638, 3648, 3), new WorldPoint(2645, 3654, 3), ObjectID.GAP_14991),
      new ObstacleArea(
          new WorldPoint(2642, 3656, 3), new WorldPoint(2651, 3663, 3), ObjectID.TIGHTROPE_14992),
      new ObstacleArea(
          new WorldPoint(2654, 3663, 3), new WorldPoint(2667, 3686, 3), ObjectID.PILE_OF_FISH)),
  ARDOUGNE(
      new ObstacleArea(
          new WorldPoint(2640, 3274, 0), new WorldPoint(2678, 3321, 0), ObjectID.WOODEN_BEAMS),
      new ObstacleArea(
          new WorldPoint(2670, 3298, 3), new WorldPoint(2675, 3312, 3), ObjectID.GAP_15609),
      new ObstacleArea(
          new WorldPoint(2660, 3317, 3), new WorldPoint(2666, 3323, 3), ObjectID.PLANK_26635),
      new ObstacleArea(
          new WorldPoint(2652, 3317, 3), new WorldPoint(2658, 3322, 3), ObjectID.GAP_15610),
      new ObstacleArea(
          new WorldPoint(2647, 3310, 3), new WorldPoint(2654, 3315, 3), ObjectID.GAP_15611),
      new ObstacleArea(
          new WorldPoint(2650, 3299, 3), new WorldPoint(2656, 3310, 3), ObjectID.STEEP_ROOF),
      new ObstacleArea(
          new WorldPoint(2653, 3290, 3), new WorldPoint(2658, 3298, 3), ObjectID.GAP_15612));

  private final ObstacleArea[] areas;

  Course(ObstacleArea... area) {
    areas = area;
  }

  public int getNextObstacleId() {
    for (ObstacleArea area : areas) {
      if (area.contains(Players.getLocal())) {
        return area.getId();
      }
    }

    return 0;
  }

  public boolean isReachable(Locatable locatable) {
    if (locatable == null) {
      return false;
    }

    for (ObstacleArea area : areas) {
      if (area.contains(Players.getLocal()) && area.contains(locatable)) {
        return true;
      }
    }

    return false;
  }
}
