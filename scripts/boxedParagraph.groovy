/* BoxedParagraph script from the SimplyClassyCSS project. https://github.com/i-plasm/simply-classy-css 
 *
 * Wraps the selected text in a JTextComponent with the desired syntax to make use of the SimplyClassyCSS
 * special tags and classes for highlighting and emphasizing text.
 * BoxedParagraph automatically surrounds the paragraph you'd like to enclose in a box with 
 * <p class="...">my_paragraph</p> 
 * The default is dotted box (`b-do`). You can change it by modifying 
 * the `class` attribute (see the "Syntax" table for the options - for instance `b-do` stands for "dotted box",
 * `b-so` is "solid box", etc.).
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

        editor.replaceSelection("<p class=\"b-do\">" + editor.getSelectedText() + "</p>")


    }
