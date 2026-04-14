package backend.controller;

import backend.model.Usuario;
import backend.repository.UsuarioRepository;
import backend.security.JwtUtil;
import backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listarUsuarios(){
        return usuarioService.listarTodos();
    }

    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario){
        return usuarioService.guardar(usuario);
    }

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Usuario loginData){
        Usuario usuario = usuarioService.autenticar(loginData.getCorreo(), loginData.getPassword());

        if (usuario == null){
            throw new RuntimeException("Correo o contraseña incorrecta");

        }

        String token = jwtUtil.generarToken(usuario.getCorreo());

        Map<String, Object> response = new HashMap<>();
        response.put("usuario", usuario);
        response.put("token", token);

        return response;
    }



}
