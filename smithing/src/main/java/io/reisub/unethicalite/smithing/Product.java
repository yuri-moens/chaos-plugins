package io.reisub.unethicalite.smithing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.widgets.WidgetInfo;

@AllArgsConstructor
@Getter
public enum Product {
  DAGGER(1, WidgetInfo.SMITHING_ANVIL_DAGGER.getChildId()),
  SWORD(1, WidgetInfo.SMITHING_ANVIL_SWORD.getChildId()),
  SCIMITAR(2, WidgetInfo.SMITHING_ANVIL_SCIMITAR.getChildId()),
  LONG_SWORD(2, WidgetInfo.SMITHING_ANVIL_LONG_SWORD.getChildId()),
  TWO_H_SWORD(3, WidgetInfo.SMITHING_ANVIL_TWO_H_SWORD.getChildId()),
  AXE(1, WidgetInfo.SMITHING_ANVIL_AXE.getChildId()),
  MACE(1, WidgetInfo.SMITHING_ANVIL_MACE.getChildId()),
  WARHAMMER(3, WidgetInfo.SMITHING_ANVIL_WARHAMMER.getChildId()),
  BATTLE_AXE(3, WidgetInfo.SMITHING_ANVIL_BATTLE_AXE.getChildId()),
  CLAWS(2, WidgetInfo.SMITHING_ANVIL_CLAWS.getChildId()),
  CHAIN_BODY(3, WidgetInfo.SMITHING_ANVIL_CHAIN_BODY.getChildId()),
  PLATE_LEGS(3, WidgetInfo.SMITHING_ANVIL_PLATE_LEGS.getChildId()),
  PLATE_SKIRT(3, WidgetInfo.SMITHING_ANVIL_PLATE_SKIRT.getChildId()),
  PLATE_BODY(5, WidgetInfo.SMITHING_ANVIL_PLATE_BODY.getChildId()),
  NAILS(1, WidgetInfo.SMITHING_ANVIL_NAILS.getChildId()),
  MED_HELM(1, WidgetInfo.SMITHING_ANVIL_MED_HELM.getChildId()),
  FULL_HELM(2, WidgetInfo.SMITHING_ANVIL_FULL_HELM.getChildId()),
  SQ_SHIELD(2, WidgetInfo.SMITHING_ANVIL_SQ_SHIELD.getChildId()),
  KITE_SHIELD(3, WidgetInfo.SMITHING_ANVIL_KITE_SHIELD.getChildId()),
  DART_TIPS(1, WidgetInfo.SMITHING_ANVIL_DART_TIPS.getChildId()),
  ARROW_HEADS(1, WidgetInfo.SMITHING_ANVIL_ARROW_HEADS.getChildId()),
  KNIVES(1, WidgetInfo.SMITHING_ANVIL_KNIVES.getChildId()),
  JAVELIN_HEADS(1, WidgetInfo.SMITHING_ANVIL_JAVELIN_HEADS.getChildId()),
  BOLTS(1, WidgetInfo.SMITHING_ANVIL_BOLTS.getChildId()),
  LIMBS(1, WidgetInfo.SMITHING_ANVIL_LIMBS.getChildId());

  private final int requiredBars;
  private final int interfaceId;
}
