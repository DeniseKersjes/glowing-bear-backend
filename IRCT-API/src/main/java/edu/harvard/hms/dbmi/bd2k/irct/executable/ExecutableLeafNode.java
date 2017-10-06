/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package edu.harvard.hms.dbmi.bd2k.irct.executable;

import edu.harvard.hms.dbmi.bd2k.irct.action.Action;
import edu.harvard.hms.dbmi.bd2k.irct.event.IRCTEventListener;
import edu.harvard.hms.dbmi.bd2k.irct.exception.ResourceInterfaceException;
import edu.harvard.hms.dbmi.bd2k.irct.model.result.Result;
import edu.harvard.hms.dbmi.bd2k.irct.model.security.User;
import edu.harvard.hms.dbmi.bd2k.irct.util.Utilities;

/**
 * A leaf node in an execution tree that can be executed. It does not have any
 * children
 */
public class ExecutableLeafNode implements Executable {

	private User user;
	private Action action;
	private ExecutableStatus state;
	
	private IRCTEventListener irctEventListener;

	@Override
	public void setup(User user) {
		this.user = user;
		this.state = ExecutableStatus.CREATED;
		
		this.irctEventListener = Utilities.getIRCTEventListener();
	}

	@Override
	public void run() throws ResourceInterfaceException {
		irctEventListener.beforeAction(user, action);
		
		this.state = ExecutableStatus.RUNNING;
		this.action.run(this.user);
		this.state = ExecutableStatus.COMPLETED;
		
		irctEventListener.afterAction(user, action);
	}

	@Override
	public ExecutableStatus getStatus() {
		return this.state;
	}

	@Override
	public Result getResults() throws ResourceInterfaceException {
		return this.action.getResults(this.user);
	}

	/**
	 * Returns the action that is to be executed
	 * 
	 * @return Action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * Sets the action that is to be executed
	 * 
	 * @param action
	 *            Action
	 */
	public void setAction(Action action) {
		this.action = action;
	}

}
