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

import sprint.feedback.models.Usuario;
import sprint.feedback.repository.EmpresaRepository;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    Logger log = LoggerFactory.getLogger(UsuarioController.class);

    List<Usuario> usuario = new ArrayList<>();

    @Autowired // IoD IoC
    EmpresaRepository repository;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> index(@RequestParam(required = false) String name, @PageableDefault(size = 5) Pageable pageable){

        List<EntityModel<Usuario>> usuarioModel = new ArrayList<>();

        if (name == null) {
            List<Usuario> usuario = repository.findAll(pageable).getContent();
            for (Usuario usuario : usuario) {
                usuarioModel.add(getUsuarioModel(usuario));
            }
        } else {
            List<Usuario> usuario = repository.findByNameContaining(name, pageable).getContent();
            for (Usuario usuario : usuario) {
                usuarioModel.add(getUsuarioModel(usuario));
            }
        }

        CollectionModel<EntityModel<Usuario>> collectionModel = CollectionModel.of(usuarioModel);
        collectionModel.add(getSelfLink());
        return ResponseEntity.ok(collectionModel);

    }

    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> create(@RequestBody @Valid Usuario usuario){
        log.info("Cadastrando usuario: " + usuario);
        Usuario postObj = repository.save(usuario);
        EntityModel<Usuario> usuarioModel = getUsuarioModel(postObj);
        usuarioModel.add(getSelfLink());
        usuarioModel.add(getUpdateLink(postObj.getId()));
        usuarioModel.add(getDeleteLink(postObj.getId()));
        return ResponseEntity.created(usuarioModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(usuarioModel);
    }
    
    @GetMapping("{id}")
    public ResponseEntity<EntityModel<Usuario>> show(@PathVariable Long id){
        log.info("Buscando usuario com id " + id);
        Usuario usuario = getUsuario(id);
        EntityModel<Usuario> usuarioModel = getUsuarioModel(usuario);
        usuarioModel.add(getSelfLink());
        usuarioModel.add(getUpdateLink(id));
        usuarioModel.add(getDeleteLink(id));
        return ResponseEntity.ok(usuarioModel);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Usuario> destroy(@PathVariable Long id){
        log.info("Apagando usuario com id " + id);
        repository.delete(getUsuario(id));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<EntityModel<Usuario>> update(@PathVariable Long id, @RequestBody @Valid Usuario usuario){
        log.info("Alterando usuario com id " + id);
        getUsuario(id);
        usuario.setId(id);
        Usuario putObj = repository.save(usuario);
        EntityModel<Usuario> usuarioModel = getUsuarioModel(putObj);
        usuarioModel.add(getSelfLink());
        usuarioModel.add(getDeleteLink(putObj.getId()));
        return ResponseEntity.ok(usuario);
    }

    private Usuario getUsuario(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não existe"));
    }
}
