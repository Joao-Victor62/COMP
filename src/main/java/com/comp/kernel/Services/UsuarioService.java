package com.comp.kernel.Services;

import com.comp.kernel.DTOs.UsuarioDTO;
import com.comp.kernel.Models.Usuario;
import com.comp.kernel.Repositories.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsuarioService{

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder)
    {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario criarUsuario(UsuarioDTO dto)
    {
        if (dto.getEmail().isEmpty() || dto.getSenha().isEmpty() || dto.getNome().isEmpty())
            throw new IllegalArgumentException("Os campos devem ser preenchidos");
        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setNome(dto.getNome());
        String senhaEncriptada = passwordEncoder.encode(dto.getSenha());
        if (checkSenha(dto.getSenha(), senhaEncriptada))
            usuario.setSenha(senhaEncriptada);
        else
            throw new IllegalArgumentException("Erro ao encriptar a senha");
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> getUsuarioById(Long id)
    {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> getAllUsuarios()
    {
        return usuarioRepository.findAll();
    }



    public Usuario updateUsuario (UsuarioDTO dto, Long id)
    {
        Usuario usuarioAux;
        Optional<Usuario> usuario = getUsuarioById(id);
        if (usuario.isEmpty())
            throw new IllegalArgumentException("Usuário não encontrado para o id " + id + ". Falha na atualização.");
        else
        {
            usuarioAux = usuario.get();
            String senhaEncriptada = passwordEncoder.encode(dto.getSenha());
            if (!dto.getNome().isEmpty() && !dto.getNome().equals(usuarioAux.getNome())) //Se não estiver vazio ou igual, altera o registro
                usuarioAux.setNome(dto.getNome());
            if (!dto.getSenha().isEmpty() && !dto.getSenha().equals(usuarioAux.getSenha()))
                usuarioAux.setSenha(passwordEncoder.encode(dto.getSenha()));
            if (!dto.getEmail().isEmpty() && !dto.getEmail().equals(usuarioAux.getEmail()))
                usuarioAux.setEmail(dto.getEmail());

            return usuarioRepository.save(usuarioAux);
        }
    }

    public boolean deleteUsuario (Long id)
    {
        Optional<Usuario> usuario = getUsuarioById(id);
        boolean delete = false;
        Usuario usuarioAux;
        if (usuario.isEmpty())
            throw new IllegalArgumentException("Usuário não encontrado para o id " + id + ". Falha na deleção.");
        else
        {
            usuarioAux = usuario.get();
            usuarioRepository.deleteById(usuarioAux.getIdUsuario());
            delete = true;
        }
        return delete;
    }

    public boolean checkSenha(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
