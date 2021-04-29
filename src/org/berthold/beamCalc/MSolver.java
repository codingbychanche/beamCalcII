package org.berthold.beamCalc;

import java.util.ArrayList;
import java.util.List;

/**
 * Calculates the bending moments along the length of a {@link Beam}- object.
 * Q(x) => M(x)
 * 
 * @author Berthold
 *
 */
public class MSolver {

	// Any step between two nighboring shearing forces counts as an discontiunuity
	private static final int DISCONTIUNUITY_THRESHOLD = 1;
	
	/**
	 * Calculates the bending moments along the beam.
	 * 
	 * Algorithm:<br>
	 * q(n+1)-q(n) x X (distance from left end of beam)
	 * 
	 * @param qTable
	 * @param beam
	 * @return A table of {@link StressResultantValue}- objects containing the
	 *         bending moments along the beam => M(x).
	 */
	public static StressResultantTable solve(StressResultantTable qTable, Beam beam) {

		StressResultantTable mTable = new StressResultantTable(beam, qTable.getSectionLength_m());
	
		double m_Nm = 0;
		double m1_Nm = 0;
		double q_N, q1_N;
		double deltaM_Nm = 0;
		double sectionLength_m = qTable.getSectionLength_m();
		double x = 0;

		for (int n = 0; n <= qTable.getLength() - 2; n++) {
       
			
			q_N = qTable.getShearingForceAtIndex(n).getShearingForce();
			q1_N = qTable.getShearingForceAtIndex(n + 1).getShearingForce();
			double deltaQ_N = q1_N - q_N;

			// Checks if leading sign changes or if there is an discontiunuity
			// if so, disregard....
			if (Math.signum(q_N) == Math.signum(q1_N) && Math.abs(deltaQ_N) <= DISCONTIUNUITY_THRESHOLD) {
				m_Nm = q_N * x;
				m1_Nm = q1_N * (x + sectionLength_m);
				deltaM_Nm = m1_Nm - m_Nm;
			} else {
				
			}
			
			m_Nm = mTable.getShearingForceAtIndex(n).getShearingForce();
			mTable.getShearingForceAtIndex(n + 1).setShearingForce(m_Nm + deltaM_Nm);
		
			// Check for disconuinity in Q(x) because
			// M(x) must also be a diconuinity
			if (qTable.getShearingForceAtIndex(n).isDiscontiunuity()) {
				mTable.getShearingForceAtIndex(n).setDiscontiunuity(true);
				mTable.getShearingForceAtIndex(n).setShearingForceDeltaBy(m_Nm+deltaM_Nm);
			}	

			// Next
			x = x + sectionLength_m;
		}
		return mTable;
	}
}