package com.tftrolldownsimulator.models;

import java.util.ArrayList;
import java.util.List;

public class ExperienceManager {
  private int level;
  private int xp;

  private List<LevelObserver> levelObservers = new ArrayList<>();

  /**
   * index i gives the xp required to go from level i to level i + 1,
   * eg. i = 8 gives 64 xp to level from level 9 to 10.
   * 
   * Level intervals is slightly changed at level 1 and 2 (2, 2 -> 4, 4) to make
   * reaching level 2 possible
   */
  public static final List<Integer> LEVEL_UP_INTERVALS = List.of(0, 4, 4, 6, 10, 20, 36, 48, 76, 84);

  public static final int XP_GAIN = 4;

  public ExperienceManager() {
    this.level = 1;
    this.xp = 0;
  }

  public int getLevel() {
    return this.level;
  }

  public int getXP() {
    return this.xp;
  }

  public int getRequiredXP() {
    if (!this.canAddXP()) {
      return 0;
    }

    return ExperienceManager.LEVEL_UP_INTERVALS.get(this.level);
  }

  public void addXP() {
    if (!this.canAddXP()) {
      throw new IllegalStateException("You cannot buy XP when you are at the highest level");
    }

    this.xp += ExperienceManager.XP_GAIN;

    while (this.xp >= ExperienceManager.LEVEL_UP_INTERVALS.get(this.level)) {
      this.xp -= ExperienceManager.LEVEL_UP_INTERVALS.get(this.level);
      this.level += 1;
      notifyLevelObservers(this.level);

      if (!canAddXP()) {
        this.xp = 0;
        break;
      }
    }
  }

  public boolean canAddXP() {
    return this.level != ExperienceManager.LEVEL_UP_INTERVALS.size();
  }

  public void addLevelObserver(LevelObserver observer) {
    this.levelObservers.add(observer);
  }

  private void notifyLevelObservers(int level) {
    this.levelObservers.forEach(levelObserver -> levelObserver.levelChanged(level));
  }
}
