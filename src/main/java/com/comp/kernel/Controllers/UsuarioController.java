package com.comp.kernel.Controllers;

import com.comp.kernel.DTOs.UsuarioDTO;
import com.comp.kernel.Models.Usuario;
import com.comp.kernel.Repositories.UsuarioRepository;
import com.comp.kernel.Services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api")
public class UsuarioController {

    public final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/v1/registrar/usuario")
    public ResponseEntity<Map<String, String>> createUsuario(@RequestBody Map<String, String> data)
    {
        ResponseEntity<Map<String, String>> response = ResponseEntity.badRequest().body(Map.of("Erro", "Desconhecido"));
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome(data.get("name"));
        dto.setEmail(data.get("email"));
        dto.setSenha(data.get("senha"));
        if(dto.getNome().isEmpty() || dto.getEmail().isEmpty() || dto.getSenha().isEmpty())
            response = ResponseEntity.badRequest().body(Map.of("Erro", "Preencha todos os campos"));
        else {
            try
            {
                if (usuarioService.criarUsuario(dto) != null)
                {
                    response = ResponseEntity.ok(Map.of("nome", dto.getNome(),
                                                    "email", dto.getEmail()));
                }
            } catch (Exception e) {
               System.out.println("Erro UsuarioService");
               response = ResponseEntity.badRequest().body(Map.of("Erro", "Erro UsuarioService"));
            }
        }
        return response;
    }

    @PutMapping("/v1/update/user/{id}")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable Long id, @RequestBody Map<String, String> data)
    {
        UsuarioDTO usuarioNew = new UsuarioDTO();
        ResponseEntity<Map<String, String>> response = ResponseEntity.badRequest().body(Map.of("Erro: ", "Desconhecido"));
        try
        {
            System.out.println(usuarioNew.getSenha());
            System.out.println(usuarioNew.getEmail());
            System.out.println(usuarioNew.getNome());
            usuarioService.updateUsuario(usuarioNew, id);
            response = ResponseEntity.ok().body(Map.of("Usuario " + id, "atualizado com sucesso"));
        }
        catch (Exception e)
        {
            System.out.println("Erro UsuarioService");
            e.printStackTrace();
            response = ResponseEntity.badRequest().body(Map.of("Erro", e.getMessage()));
        }
        return response;
    }

    @GetMapping("/v1/getUser/{id}")
    public ResponseEntity<Map<String, String>> getUsuario (@PathVariable Long id)
    {
        ResponseEntity<Map<String, String>> response = ResponseEntity.badRequest().body(Map.of("Erro: ", "Desconhecido"));
        try
        {
            Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
            if(usuario.isEmpty())
                response = ResponseEntity.badRequest().body(Map.of("Erro: ", "Usuário nao encontrado"));
            else {
                System.out.println("Usuario encontrado: " + usuario.get().getNome());
                response = ResponseEntity.ok(Map.of("Usuario", usuario.get().getNome()));
            }
        }
        catch (Exception e)
        {
            System.out.println("Erro UsuarioService");
            response = ResponseEntity.badRequest().body(Map.of("Erro", e.getMessage()));
        }
        return response;
    }

    @GetMapping("/api/v1/buscarUsuarios")
    public List<Usuario> buscarPorNome(@RequestParam String nome)
    {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }

    @DeleteMapping("/v1/delete/usuario/{id}")
    public ResponseEntity<Map<String,String>> deleteUsuario (@PathVariable Long id)
    {
        ResponseEntity<Map<String,String>> response;
        if(usuarioService.deleteUsuario(id))
            response = ResponseEntity.ok().body(Map.of("Usuario de id deletado:", ""+id));
        else
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Erro: ", "Usuario nao encontrado"));
        return response;
    }
}
