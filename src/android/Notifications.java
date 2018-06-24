package com.gae.scaffolder.plugin;

/**
 * Created by Abinash.Sahoo on 4/5/2017.
 */

public class Notifications {
  int id;
  String message;
  String flag;
  String title;
  String messageDate;
  String messageId;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getMessageDate() {
    return messageDate;
  }

  public void setMessageDate(String messageDate) {
    this.messageDate = messageDate;
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  @Override
  public String toString() {
    return "Notifications{" +
      "id=" + id +
      ", message='" + message + '\'' +
      ", flag='" + flag + '\'' +
      ", title='" + title + '\'' +
      ", messageDate='" + messageDate + '\'' +
      ", messageId='" + messageId + '\'' +
      '}';
  }
}
