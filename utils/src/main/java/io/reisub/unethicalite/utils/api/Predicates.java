package io.reisub.unethicalite.utils.api;

import dev.unethicalite.api.EntityNameable;
import dev.unethicalite.api.Identifiable;
import dev.unethicalite.api.SceneEntity;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import net.runelite.api.Item;

public class Predicates {
  public static <T> Predicate<T> distinctByProperty(Function<? super T, ?> propertyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(propertyExtractor.apply(t));
  }

  public static <T extends Identifiable> Predicate<T> ids(Collection<Integer> ids) {
    return t -> ids.contains(t.getId());
  }

  public static <T extends EntityNameable> Predicate<T> names(Collection<String> names) {
    return t -> names.contains(t.getName());
  }

  public static <T extends Item> Predicate<T> itemConfigList(ConfigList configList) {
    Predicate<T> nameablePredicate = names(configList.getStrings());
    Predicate<T> identifiablePredicate = ids(configList.getIntegers());

    return nameablePredicate.or(identifiablePredicate);
  }

  public static <T extends SceneEntity> Predicate<T> entityConfigList(ConfigList configList) {
    Predicate<T> nameablePredicate = names(configList.getStrings());
    Predicate<T> identifiablePredicate = ids(configList.getIntegers());

    return nameablePredicate.or(identifiablePredicate);
  }
}
