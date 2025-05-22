package com.tftrolldownsimulator.persistence;

public interface Persistence<T> {
  public T getState();

  public void save();

  public void load();
}
