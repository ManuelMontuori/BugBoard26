package com.bugboard.api.services;

import com.bugboard.api.exceptions.CognitoServiceException;
import com.bugboard.api.exceptions.UserExistsException;
import com.bugboard.api.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class CognitoUserService {

    private final CognitoIdentityProviderClient cognitoClient;

    @Value("${aws.cognito.user-pool-id}")
    private String userPoolId;

    public CognitoUserService(CognitoIdentityProviderClient cognitoClient) {

        this.cognitoClient = cognitoClient;
    }

    public void registraUtenteSuCognito(User user) {


        List<AttributeType> userAttributes = new ArrayList<>();

        userAttributes.add(AttributeType.builder()
                .name("email")
                .value(user.getEmail())
                .build());

        userAttributes.add(AttributeType.builder()
                .name("email_verified")
                .value("true")
                .build());

        userAttributes.add(AttributeType.builder()
                .name("custom:uuid")
                .value(user.getUuid().toString())
                .build());

        userAttributes.add(AttributeType.builder()
                .name("custom:role")
                .value(user.getRole().toString())
                .build());

        AdminCreateUserRequest request = AdminCreateUserRequest.builder()
                .userPoolId(userPoolId)
                .username(user.getEmail())
                .userAttributes(userAttributes)
                .desiredDeliveryMediums(DeliveryMediumType.EMAIL)
                .build();

        try {
            cognitoClient.adminCreateUser(request);

        } catch (UsernameExistsException e) {
            throw new UserExistsException("L'utente con questa email esiste già.", e);

        } catch (InvalidParameterException e) {
            throw new IllegalArgumentException("Parametri non validi per la creazione utente su Cognito: " + e.awsErrorDetails().errorMessage(), e);

        } catch (CognitoIdentityProviderException e) {
            throw new CognitoServiceException("Errore di Cognito: " + e.awsErrorDetails().errorMessage(), e);
        }
    }
}