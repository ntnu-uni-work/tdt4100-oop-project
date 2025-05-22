package com.tftrolldownsimulator.models;

import java.util.List;

import com.tftrolldownsimulator.models.generated.Trait;

class UnitStats {
  private double armor, attackSpeed, critChance, critMultiplier, damage, hp, initialMana, magicResist, mana, range;

  public double getRange() {
    return range;
  }

  public double getMana() {
    return mana;
  }

  public double getMagicResist() {
    return magicResist;
  }

  public double getInitialMana() {
    return initialMana;
  }

  public double getHp() {
    return hp;
  }

  public double getDamage() {
    return damage;
  }

  public double getCritMultiplier() {
    return critMultiplier;
  }

  public double getCritChance() {
    return critChance;
  }

  public double getAttackSpeed() {
    return attackSpeed;
  }

  public double getArmor() {
    return armor;
  }
}

public class Unit {
  private int cost;
  private String name, icon;
  private UnitStats stats;
  private List<Trait> traits;

  public Unit(Unit unit) {
    this.cost = unit.cost;
    this.name = unit.name;
    this.icon = unit.icon;
    this.stats = unit.stats;
    this.traits = unit.traits;
  }

  public int getCost() {
    return cost;
  }

  public String getIcon() {
    return icon;
  }

  public String getName() {
    return name;
  }

  public UnitStats getStats() {
    return stats;
  }

  public List<Trait> getTraits() {
    return traits;
  }

  @Override
  public String toString() {
    return String.format("%s (%d)", this.name, this.cost);
  }
}
