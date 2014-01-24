package br.com.egs.task.control.core.entities;

/**
 * Represents an Application in which the users can develop tasks.
 */
public class Application {
    private String name;

    public Application(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
