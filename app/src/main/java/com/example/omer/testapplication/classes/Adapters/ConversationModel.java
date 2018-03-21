package com.example.omer.testapplication.classes.Adapters;

public class ConversationModel {
    public int Id;
    public int ConversationID;
    public int SenderID;
    public int ReceiverID;
    public String MsgDate;
    public String MessageText;
    public String ReceiverName;
    public String SendererName;
    public String SenderImage;
    public String ReceiverImage;
    public int MsgStatus;

    public ConversationModel(int id, int conversationID, int senderID, int receiverID, String msgDate, String messageText, String receiverName, String sendererName, String senderImage, String receiverImage, int msgStatus) {
        Id = id;
        ConversationID = conversationID;
        SenderID = senderID;
        ReceiverID = receiverID;
        MsgDate = msgDate;
        MessageText = messageText;
        ReceiverName = receiverName;
        SendererName = sendererName;
        SenderImage = senderImage;
        ReceiverImage = receiverImage;
        MsgStatus = msgStatus;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getConversationID() {
        return ConversationID;
    }

    public void setConversationID(int conversationID) {
        ConversationID = conversationID;
    }

    public int getSenderID() {
        return SenderID;
    }

    public void setSenderID(int senderID) {
        SenderID = senderID;
    }

    public int getReceiverID() {
        return ReceiverID;
    }

    public void setReceiverID(int receiverID) {
        ReceiverID = receiverID;
    }

    public String getMsgDate() {
        return MsgDate;
    }

    public void setMsgDate(String msgDate) {
        MsgDate = msgDate;
    }

    public String getMessageText() {
        return MessageText;
    }

    public void setMessageText(String messageText) {
        MessageText = messageText;
    }

    public String getReceiverName() {
        return ReceiverName;
    }

    public void setReceiverName(String receiverName) {
        ReceiverName = receiverName;
    }

    public String getSendererName() {
        return SendererName;
    }

    public void setSendererName(String sendererName) {
        SendererName = sendererName;
    }

    public String getSenderImage() {
        return SenderImage;
    }

    public void setSenderImage(String senderImage) {
        SenderImage = senderImage;
    }

    public String getReceiverImage() {
        return ReceiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        ReceiverImage = receiverImage;
    }

    public int getMsgStatus() {
        return MsgStatus;
    }

    public void setMsgStatus(int msgStatus) {
        MsgStatus = msgStatus;
    }

}