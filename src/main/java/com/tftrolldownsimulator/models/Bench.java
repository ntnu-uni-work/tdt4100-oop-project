package com.tftrolldownsimulator.models;

public class Bench {
  public static final int SIZE = 9;

  private BoardUnit[] units = new BoardUnit[SIZE];

  public Bench() {

  }

  public boolean isFull() {
    for (BoardUnit unit : units) {
      if (unit == null) {
        return false;
      }
    }

    return true;
  }

  public BoardUnit[] getUnits() {
    return this.units;
  }

  public void addUnit(BoardUnit unit) {
    for (int i = 0; i < SIZE; i++) {
      if (this.units[i] == null) {
        this.units[i] = unit;
        return;
      }
    }

    throw new IllegalStateException("Your bench is full");
  }

  public BoardUnit putUnit(BoardUnit unit, int index) {
    BoardUnit currentSpot = this.units[index];

    this.units[index] = unit;

    return currentSpot;
  }

  public BoardUnit popUnit(int index) {
    BoardUnit unit = this.units[index];

    if (unit == null) {
      throw new IllegalArgumentException(String.format("There is no unit at %d", index));
    }

    this.units[index] = null;

    return unit;
  }

  public int[] unitCount(Unit unit) {
    int[] count = new int[4];

    for (int i = 0; i < SIZE; i++) {
      BoardUnit benchUnit = units[i];

      if (benchUnit == null) {
        continue;
      }

      if (benchUnit.getName() == unit.getName()) {
        count[benchUnit.getStarLevel() - 1] += 1;
      }
    }

    return count;
  }

  public void removeMerging(Unit unit, int starLevel) {
    for (int i = 0; i < SIZE; i++) {
      BoardUnit benchUnit = this.units[i];

      if (benchUnit == null) {
        continue;
      }

      if (benchUnit.getName() == unit.getName() && benchUnit.getStarLevel() < starLevel) {
        this.units[i] = null;
      }
    }
  }

  public void upgradeUnit(Unit unit, int starLevel) {
    for (int i = 0; i < SIZE; i++) {
      BoardUnit benchUnit = this.units[i];

      if (benchUnit == null) {
        continue;
      }

      if (benchUnit.getStarLevel() == starLevel - 1 && benchUnit.getName() == unit.getName()) {
        benchUnit.levelUp();
        return;
      }
    }
  }
}
