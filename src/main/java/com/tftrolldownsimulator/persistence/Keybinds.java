package com.tftrolldownsimulator.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.tftrolldownsimulator.models.Keybinding;

import javafx.scene.input.KeyCode;

public class Keybinds implements Persistence<Map<Keybinding, KeyCode>> {
  private static final String FILE_PATH = "data/keybinds.cfg";

  private Map<Keybinding, KeyCode> keybinds;
  private Map<KeyCode, Keybinding> reverseKeybinds;

  public Keybinds() {
    this.keybinds = new HashMap<>();
    this.reverseKeybinds = new HashMap<>();
  }

  public Keybinds(boolean load) {
    this.keybinds = new HashMap<>();
    this.reverseKeybinds = new HashMap<>();

    if (load) {
      this.load();
    }
  }

  @Override
  public Map<Keybinding, KeyCode> getState() {
    return Collections.unmodifiableMap(this.keybinds);
  }

  public Map<KeyCode, Keybinding> getReverseState() {
    return Collections.unmodifiableMap(this.reverseKeybinds);
  }

  public void setKeybinding(Keybinding key, KeyCode value) {
    if (key == null || value == null) {
      throw new IllegalArgumentException("Parameters cannot be null");
    }

    if (this.reverseKeybinds.containsKey(value)) {
      throw new IllegalStateException("Cannot have two of the same keybinding");
    }

    KeyCode oldBinding = this.keybinds.get(key);

    this.keybinds.put(key, value);
    this.reverseKeybinds.put(value, key);

    if (oldBinding != null) {
      this.reverseKeybinds.remove(oldBinding);
    }
  }

  @Override
  public void save() {
    File file = new File(FILE_PATH);

    file.getParentFile().mkdirs();

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      for (var entry : this.keybinds.entrySet()) {
        writer.write(String.format("%s=%s\n", entry.getKey().toString(), entry.getValue()));
      }
    } catch (IOException e) {
      System.err.println("Error saving data: " + e.getMessage());
    }
  }

  @Override
  public void load() {
    File file = new File(FILE_PATH);

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;

      while ((line = reader.readLine()) != null) {
        String[] entry = line.split("=");
        this.keybinds.put(Keybinding.valueOf(entry[0]), KeyCode.valueOf(entry[1]));
        this.reverseKeybinds.put(KeyCode.valueOf(entry[1]), Keybinding.valueOf(entry[0]));
      }
    } catch (IOException e) {
      System.err.println("Error loading data: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.err.println("You have a corrupt keybindings file!");
    }
  }

  public static void main(String[] args) {
    Keybinds a = new Keybinds();
    a.load();
  }
}
