package de.fruitfly.kwirk.tile;

import de.fruitfly.kwirk.Entity;

public class RefTile extends Tile {
	private Entity parent;

	public RefTile(Entity parent) {
		this.parent = parent;
	}

	public Entity getParent() {
		return this.parent;
	}
}
