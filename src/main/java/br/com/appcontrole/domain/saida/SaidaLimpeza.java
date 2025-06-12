package br.com.appcontrole.domain.saida;

import br.com.appcontrole.domain.saida.SaidaLimpeza; // Importa o seu SaidaRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component; // Importante para o Spring gerenciar a classe
import org.springframework.transaction.annotation.Transactional; // Importante para operações de banco de dados

import java.time.LocalDateTime; // Para trabalhar com data e hora
import java.time.Period;       // Para subtrair períodos (como meses)

@Component
public class SaidaLimpeza {

    @Autowired // Injeta o SaidaRepository para interagir com o banco de dados
    private SaidaRepository saidaRepository;

    // Este método será executado periodicamente para limpar registros antigos.
    // A expressão cron "0 0 1 * * ?" significa:
    // - 0 segundos
    // - 0 minutos
    // - 0 hora (0 = meia-noite)
    // - * : qualquer dia do mês
    // - * : qualquer mês
    // - ? : qualquer dia da semana
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional // Garante que a operação de exclusão seja atômica (ou tudo é deletado ou nada)
    public void LimpezaDeSaidaVelha() {
        // Calcula a data e hora exata de X meses atrás a partir do momento atual.
        // Se hoje é 01/01/2025 12:00, sixMonthsAgo será 01/06/2024 12:00.
        LocalDateTime seisMesesAtras = LocalDateTime.now().minus(Period.ofMonths(6));

        System.out.println("# Executando tarefa de limpeza de Saídas. Deletando registros com dataSaida anterior a: " + seisMesesAtras + " #");

        // Chama o método do repositório para deletar todos os registros se a 'dataSaida' for anterior ao 'seisMesesAtras'.
        int deletedCount = saidaRepository.deleteByDataSaidaBefore(seisMesesAtras);

        System.out.println("# " + deletedCount + " registros de Saída deletados. #");
    }
}
