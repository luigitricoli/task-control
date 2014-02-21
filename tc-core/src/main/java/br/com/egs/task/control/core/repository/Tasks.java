package br.com.egs.task.control.core.repository;

import java.util.List;

import br.com.egs.task.control.core.entities.Task;

public interface Tasks {
    /**
     *
     * @param criteria
     * @return
     */
    public List<Task> searchTasks(TaskSearchCriteria criteria);

    /**
     *
     * @param task
     * @return The task, containing the generated ID.
     */
    public Task add(Task task);
}
