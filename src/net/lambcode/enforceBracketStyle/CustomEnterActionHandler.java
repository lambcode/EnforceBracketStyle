package net.lambcode.enforceBracketStyle;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.Caret;

public class CustomEnterActionHandler {

    public boolean shouldFormatBracket(Editor editor) {
        int currentOffset = editor.getCaretModel().getCurrentCaret().getOffset();
        boolean leftIsOpenBracket = editor.getDocument().getCharsSequence().charAt(currentOffset - 1) == '{';
        boolean rightIsCloseBracket = editor.getDocument().getCharsSequence().charAt(currentOffset) == '}';

        return leftIsOpenBracket && rightIsCloseBracket;
    }
}
