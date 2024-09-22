package com.prueba.sofkaestadisticas.aplicattion.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticaCambioClienteResponse {
    
    private String sofkianoId;

    private String nombreSofkiano;

    private List<ClienteInfo> clientesEgreso;
    
    private List<ClienteInfo> clientesIngreso;
    

}


// package com.prueba.sofkaestadisticas.aplicattion.response;

// import java.time.LocalDateTime;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;


// @Data
// @AllArgsConstructor
// @NoArgsConstructor

// public class EstadisticaCambioClienteResponse {
    
//     private String sofkianoId;

//     private String nombreSofkiano;

//     private LocalDateTime fechaIngreso;

//     private LocalDateTime fechaEgreso;

//     private String nombreCliente;
// }
