package com.jb.fe.components;

public class ItemType {

	public enum ItemClass {
		SWORD, AXE, LANCE, ELEMENTAL, LIGHT, DARK, STAVES, HEALING, PROMOTION, BOW, NO_WEAKNESS
	}

	public ItemClass itemClass;
	public ItemClass strongAgainst;

	public ItemType(ItemClass itemClass) {
		this.itemClass = itemClass;

		getOpposite();
	}

	public void getOpposite() {
		switch (this.itemClass) {
		case SWORD:
			strongAgainst = ItemClass.AXE;
			break;
		case AXE:
			strongAgainst = ItemClass.LANCE;
			break;
		case LANCE:
			strongAgainst = ItemClass.SWORD;
			break;
		case ELEMENTAL:
			strongAgainst = ItemClass.LIGHT;
			break;
		case LIGHT:
			strongAgainst = ItemClass.DARK;
			break;
		case DARK:
			strongAgainst = ItemClass.ELEMENTAL;
			break;
		default:
			strongAgainst = ItemClass.NO_WEAKNESS;
			break;
		}
	}

}
