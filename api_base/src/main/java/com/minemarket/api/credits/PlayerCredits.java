package com.minemarket.api.credits;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerCredits {

	protected UUID uuid;
	protected String nick;
	protected int credits;
	protected long lastUpdate;
	
}
