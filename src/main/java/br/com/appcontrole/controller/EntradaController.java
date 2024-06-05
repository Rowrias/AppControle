package br.com.appcontrole.controller;

import br.com.appcontrole.model.Entrada;
import br.com.appcontrole.service.EntradaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entradas")
public class EntradaController {

    @Autowired
    private EntradaService entradaService;

    @PostMapping
    public ResponseEntity<Entrada> createEntrada(@RequestBody Entrada entrada) {
        Entrada createdEntrada = entradaService.insere(entrada);
        return ResponseEntity.ok(createdEntrada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entrada> getEntradaById(@PathVariable Long id) {
        Entrada entrada = entradaService.buscaPorId(id);
        if (entrada != null) {
            return ResponseEntity.ok(entrada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Entrada>> getAllEntradas() {
        List<Entrada> entradas = entradaService.buscaTodos();
        return ResponseEntity.ok(entradas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entrada> updateEntrada(@PathVariable Long id, @RequestBody Entrada entrada) {
        if (!id.equals(entrada.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Entrada updatedEntrada = entradaService.atualiza(entrada);
        return ResponseEntity.ok(updatedEntrada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntrada(@PathVariable Long id) {
        entradaService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
