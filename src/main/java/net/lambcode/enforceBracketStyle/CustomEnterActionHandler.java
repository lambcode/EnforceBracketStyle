package net.lambcode.enforceBracketStyle;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.Caret;

public class CustomEnterActionHandler {

    public boolean shouldFormatBracket(CharSequence charSequence, int currentOffset) {
        return characterToLeftIsOpenBracket(charSequence, currentOffset) && notFirstNonWhitespaceCharacterOnLine(charSequence, currentOffset);
    }

    private boolean characterToLeftIsOpenBracket(CharSequence charSequence, int currentOffset) {
        if (currentOffset == 0)
            return false;

        return charSequence.charAt(currentOffset - 1) == '{';
    }

    private boolean notFirstNonWhitespaceCharacterOnLine(CharSequence charSequence, int currentOffset) {
        boolean encounteredNonWhiteSpace = false;
        boolean encounteredNewline = false;
        currentOffset -= 2; //skip current & prior character ( the '{' bracket )

        while (!encounteredNewline
                && !encounteredNonWhiteSpace
                && currentOffset < charSequence.length()
                && currentOffset >= 0)
        {
            char currentChar = charSequence.charAt(currentOffset);

            if (isLineBreakCharacter(currentChar))
                encounteredNewline = true;
            else if (!Character.isWhitespace(currentChar))
                encounteredNonWhiteSpace = true;

            currentOffset--;
        }
        return encounteredNonWhiteSpace;
    }

    private boolean isLineBreakCharacter(char currentChar) {
        return currentChar == '\r' || currentChar == '\n';
    }
}
