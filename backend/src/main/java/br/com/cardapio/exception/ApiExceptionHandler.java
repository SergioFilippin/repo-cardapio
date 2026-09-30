package br.com.cardapio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        return problema(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage());
    }

    @ExceptionHandler({RegraNegocioException.class, IllegalArgumentException.class})
    public ResponseEntity<ProblemDetail> tratarRegraNegocio(RuntimeException ex) {
        return problema(HttpStatus.BAD_REQUEST, "Requisição inválida", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> tratarValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new LinkedHashMap<>();
        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            erros.putIfAbsent(erro.getField(), erro.getDefaultMessage());
        }

        ProblemDetail detail = criarProblema(
                HttpStatus.BAD_REQUEST, "Dados inválidos", "Revise os campos informados.");
        detail.setProperty("erros", erros);
        return ResponseEntity.badRequest().body(detail);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> tratarJsonInvalido(HttpMessageNotReadableException ex) {
        return problema(HttpStatus.BAD_REQUEST, "JSON inválido",
                "Verifique os tipos dos campos e use um tipo de prato válido.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> tratarParametroInvalido(MethodArgumentTypeMismatchException ex) {
        return problema(HttpStatus.BAD_REQUEST, "Parâmetro inválido",
                "O valor informado para '" + ex.getName() + "' não é válido.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> tratarConflito(DataIntegrityViolationException ex) {
        return problema(HttpStatus.CONFLICT, "Conflito de dados",
                "A operação viola uma restrição de integridade dos dados.");
    }

    private ResponseEntity<ProblemDetail> problema(HttpStatus status, String titulo, String detalhe) {
        return ResponseEntity.status(status).body(criarProblema(status, titulo, detalhe));
    }

    private ProblemDetail criarProblema(HttpStatus status, String titulo, String detalhe) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detalhe);
        problem.setTitle(titulo);
        problem.setType(URI.create("about:blank"));
        return problem;
    }
}
