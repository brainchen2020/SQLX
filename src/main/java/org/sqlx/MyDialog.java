//package org.intellij.sdk.action;
//
//import com.google.gson.JsonObject;
//import com.google.gson.stream.JsonReader;
//import com.intellij.openapi.application.ApplicationManager;
//import com.intellij.openapi.command.WriteCommandAction;
//import com.intellij.openapi.editor.Document;
//import com.intellij.openapi.editor.Editor;
//import com.intellij.openapi.fileEditor.FileDocumentManager;
//import com.intellij.openapi.fileEditor.FileEditorManager;
//import com.intellij.openapi.ui.DialogWrapper;
//import com.sun.istack.Nullable;
//import org.apache.http.HttpEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONObject;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.StringReader;
//import java.nio.charset.Charset;
//
//public class MyDialog extends DialogWrapper {
//  private JTextArea textArea;
//  private String responseContent;
//  private String lastInput;
//  public MyDialog() {
//    super(true); // true for modal dialog
//    setTitle("My Dialog Title");
//    init();
//  }
//
//  @Nullable
//  @Override
//  protected JComponent createCenterPanel() {
//    JPanel panel = new JPanel();
//    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
//
//    textArea = new JTextArea();
//    textArea.setLineWrap(true);
//    textArea.setWrapStyleWord(true);
//    JScrollPane scrollPane = new JScrollPane(textArea);
//    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//    scrollPane.setPreferredSize(new Dimension(320, 100));
//    panel.add(scrollPane);
//
//    return panel;
//  }
//
//  @Override
//  protected Action[] createActions() {
//    return new Action[]{getOKAction(), getCancelAction()};
//  }
//
//  @Override
//  protected void doOKAction() {
//    // Send POST request with JSON content
//    this.lastInput = getText();
//    // Construct JSON object to send in request body
//    JsonObject jsonObject = new JsonObject();
////    jsonObject.addProperty("prompt", lastInput);
////    jsonObject.addProperty("table_info", "");
//    jsonObject.addProperty("query", lastInput);
//
//    this.sendJsonPostRequest("http://localhost:8000/0.0.1/mybatis/query", jsonObject);
//
//    super.doOKAction();
//  }
//
//  public String getText() {
//    return textArea.getText();
//  }
//  public String getResponseContent() {
//    return responseContent;
//  }
//  public String getRespText(){
//    // Parse the JSON object from the response content
//    JSONObject jsonObject = new JSONObject(responseContent);
//
//    // Access values in the JSON object
//    String text = jsonObject.getString("text");
//    int code = jsonObject.getInt("code");
//    return text;
//  }
//  public void sendJsonPostRequest(String url, JsonObject jsonObject) {
//    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//      HttpPost httpPost = new HttpPost(url);
//      httpPost.setHeader("Content-Type", "application/json");
//
//      // Set request body
//      StringEntity entity = new StringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON);
//      httpPost.setEntity(entity);
//
//      // Send POST request
//      try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
//        System.out.println(response.getStatusLine().getStatusCode());
//        // Read response content
//        HttpEntity responseEntity = response.getEntity();
//        responseContent = EntityUtils.toString(responseEntity, Charset.forName("utf-8"));
//        System.out.println(responseContent);
//
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//}
