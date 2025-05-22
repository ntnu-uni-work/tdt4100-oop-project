package com.tftrolldownsimulator.models.deserializers;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.tftrolldownsimulator.models.generated.Trait;

public class TraitDeserializer implements JsonDeserializer<Trait> {
  @Override
  public Trait deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    return Trait.fromString(json.getAsString());
  }
}