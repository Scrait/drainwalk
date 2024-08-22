package com.tom.cpl.tag;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CPMTag<T> implements Tag<T> {
	private final TagManager<T> manager;
	protected List<T> stacks;
	protected final List<String> entries;
	protected final String id;
	protected final boolean builtin;

	public CPMTag(TagManager<T> manager, String id, List<String> entries) {
		this.manager = manager;
		this.id = id;
		this.entries = entries;
		this.builtin = false;
	}

	public CPMTag(TagManager<T> manager, String id, List<String> entries, boolean builtin) {
		this.manager = manager;
		this.id = id;
		this.entries = builtin ? Collections.unmodifiableList(entries) : entries;
		this.builtin = builtin;
	}

	@Override
	public List<T> getAllStacks() {
		return getAllStacksInt(null);
	}

	protected List<T> getAllStacksInt(Set<CPMTag<T>> dejavu) {
		if(stacks == null)stacks = manager.listStacksInt(entries, dejavu);
		return stacks;
	}

	@Override
	public boolean is(T stack) {
		return manager.isInTag(entries, stack);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entries == null) ? 0 : entries.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		CPMTag other = (CPMTag) obj;
		if (entries == null) {
			if (other.entries != null) return false;
		} else if (!entries.equals(other.entries)) return false;
		return true;
	}

	@Override
	public String getId() {
		return "$" + id;
	}

	public List<String> getEntries() {
		return entries;
	}

	public boolean isBuiltin() {
		return builtin;
	}
}
