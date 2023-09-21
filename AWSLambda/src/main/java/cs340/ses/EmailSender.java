package cs340.ses;

// these are the imports for SDK v1
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.simpleemail.*;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.regions.Regions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EmailSender {
    public EmailResult handleRequest(EmailRequest request, Context context) {

        LambdaLogger logger = context.getLogger();
        logger.log("Entering send_email");
        EmailResult emailResult = new EmailResult();
        try {
            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard()

                            // Replace US_WEST_2 with the AWS Region you're using for
                            // Amazon SES.
                            .withRegion(Regions.US_WEST_1).build();

            ArrayList<String> toAddresses = new ArrayList<>();
            toAddresses.add(request.toAddress);
            Destination destination = new Destination(toAddresses);
            Message message = new Message(new Content(request.subject), new Body(new Content(request.textBody)).withHtml(new Content(request.htmlBody)));
            SendEmailRequest sendRequest = new SendEmailRequest(request.fromAddress, destination, message);
            SendEmailResult result = client.sendEmail(sendRequest);

            logger.log("Email sent!");

            emailResult.message = "Email sent!";

        } catch (Exception ex) {
            logger.log("The email was not sent. Error message: " + ex.getMessage());
            emailResult.message = "The email was not sent. Error message: " + ex.getMessage();
            throw new RuntimeException(ex);
        }
        finally {
            logger.log("Leaving send_email");
            long currentTime = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
            Date resultdate = new Date(currentTime);
            emailResult.timestamp = sdf.format(resultdate);
        }

        return emailResult;
    }

}