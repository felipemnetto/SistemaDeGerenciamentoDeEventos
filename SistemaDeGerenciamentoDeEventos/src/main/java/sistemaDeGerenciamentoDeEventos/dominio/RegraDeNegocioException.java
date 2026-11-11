package sistemaDeGerenciamentoDeEventos.dominio;

public class RegraDeNegocioException extends RuntimeException{

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }

    public RegraDeNegocioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

}
