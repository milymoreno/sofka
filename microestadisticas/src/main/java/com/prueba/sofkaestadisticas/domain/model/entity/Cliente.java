package com.prueba.sofkaestadisticas.domain.model.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@DynamoDBDocument
public class Cliente {
    
    @DynamoDBAttribute(attributeName = "clienteId")
    private String clienteId;

    @DynamoDBAttribute(attributeName = "nombreCliente")
    private String nombreCliente;

    @DynamoDBAttribute(attributeName = "fecha")
    private String fecha; // Debe ser un String formateado como ISO 8601  

} 

