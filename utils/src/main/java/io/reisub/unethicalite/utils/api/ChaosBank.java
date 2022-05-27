package io.reisub.unethicalite.utils.api;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.runelite.api.Item;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Bank;

public class ChaosBank {
  public static void depositAll(String... names) {
    depositAll(true, names);
  }

  public static void depositAll(boolean delay, String... names) {
    depositAll(delay, x -> Arrays.stream(names).anyMatch(name -> x.getName().equals(name)));
  }

  public static void depositAll(int... ids) {
    depositAll(true, ids);
  }

  public static void depositAll(boolean delay, int... ids) {
    depositAll(delay, x -> Arrays.stream(ids).anyMatch(id -> x.getId() == id));
  }

  public static void depositAll(Predicate<Item> filter) {
    depositAll(true, filter);
  }

  public static void depositAll(boolean delay, Predicate<Item> filter) {
    Set<Item> items =
        Bank.Inventory.getAll(filter).stream()
            .filter(Predicates.distinctByProperty(Item::getId))
            .collect(Collectors.toSet());

    items.forEach(
        (item) -> {
          Bank.depositAll(item.getId());

          if (delay) {
            Time.sleepTick();
          }
        });
  }

  public static void depositAllExcept(String... names) {
    depositAllExcept(true, names);
  }

  public static void depositAllExcept(boolean delay, String... names) {
    depositAllExcept(delay, x -> Arrays.stream(names).anyMatch(name -> x.getName().equals(name)));
  }

  public static void depositAllExcept(int... ids) {
    depositAllExcept(true, ids);
  }

  public static void depositAllExcept(boolean delay, int... ids) {
    depositAllExcept(delay, x -> Arrays.stream(ids).anyMatch(id -> x.getId() == id));
  }

  public static void depositAllExcept(Predicate<Item> filter) {
    depositAllExcept(true, filter);
  }

  public static void depositAllExcept(boolean delay, Predicate<Item> filter) {
    depositAll(delay, filter.negate());
  }

  @Deprecated
  public static void bankInventoryInteract(Item item, String action) {
    item.interact(action);
  }
}
