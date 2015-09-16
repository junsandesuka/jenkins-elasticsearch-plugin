package me.enomoto.jenkins.plugins.elasticsearch;

import java.io.Serializable;
import java.util.Map;

public class BuildResult implements Serializable {

    private static final long serialVersionUID = 739638366517082256L;

    private String identifier;

    private String uniqueJobName;

    private int buildNumber;

    private String buildDescription;

    private String startTime;

    private long duration;

    private Map<String, String> environmentVariables;

    private String result;

    private String timestamp;

    private String buildUrl;

    private String jobUrl;

    private String jobDescription;

    private String jobName;

    private Map<String, String> systemProperties;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public String getUniqueJobName() {
        return uniqueJobName;
    }

    public void setUniqueJobName(final String uniqueJobName) {
        this.uniqueJobName = uniqueJobName;
    }

    public int getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(final int buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getBuildDescription() {
        return buildDescription;
    }

    public void setBuildDescription(final String buildDescription) {
        this.buildDescription = buildDescription;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(final long duration) {
        this.duration = duration;
    }

    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public void setEnvironmentVariables(final Map<String, String> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public String getResult() {
        return result;
    }

    public void setResult(final String result) {
        this.result = result;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBuildUrl() {
        return buildUrl;
    }

    public void setBuildUrl(final String buildUrl) {
        this.buildUrl = buildUrl;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public void setJobUrl(final String jobUrl) {
        this.jobUrl = jobUrl;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(final String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(final String jobName) {
        this.jobName = jobName;
    }

    public Map<String, String> getSystemProperties() {
        return systemProperties;
    }

    public void setSystemProperties(final Map<String, String> systemProperties) {
        this.systemProperties = systemProperties;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(final String startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "BuildResult [buildNumber=" + buildNumber + ", buildDescription=" + buildDescription + ", startTime=" + startTime + ", duration=" + duration + ", result=" + result + ", timestamp=" + timestamp + ", buildUrl=" + buildUrl + ", jobUrl=" + jobUrl + ", jobDescription=" + jobDescription + ", jobName=" + jobName + "]";
    }
}
