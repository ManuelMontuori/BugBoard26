package com.bugboard.api.services;

import com.bugboard.api.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.DeliveryMediumType;

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

        // 1. Prepariamo gli attributi da inviare a Cognito
        List<AttributeType> userAttributes = new ArrayList<>();

        // Impostiamo l'email (obbligatoria se usata come login)
        userAttributes.add(AttributeType.builder()
                .name("email")
                .value(user.getEmail())
                .build());

        // Confermiamo l'email automaticamente così Cognito sa di poter inviare messaggi
        userAttributes.add(AttributeType.builder()
                .name("email_verified")
                .value("true")
                .build());

        // Inseriamo l'UUID custom generato da Hibernate (es: "custom:uuid")
        userAttributes.add(AttributeType.builder()
                .name("custom:uuid")
                .value(user.getUuid().toString()) // Trasformiamo il UUID in Stringa
                .build());

        // 2. Costruiamo la richiesta di creazione
        AdminCreateUserRequest request = AdminCreateUserRequest.builder()
                .userPoolId(userPoolId)
                .username(user.getEmail()) // L'email fa da Username principale
                .userAttributes(userAttributes)
                .desiredDeliveryMediums(DeliveryMediumType.EMAIL) // Specifichiamo che l'invito vada via email
                // NOTA: Non settando "temporaryPassword", Cognito ne genera una random e la invia da solo
                .build();

        try {
            // 3. Eseguiamo la chiamata ad AWS
            AdminCreateUserResponse response = cognitoClient.adminCreateUser(request);
            System.out.println("Utente creato con successo su Cognito. Sub: " + response.user().username());

        } catch (Exception e) {
            // Gestisci le eccezioni (es. UsernameExistsException se l'email esiste già)
            throw new RuntimeException("Errore durante la creazione dell'utente su Cognito: " + e.getMessage(), e);
        }
    }
}