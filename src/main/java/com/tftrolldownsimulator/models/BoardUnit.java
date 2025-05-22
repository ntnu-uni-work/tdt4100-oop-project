package com.tftrolldownsimulator.models;

public class BoardUnit extends Unit {
  private int starLevel = 1;

  public BoardUnit(Unit unit) {
    super(unit);
  }

  public int getStarLevel() {
    return starLevel;
  }

  public void levelUp() {
    starLevel += 1;
  }

  public int getCopies() {
    if (this.starLevel == 1) {
      return 1;
    }

    return this.starLevel * 3;
  }
}
