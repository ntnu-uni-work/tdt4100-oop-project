package com.tftrolldownsimulator.models;

public class Board implements LevelObserver {
  public static final int ROWS = 4;
  public static final int COLS = 7;

  private BoardUnit[][] unitGrid = new BoardUnit[ROWS][COLS];

  private int maxUnitCount;
  private int unitCount;

  public Board(int level) {
    this.maxUnitCount = level;
  }

  public BoardUnit putUnit(BoardUnit unit, int row, int col) {
    if (unitCount == maxUnitCount) {
      throw new IllegalStateException("You cannot place more units");
    }

    BoardUnit currentSpot = this.unitGrid[row][col];

    this.unitGrid[row][col] = unit;

    return currentSpot;
  }

  public boolean hasUnit(Unit unit) {
    for (int i = 0; i < ROWS; i++) {
      for (int j = 0; j < COLS; j++) {
        BoardUnit boardUnit = this.unitGrid[i][j];

        if (boardUnit == null) {
          continue;
        }

        if (boardUnit.getName() == unit.getName()) {
          return true;
        }
      }
    }

    return false;
  }

  public BoardUnit popUnit(int row, int col) {
    BoardUnit unit = this.unitGrid[row][col];

    if (unit == null) {
      throw new IllegalArgumentException(String.format("There is no unit at (%d, %d)", row, col));
    }

    this.unitGrid[row][col] = null;

    return unit;
  }

  public int[] unitCount(Unit unit) {
    int[] count = new int[4];

    for (int i = 0; i < ROWS; i++) {
      for (int j = 0; j < COLS; j++) {
        BoardUnit boardUnit = this.unitGrid[i][j];

        if (boardUnit == null) {
          continue;
        }

        if (boardUnit.getName() == unit.getName()) {
          count[boardUnit.getStarLevel() - 1] += 1;
        }
      }
    }

    return count;
  }

  public void removeMerging(Unit unit, int starLevel) {
    for (int i = 0; i < ROWS; i++) {
      for (int j = 0; j < COLS; j++) {
        BoardUnit boardUnit = this.unitGrid[i][j];

        if (boardUnit == null) {
          continue;
        }

        if (boardUnit.getStarLevel() < starLevel && boardUnit.getName() == unit.getName()) {
          this.unitGrid[i][j] = null;
        }
      }
    }
  }

  public void upgradeUnit(Unit unit, int starLevel) {
    for (int i = 0; i < ROWS; i++) {
      for (int j = 0; j < COLS; j++) {
        BoardUnit boardUnit = this.unitGrid[i][j];

        if (boardUnit == null) {
          continue;
        }

        if (boardUnit.getStarLevel() == starLevel - 1 && boardUnit.getName() == unit.getName()) {
          boardUnit.levelUp();
          return;
        }
      }
    }
  }

  @Override
  public void levelChanged(int level) {
    this.maxUnitCount = level;
  }
}
