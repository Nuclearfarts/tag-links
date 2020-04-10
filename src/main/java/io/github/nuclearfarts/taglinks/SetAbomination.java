package io.github.nuclearfarts.taglinks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SetAbomination<T> {

	private final Map<T, Set<Set<T>>> setsEachEntryIsIn = new HashMap<>();
	private final Map<Set<T>, Set<Set<T>>> setsOverlappingWith = new HashMap<>();
	
	private final Set<Set<T>> processed = new HashSet<>();
	
	public void addSet(Set<T> set) {
		Set<Set<T>> overlap = new HashSet<>();
		for(T t : set) {
			Set<Set<T>> sets = setsEachEntryIsIn.computeIfAbsent(t, o -> new HashSet<>());
			for(Set<T> overlappingSet : sets) {
				setsOverlappingWith.get(overlappingSet).add(set);
				overlap.add(overlappingSet);
			}
			sets.add(set);
		}
		setsOverlappingWith.put(set, overlap);
	}
	
	public Set<Set<T>> calculate() {
		Set<Set<T>> result = new HashSet<>();
		for(Set<T> set : setsOverlappingWith.keySet()) {
			if(!processed.contains(set)) {
				result.add(process(set));
			}
		}
		return result;
	}
	
	private Set<T> process(Set<T> set) {
		Set<T> result = new HashSet<>();
		if(processed.add(set)) {
			result.addAll(set);
			Set<Set<T>> overlappingSets = setsOverlappingWith.get(set);
			for(Set<T> overlapSet : overlappingSets) {
				result.addAll(process(overlapSet));
			}
		}
		return result;
	}
}
