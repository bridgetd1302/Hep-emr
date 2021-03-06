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

package org.openmrs.module.chaiemr.calculation.library.hiv.art;

import org.openmrs.Concept;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.BooleanResult;
import org.openmrs.module.chaicore.calculation.CalculationUtils;
import org.openmrs.module.chaicore.calculation.Calculations;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.calculation.EmrCalculationUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Calculates whether a patient is a transfer in based on the status
 */
public class IsTransferInCalculation extends AbstractPatientCalculation {

	/**
	 * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(java.util.Collection,
	 *      java.util.Map, org.openmrs.calculation.patient.PatientCalculationContext)
	 */
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues,
										 PatientCalculationContext context) {

		Concept transferInStatus = Dictionary.getConcept(Dictionary.TRANSFER_IN);

		CalculationResultMap transferInStatusResults = Calculations.lastObs(transferInStatus,cohort,context);
		Set<Integer> transferInDate = CalculationUtils.patientsThatPass(calculate(new TransferInDateCalculation(), cohort, context));

		CalculationResultMap result = new CalculationResultMap();
		for (Integer ptId : cohort) {
			boolean isTransferIn = false;

			Concept status = EmrCalculationUtils.codedObsResultForPatient(transferInStatusResults, ptId);

			if (((status != null) && (status.equals(Dictionary.getConcept(Dictionary.YES)))) || transferInDate.contains(ptId) ) {
				isTransferIn = true;
			}

			result.put(ptId, new BooleanResult(isTransferIn, this, context));
		}
		return result;
	}
}
