package io.github.nuclearfarts.taglinks.mixin;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagContainer;
import net.minecraft.util.Identifier;

import io.github.nuclearfarts.taglinks.IdentifierDeserializer;
import io.github.nuclearfarts.taglinks.SetAbomination;
import io.github.nuclearfarts.taglinks.TagLinkGroup;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Predicates;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TagContainer.class)
public class TagContainerMixin<T> {
	@Unique private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Identifier.class, IdentifierDeserializer.INSTANCE).create();
	@Unique private static final Logger LOGGER = LogManager.getLogger("TagLinks");
	@Unique private final Map<Identifier, TagLinkGroup> data = new HashMap<>();
	
	private @Shadow @Final String dataType;
	private @Shadow @Final boolean ordered;
	
	@Inject(at = @At("HEAD"), method = "method_18243(Lnet/minecraft/resource/ResourceManager;)Ljava/util/Map;") // lambda - CompletableFuture.supplyAsync argument, head of prepareReload
	private void loadResources(ResourceManager manager, CallbackInfoReturnable<Map<Identifier, Tag.Builder<T>>> callback) {
		Collection<Identifier> resources = manager.findResources("taglinks/" + dataType + ".json", Predicates.alwaysTrue());
		SetAbomination<Identifier> setThing = new SetAbomination<>();
		for(Identifier id : resources) {
			Resource r;
			try {
				r = manager.getResource(id);
				Identifier[][] groups = GSON.fromJson(new InputStreamReader(r.getInputStream()), Identifier[][].class);
				for(Identifier[] group : groups) {
					setThing.addSet(Sets.newHashSet(group));
				}
			} catch (Exception e) {
				LOGGER.error("Error loading tag link data " + id, e);
			}
		}
		Set<Set<Identifier>> mergedGroups = setThing.calculate();
		System.out.println("Loaded " + mergedGroups);
		for(Set<Identifier> groupSet : mergedGroups) {
			TagLinkGroup group = new TagLinkGroup();
			for(Identifier tag : groupSet) {
				data.put(tag, group);
			}
		}
	}
	
	@Inject(at = @At("HEAD"), method = "method_20590(Lnet/minecraft/util/Identifier;)Lnet/minecraft/tag/Tag$Builder;", cancellable = true) // lambda -- computeIfAbsent argument, line 113
	private void injectLinks(Identifier id, CallbackInfoReturnable<Tag.Builder<T>> callback) {
		TagLinkGroup link = null;
		if((link = data.get(id)) != null) {
			callback.setReturnValue(link.getTagBuilder(ordered));
		}
	}
}
