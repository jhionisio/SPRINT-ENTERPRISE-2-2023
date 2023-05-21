package sprint.feedback.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sprint.feedback.models.Empresa;
import sprint.feedback.repository.EmpresaRepository;

@RestController
@RequestMapping("/api/empresa")
public class EmpresaController {
    Logger log = LoggerFactory.getLogger(EmpresaController.class);

    List<Empresa> empresa = new ArrayList<>();

    @Autowired // IoD IoC
    EmpresaRepository repository;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Empresa>>> index(@RequestParam(required = false) String name, @PageableDefault(size = 5) Pageable pageable){

        List<EntityModel<Empresa>> empresaModel = new ArrayList<>();

        if (name == null) {
            List<Empresa> empresa = repository.findAll(pageable).getContent();
            for (Empresa empresa : empresa) {
                empresaModel.add(getEmpresaModel(empresa));
            }
        } else {
            List<Empresa> empresa = repository.findByNameContaining(name, pageable).getContent();
            for (Empresa empresa : empresa) {
                empresaModel.add(getEmpresaModel(empresa));
            }
        }

        CollectionModel<EntityModel<Empresa>> collectionModel = CollectionModel.of(empresaModel);
        collectionModel.add(getSelfLink());
        return ResponseEntity.ok(collectionModel);

    }

    @PostMapping
    public ResponseEntity<EntityModel<Empresa>> create(@RequestBody @Valid Empresa empresa){
        log.info("Cadastrando empresa: " + empresa);
        Empresa postObj = repository.save(empresa);
        EntityModel<Empresa> empresaModel = getEmpresaModel(postObj);
        empresaModel.add(getSelfLink());
        empresaModel.add(getUpdateLink(postObj.getId()));
        empresaModel.add(getDeleteLink(postObj.getId()));
        return ResponseEntity.created(empresaModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(empresaModel);
    }
    
    @GetMapping("{id}")
    public ResponseEntity<EntityModel<Empresa>> show(@PathVariable Long id){
        log.info("Buscando empresa com id " + id);
        Empresa empresa = getEmpresa(id);
        EntityModel<Empresa> empresaModel = getEmpresaModel(empresa);
        empresaModel.add(getSelfLink());
        empresaModel.add(getUpdateLink(id));
        empresaModel.add(getDeleteLink(id));
        return ResponseEntity.ok(empresaModel);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Empresa> destroy(@PathVariable Long id){
        log.info("Apagando empresa com id " + id);
        repository.delete(getEmpresa(id));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<EntityModel<Empresa>> update(@PathVariable Long id, @RequestBody @Valid Empresa empresa){
        log.info("Alterando empresa com id " + id);
        getEmpresa(id);
        empresa.setId(id);
        Empresa putObj = repository.save(empresa);
        EntityModel<Empresa> empresaModel = getEmpresaModel(putObj);
        empresaModel.add(getSelfLink());
        empresaModel.add(getDeleteLink(putObj.getId()));
        return ResponseEntity.ok(empresa);
    }

    private Empresa getEmpresa(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não existe"));
    }
}
