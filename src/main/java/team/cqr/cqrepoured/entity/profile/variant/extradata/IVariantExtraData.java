package team.cqr.cqrepoured.entity.profile.variant.extradata;

import com.mojang.serialization.Codec;

public interface IVariantExtraData<T> {
	
	public T getExtraData();
	public void setExtraData(T data);
	
	public Codec<? extends IVariantExtraData<? extends T>> getType();

}
