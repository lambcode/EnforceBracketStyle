package net.lambcode.enforceBracketStyle;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.editor.event.EditorFactoryAdapter;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;

public class EnforceBracketStylePlugin implements ApplicationComponent {
    private TypedActionHandler oldHandler;

    public EnforceBracketStylePlugin() {
    }

    @Override
    public void initComponent() {
        ActionManager actionManager = ActionManager.getInstance();
        AnAction enforceBracketStyleAction = actionManager.getAction("EnforceBracketStyleAction");

        EditorFactory.getInstance().addEditorFactoryListener(new EditorFactoryAdapter() {
            @Override
            public void editorCreated(@NotNull EditorFactoryEvent event) {
                enforceBracketStyleAction.registerCustomShortcutSet(KeyEvent.VK_ENTER, 0, event.getEditor().getComponent());
            }
        });
    }

    @Override
    public void disposeComponent() {
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "EnforceBracketStylePlugin";
    }
}
