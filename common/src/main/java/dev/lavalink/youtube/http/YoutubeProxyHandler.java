package dev.lavalink.youtube.http;

import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterfaceManager;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class YoutubeProxyHandler {
    private static final Logger log = LoggerFactory.getLogger(YoutubeProxyHandler.class);
    private final HttpInterfaceManager manager;


    /**
     * Constructor for YoutubeProxyHandler.
     *
     * @param manager The HttpInterfaceManager to manage HTTP connections.
     */
    public YoutubeProxyHandler(HttpInterfaceManager manager) {
        this.manager = manager;
    }

    private void apply(HttpClientBuilder httpClientBuilder, @NotNull URI proxyURI) {

        HttpHost proxy = new HttpHost(proxyURI.getHost(), proxyURI.getPort(), proxyURI.getScheme());
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        if (proxyURI.getUserInfo() != null) {
            String[] userInfo = proxyURI.getUserInfo().split(":", 2);
            credsProvider.setCredentials(new AuthScope(proxy.getHostName(), proxy.getPort()),
                    new UsernamePasswordCredentials(userInfo[0], userInfo[1])
            );
        }

        httpClientBuilder.addInterceptorFirst((HttpRequestInterceptor) (httpRequest, httpContext) -> {
            log.debug("Proxy client intercepted request: {}", httpRequest.getRequestLine());
        });

        httpClientBuilder.setProxy(proxy).setDefaultCredentialsProvider(credsProvider);
    }

    public void init(@Nullable URI proxyURI) {

        if (proxyURI == null) {
            log.warn("No proxy URI provided, skipping proxy configuration.");
            return;
        }

        manager.configureBuilder(httpClientBuilder -> {

            log.info("Builder configured for YouTube proxy handler");
            log.info("Using proxy URI: {}", proxyURI);

            apply(httpClientBuilder, proxyURI);
        });

    }
}
