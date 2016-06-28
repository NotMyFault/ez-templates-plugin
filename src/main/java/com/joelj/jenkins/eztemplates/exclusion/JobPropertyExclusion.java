package com.joelj.jenkins.eztemplates.exclusion;

import com.google.common.base.Throwables;
import hudson.model.AbstractProject;
import hudson.model.JobProperty;
import jenkins.model.Jenkins;

import java.io.IOException;

/**
 * Generic {@link Exclusion} which retains a given {@link JobProperty} through cloning
 */
public class JobPropertyExclusion extends HardCodedExclusion {
    private final String id;
    private final String description;
    private final String className;
    private JobProperty cached;

    public JobPropertyExclusion(String id, String description, String className) {
        this.id = id;
        this.description = description;
        this.className = className;
    }

    @Override
    public void preClone(AbstractProject implementationProject) {
        cached = implementationProject.getProperty(className);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void postClone(AbstractProject implementationProject) {
        try {
            if (cached != null) {
                implementationProject.removeProperty(cached.getClass());
                implementationProject.addProperty(cached);
            }
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getDisabledText() {
        // Assumes id is _also_ the plugin
        return Exclusions.checkPlugin(id);
    }
}
