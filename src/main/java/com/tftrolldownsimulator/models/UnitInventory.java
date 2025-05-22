package com.tftrolldownsimulator.models;

public class UnitInventory {
  private Bench bench;
  private Board board;

  public UnitInventory(int level) {
    this.bench = new Bench();
    this.board = new Board(level);
  }

  public BoardUnit[] getBenchUnits() {
    return this.bench.getUnits();
  }

  private static boolean hasPair(Unit unit, int starLevel, int[] benchCount, int[] boardCount) {
    return benchCount[starLevel - 1] == 2
        || (benchCount[starLevel - 1] == 1 && boardCount[starLevel - 1] == 1)
        || boardCount[starLevel - 1] == 2;
  }

  public boolean canAddUnit(Unit unit) {
    int[] benchCount = this.bench.unitCount(unit);
    int[] boardCount = this.board.unitCount(unit);

    return !this.bench.isFull() || UnitInventory.hasPair(unit, 1, benchCount, boardCount);
  }

  private void mergeUnit(Unit unit, int toStarLevel) {
    if (this.board.hasUnit(unit)) {
      this.board.upgradeUnit(unit, toStarLevel);
      this.board.removeMerging(unit, toStarLevel);
    } else {
      this.bench.upgradeUnit(unit, toStarLevel);
    }
    this.bench.removeMerging(unit, toStarLevel);
  }

  public void addUnit(Unit unit) {
    BoardUnit boardUnit = new BoardUnit(unit);

    int[] benchCount = this.bench.unitCount(unit);
    int[] boardCount = this.board.unitCount(unit);

    if (UnitInventory.hasPair(boardUnit, 1, benchCount, boardCount)
        && UnitInventory.hasPair(boardUnit, 2, benchCount, boardCount)
        && UnitInventory.hasPair(boardUnit, 3, benchCount, boardCount)) {
      this.mergeUnit(unit, 4);
      return;
    }

    if (UnitInventory.hasPair(boardUnit, 1, benchCount, boardCount)
        && UnitInventory.hasPair(boardUnit, 2, benchCount, boardCount)) {
      this.mergeUnit(unit, 3);
      return;
    }

    if (UnitInventory.hasPair(boardUnit, 1, benchCount, boardCount)) {
      this.mergeUnit(unit, 2);
      return;
    }

    this.bench.addUnit(boardUnit);
  }

  public int sellUnit(int index) {
    BoardUnit unit = this.bench.popUnit(index);

    if (unit.getStarLevel() == 1) {
      return unit.getCost();
    }

    if (unit.getCost() == 1) {
      return (unit.getStarLevel() - 1) * 3;
    }

    return unit.getCost() * (unit.getStarLevel() - 1) * 3 - 1;
  }

  public void moveToBench(int from, int to) {
    BoardUnit unit = this.bench.popUnit(from);
    BoardUnit swapped = this.bench.putUnit(unit, to);

    if (swapped == null) {
      return;
    }

    this.bench.putUnit(swapped, from);
  }
}
