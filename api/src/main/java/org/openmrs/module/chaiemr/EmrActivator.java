/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.chaiemr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.openmrs.api.context.Context;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.chaicore.CoreContext;
import org.openmrs.module.chaiemr.advice.TestAdvice;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class EmrActivator implements ModuleActivator {

	protected static final Log log = LogFactory.getLog(EmrActivator.class);

	static {
		// Possibly bad practice but we really want to see these startup log messages
		LogManager.getLogger("org.openmrs.module.chaicore").setLevel(Level.INFO);
		LogManager.getLogger("org.openmrs.module.chaiemr").setLevel(Level.INFO);
	}

	/**
	 * @see ModuleActivator#willRefreshContext()
	 */
	public void willRefreshContext() {
		log.info("ChaiEMR context refreshing...");
	}

	/**
	 * @see ModuleActivator#willStart()
	 */
	public void willStart() {
		log.info("ChaiEMR starting...");
	}

	/**
	 * @see ModuleActivator#contextRefreshed()
	 */
	public void contextRefreshed() {
		Configuration.configure();

		try {
			CoreContext.getInstance().refresh();
		}
		catch (Exception ex) {
			// If an error occurs during core refresh, we need ChaiEMR to still start so that the error can be
			// communicated to an admin user. So we catch exceptions, log them and alert super users.
			log.error("Unable to refresh core context", ex);

			// TODO re-enable once someone fixes TRUNK-4267
			//Context.getAlertService().notifySuperUsers("Unable to start ChaiEMR", ex);
		}
	}

	/**
	 * @see ModuleActivator#started()
	 */
	public void started() {
		log.info("ChaiEMR started");
	}

	/**
	 * @see ModuleActivator#willStop()
	 */
	public void willStop() {
		log.info("ChaiEMR stopping...");
	}

	/**
	 * @see ModuleActivator#stopped()
	 */
	public void stopped() {
		log.info("ChaiEMR stopped");
	}
}