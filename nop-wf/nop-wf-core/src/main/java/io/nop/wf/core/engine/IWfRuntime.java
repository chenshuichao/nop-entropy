package io.nop.wf.core.engine;

import io.nop.api.core.exceptions.ErrorCode;
import io.nop.api.core.exceptions.NopException;
import io.nop.core.context.IEvalContext;
import io.nop.core.context.IServiceContext;
import io.nop.wf.api.actor.IWfActor;
import io.nop.wf.core.impl.IWorkflowImplementor;
import io.nop.wf.core.impl.IWorkflowStepImplementor;
import io.nop.wf.core.model.WfModel;
import io.nop.wf.core.store.IWorkflowActionRecord;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IWfRuntime extends IEvalContext {
    void delayExecute(Runnable command);

    Object getValue(String name);

    void setValue(String name, Object value);

    List<IWfActor> getSelectedActors();

    Map<String, List<IWfActor>> getSelectedStepActors();

    Set<String> getRejectSteps();

    void setRejectSteps(Set<String> rejectSteps);

    Set<String> getTargetSteps();

    void setTargetSteps(Set<String> targetSteps);

    Set<String> getTargetCases();

    void setTargetCases(Set<String> targetCases);

    IServiceContext getServiceContext();

    IWorkflowImplementor getWf();

    WfModel getWfModel();

    IWfActor getCaller();

    List<IWfActor> getSelectedActors(String targetStep);

    void setSelectedActors(List<IWfActor> actors);

    void setSelectedStepActors(Map<String, List<IWfActor>> selectedStepActors);

    List<IWfActor> getCurrentActors();

    void setCurrentActors(List<IWfActor> currentActors);

    IWorkflowStepImplementor getPrevStep();

    IWorkflowStepImplementor getCurrentStep();

    NopException newError(ErrorCode errorCode);

    void triggerEvent(String event);

    void saveWfRecord(int status);

    IWorkflowStepImplementor getActionStep();

    IWorkflowActionRecord getActionRecord();

    Throwable getException();

    void setException(Throwable exception);

    void markEnd();

    boolean willEnd();
}