package sprint.feedback.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import in.one.in_one.models.Categorias;
import in.one.in_one.models.Documentos;
import in.one.in_one.repository.CategoriasRepository;
import in.one.in_one.repository.DocumentosRepository;

@Configuration
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    CategoriasRepository categoriasRepository;
    DocumentosRepository documentosRepository;

    @Override
    public void run(String... args) throws Exception {
        documentosRepository.saveAll(List.of(
            new Documentos(1L, 543345543345, "x", "Registro geral", "Emitido,dia,:,8"),
            new Documentos(1L, 768876678876, "y", "Registro geral", "Emitido,dia,:,9")

        ))
        categoriasRepository.saveAll(List.of(
            new Categorias(1L, 1, "white", "Certificados", getDocId(1L,2L)),
            new Categorias(1L, 1, "white", "Certificados", getDocId(1L,2L)),
            new Categorias(1L, 1, "white", "Certificados", getDocId(1L,2L))
        ));
        
    }
    
}
