package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public static List<Message> getAllMessagesByAccountId(Integer postedBy) {
        List<Message> messages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();

        String sql = "SELECT * FROM message WHERE posted_by = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, postedBy);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int message_id = rs.getInt("message_id");
                    int posted_by = rs.getInt("posted_by");
                    String message_text = rs.getString("message_text");
                    long time_posted_epoch = rs.getLong("time_posted_epoch");
                    Message message = new Message(message_id, posted_by, message_text, time_posted_epoch);
                    messages.add(message);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public static List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM message";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int message_id = rs.getInt("message_id");
                        int posted_by = rs.getInt("posted_by");
                        String message_text = rs.getString("message_text");
                        long time_posted_epoch = rs.getLong("time_posted_epoch");
                        Message message = new Message(message_id, posted_by, message_text, time_posted_epoch);
                        messages.add(message);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public static Message getMessageById(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, messageId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int message_id = rs.getInt("message_id");
                    int posted_by = rs.getInt("posted_by");
                    String message_text = rs.getString("message_text");
                    long time_posted_epoch = rs.getLong("time_posted_epoch");
                    return new Message(message_id, posted_by, message_text, time_posted_epoch);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int message_id = generatedKeys.getInt(1);
                        message.setMessage_id(message_id);
                        return message;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteMessageById(int messageId) {
        Connection connection = ConnectionUtil.getConnection();

        String sql = "DELETE FROM message WHERE message_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, messageId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateMessageById(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, message.getMessage_text());
            ps.setInt(2, message.getMessage_id());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}