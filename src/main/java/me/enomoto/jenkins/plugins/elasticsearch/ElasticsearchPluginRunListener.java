package me.enomoto.jenkins.plugins.elasticsearch;

import hudson.Extension;
import hudson.model.Describable;
import hudson.model.TaskListener;
import hudson.model.Descriptor;
import hudson.model.Run;
import hudson.model.listeners.RunListener;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Index;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

@SuppressWarnings("rawtypes")
@Extension
public class ElasticsearchPluginRunListener extends RunListener<Run> implements Describable<ElasticsearchPluginRunListener> {

    public ElasticsearchPluginRunListener() {
    }

    @Override
    public Descriptor<ElasticsearchPluginRunListener> getDescriptor() {
        return new DescriptorImpl();
    }

    @Override
    public void onCompleted(final Run run, final TaskListener listener) {
        final DescriptorImpl descriptor = (DescriptorImpl) getDescriptor();
        JestClient client = null;
        try {

            final JestClientFactory factory = new JestClientFactory();
            factory.setHttpClientConfig(new HttpClientConfig.Builder(descriptor.getServer()).multiThreaded(true).build());
            client = factory.getObject();

            final BuildResult buildResult = new BuildResult();
            buildResult.setIdentifier(descriptor.getIdentifier());
            buildResult.setUniqueJobName(descriptor.getIdentifier() + ":" + run.getParent().getName());
            buildResult.setBuildDescription(run.getDescription());
            buildResult.setDuration(run.getDuration());
            buildResult.setEnvironmentVariables(run.getEnvironment(listener));
            buildResult.setBuildNumber(run.getNumber());
            buildResult.setResult(run.getResult().toString());
            buildResult.setStartTime(toISODateString(new Date(run.getStartTimeInMillis())));
            buildResult.setTimestamp(toISODateString(run.getTime()));
            buildResult.setBuildUrl(run.getUrl());
            try {
                buildResult.setJobUrl(run.getParent().getAbsoluteUrl());
            } catch (final IllegalStateException e) {
                buildResult.setJobUrl(run.getParent().getUrl());
            }
            buildResult.setJobDescription(run.getParent().getDescription());
            buildResult.setJobDescription(run.getParent().getDescription());
            buildResult.setJobName(run.getParent().getName());
            buildResult.setSystemProperties(getSystemPropertiesMap());

            final Index index = new Index.Builder(buildResult).index(descriptor.getIndex()).type(descriptor.getType()).id(run.getId()).build();
            final JestResult result = client.execute(index);
            if (result.isSucceeded()) {
                listener.getLogger().println("[SUCCESS] Elasticsearch Plugin Index(" + run.getId() + ")");
            } else {
                listener.getLogger().println("[WARN] Elasticsearch Plugin Index failed. (" + run.getId() + ")" + result.getErrorMessage());
            }
        } catch (final IOException e) {
            listener.getLogger().println("[WARN] Elasticsearch Plugin Failed. " + e.getLocalizedMessage());
            e.printStackTrace();
        } catch (final InterruptedException e) {
            listener.getLogger().println("[WARN] Elasticsearch Plugin Failed. " + e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.shutdownClient();
            }
        }
    }

    private Map<String, String> getSystemPropertiesMap() {
        final Map<String, String> map = new LinkedHashMap<String, String>();
        for (final Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
            map.put(entry.getKey() + "", entry.getValue() + "");
        }
        return map;
    }

    private String toISODateString(final Date date) {
        final TimeZone tz = TimeZone.getTimeZone("UTC");
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        df.setTimeZone(tz);
        return df.format(date);
    }

    @Extension
    public static final class DescriptorImpl extends Descriptor<ElasticsearchPluginRunListener> {

        private String server;

        private String index;

        private String type;

        private String identifier;

        /**
         * In order to load the persisted global configuration, you have to call load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Elasticsearch Plugin";
        }

        @Override
        public boolean configure(final StaplerRequest req, final JSONObject formData) throws FormException {
            // To persist global configuration information,
            // set that to properties and call save().
            server = formData.getString("server");
            index = formData.getString("index");
            type = formData.getString("type");
            identifier = formData.getString("identifier");
            save();
            return super.configure(req, formData);
        }

        public String getServer() {
            return server;
        }

        public String getIndex() {
            return index;
        }

        public String getType() {
            return type;
        }

        public String getIdentifier() {
            return identifier;
        }

        @Override
        public String toString() {
            return "DescriptorImpl [server=" + server + ", index=" + index + ", type=" + type + "]";
        }
    }
}
