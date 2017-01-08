package net.lambcode.enforceBracketStyle;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.KeyEvent;


public class EnforceBracketStyleAction extends EditorAction {

    protected EnforceBracketStyleAction() {
        super(new Handler());
    }

    private static class Handler extends EditorActionHandler {
        private final CustomEnterActionHandler customEnterActionHandler;

        public Handler() {
            customEnterActionHandler = new CustomEnterActionHandler();
        }

        @Override
        public void execute(@NotNull Editor editor, @NotNull DataContext context) {
            if (customEnterActionHandler.shouldFormatBracket(editor)) {
                editor.getCaretModel().moveCaretRelatively(-1, 0  , false, false, false);
                runAlreadyRegisteredActions(context);
                editor.getCaretModel().moveCaretRelatively(1, 0  , false, false, false);
            }

            runAlreadyRegisteredActions(context);
        }

        private void runAlreadyRegisteredActions(DataContext context) {
            Keymap activeKeymap = KeymapManager.getInstance().getActiveKeymap();
            KeyStroke enterKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
            for (String id : activeKeymap.getActionIds(enterKeyStroke)) {
                AnAction action = ActionManager.getInstance().getAction(id);
                if (action != null)
                    if (executeAction(action, context))
                        break;
            }
        }

        /** Retrieved from ideavim source code **/
        public static boolean executeAction(@NotNull AnAction action, @NotNull DataContext context) {
            // Hopefully all the arguments are sufficient. So far they all seem to work OK.
            // We don't have a specific InputEvent so that is null
            // What is "place"? Leave it the empty string for now.
            // Is the template presentation sufficient?
            // What are the modifiers? Is zero OK?
            final AnActionEvent event = new AnActionEvent(null, context, "", action.getTemplatePresentation(),
                    ActionManager.getInstance(), 0);
            action.update(event);
            if (event.getPresentation().isEnabled()) {
                action.actionPerformed(event);
                return true;
            }
            return false;
        }
    }

}
