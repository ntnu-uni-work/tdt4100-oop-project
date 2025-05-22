package com.tftrolldownsimulator.models;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.tftrolldownsimulator.models.deserializers.TraitDeserializer;
import com.tftrolldownsimulator.models.generated.Trait;

public class TFTSet {
  public static final List<Unit> UNITS = getSetUnits();
  public static final List<Unit> BUYABLE_UNITS = filterUnits(unit -> unit.getCost() != 11);
  public static final Map<Integer, List<Unit>> UNITS_COSTING = Map.of(
      1, filterUnits(1),
      2, filterUnits(2),
      3, filterUnits(3),
      4, filterUnits(4),
      5, filterUnits(5),
      6, filterUnits(6));

  /**
   * Unique to set 13
   */
  public static final List<Unit> UNITS_6_COST = TFTSet.filterUnits(6);

  private static List<Unit> getSetUnits() {
    try {
      ClassLoader classLoader = JsonReader.class.getClassLoader();
      Reader reader = new InputStreamReader(classLoader.getResourceAsStream("data/champions.json"));

      Gson gson = new GsonBuilder().registerTypeAdapter(Trait.class, new TraitDeserializer()).create();
      Type listType = new TypeToken<List<Unit>>() {
      }.getType();
      return gson.fromJson(reader, listType);
    } catch (Exception e) {
      throw e;
    }
  }

  public static List<Unit> filterUnits(int cost) {
    return UNITS.stream().filter(unit -> unit.getCost() == cost).collect(Collectors.toList());
  }

  public static List<Unit> filterUnits(Predicate<Unit> filter) {
    return UNITS.stream().filter(filter).collect(Collectors.toList());
  }
}
