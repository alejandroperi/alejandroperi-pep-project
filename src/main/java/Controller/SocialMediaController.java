package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Service.AccountService;
import Service.MessageService;
import Model.Message;
import DAO.MessageDAO;

import static org.mockito.Mockito.clearAllCaches;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("register", this::register);
        app.post("login", this::login);
        app.post("messages", this::createMessage);
        app.get("messages", this::messages);
        app.get("messages/{id}", this::messagesById);
        app.patch("messages/{id}", this::updateMessageByID);
        app.delete("messages/{id}", this::deleteMessageById);
        app.get("accounts/{id}/messages", this::getAccountMessages);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");

    }

    private void updateMessageByID(Context context){
        Integer messageId = Integer.parseInt(context.pathParam("id"));
        Message message = MessageDAO.getMessageById(messageId);
    
        
        Message requestMessage = context.bodyAsClass(Message.class);
        if (message == null || requestMessage.getMessage_text() == null || requestMessage.getMessage_text().trim().isEmpty() || requestMessage.getMessage_text().length() >= 255){
            context.status(400);
            context.result("");
            return;
        } 
        message.setMessage_text(requestMessage.getMessage_text());
        Boolean updatedMessage = MessageDAO.updateMessageById(message);
        if (updatedMessage){
            context.status(200);
            context.json(message, getClass());
        }else{
            context.status(400);
            context.result("");
        }
    }

    private void getAccountMessages(Context context) {
        Integer accountId = Integer.parseInt(context.pathParam("id"));
        List<Message> messages = MessageDAO.getAllMessagesByAccountId(accountId);

        if (messages.size() == 0) {
            context.status(200);
            context.json(messages, getClass());
        } else {
            context.status(200);
            context.json(messages, getClass());
        }
    }

    private void createMessage(Context context) {
        Message message = context.bodyAsClass(Message.class);
        Message created_message = MessageService.createMessage(message);
        if (created_message.getMessage_id() > 0) {
            context.status(200);
            context.json(created_message, getClass());
        } else {
            context.status(400);
            context.result("");
        }
    }

    private void messagesById(Context context) {
        Integer messageId = Integer.parseInt(context.pathParam("id"));
        Message message = MessageService.getMessageById(messageId);
        if (message != null) {
            context.status(200);
            context.json(message, getClass());
        } else {
            context.status(200);
            context.result("");
        }
    }

    private void messages(Context context) {
        List<Message> messages = MessageDAO.getAllMessages();
        context.status(200);
        context.json(messages, getClass());
    }

    private void login(Context context) {
        Account account = context.bodyAsClass(Account.class);
        if (AccountService.authenticate(account)) {
            context.status(200);
            Account dbAccount = AccountService.getAccountByUsername(account);
            context.json(dbAccount, getClass());
        } else {
            context.status(401);
            context.result("");
        }
    }

    private void register(Context context) {
        Account account = context.bodyAsClass(Account.class);

        if (AccountService.registerUser(account)) {
            context.status(200);
            Account dbAccount = AccountService.getAccountByUsername(account);
            context.json(dbAccount, getClass());

        } else {
            context.status(400);
            context.result("");
        }

    }

    private void deleteMessageById(Context context) {
        Integer messageId = Integer.parseInt(context.pathParam("id"));
        Message message = MessageService.deleteMessageById(messageId);
        if (message.getMessage_id() > 0) {
            context.status(200);
            context.json(message, getClass());
        } else {
            context.status(200);
            context.result("");
        }
    }

}