package uk.gov.dft.bluebadge.client.usermanagement.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Value("${servicenameclient.servicehost.scheme}")
    private String scheme;
    @Value("${servicenameclient.servicehost.host}")
    private String host;
    @Value("${servicenameclient.servicehost.port}")
    private Integer port;
    @Value("${servicenameclient.servicehost.connectiontimeout}")
    private Integer connectionTimeout;
    @Value("${servicenameclient.servicehost.requesttimeout}")
    private Integer requestTimeout;
    @Value("${servicenameclient.servicehost.contextpath}")
    private String contextPath;

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getUrlPrefix(){
        return scheme + "://" + host + ":" + port + "/" + contextPath;
    }
}
