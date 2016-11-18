package com.oprun.helper;

import java.util.Map;
import java.util.Map.Entry;

public class HelperMap {

	private static Integer findByKey(Map<Integer, String> testMap, String value) {
		for (Entry<Integer, String> entry : testMap.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}

}
