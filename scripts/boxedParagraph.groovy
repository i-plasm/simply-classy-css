/* BoxedParagraph script from the SimplyClassyCSS project. https://github.com/i-plasm/simply-classy-css 
 *
 * Wraps the selected text in a JTextComponent with the desired syntax to make use of the SimplyClassyCSS
 * special tags and classes for highlighting and emphasizing text.
 * BoxedParagraph automatically surrounds the paragraph you'd like to enclose in a box with 
 * <p class="blue-dotted"">my_paragraph</p> 
 * The default is blue dotted box (`blue-dotted`). You can change it by modifying 
 * the `class` attribute (see the "Syntax" table for the options - for instance to `blue-dotted`,
 * `blue-solid`, etc.).
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

        editor.replaceSelection("<p class=\"blue-dotted\">" + editor.getSelectedText() + "</p>")


    }
