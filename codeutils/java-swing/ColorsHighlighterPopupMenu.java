
import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractButton;
import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

/*
 * A PopupMenu for highlighting selected text in a JEditorPane by use of the SimplyClassyCSS special classes. 
 * 
 * For the highlights to be rendered, the desired html output document must use any SimplyClassyCSS style.
 *
 * SimplyClassyCSS on Github (for downloads, gallery styles and instructions): 
 * https://github.com/i-plasm/simply-classy-css
 * 
 * If the contents of the JEditorPane should be treated as plain text, then it will parse it and if approved, 
 * then it will directly insert/modify the tag containing the SimplyClassyCSS class. NOTE: a markdown document
 * is an example of a case that should be treated as plain text. The renderer of the markdown as html will have
 * to be set up to use any style of SimplyClassyCSS.
 *
 * If the contents are not to be treated as plain text, then it treats them as HTML. It will then use the standard
 * Java API for modifying the JEditoPane's HTMLDocument so that the selected characters will be highlighted with
 * the chosen color by way of inserting/modifying the html tags containing the special CSS classes.
 * 
 * Removal of highlights is also supported, and the handling is analogous to the handling of highlighting. 
 */
public class ColorsHighlighterPopupMenu extends JPopupMenu {

	public ColorsHighlighterPopupMenu(String[] colors, JEditorPane editor, boolean treatAsPlainText) {

		JMenuItem[] menuItems = new JMenuItem[] {
			(JMenuItem) makeButton("YELLOW"),
			(JMenuItem) makeButton("GREEN"),
			(JMenuItem) makeButton("BLUE"),
			(JMenuItem) makeButton("MAGENTA"),
			(JMenuItem) makeButton("RED")
	};
		
		for (JMenuItem  menuItem : menuItems) {
			menuItem.setName(menuItem.getText().toLowerCase());
			this.add(menuItem);
			String color = menuItem.getName();
			menuItem.addActionListener(l -> highlight(color, editor, treatAsPlainText));
		}
	}

	private static AbstractButton makeButton(String symbol) {
		JMenuItem b = new JMenuItem(symbol) {

					/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

					@Override protected void paintComponent(Graphics g) {
						super.paintComponent(g);

						Color color;
						try {
							Field field = Class.forName("java.awt.Color").getField(symbol.toLowerCase());
							color = (Color)field.get(null);
						} catch (Exception e) {
							color = null; // Not defined
						}

						g.setColor(color);
						g.fillRect(2, 2, getWidth() -4, getHeight()-4);					
					}
				};
		b.setOpaque(true);

		return b;
	}

	public static void highlight(String color, JEditorPane editor, boolean treatAsPlainText) {

		if (editor.getSelectionEnd() - editor.getSelectionStart()== 0){
			JOptionPane.showMessageDialog(null, "You must first select some text.");
			return;
		}

		if (!treatAsPlainText) {
			highlight(color, editor);
		}
		else {


			String selectedText = editor.getSelectedText();
			//if (selectedText.containsIgnoreCase("<span>"))
			if (selectedText.toLowerCase().contains("<span")) {
				switchPlainTextTagColor(editor, color);
			}
			else {
				editor.replaceSelection("<span class=\"simply-" + color + "\">" + editor.getSelectedText() + "</span>");
			}
		}
	}

	/*
	 * Assumes the selected text in the Editor contains the string "<span" (ignoring case).
	 */
	public static void switchPlainTextTagColor(JEditorPane editor, String color) {
		String selectedText = editor.getSelectedText();
		if (selectedText.replaceFirst("<span|<SPAN", "").toLowerCase().contains("<span") ||
				selectedText.replaceFirst("</span|</SPAN", "").toLowerCase().contains("</span")) {
			JOptionPane.showMessageDialog(null, "It seems your text selection contains more than one <span> tag. Please make a proper selection.");
			return;

		}
		
		if (selectedText.toLowerCase().indexOf("<span") > selectedText.toLowerCase().indexOf("</span")) {
			JOptionPane.showMessageDialog(null, "It seems your text selection doesn't have an appropriate <span> tag opening, closing or ordering for the color switch to occur. Please make another selection.");
		}
		
		
		Pattern p = Pattern.compile("(.*?)<span class=\"simply-[a-zA-Z]+\">(.*?)</span>(.*?)", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(selectedText);

		String outputString = "";
		if (m.find()) {
			outputString = m.replaceAll(m.group(1) + "<span class=\"simply-" + color + "\">" + m.group(2) + "</span>" + m.group(3));
			editor.replaceSelection(outputString);
		}
		else {
			JOptionPane.showInternalMessageDialog(null, "It seems your text selection may be not have"
					+ "proper highlighting tag syntax used by SimplyClassyCSS styles, or the <span> tag has more than one attribute. If you believe your SimplyClassyCSS syntax is ok keep in mind doesn't allow redundant spaces or semicolon");
		}
	}

	public static void highlight(String color, JEditorPane componentToEvaluate) {
		JEditorPane htmlEditor = (JEditorPane) componentToEvaluate;
		HTMLDocument doc = (HTMLDocument) htmlEditor.getDocument();


		SimpleAttributeSet firstSet = new SimpleAttributeSet();
		firstSet.addAttribute(HTML.Attribute.CLASS, "simply-" + color);
		SimpleAttributeSet secondSet = new SimpleAttributeSet();
		secondSet.addAttribute(HTML.Tag.SPAN, firstSet);

		doc.setCharacterAttributes(htmlEditor.getSelectionStart(),
				htmlEditor.getSelectionEnd() - htmlEditor.getSelectionStart(), secondSet, false);
	}

	public static void removeSimplyClassyCSSHighlights( JEditorPane editor, boolean treatAsPlainText) {

		if (editor.getSelectionEnd() - editor.getSelectionStart()== 0){
			JOptionPane.showMessageDialog(null, "You must first select some text.");
			return;
		}
		if (treatAsPlainText) {
			removeSimplyClassyCSSPlainTextHighlight(editor);
		}
		else {
			removeSimplyClassyCSSHTMLHighlights(editor);
		}
	}

	public static void removeSimplyClassyCSSPlainTextHighlight( JEditorPane editor) {
		String selectedText = editor.getSelectedText();
		//if (selectedText.containsIgnoreCase("<span>"))
		if (!selectedText.toLowerCase().contains("<span")) return;
		if (selectedText.replaceFirst("<span|<SPAN", "").toLowerCase().contains("<span") ||
				selectedText.replaceFirst("</span|</SPAN", "").toLowerCase().contains("</span")) {
			JOptionPane.showMessageDialog(null, "It seems your text selection contains more than one <span> tag. Please make a proper selection.");
			return;

		}
		Pattern p = Pattern.compile("(.*?)<span class=\"simply-[a-z]+\">(.*?)</span>(.*?)", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(selectedText);

		String outputString = "";
		if (m.find()) {
			outputString = m.replaceAll(m.group(1) + m.group(2) + m.group(3));
			editor.replaceSelection(outputString);
		}
	}

	public static void removeSimplyClassyCSSHTMLHighlights( JEditorPane componentToEvaluate) {

		JEditorPane htmlEditor = componentToEvaluate;
		HTMLDocument doc = (HTMLDocument) htmlEditor.getDocument();

		int textEnd = htmlEditor.getSelectionEnd();

		for (int i = htmlEditor.getSelectionStart(); i < textEnd;) {
			Element characterElement = doc.getCharacterElement(i);
			SimpleAttributeSet charElemSimpleAttributeSet =
					new SimpleAttributeSet(characterElement.getAttributes().copyAttributes());

			MutableAttributeSet spanAttributes =
					(MutableAttributeSet) charElemSimpleAttributeSet.getAttribute(HTML.Tag.SPAN);

			if (spanAttributes != null) {

				ArrayList<?> spanAttributeKeys =
						Collections.list(spanAttributes.getAttributeNames());
				for (Object spanAttrKey : spanAttributeKeys) {
					boolean isSimplyClass =
							spanAttributes.getAttribute(spanAttrKey).toString().toLowerCase().contains("simply-");
					if (isSimplyClass) {
						charElemSimpleAttributeSet.removeAttribute(HTML.Tag.SPAN);
					}
				}
			}

			int last = textEnd < characterElement.getEndOffset() ? textEnd
					: characterElement.getEndOffset();

			try {
				doc.setCharacterAttributes(i, last - i, charElemSimpleAttributeSet, true);
			} catch (Exception e) {
			}
			i = i < last ? last : i + 1;
		}
	}
}

