package fichier_doc;

public enum AutomateState {
    INIT,
    FIRST, // Etat 1 de fat SM
    SECOND, // Etat quand on detecte _ dans etat 1
    THIRD, // Etat 1 de baby SM
    RETURN,
    ERROR
}
