package com.tftrolldownsimulator.models;

import java.util.List;

public class Tactician {
  private int gold;
  private ExperienceManager experience;
  private Shop shop;
  private UnitInventory unitInventory;

  public static final int XP_COST = 4;
  public static final int REROLL_COST = 2;

  public Tactician() {
    this.experience = new ExperienceManager();

    int initialLevel = this.experience.getLevel();

    this.shop = new Shop(initialLevel);
    this.experience.addLevelObserver(this.shop);

    this.unitInventory = new UnitInventory(initialLevel);
  }

  public boolean canBuyXP() {
    return this.gold >= XP_COST && this.experience.canAddXP();
  }

  public boolean canReroll() {
    return this.gold >= REROLL_COST;
  }

  public void setGold() {
    System.out.println("clicked");
    this.gold = 10000;
  }

  public int getGold() {
    return this.gold;
  }

  public int getLevel() {
    return this.experience.getLevel();
  }

  public String getXPProgress() {
    if (!this.experience.canAddXP()) {
      return "";
    }

    return String.format("%d/%d", this.experience.getXP(), this.experience.getRequiredXP());
  }

  public Unit[] getShopUnits() {
    return this.shop.getUnits();
  }

  public List<Double> getShopOdds() {
    return this.shop.getShopOdds();
  }

  public BoardUnit[] getBenchUnits() {
    return this.unitInventory.getBenchUnits();
  }

  public void buyXP() {
    if (!this.canBuyXP()) {
      return;
    }

    this.gold -= XP_COST;

    this.experience.addXP();
  }

  public void rerollShop() {
    if (!this.canReroll()) {
      return;
    }

    this.gold -= REROLL_COST;

    this.shop.nextShop();
  }

  public void buyUnit(int index) {
    Unit unit = this.shop.getUnits()[index];

    if (unit == null) {
      return;
    }

    if (this.gold < unit.getCost()) {
      return;
    }

    if (!this.unitInventory.canAddUnit(unit)) {
      return;
    }

    this.gold -= unit.getCost();

    Unit boughtUnit = this.shop.popUnit(index);

    this.unitInventory.addUnit(boughtUnit);
  }

  public void sellUnit(int index) {
    this.shop.addUnit(this.unitInventory.getBenchUnits()[index]);

    int sellValue = this.unitInventory.sellUnit(index);

    this.gold += sellValue;
  }

  public void moveUnitToBench(int from, int to) {
    this.unitInventory.moveToBench(from, to);
  }
}
