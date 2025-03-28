package fichier_doc; /** @author Ahmed Khoumsi */

/** Classe Abstraite dont heriteront les classes fichier_doc.FeuilleAST et fichier_doc.NoeudAST
 */
public abstract class ElemAST {

  
  /** Evaluation d'AST
   */
  public abstract int EvalAST();


  /** Lecture d'AST
   */
  public abstract String LectAST();


/** ErreurEvalAST() envoie un message d'erreur lors de la construction d'AST
 */  
  public void ErreurEvalAST(String s) {	
    // 
  }

}
