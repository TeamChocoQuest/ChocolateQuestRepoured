package team.cqr.cqrepoured.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public interface IMainMod {
	
	public default List<ISubProjectMain> getSubModMains() {
		List<ISubProjectMain> result = new ArrayList<>();
		Iterator<ISubProjectMain> iterator = ServiceLoader.load(ISubProjectMain.class).iterator();
		while(iterator.hasNext()) {
			result.add(iterator.next());
		}
		return result;
	}

}
