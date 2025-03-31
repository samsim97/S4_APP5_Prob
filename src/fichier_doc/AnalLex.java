package fichier_doc; /** @author Ahmed Khoumsi */

import java.util.regex.Pattern;

/** Cette classe effectue l'analyse lexicale
 */
public class AnalLex {

// Attributs
//  ...
  // private final String LITERAL_PATTERN = "([A-Z][a-zA-Z]*(_[a-zA-Z]+)*)|[0-9]+";
  // private final String PARENTHESIS_OPEN_PATTERN = "\\({1}";
  // private final String PARENTHESIS_CLOSE_PATTERN = "\\){1}";
  // private final String PLUS_MINUS_PATTERN = "+|-";
  // private final String MULTIPLIER_DIVIDER_PATTERN = "*|/";

  private final String ALPHABETIC_UPPER_CASE_VALUE_PATTERN = "^[A-Z]$";
  private final String ALPHABETIC_LOWER_CASE_VALUE_PATTERN = "^[a-z]$";
  private final String ALPHABETIC_UPPER_LOWER_CASE_VALUE_PATTERN = "^[a-zA-Z]$";
  private final String UNDER_PATTERN = "^_$";
  private final String DIGIT_PATTERN = "^[0-9]$";
  private final String PARENTHESIS_OPEN_PATTERN = "^\\($";
  private final String PARENTHESIS_CLOSE_PATTERN = "^\\)$";
  private final String PLUS_MINUS_PATTERN = "^\\+|-$";
  private final String MULTIPLIER_DIVIDER_PATTERN = "^\\*|/$";
  private final String SEMI_COLON_PATTERN = "^;$";
  private final String EQUAL_PATTERN = "^=$";

  private String expression;
  private int selectedCharIndex;
  private AutomateState automateState;

  private Pattern pattern_alpha_upper_case;
  private Pattern pattern_alpha_lower_case;
  private Pattern pattern_upper_lower_case;
  private Pattern pattern_underscore;
  private Pattern pattern_digit;
  private Pattern pattern_open_parenthesis;
  private Pattern pattern_close_parenthesis;
  private Pattern pattern_plus_minus;
  private Pattern pattern_multiplier_divider;
  private Pattern pattern_semi_colon;
  private Pattern pattern_equal;


/** Constructeur pour l'initialisation d'attribut(s)
 */
  public AnalLex(String expression) {  // arguments possibles
    this.expression = expression.replace("\r", "").replace("\n", "").replace(" ", "");
    this.selectedCharIndex = 0;
    this.automateState = AutomateState.INIT;

    pattern_alpha_upper_case = Pattern.compile(ALPHABETIC_UPPER_CASE_VALUE_PATTERN);
    pattern_alpha_lower_case = Pattern.compile(ALPHABETIC_LOWER_CASE_VALUE_PATTERN);
    pattern_upper_lower_case = Pattern.compile(ALPHABETIC_UPPER_LOWER_CASE_VALUE_PATTERN);
    pattern_underscore = Pattern.compile(UNDER_PATTERN);
    pattern_digit = Pattern.compile(DIGIT_PATTERN);
    pattern_open_parenthesis = Pattern.compile(PARENTHESIS_OPEN_PATTERN);
    pattern_close_parenthesis = Pattern.compile(PARENTHESIS_CLOSE_PATTERN);
    pattern_plus_minus = Pattern.compile(PLUS_MINUS_PATTERN);
    pattern_multiplier_divider = Pattern.compile(MULTIPLIER_DIVIDER_PATTERN);
    pattern_semi_colon = Pattern.compile(SEMI_COLON_PATTERN);
    pattern_equal = Pattern.compile(EQUAL_PATTERN);
    // AnalyseExpression(expression);
  }

/** resteTerminal() retourne :
      false  si tous les terminaux de l'expression arithmetique ont ete retournes
      true s'il reste encore au moins un terminal qui n'a pas ete retourne 
 */
  public boolean resteTerminal( ) {
    return expression.length() >= selectedCharIndex + 1;
  }
  
  
/** prochainTerminal() retourne le prochain terminal
      Cette methode est une implementation d'un AEF
 */  
  public Terminal prochainTerminal( ) {
    if (selectedCharIndex == expression.length()) {
      throw new RuntimeException("You donut check with 'resteTerminal' first.");
    }

    automateState = AutomateState.INIT;
    Terminal terminal;
    while (true) {
      switch (automateState) {
        case INIT:
          if (pattern_alpha_upper_case.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            automateState = AutomateState.FIRST;
          } else if (pattern_digit.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            automateState = AutomateState.THIRD;
          } else if (pattern_open_parenthesis.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.PARENTHESIS_OPEN);
            selectedCharIndex++;
            return terminal;
          } else if (pattern_close_parenthesis.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.PARENTHESIS_CLOSE);
            selectedCharIndex++;
            return terminal;
          } else if (pattern_plus_minus.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.OPERATOR_PLUS_MINUS);
            selectedCharIndex++;
            return terminal;
          } else if (pattern_multiplier_divider.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.OPERATOR_MULTIPLIER_DIVIDER);
            selectedCharIndex++;
            return terminal;
          } else if (pattern_semi_colon.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.INSTRUCTION_END);
            selectedCharIndex++;
            return terminal;
          } else if (pattern_semi_colon.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.INSTRUCTION_END);
            selectedCharIndex++;
            return terminal;
          } else if (pattern_equal.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.OPERATOR_ASSIGN);
            selectedCharIndex++;
            return terminal;
          } else {
            automateState = AutomateState.ERROR;
          }
          break;
        case FIRST: // Etat 1 de fat SM
          if (pattern_upper_lower_case.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            automateState = AutomateState.FIRST;
            selectedCharIndex++;
          } else if (pattern_underscore.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            automateState = AutomateState.SECOND;
            selectedCharIndex++;
          } else if (pattern_open_parenthesis.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.LITERAL);
            return terminal;
          } else if (pattern_close_parenthesis.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.LITERAL);
            return terminal;
          } else if (pattern_plus_minus.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.LITERAL);
            return terminal;
          } else if (pattern_multiplier_divider.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.LITERAL);
            return terminal;
          } else if (pattern_semi_colon.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.LITERAL);
            return terminal;
          } else if (pattern_equal.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.LITERAL);
            return terminal;
          } else {
            automateState = AutomateState.ERROR;
          }
          break;
        case SECOND: // Etat quand on detecte _ dans etat 1
          if (pattern_upper_lower_case.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            automateState = AutomateState.FIRST;
            selectedCharIndex++;
          } else {
            automateState = AutomateState.ERROR;
          }
          break;
        case THIRD: // Etat 1 de baby SM
          if (pattern_digit.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            automateState = AutomateState.THIRD;
            selectedCharIndex++;
          } else if (pattern_open_parenthesis.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.LITERAL);
            return terminal;
          } else if (pattern_close_parenthesis.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.LITERAL);
            return terminal;
          } else if (pattern_plus_minus.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.LITERAL);
            return terminal;
          } else if (pattern_multiplier_divider.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.LITERAL);
            return terminal;
          } else if (pattern_semi_colon.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.LITERAL);
            return terminal;
          } else if (pattern_equal.matcher(String.valueOf(expression.charAt(selectedCharIndex))).find()) {
            terminal = new Terminal(TerminalTokenType.LITERAL);
            return terminal;
          } else {
            automateState = AutomateState.ERROR;
          }
          break;
        case RETURN:
          break;
        case ERROR:
          ErreurLex("Invalid char (" + expression.charAt(selectedCharIndex) + ") at position " + selectedCharIndex);
          break;
        case null, default:
          ErreurLex("Invalid state");
          break;
      }
    }
  }

 
/** ErreurLex() envoie un message d'erreur lexicale
 */ 
  public void ErreurLex(String s) {	
     throw new RuntimeException(s);
  }
}
