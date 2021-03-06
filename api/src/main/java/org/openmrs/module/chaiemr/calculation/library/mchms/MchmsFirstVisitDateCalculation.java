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

package org.openmrs.module.chaiemr.calculation.library.mchms;

import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Program;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.Calculations;
import org.openmrs.module.chaicore.calculation.Filters;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.chaiemr.calculation.EmrCalculationUtils;
import org.openmrs.module.chaiemr.metadata.MchMetadata;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Calculates the date on which an MCH patient had her first MCHMS consultation visit
 */
public class MchmsFirstVisitDateCalculation extends AbstractPatientCalculation {

	/**
	 * @should return null for patients who have not tested for HIV
	 * @should return test date for patients who have tested for HIV
	 * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(java.util.Collection, java.util.Map, org.openmrs.calculation.patient.PatientCalculationContext)
	 */
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> params,
										 PatientCalculationContext context) {

		Program mchmsProgram = MetadataUtils.existing(Program.class, MchMetadata._Program.MCHMS);
		EncounterType mchConsultation = MetadataUtils.existing(EncounterType.class, MchMetadata._EncounterType.MCHMS_CONSULTATION);

		// Get all patients who are alive and in MCH-MS program
		Set<Integer> alive = Filters.alive(cohort, context);
		Set<Integer> inMchmsProgram = Filters.inProgram(mchmsProgram, alive, context);

		CalculationResultMap crm = Calculations.firstEncounter(mchConsultation, inMchmsProgram, context);

		CalculationResultMap resultMap = new CalculationResultMap();

		for (Integer ptId : cohort) {
			if (inMchmsProgram.contains(ptId)) {
				Encounter encounter = EmrCalculationUtils.encounterResultForPatient(crm, ptId);
				if (encounter != null) {
					resultMap.put(ptId, new SimpleResult(encounter.getEncounterDatetime(), null));
				} else {
					resultMap.put(ptId, null);
				}
			} else {
				resultMap.put(ptId, null);
			}
		}

		return resultMap;
	}
}