//// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
//
//package org.intellij.sdk.action;
//
//import com.intellij.openapi.actionSystem.AnAction;
//import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.actionSystem.CommonDataKeys;
//import com.intellij.openapi.application.ApplicationManager;
//import com.intellij.openapi.command.WriteCommandAction;
//import com.intellij.openapi.editor.Document;
//import com.intellij.openapi.editor.Editor;
//import com.intellij.openapi.editor.LogicalPosition;
//import com.intellij.openapi.editor.event.EditorMouseAdapter;
//import com.intellij.openapi.editor.event.EditorMouseEvent;
//import com.intellij.openapi.editor.event.EditorMouseMotionListener;
//import com.intellij.openapi.fileEditor.FileDocumentManager;
//import com.intellij.openapi.fileEditor.FileEditorManager;
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.project.ProjectManager;
//import com.intellij.openapi.ui.Messages;
//import com.intellij.openapi.util.TextRange;
//import com.intellij.pom.Navigatable;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.MouseListener;
//
///**
// * Action class to demonstrate how to interact with the IntelliJ Platform.
// * The only action this class performs is to provide the user with a popup dialog as feedback.
// * Typically this class is instantiated by the IntelliJ Platform framework based on declarations
// * in the plugin.xml file. But when added at runtime this class is instantiated by an action group.
// */
//public class PopupDialogAction extends AnAction {
//
//  /**
//   * This default constructor is used by the IntelliJ Platform framework to instantiate this class based on plugin.xml
//   * declarations. Only needed in {@link PopupDialogAction} class because a second constructor is overridden.
//   *
//   * @see AnAction#AnAction()
//   */
//  public PopupDialogAction() {
//    super();
//  }
//
//  /**
//   * This constructor is used to support dynamically added menu actions.
//   * It sets the text, description to be displayed for the menu item.
//   * Otherwise, the default AnAction constructor is used by the IntelliJ Platform.
//   *
//   * @param text        The text to be displayed as a menu item.
//   * @param description The description of the menu item.
//   * @param icon        The icon to be used with the menu item.
//   */
//  public PopupDialogAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
//    super(text, description, icon);
//  }
//
//  /**
//   * Gives the user feedback when the dynamic action menu is chosen.
//   * Pops a simple message dialog. See the psi_demo plugin for an
//   * example of how to use {@link AnActionEvent} to access data.
//   *
//   * @param event Event received when the associated menu item is chosen.
//   */
//  @Override
//  public void actionPerformed(@NotNull AnActionEvent event) {
//    // Using the event, create and show a dialog
//    Project currentProject = event.getProject();
//    StringBuilder dlgMsg = new StringBuilder(event.getPresentation().getText() + " Selected!");
//    String dlgTitle = event.getPresentation().getDescription();
//    // If an element is selected in the editor, add info about it.
//    Navigatable nav = event.getData(CommonDataKeys.NAVIGATABLE);
//    if (nav != null) {
//      dlgMsg.append(String.format("\nSelected Element: %s", nav.toString()));
//    }
////    Messages.showMessageDialog(currentProject, dlgMsg.toString(), dlgTitle, Messages.getInformationIcon());
//
//    MyDialog dialog = new MyDialog();
//    dialog.show();
//    // insert the response at the current caret position
//    Editor editor = getCurrentEditor();
//    int offset = getCaretOffset(editor);
//    WriteCommandAction.runWriteCommandAction(currentProject, () -> {
//      Document document = editor.getDocument();
//      document.insertString(offset, "\n"+dialog.getRespText());
//    });
//
//  }
//
//  private int getCaretOffset(Editor editor) {
//
//    // Get the document for the current editor
//    Document document = editor.getDocument();
//    // Get the current line number
//    int lineNumber = editor.getCaretModel().getLogicalPosition().line;
//    // Get the start offset and end offset for the current line
//    int startOffset = document.getLineStartOffset(lineNumber);
//    int endOffset = document.getLineEndOffset(lineNumber);
//    // Convert the caret position to a document offset
//    return endOffset;
//  }
//
//  private Editor getCurrentEditor() {
//    // Get the project for the current editor
//    Project project = ProjectManager.getInstance().getOpenProjects()[0];
//
//    // Get the current editor
//    FileEditorManager editorManager = FileEditorManager.getInstance(project);
//    return editorManager.getSelectedTextEditor();
//  }
//
//  /**
//   * Determines whether this menu item is available for the current context.
//   * Requires a project to be open.
//   *
//   * @param e Event received when the associated group-id menu is chosen.
//   */
//  @Override
//  public void update(AnActionEvent e) {
//    // Set the availability based on whether a project is open
//    Project project = e.getProject();
//    e.getPresentation().setEnabledAndVisible(project != null);
//  }
//
//}
