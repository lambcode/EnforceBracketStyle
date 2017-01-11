package net.lambcode.enforceBracketStyle;

import com.intellij.application.options.JavaCodeStyleSettingsProvider;
import com.intellij.codeStyle.CodeStyleFacade;
import com.intellij.core.CoreJavaCodeStyleSettingsFacade;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.projectView.SettingsProvider;
import com.intellij.lang.Language;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.codeStyle.*;
import com.intellij.psi.impl.source.codeStyle.CodeFormatterFacade;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.KeyEvent;


public class EnforceBracketStyleAction extends EditorAction {

    protected EnforceBracketStyleAction() {
        super(new Handler());
    }

    private static class Handler extends EditorActionHandler {
        private final CustomEnterActionHandler customEnterActionHandler;

        public Handler()
        {
            customEnterActionHandler = new CustomEnterActionHandler();
        }

        @Override
        public void execute(@NotNull Editor editor, @NotNull DataContext context) {

            int currentOffset = editor.getCaretModel().getCurrentCaret().getOffset();
            CharSequence charSequence = editor.getDocument().getCharsSequence();
            if (documentIsJava(editor.getDocument()) && isBraceOnNewlineSettingOn() && customEnterActionHandler.shouldFormatBracket(charSequence, currentOffset)) {
                editor.getCaretModel().moveCaretRelatively(-1, 0  , false, false, false);
                runAlreadyRegisteredActions(context);
                editor.getCaretModel().moveCaretRelatively(1, 0  , false, false, false);
            }

            runAlreadyRegisteredActions(context);
        }

        private boolean documentIsJava(Document document) {
            VirtualFile file = FileDocumentManager.getInstance().getFile(document);
            if (file == null)
                return false;

            return file.getFileType() == JavaFileType.INSTANCE;
        }

        private boolean isBraceOnNewlineSettingOn()
        {
            CommonCodeStyleSettings currentSettings = CodeStyleSettingsManager.getInstance().getCurrentSettings().getCommonSettings(JavaLanguage.INSTANCE);
            return currentSettings.CLASS_BRACE_STYLE == currentSettings.NEXT_LINE && currentSettings.METHOD_BRACE_STYLE == currentSettings.NEXT_LINE;
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
        private static boolean executeAction(@NotNull AnAction action, @NotNull DataContext context) {
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
