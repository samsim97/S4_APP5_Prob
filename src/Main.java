import fichier_doc.*;

public class Main {
    public static void main(String[] args) {
        String toWrite = "";
        System.out.println("Debut d'analyse lexicale");
        if (args.length == 0) {
            args = new String [2];
            args[0] = "ExpArith.txt";
            args[1] = "ResultatLexical.txt";
        }
        Reader r = new Reader(args[0]);

        // AnalLex lexical = new AnalLex("A=A+B;\nC=A+B;");
        AnalLex lexical = new AnalLex(r.toString()); // Creation de l'analyseur lexical

        // Execution de l'analyseur lexical
        Terminal t = null;
        while(lexical.resteTerminal()) {
            t = lexical.prochainTerminal();
            toWrite += t.name; // + "\n" ;  // toWrite contient le resultat
            if (t.type == TerminalTokenType.INSTRUCTION_END) {
                toWrite += "\n";
            }
        }				   //    d'analyse lexicale
        System.out.println(toWrite); 	// Ecriture de toWrite sur la console
        Writer w = new Writer(args[1],toWrite); // Ecriture de toWrite dans fichier args[1]
        System.out.println("Fin d'analyse lexicale");
    }
}