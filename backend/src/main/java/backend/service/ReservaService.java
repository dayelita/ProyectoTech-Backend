package backend.service;
import backend.model.Reserva;
import backend.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service

public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;


    public Reserva crearReserva(Reserva nueva) throws Exception{
        if (nueva == null || nueva.getFechaHoraInicio() == null || nueva.getFechaHoraFin() == null){
            throw new Exception("Error: los datos de la reserva o las fechas estan incompletas");
        }
        long horas = Duration.between(nueva.getFechaHoraInicio(),nueva.getFechaHoraFin()).toHours();
        if (horas < 3){
            throw new Exception("La reserva debe tener una duracion minima de 3 horas.");
        }

        List<Reserva> choques = reservaRepository.findOverlappingReservations(
                nueva.getFechaHoraInicio(),
                nueva.getFechaHoraFin()
        );
        if (!choques.isEmpty()){
            throw new Exception("El horario seleccionado ya esta ocupado");
        }
        nueva.setEstado("PENDIENTE");
        return reservaRepository.save(nueva);
    }
    public List<Reserva> obtenerTodas(){
        return reservaRepository.findAll();
    }
    public Reserva cambiarEstado(Long id, String nuevoEstado) throws Exception{
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new Exception("Reserva no encontrada"));

        if (!nuevoEstado.equals("APROBADO") && !nuevoEstado.equals("RECHAZADO")){
            throw new Exception("Estado no valido");
        }
        reserva.setEstado(nuevoEstado);
        return reservaRepository.save(reserva);
    }
}
