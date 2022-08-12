package io.reisub.unethicalite.mahoganyhomes.data;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import lombok.Data;
import lombok.Getter;
import net.runelite.api.AnimationID;
import net.runelite.api.ChatMessageType;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.util.Text;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.unethicalite.api.events.MenuAutomated;
import net.unethicalite.client.Static;

// Taken from https://github.com/TheStonedTurtle/Mahogany-Homes
public class PlankSack {

  private static final List<Integer> PLANKS = Arrays.asList(ItemID.PLANK, ItemID.OAK_PLANK,
      ItemID.TEAK_PLANK, ItemID.MAHOGANY_PLANK);
  private static final List<String> PLANK_NAMES = Arrays.asList("Plank", "Oak plank", "Teak plank",
      "Mahogany plank");
  private static final Set<Integer> MAHOGANY_HOMES_REPAIRS = Sets.newHashSet(
      39982, 39995, 40011, 40089, 40099, 40158, 40159, 40163, 40168, 40170, 40177, 40295, 40298);
  private static final int CONSTRUCTION_WIDGET_GROUP = 458;
  private static final int CONSTRUCTION_WIDGET_BUILD_IDX_START = 4;
  private static final int CONSTRUCTION_SUBWIDGET_MATERIALS = 3;
  private static final int CONSTRUCTION_SUBWIDGET_CANT_BUILD = 5;
  private static final int CONSTRUCTION_IMCANDO_MAHOGANY_HOMES = 8912;
  private static final int SCRIPT_CONSTRUCTION_OPTION_CLICKED = 1405;
  private static final int SCRIPT_CONSTRUCTION_OPTION_KEYBIND = 1632;
  private static final int SCRIPT_BUILD_CONSTRUCTION_MENU_ENTRY = 1404;

  @Data
  private static class BuildMenuItem {

    private final Item[] planks;
    private final boolean canBuild;
  }

  @Inject
  private ItemManager itemManager;

  @Getter
  private int plankCount = -1;

  private Multiset<Integer> inventorySnapshot;
  private boolean checkForUpdate = false;

  private int menuItemsToCheck = 0;
  private final List<BuildMenuItem> buildMenuItems = new ArrayList<>();

  private boolean watchForAnimations = false;
  private int lastAnimation = -1;

  @Subscribe
  public void onItemContainerChanged(ItemContainerChanged event) {
    if (event.getContainerId() != InventoryID.INVENTORY.getId()) {
      return;
    }

    if (checkForUpdate) {
      checkForUpdate = false;
      Multiset<Integer> currentInventory = createSnapshot(event.getItemContainer());
      Multiset<Integer> deltaMinus = Multisets.difference(currentInventory, inventorySnapshot);
      Multiset<Integer> deltaPlus = Multisets.difference(inventorySnapshot, currentInventory);
      deltaPlus.forEachEntry((id, c) -> plankCount += c);
      deltaMinus.forEachEntry((id, c) -> plankCount -= c);
      setPlankCount(plankCount);
    }
  }

  @Subscribe
  public void onInvokeMenuAction(MenuAutomated event) {
    final int eventId = event.getIdentifier();
    final MenuAction opCode = event.getOpcode();
    final int fillId = 2;
    final int emptyId = 6;
    final int bankUseId = 9;

    if (event.getParam1() ==  WidgetInfo.INVENTORY.getPackedId()
        || event.getParam1() == WidgetInfo.BANK_ITEM_CONTAINER.getPackedId()) {
      if (event.getItemId() == ItemID.PLANK_SACK
          && (opCode == MenuAction.CC_OP || opCode == MenuAction.CC_OP_LOW_PRIORITY)
          && (eventId == fillId || eventId == emptyId || eventId == bankUseId)) {
        inventorySnapshot = createSnapshot(
            Static.getClient().getItemContainer(InventoryID.INVENTORY));
        checkForUpdate = true;
      } else if (opCode == MenuAction.WIDGET_TARGET_ON_WIDGET
          && Static.getClient().getSelectedWidget() != null) {
        final int firstSelectedItemId = Static.getClient().getSelectedWidget().getItemId();

        if ((firstSelectedItemId == ItemID.PLANK_SACK && PLANKS.contains(event.getItemId()))
            || (PLANKS.contains(firstSelectedItemId) && event.getItemId() == ItemID.PLANK_SACK)) {
          inventorySnapshot = createSnapshot(
              Static.getClient().getItemContainer(InventoryID.INVENTORY));
          checkForUpdate = true;
        }
      }
    } else if (opCode == MenuAction.GAME_OBJECT_THIRD_OPTION
        && MAHOGANY_HOMES_REPAIRS.contains(eventId)) {
      watchForAnimations = MAHOGANY_HOMES_REPAIRS.contains(eventId);
      inventorySnapshot = createSnapshot(
          Static.getClient().getItemContainer(InventoryID.INVENTORY));
    }
  }

  @Subscribe
  public void onScriptPreFired(ScriptPreFired event) {
    // Construction menu option selected
    // Consutrction menu option selected with keybind
    if (event.getScriptId() != SCRIPT_CONSTRUCTION_OPTION_CLICKED
        && event.getScriptId() != SCRIPT_CONSTRUCTION_OPTION_KEYBIND) {
      return;
    }

    Widget widget = event.getScriptEvent().getSource();
    int idx = WidgetInfo.TO_CHILD(widget.getId()) - CONSTRUCTION_WIDGET_BUILD_IDX_START;
    if (idx >= buildMenuItems.size()) {
      return;
    }
    BuildMenuItem item = buildMenuItems.get(idx);
    if (item != null && item.canBuild) {
      Multiset<Integer> snapshot = createSnapshot(
          Static.getClient().getItemContainer(InventoryID.INVENTORY));
      if (snapshot != null) {
        for (Item i : item.planks) {
          if (!snapshot.contains(i.getId())) {
            plankCount -= i.getQuantity();
          } else if (snapshot.count(i.getId()) < i.getQuantity()) {
            plankCount -= i.getQuantity() - snapshot.count(i.getId());
          }
        }
        setPlankCount(plankCount);
      }
    }

    buildMenuItems.clear();
  }

  @Subscribe
  public void onScriptPostFired(ScriptPostFired event) {
    if (event.getScriptId() != SCRIPT_BUILD_CONSTRUCTION_MENU_ENTRY) {
      return;
    }
    // Construction menu add object
    menuItemsToCheck += 1;
    // Cancel repair-based animation checking
    watchForAnimations = false;
  }

  @Subscribe
  public void onGameTick(GameTick event) {
    if (menuItemsToCheck > 0) {
      for (int i = 0; i < menuItemsToCheck; i++) {
        int idx = CONSTRUCTION_WIDGET_BUILD_IDX_START + i;
        Widget widget = Static.getClient().getWidget(CONSTRUCTION_WIDGET_GROUP, idx);
        if (widget == null) {
          continue;
        }

        boolean canBuild
            = widget.getDynamicChildren()[CONSTRUCTION_SUBWIDGET_CANT_BUILD].isHidden();
        Widget materialWidget = widget.getDynamicChildren()[CONSTRUCTION_SUBWIDGET_MATERIALS];
        if (materialWidget == null) {
          continue;
        }

        String[] materialLines = materialWidget.getText().split("<br>");
        List<Item> materials = new ArrayList<>();
        for (String line : materialLines) {
          String[] data = line.split(": ");
          if (data.length < 2) {
            continue;
          }

          String name = data[0];
          int count = Integer.parseInt(data[1]);
          if (PLANK_NAMES.contains(name)) {
            materials.add(new Item(PLANKS.get(PLANK_NAMES.indexOf(name)), count));
          }
        }
        buildMenuItems.add(new BuildMenuItem(materials.toArray(new Item[0]), canBuild));
      }
      menuItemsToCheck = 0;
    }
  }

  @Subscribe
  public void onAnimationChanged(AnimationChanged event) {
    if (event.getActor() != Static.getClient().getLocalPlayer()
        || Static.getClient().getLocalPlayer() == null) {
      return;
    }

    if (watchForAnimations) {
      int anim = Static.getClient().getLocalPlayer().getAnimation();
      if ((lastAnimation == AnimationID.CONSTRUCTION
          || lastAnimation == CONSTRUCTION_IMCANDO_MAHOGANY_HOMES) && anim != lastAnimation) {
        Multiset<Integer> current = createSnapshot(
            Static.getClient().getItemContainer(InventoryID.INVENTORY));
        Multiset<Integer> delta = Multisets.difference(inventorySnapshot, current);
        if (delta.size() == 0) {
          setPlankCount(plankCount - 1);
        }
        watchForAnimations = false;
        lastAnimation = -1;
      } else {
        lastAnimation = anim;
      }
    }
  }

  @Subscribe
  public void onChatMessage(ChatMessage event) {
    if (event.getType() != ChatMessageType.GAMEMESSAGE) {
      return;
    }
    if (event.getMessage().startsWith("Basic\u00A0planks:")) {
      String message = Text.removeTags(event.getMessage());
      setPlankCount(
          Arrays.stream(message.split(",")).mapToInt(s -> Integer.parseInt(s.split(":\u00A0")[1]))
              .sum());
    } else if (event.getMessage().equals("You haven't got any planks that can go in the sack.")) {
      checkForUpdate = false;
    } else if (event.getMessage().equals("Your sack is full.")) {
      setPlankCount(28);
      checkForUpdate = false;
    } else if (event.getMessage().equals("Your sack is empty.")) {
      setPlankCount(0);
      checkForUpdate = false;
    }
  }

  private void setPlankCount(int count) {
    plankCount = Ints.constrainToRange(count, 0, 28);
  }

  private Multiset<Integer> createSnapshot(ItemContainer container) {
    if (container == null) {
      return null;
    }
    Multiset<Integer> snapshot = HashMultiset.create();
    Arrays.stream(container.getItems())
        .filter(item -> PLANKS.contains(item.getId()))
        .forEach(i -> snapshot.add(i.getId(), i.getQuantity()));
    return snapshot;
  }
}
