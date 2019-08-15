package com.prototype.service;

import java.io.Closeable;
import java.util.UUID;


public abstract class Service implements Closeable {

	private UUID uuid;

	public Service() {

		//
	}

	public UUID getUuid() {

		return uuid;
	}

	public void setUuid(UUID uuid) {

		this.uuid = uuid;
	}
}
