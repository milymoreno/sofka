package com.prueba.sofkaestadisticas.aplicattion.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteInfo {
    private String clienteId;

    private String nombreCliente;

    private LocalDateTime fecha;
    
    
}
