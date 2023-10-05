/* Green-SpanHighlighter4FreeplaneHTML script. This is an experimental script for
 * in-line highlighting of HTML nodes in Freeplane. Future versions may also support 
 * highlighting in the SimplyHTML editor itself.
 * This is an EXPERIMENTAL script from the SimplyClassyCSS project. 
 * https://github.com/i-plasm/simply-classy-css
 *
 * PLEASE REMEMBER THIS SCRIPT IS MEANT TO BE TESTED FOR
 * RELIABILITY, COMPATIBILITY WITH FREEPLANE, ETC. NO GUARANTEES OF ANY KIND.
 *
 * REQUIREMENTS:
 *
 * - Freeplane v.1.11.7 or above
 * - Any SimplyClassyCSS style. The CSS of node core that is meant to be highlighted
 *   must be set to such style
 * - The node core must be of the HTML content type
 * 
 * INSTALLATION:
 *
 * Install the script and set a Freeplane shortcut to it (it gets installed to 
 * the menu: "FORMAT -> HIGHLIGHT -> GREEN SPAN HIGHLIGHTER4FREEPLANE HTML"). 
 * 
 * IMPORTANT! the need for a shortcut seems to be ABSOLUTELY REQUIRED in Windows, otherwise 
 * clicking on the node core and then going to the menu makes the core html editor lose focus, 
 * and the script can't work without this focus.
 *
 * USAGE
 * 
 * The script is meant to be a easy, fast and automatic of highlighting html nodes in-line
 * Assuming your environment meets the requirements, it's really just a mater of selecting the
 * text:
 *
 * 2. In the in-line html node editor, select the text to be highlighted
 * 3. Call the script via the shortcut
 * 
 *
 * HOW IT WORKS:
 *
 * It wraps the raw html (via standard Java API methods for JEditorPane) of the selected text in
 * the in-line node editor of Freeplane with the desired html tag to make use of the SimplyClassyCSS
 * special CSS classes for highlighting and emphasizing text. In this particular script,
 * the class is `simply-green`, which defines a green highlighter.
 * In the raw html, the selected text ends up being ttagged with
 * <span class="simply-green">my_text</span>
 *
 * SIMILAR SCRIPTS CAN BE MADE TO SUPPORT OTHER COLORS CURRENTLY OFFERED BY SIMPLYCLASSYCSS: 
 * yellow, blue, green, red, purple
 *
 * Discussion on: https://github.com/freeplane/freeplane/discussions/1271
 */

// @ExecutionModes({ON_SINGLE_NODE="/main_menu/format/highlight"})

import java.awt.Color;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.MutableAttributeSet;
                        
highlight()

def static void highlight() {

Component keyboardFocusOwner =
    KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner()

	Component componentToEvaluate = keyboardFocusOwner;
	while (componentToEvaluate != null) {
		if (componentToEvaluate.getClass().getName().contains("MTextController")) {
			break;
		}
		componentToEvaluate = componentToEvaluate.getParent();
	}
	    
    if (componentToEvaluate instanceof JEditorPane ) {
            
		SimpleAttributeSet aSet = new SimpleAttributeSet();
		aSet.addAttribute(HTML.Attribute.CLASS, "simply-green");
		SimpleAttributeSet set = new SimpleAttributeSet();
		set.addAttribute(HTML.Tag.SPAN, aSet);

		JEditorPane htmlEditor = (JEditorPane) componentToEvaluate;
		HTMLDocument doc = (HTMLDocument) htmlEditor.getDocument();
		doc.setCharacterAttributes(htmlEditor.getSelectionStart(),
		htmlEditor.getSelectionEnd() - htmlEditor.getSelectionStart(), set, false);
    }
 
                
}


