package com.minemarket.api.exceptions;

import com.minemarket.api.types.PendingCommand;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AfterExecutionCommandUpdateFailureException extends MineMarketException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7285914350197530126L;
	private final PendingCommand command;

}
