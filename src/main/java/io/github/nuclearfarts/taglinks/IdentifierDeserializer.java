package io.github.nuclearfarts.taglinks;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.util.Identifier;

public class IdentifierDeserializer implements JsonDeserializer<Identifier> {
	public static final IdentifierDeserializer INSTANCE = new IdentifierDeserializer();
	
	private IdentifierDeserializer() { }
	
	@Override
	public Identifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return new Identifier(json.getAsString());
	}

}
