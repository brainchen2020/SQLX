package org.sqlx;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

@State(
    name = "MyPluginSettings",
    storages = {
        @Storage("myPluginSettings.xml")}
)
public class MyPluginSettings implements PersistentStateComponent<MyPluginSettings.State> {

    private State state = new State();

    public static MyPluginSettings getInstance() {
        return ServiceManager.getService(MyPluginSettings.class);
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void loadState(State state) {
        this.state = state;
    }

    public static class State {
        public String myString = null;
        public String uuid = null;
        public String selectedFilePath = null;
        public Long created = null;

        @Override
        public String toString() {
            return "State{" +
                    "myString='" + myString + '\'' +
                    ", uuid='" + uuid + '\'' +
                    ", selectedFilePath='" + selectedFilePath + '\'' +
                    ", created=" + created +
                    '}';
        }
    }
}