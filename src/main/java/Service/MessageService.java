package Service;

import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;
import java.util.List;

public class MessageService {

    public static Message createMessage(Message message) {
        /*
         * The creation of the message will be successful if and only if the
         * message_text is not blank,
         * is under 255 characters, and posted_by refers to a real, existing user
         * 
         */
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()
                || message.getMessage_text().length() >= 255) {
            return message;
        }
        if (AccountService.getAccountById(message.getPosted_by()) == null) {
            return message;
        }

        Message created_message = MessageDAO.createMessage(message);
        return created_message;
    }

    public static List<Message> getAllMessages() {
        List<Message> messages = MessageDAO.getAllMessages();
        return messages;
    }

    public static Message getMessageById(Integer messageId) {
        Message result_message = MessageDAO.getMessageById(messageId);
        return result_message;
    }

    public static Message deleteMessageById(Integer messageId) {
        Message message = getMessageById(messageId);
        if (message == null){
            message = new Message(0, 0, null, 0);
        }
        Boolean result_message = MessageDAO.deleteMessageById(messageId);
        if (!result_message){
            message.setMessage_id(0);
        }
        return message;
    }
}