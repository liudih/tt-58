package com.tomtop.services.common;

import java.util.UUID;

/**
 * Standardize UUID generation.
 * 
 * @author kmtong
 *
 */
public class UUIDGenerator {

	public static String createAsString() {
		UUID uuid = UUID.randomUUID();
		if (uuid != null) {
			return uuid.toString();
		}
		return null;
	}

	public static UUID create() {
		return UUID.randomUUID();
	}

}
