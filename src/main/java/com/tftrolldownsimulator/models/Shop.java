package com.tftrolldownsimulator.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Shop implements LevelObserver {
  private int level;
  private Unit[] units = new Unit[5];

  /**
   * Keys are unit names
   */
  private Map<String, Integer> pool = new HashMap<>();

  /**
   * Index i gives the amount of copies there exists for a given cost i + 1.
   * eg. i = 2 gives 18 copies of every 3 cost
   */
  public static final List<Integer> BAG_SIZES = List.of(30, 25, 18, 10, 9, 9);

  /**
   * Index i gives the odds for a given level i + 1.
   * The index j in the odds gives the odds for a cost j + 1 to appear.
   * Odds are constructed such that the sum of odds = 1.
   */
  public static final List<List<Double>> ODDS = List.of(
      List.of(1.0, 0.0, 0.0, 0.0, 0.0),
      List.of(1.0, 0.0, 0.0, 0.0, 0.0),
      List.of(0.75, 0.25, 0.0, 0.0, 0.0),
      List.of(0.55, 0.3, 0.15, 0.0, 0.0),
      List.of(0.45, 0.33, 0.2, 0.02, 0.0),
      List.of(0.3, 0.4, 0.25, 0.05, 0.0),
      List.of(0.19, 0.3, 0.4, 0.1, 0.01),
      List.of(0.18, 0.25, 0.32, 0.22, 0.03),
      List.of(0.15, 0.2, 0.25, 0.3, 0.1),
      List.of(0.05, 0.1, 0.2, 0.4, 0.25));

  private List<Unit> makeDrawPool(int cost) {
    List<Unit> drawPool = new ArrayList<>();

    TFTSet.UNITS_COSTING.get(cost).forEach(unit -> {
      for (int i = 0; i < this.pool.get(unit.getName()); i++) {
        drawPool.add(unit);
      }
    });

    return drawPool;
  }

  private void clearShop() {
    for (int i = 0; i < this.units.length; i++) {
      Unit unit = this.units[i];

      if (unit == null) {
        continue;
      }

      this.addUnit(unit.getName(), unit.getCost());

      this.units[i] = null;
    }
  }

  private static boolean isValidCost(int cost) {
    return cost >= 1 && cost <= 6;
  }

  public Shop(int level) {
    if (level < 1 || level > 10) {
      throw new IllegalArgumentException("Shop level must be between 1 - 10");
    }

    this.level = level;

    for (Unit unit : TFTSet.BUYABLE_UNITS) {
      this.pool.put(unit.getName(), Shop.BAG_SIZES.get(unit.getCost() - 1));
    }

    this.nextShop();
  }

  public void nextShop() {
    this.clearShop();

    for (int i = 0; i < 5; i++) {
      double rarity = RandomUtil.nextDouble();
      int cost = 1;

      while (rarity > this.getShopOdds().get(cost - 1)) {
        rarity -= this.getShopOdds().get(cost - 1);
        cost += 1;
      }

      List<Unit> drawPool = this.makeDrawPool(cost);
      Unit unit = drawPool.get(RandomUtil.nextInt(drawPool.size()));

      this.pool.compute(unit.getName(), (k, v) -> Math.max(v - 1, 0));

      this.units[i] = unit;
    }

    // for (Unit unit : this.getUnits()) {
    // System.out.println(unit);
    // }
    // System.out.println("-");
  }

  public Unit popUnit(int index) {
    Unit unit = this.units[index];

    this.units[index] = null;

    return unit;
  }

  public void addUnit(BoardUnit unit) {
    if (unit == null) {
      throw new IllegalArgumentException("Cannot add null unit");
    }

    this.pool.compute(unit.getName(), (k, v) -> Math.min(v + unit.getCopies(), Shop.BAG_SIZES.get(unit.getCost() - 1)));
  }

  public void addUnit(String name, int cost) {
    if (!this.pool.containsKey(name)) {
      throw new IllegalArgumentException("Cannot add null unit");
    }

    if (!isValidCost(cost)) {
      throw new IllegalArgumentException("Invalid cost");
    }

    this.pool.compute(name, (k, v) -> Math.min(v + 1, Shop.BAG_SIZES.get(cost - 1)));
  }

  public Unit[] getUnits() {
    return this.units.clone();
  }

  public List<Double> getShopOdds() {
    return Shop.ODDS.get(this.level - 1);
  }

  @Override
  public void levelChanged(int level) {
    this.level = level;
  }

  @Override
  public String toString() {
    return Arrays.stream(this.units).map(Unit::toString).collect(Collectors.joining("\t|\t"));
  }

  public static void main(String[] args) {
    Shop shop = new Shop(4);
    Unit unit = shop.popUnit(0);
    System.out.println(shop.units[0]);
    System.out.println(unit);
  }
}
