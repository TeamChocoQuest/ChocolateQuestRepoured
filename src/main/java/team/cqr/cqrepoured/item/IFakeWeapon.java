package team.cqr.cqrepoured.item;

public interface IFakeWeapon<T extends Item & ISupportWeapon<?>> {

	T getOriginalItem();

}
