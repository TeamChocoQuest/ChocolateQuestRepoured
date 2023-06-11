package team.cqr.cqrepoured.item;

public interface ISupportWeapon<T extends Item & IFakeWeapon<?>> {

	T getFakeWeapon();

}
