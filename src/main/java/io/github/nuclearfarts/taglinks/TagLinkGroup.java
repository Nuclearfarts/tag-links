package io.github.nuclearfarts.taglinks;

import net.minecraft.tag.Tag;
import net.minecraft.tag.Tag.Builder;

public class TagLinkGroup {
	private Tag.Builder<?> builder;
	
	@SuppressWarnings("unchecked")
	public <T> Tag.Builder<T> getTagBuilder(boolean ordered) {
		if(builder == null) {
			builder = Tag.Builder.create();
			builder.ordered(ordered);
		}
		return (Builder<T>) builder;
	}
}
