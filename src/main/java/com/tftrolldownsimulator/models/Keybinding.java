package com.tftrolldownsimulator.models;

import java.util.AbstractMap;
import java.util.Map;

public enum Keybinding {
  REROLL("Reroll"),
  BUY_XP("Buy XP");

  private static final Map<String, Keybinding> labelMap = Map.ofEntries(
      new AbstractMap.SimpleEntry<>("Reroll", Keybinding.REROLL),
      new AbstractMap.SimpleEntry<>("Buy XP", Keybinding.BUY_XP));

  private final String label;

  Keybinding(String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }

  public static Keybinding fromLabel(String label) {
    return Keybinding.labelMap.get(label);
  }
}
