package io.reisub.unethicalite.utils.api;

import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;

public class ConfigList {
  @Getter private final Set<Integer> integers;
  @Getter private final Set<String> strings;

  private ConfigList() {
    integers = new LinkedHashSet<>();
    strings = new LinkedHashSet<>();
  }

  public static ConfigList parseList(String list) {
    ConfigList configList = new ConfigList();

    for (String string : list.split("[\\n;,]")) {
      if (string.equals("")) {
        continue;
      }

      // get rid of any comments and trim what's left
      string = string.split("//")[0].trim();

      try {
        configList.integers.add(Integer.parseInt(string));
      } catch (NumberFormatException e) {
        configList.strings.add(string);
      }
    }

    return configList;
  }
}
