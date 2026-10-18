package br.cesar.hemocentro.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.cesar.hemocentro.aplicacao.CpfJaCadastradoException;
import br.cesar.hemocentro.aplicacao.DoadorInaptoException;
import br.cesar.hemocentro.aplicacao.NaoEncontradoException;
import br.cesar.hemocentro.dominio.EstoqueInsuficienteException;

/** Converte erros de regra de negócio em respostas HTTP no formato Problem Details (RFC 9457). */
@RestControllerAdvice
public class TratamentoDeErros extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NaoEncontradoException.class)
    ProblemDetail naoEncontrado(NaoEncontradoException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(CpfJaCadastradoException.class)
    ProblemDetail cpfDuplicado(CpfJaCadastradoException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(DoadorInaptoException.class)
    ProblemDetail inapto(DoadorInaptoException e) {
        var problema = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        problema.setProperty("motivos", e.getMotivos());
        return problema;
    }

    @ExceptionHandler(EstoqueInsuficienteException.class)
    ProblemDetail estoqueInsuficiente(EstoqueInsuficienteException e) {
        var problema = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problema.setProperty("solicitadas", e.getSolicitadas());
        problema.setProperty("disponiveis", e.getDisponiveis());
        return problema;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail argumentoInvalido(IllegalArgumentException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
