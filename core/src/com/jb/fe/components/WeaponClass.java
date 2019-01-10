package com.jb.fe.components;

public class WeaponClass {

	public enum WeaponType {
		SWORD, AXE, LANCE, ELEMENTAL, LIGHT, DARK, STAVES, HEALING, PROMOTION, BOW, NO_WEAKNESS
	}

	public WeaponType itemClass;
	public WeaponType strongAgainst;

	public WeaponClass(WeaponType itemClass) {
		this.itemClass = itemClass;

		getOpposite();
	}

	public void getOpposite() {
		switch (this.itemClass) {
		case SWORD:
			strongAgainst = WeaponType.AXE;
			break;
		case AXE:
			strongAgainst = WeaponType.LANCE;
			break;
		case LANCE:
			strongAgainst = WeaponType.SWORD;
			break;
		case ELEMENTAL:
			strongAgainst = WeaponType.LIGHT;
			break;
		case LIGHT:
			strongAgainst = WeaponType.DARK;
			break;
		case DARK:
			strongAgainst = WeaponType.ELEMENTAL;
			break;
		default:
			strongAgainst = WeaponType.NO_WEAKNESS;
			break;
		}
	}
	
	public String toString() {
		return itemClass.toString();
	}

}
