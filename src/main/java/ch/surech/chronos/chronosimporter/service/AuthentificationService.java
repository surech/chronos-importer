package ch.surech.chronos.chronosimporter.service;

import com.microsoft.aad.msal4j.DeviceCode;
import com.microsoft.aad.msal4j.DeviceCodeFlowParameters;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.PublicClientApplication;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.Set;
import java.util.function.Consumer;

@Service
public class AuthentificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthentificationService.class);

    @Value("${microsoft.graph.auth.appId}")
    private String appId;

    @Value("${microsoft.graph.auth.scopes}")
    private String[] appScopes;

    @Value("${microsoft.graph.auth.authority}")
    private String authority;

    private IAuthenticationResult authenticationResult;

    public String getAccessToken(){
        return authenticationResult.accessToken();
    }

    public void signIn(){
        PublicClientApplication app;
        try {
            // Build the MSAL application object with
            // app ID and authority
            app = PublicClientApplication.builder(appId)
                    .authority(authority)
                    .build();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        // Create consumer to receive the DeviceCode object
        // This method gets executed during the flow and provides
        // the URL the user logs into and the device code to enter
        Consumer<DeviceCode> deviceCodeConsumer = (DeviceCode deviceCode) -> {
            // Print the login information to the console
            LOGGER.info(deviceCode.message());
        };

        // Request a token, passing the requested permission scopes
        this.authenticationResult = app.acquireToken(
                DeviceCodeFlowParameters
                        .builder(Set.of(appScopes), deviceCodeConsumer)
                        .build()
        ).exceptionally(ex -> {
            LOGGER.warn("Unable to authenticate - " + ex.getMessage());
            throw new RuntimeException("Unable to authenticate", ex);
        }).join();
    }

}
