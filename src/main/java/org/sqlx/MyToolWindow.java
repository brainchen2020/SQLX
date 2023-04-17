package org.sqlx;

import com.google.gson.JsonObject;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDialog;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import icons.MyURL;
import net.minidev.json.parser.ParseException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class MyToolWindow extends SimpleToolWindowPanel {

    private final JTextArea textArea;
    private final JBTextField selectedFilePathField;
    private final JButton uploadButton;

    private final String tips = "Ask for SQL";
    private static final MyPluginSettings myPluginSettings;
    static {
        //check if save SQL file or not
        myPluginSettings = MyPluginSettings.getInstance();
        if(myPluginSettings.getState().uuid == null){
            //no save get upload SQL file.
            //need to register files.
            //click upload but, select SQL file. check file size < 50M, upload request.
            //create uuid, sql file, time
            //save selected file path.
        }else{
            //save file then get uuid to request
            //upload file but will replace preview.
        }
//        myPluginSettings.getState().myString = "12345678910";
//        System.out.println("current state : " + myPluginSettings.getState());
    }

    public MyToolWindow(Project project) {
        super(true, true);

        // Create the selected file path field
        selectedFilePathField = new JBTextField();
        selectedFilePathField.setEditable(true);

        //get selectedFilePath from file.
        String selectedFilePath = myPluginSettings.getState().selectedFilePath;
        uploadButton = new JButton("Upload");
        selectedFilePathField.setText(selectedFilePath);
        if(myPluginSettings.getState().uuid != null){
            uploadButton.setText("Uploaded");
        }
        //
        // Add the fields to the tool window
        JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.add(uploadButton);
        panel.add(selectedFilePathField);

        // Create the second row panel and add the text area with hidden tips
        JPanel secondRowPanel = new JPanel(new GridLayout(2, 1));
        textArea = new JTextArea(tips);
        textArea.setToolTipText(tips);
        textArea.setEnabled(true);
        textArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textArea.getText().equals(tips)) {
                    textArea.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textArea.getText().isBlank()) {
                    textArea.setText(tips);
                }
            }
        });
        textArea.setRows(5);
        textArea.setLineWrap(true);
        JBScrollPane scrollPane = new JBScrollPane(textArea);
        secondRowPanel.add(scrollPane);
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if(myPluginSettings.getState().uuid == null){
                        throw new RuntimeException("please upload SQL file first!");
                    }
                    textArea.setEnabled(false);
                    Editor editor = getCurrentEditor();
                    String inputText = textArea.getText();
//                    System.out.println(inputText);
                    //Request API
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("query", inputText);
                    jsonObject.addProperty("uuid", myPluginSettings.getState().uuid);
                    // add uuid for each request represent a user.
                    new Thread(() ->{
                        JSONObject respObj= sendJsonPostRequest(MyURL.queryUrl, jsonObject);
                        textArea.setEnabled(true);
                        // Access values in the JSON object
                        String text = respObj.getString("text");
                        int code = respObj.getInt("code");
                        if(code == 0){
                            insertTextInEditor(editor, project, text);
                        }
                    }).start();


                }
            }
        });
//        secondRowPanel.add(textArea);

        uploadButton.addActionListener(e -> {
            // Create the file chooser dialog
            FileChooserDescriptor descriptor = new FileChooserDescriptor(true, true, false, false, false, false);
            FileChooserDialog fileChooser = FileChooserFactory.getInstance().createFileChooser(descriptor, project, null);
            VirtualFile[] selectedFiles = fileChooser.choose(null);
            if (selectedFiles.length == 0) {
                return;
            }
            VirtualFile selectedFile = selectedFiles[0];
            if (selectedFile != null) {
                String name = selectedFile.getName();
                if (name.endsWith(".sql")) {
                    selectedFilePathField.setText(selectedFile.getPath());
                    // upload file. loading.
                    uploadButton.setText("Uploading...");
                    uploadButton.setEnabled(false);
                    textArea.setEnabled(false);
                    // uploaded file response set enable.
                    new Thread(()->{
                        try {


                            JSONObject resp = uploadFile(MyURL.uploadUrl, selectedFilePathField.getText());

                            int code = resp.getInt("code");
                            if(code == 0){
                                String uuid = resp.getString("uuid");
                                myPluginSettings.getState().uuid = uuid;
                                myPluginSettings.getState().created = System.currentTimeMillis();
                                // request POST API that send file to server and get UUID return then save it.
                                myPluginSettings.getState().selectedFilePath = selectedFilePathField.getText();
                            }else{
                                throw new Exception();
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        } finally {
                            uploadButton.setEnabled(true);
                            textArea.setEnabled(true);
                            if(myPluginSettings.getState().uuid != null){
                                uploadButton.setText("Uploaded");
                            }else{
                                uploadButton.setText("Upload");
                            }
                        }
                    }).start();

                } else {
                    selectedFilePathField.setText("sql file accept only");
                }
            }
        });
//        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.NORTH);
        mainPanel.add(secondRowPanel, BorderLayout.SOUTH);

        setContent(mainPanel);
        // Get the current file editor and set the selected file path field to its path
//        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
//        VirtualFile selectedFile = fileEditorManager.getSelectedFiles()[0];
//        selectedFilePathField.setText(selectedFile.getPath());

    }

    private void insertTextInEditor(Editor editor, Project project, String text) {
        // insert the response at the current caret position

        int offset = getCaretOffset(editor);
        WriteCommandAction.runWriteCommandAction(project, () -> {
            Document document = editor.getDocument();
            document.insertString(offset, "\n" + text);
        });

    }

    private int getCaretOffset(Editor editor) {

        // Get the document for the current editor
        Document document = editor.getDocument();
        // Get the current line number
        int lineNumber = editor.getCaretModel().getLogicalPosition().line;
        // Get the start offset and end offset for the current line
        int startOffset = document.getLineStartOffset(lineNumber);
        int endOffset = document.getLineEndOffset(lineNumber);
        // Convert the caret position to a document offset
        return endOffset;
    }

    private Editor getCurrentEditor() {
        // Get the project for the current editor
        Project project = ProjectManager.getInstance().getOpenProjects()[0];

        // Get the current editor
        FileEditorManager editorManager = FileEditorManager.getInstance(project);
        return editorManager.getSelectedTextEditor();
    }

    public JSONObject sendJsonPostRequest(String url, JsonObject jsonObject) {
        System.out.println("sending post: \n" + url +"\n"+ jsonObject.toString());
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");

            // Set request body
            StringEntity entity = new StringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);

            // Send POST request
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
//                System.out.println(response.getStatusLine().getStatusCode());
                // Read response content
                HttpEntity responseEntity = response.getEntity();
                String responseContent = EntityUtils.toString(responseEntity, Charset.forName("utf-8"));
//                System.out.println(responseContent);
                // Parse the JSON object from the response content
                JSONObject respObj = new JSONObject(responseContent);
                return respObj;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject uploadFile(String uploadEndpoint, String filePath) throws IOException, ParseException {
        // Create a new file object with the given file path
        File file = new File(filePath);
        // Create a new HTTP client
        HttpClient httpClient = HttpClientBuilder.create().build();
        // Create a new HTTP POST request
        HttpPost httpPost = new HttpPost(uploadEndpoint);
        // Create a new multipart entity builder
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        // Add the file to the multipart entity builder
        builder.addBinaryBody("file", file, org.apache.http.entity.ContentType.DEFAULT_BINARY, file.getName());
        // Build the multipart entity
        HttpEntity multipart = builder.build();
        // Set the multipart entity as the request entity
        httpPost.setEntity(multipart);
        // Execute the request and get the response
        HttpResponse response = httpClient.execute(httpPost);
        // Get the response entity
        HttpEntity responseEntity = response.getEntity();
        // Convert the response entity to a string
        String responseString = EntityUtils.toString(responseEntity);
//        System.out.println(responseString);
        JSONObject jsonObject = new JSONObject(responseString);
        // Print the response string
        return jsonObject;
    }


}