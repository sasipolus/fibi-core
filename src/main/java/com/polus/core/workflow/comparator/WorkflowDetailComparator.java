package com.polus.core.workflow.comparator;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.polus.core.workflow.pojo.WorkflowDetail;

/**
 * This comparator sorts a list of WorkflowDetail by approvalStopNumber, and
 * approverNumber into ascending order.
 *
 */
public class WorkflowDetailComparator implements Comparator<WorkflowDetail> {

	@Override
	public int compare(WorkflowDetail wd1, WorkflowDetail wd2) {
		return new CompareToBuilder().append(wd1.getMapNumber(), wd2.getMapNumber())
				.append(wd1.getApprovalStopNumber(), wd2.getApprovalStopNumber())
				.append(wd1.getApproverNumber(), wd2.getApproverNumber())
				.append(wd2.getPrimaryApproverFlag(), wd1.getPrimaryApproverFlag())
				.toComparison();
	}

}
