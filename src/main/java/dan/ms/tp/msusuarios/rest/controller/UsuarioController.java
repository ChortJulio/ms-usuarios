package dan.ms.tp.msusuarios.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dan.ms.tp.msusuarios.exception.ClienteNoEncontradoException;
import dan.ms.tp.msusuarios.exception.UsuarioNoAsociadoException;
import dan.ms.tp.msusuarios.exception.UsuarioNoEncontradoException;
import dan.ms.tp.msusuarios.exception.UsuarioUsernameDuplicadoException;
import dan.ms.tp.msusuarios.modelo.Usuario;
import dan.ms.tp.msusuarios.rest.service.UsuarioService;

@RestController
@RequestMapping("api/usuario")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuario() {
        return ResponseEntity.ok().body(usuarioService.getAllUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(usuarioService.getUsuario(id));
        } catch (UsuarioNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<Usuario> getUsuarioByIdCliente(@PathVariable Integer idCliente) {
        //TODO: REVISAR
        try {
            return ResponseEntity.ok().body(usuarioService.getUsuarioByCliente(idCliente));
        } catch (UsuarioNoAsociadoException e) {
            return ResponseEntity.notFound().build();
        } catch (ClienteNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }

    }

    /*ESTA NO LA ENTENDI, dice por tipo de usuario y ciente

    @GetMapping("/{tipoUsuario}/")
    public ResponseEntity<List<Usuario>> getAllUsuarioByTipoUsuario(@PathVariable String tipoUsuario) {

        return ResponseEntity.ok().body(usuarioService.findAllByTipoUsuario(tipoUsuario));
    }
    */

    @PostMapping
    public ResponseEntity<Usuario> postUsuario(@RequestBody Usuario usuario) {
        try {
            return ResponseEntity.ok().body(usuarioService.createUsuario(usuario));
        // } catch (UsuarioDuplicadoException e){ // Actualmente es imposible esta excepción
        //     return ResponseEntity.status(HttpStatusCode.valueOf(409)).build();
        } catch (UsuarioUsernameDuplicadoException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(409)).build();
        } catch (ClienteNoEncontradoException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(409)).build(); // TODO: Ver código
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> putUsuario(@RequestBody Usuario usuario, @PathVariable Integer id) {
        try {
            usuarioService.updateUsuario(usuario, id);
            return ResponseEntity.ok().body("El usuario "+usuario.getId()+" se actualizó exitosamente.");
        } catch (UsuarioUsernameDuplicadoException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(409)).build();
        } catch (UsuarioNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        } catch (ClienteNoEncontradoException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(409)).build(); // TODO: Ver código
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        try {
            usuarioService.deleteUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (UsuarioNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
