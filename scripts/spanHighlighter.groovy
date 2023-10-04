/* SpanHighlighter script from the SimplyClassyCSS project. https://github.com/i-plasm/simply-classy-css 
 *
 * Wraps the selected text in a JTextComponent with the desired syntax to make use of the SimplyClassyCSS
 * special tags and classes for highlighting and emphasizing text.
 * SpanHighlighter automatically surrounds the text you'd like to highlight with 
 * <span class="simply-yellow">my_text</span>
 * The default is yellow (`simply-yellow`). You can modify the color simply by changing the `class` attribute (see the
 * "Syntax" table for the options - for instance `simply-yellow`, `simply-blue`, etc.).
 * 
 * More information on usage: https://github.com/i-plasm/simply-classy-css 
*/

// @ExecutionModes({ON_SINGLE_NODE="/main_menu/format/highlight"})

import java.awt.Component
import java.awt.KeyboardFocusManager
import javax.swing.text.JTextComponent

highlight()

def static void highlight() {

        Component keyboardFocusOwner =
                KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner()

        if (!(keyboardFocusOwner instanceof JTextComponent)) {
            return
        }

        JTextComponent editor = (JTextComponent) keyboardFocusOwner

        if (!editor.isEditable()) {
            return
        }

        editor.replaceSelection("<span class=\"simply-yellow\">" + editor.getSelectedText() + "</span>")


    }
