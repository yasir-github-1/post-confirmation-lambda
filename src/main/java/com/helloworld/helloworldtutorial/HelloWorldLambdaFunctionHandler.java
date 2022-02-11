package com.helloworld.helloworldtutorial;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.CognitoEvent;


public class HelloWorldLambdaFunctionHandler implements RequestHandler<Object, Object> {

    @Override
    public Object handleRequest(Object event, Context context) {
        System.out.println("Hello from lambda!");

        LinkedHashMap congitoEventObject = (LinkedHashMap) event;
        LinkedHashMap responseObject = (LinkedHashMap) congitoEventObject.get("response");
        LinkedHashMap requestObject = (LinkedHashMap) congitoEventObject.get("request");
        String userEmailAddress = (String)((LinkedHashMap)requestObject.get("userAttributes")).get("email");

//        These response properties are only available in pre-signup lambda trigger don't uncomment this if you are executing this lambda in post confirmation
//        response.put("autoConfirmUser", true);
//        response.put("autoVerifyPhone", true);
//        response.put("autoVerifyEmail", true);

        System.out.println("Congito Event Object" +  congitoEventObject);
        System.out.println("Request Object" +  requestObject);
        System.out.println("Response Object" +  responseObject);
        System.out.println("Email Address" + userEmailAddress);
//        this will only work if you have SES policy attached to your lambda and your SES account is out of the sandbox
//        sendConfirmationEmail(userEmailAddress);


/*         Payload that should be returned when calling post confirmation lambda as a linkedHashMap
        {
            "userName": "johnnysmith",
                "request": {
            "userAttributes": {
                "email": "exampleuser@example.com",
                        "email_verified": true,
                        "name": "Johnny Smith",
                        "sub": "abcdevfefe-1232132-cofeve"
            }
        },
            "response": {}
        }*/

        // returning the congito event linkedhashmap
        return event;
    }
    public void sendConfirmationEmail(String emailAddress) {
        // Replace sender@example.com with your "From" address.
        // This address must be verified with Amazon SES.
        System.out.println("Sending Confirmation email....");
        final String FROM  = "92sameed@gmail.com";

        // Replace recipient@example.com with a "To" address. If your account
        // is still in the sandbox, this address must be verified.
        final String TO = emailAddress == null ? "92sameed@gmail.com": emailAddress;

        // The subject line for the email.
        final String SUBJECT = "Signup Completed!";

        // The HTML body for the email.
        final String HTMLBODY = "<h1>Congratulations!</h1>"
                + "<p>You have successfully signed up for Cognito account<p>";

        // The email body for recipients with non-HTML email clients.
        final String TEXTBODY = "Congratulations, You have successfully signed up for Cognito account";

        try {
            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard()
                            // Replace US_WEST_2 with the AWS Region you're using for
                            // Amazon SES.
                            .withRegion("us-east-2").build();
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(TO))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData(HTMLBODY))
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData(TEXTBODY)))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData(SUBJECT)))
                    .withSource(FROM);
            client.sendEmail(request);
            System.out.println("Email sent successfully!");
        } catch (Exception ex) {
            System.out.println("The email was not sent. Error message: "
                    + ex.getMessage());
        }
    }
/*
    public static void main(String[] args) {
        HelloWorldLambdaFunctionHandler helloWorldLambdaFunctionHandler = new HelloWorldLambdaFunctionHandler();
        helloWorldLambdaFunctionHandler.handleRequest(new LinkedHashMap<Object, Object>(), null);
       helloWorldLambdaFunctionHandler.sendConfirmationEmail(null);
    }
*/

}