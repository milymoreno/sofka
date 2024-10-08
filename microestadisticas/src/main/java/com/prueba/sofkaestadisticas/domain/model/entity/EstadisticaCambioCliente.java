package com.prueba.sofkaestadisticas.domain.model.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@DynamoDBTable(tableName = "EstadisticaCambioCliente")
public class EstadisticaCambioCliente {

    @DynamoDBHashKey(attributeName = "sofkianoId")
    private String sofkianoId;

    @DynamoDBAttribute(attributeName = "nombre")
    private String nombre;

    @DynamoDBAttribute(attributeName = "clientesIngreso")
    private List<Cliente> clientesIngreso = new ArrayList<>();

    @DynamoDBAttribute(attributeName = "clientesEgreso")
    private List<Cliente> clientesEgreso = new ArrayList<>();

    public EstadisticaCambioCliente(String sofkianoId, String nombre) {
        this.sofkianoId = sofkianoId;
        this.nombre = nombre;
    }

    public EstadisticaCambioCliente(String sofkianoId, String nombre, List<Cliente> clientesIngreso, List<Cliente> clientesEgreso) {
        this.sofkianoId = sofkianoId;
        this.nombre = nombre;
        this.clientesIngreso = clientesIngreso != null ? clientesIngreso : new ArrayList<>();
        this.clientesEgreso = clientesEgreso != null ? clientesEgreso : new ArrayList<>();
    }

    public String toJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir a JSON", e);
        }
    }
}
