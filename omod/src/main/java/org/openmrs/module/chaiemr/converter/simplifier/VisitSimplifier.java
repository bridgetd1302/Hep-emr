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

package org.openmrs.module.chaiemr.converter.simplifier;

import org.openmrs.Visit;
import org.openmrs.module.chaiui.ChaiUiUtils;
import org.openmrs.module.chaiui.simplifier.AbstractSimplifier;
import org.openmrs.ui.framework.SimpleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Converts visit to simple object
 */
@Component
public class VisitSimplifier extends AbstractSimplifier<Visit> {

	@Autowired
	private ChaiUiUtils chaiui;

	/**
	 * @see AbstractSimplifier#simplify(Object)
	 */
	@Override
	protected SimpleObject simplify(Visit visit) {
		SimpleObject ret = new SimpleObject();
		ret.put("id", visit.getId());
		ret.put("visitType", visit.getVisitType().getName());
		ret.put("startDatetime", chaiui.formatDateParam(visit.getStartDatetime()));
		ret.put("stopDatetime", chaiui.formatDateParam(visit.getStopDatetime()));
		return ret;
	}
}