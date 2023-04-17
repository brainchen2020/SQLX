package org.sqlx;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.sun.istack.NotNull;

public class MyPluginToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        ContentFactory contentFactory = ContentFactory.getInstance();

        MyToolWindow myToolWindow = new MyToolWindow(project);
        Content content = contentFactory.createContent(myToolWindow, "", false);
        toolWindow.getContentManager().addContent(content);
//        toolWindow.setTitle("My Tool Window");
    }

}
