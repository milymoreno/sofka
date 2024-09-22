package com.prueba.sofkaestadisticas.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.prueba.sofkaestadisticas.domain.model.entity.EstadisticaCambioCliente;
import com.prueba.sofkaestadisticas.domain.model.entity.Cliente;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Arrays;

@Component
public class DynamoDBTableInitializer {

    private final AmazonDynamoDB amazonDynamoDB;
    private final DynamoDB dynamoDB;

    @Autowired
    public DynamoDBTableInitializer(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.dynamoDB = new DynamoDB(amazonDynamoDB);
    }

    @PostConstruct
    public void init() {
        createEstadisticaCambioClienteTable();
        insertTestData();
    }

    private void createEstadisticaCambioClienteTable() {
    String tableName = "EstadisticaCambioCliente";
        try {
            CreateTableRequest request = new CreateTableRequest()
                    .withTableName(tableName)
                    .withKeySchema(new KeySchemaElement("sofkianoId", KeyType.HASH)) 
                    .withAttributeDefinitions(
                            new AttributeDefinition("sofkianoId", ScalarAttributeType.S)) 
                    .withBillingMode(BillingMode.PAY_PER_REQUEST); // Cambiar a PAY_PER_REQUEST si no usas throughput

            // Crear la tabla
            amazonDynamoDB.createTable(request);
            System.out.println("Tabla " + tableName + " creada correctamente.");
        } catch (ResourceInUseException e) {
            System.out.println("La tabla " + tableName + " ya existe.");
        }
    }

    private void insertTestData() {
        Table table = dynamoDB.getTable("EstadisticaCambioCliente");
    
        try {
            // Crear datos de prueba con clientes de ingreso y egreso
            EstadisticaCambioCliente estadistica1 = new EstadisticaCambioCliente("1", "Sofkiano 1");
            estadistica1.setClientesIngreso(Arrays.asList(
                new Cliente("C1", "Cliente 1", "2023-09-01T10:00:00"),
                new Cliente("C2", "Cliente 2", "2023-11-02T11:00:00")
            ));
            estadistica1.setClientesEgreso(Arrays.asList(
                new Cliente("C1", "Cliente 1", "2023-10-03T12:00:00")
            ));
    
            EstadisticaCambioCliente estadistica2 = new EstadisticaCambioCliente("2", "Sofkiano 2");
            estadistica2.setClientesIngreso(Arrays.asList(
                new Cliente("C4", "Cliente 4", "2023-09-05T09:00:00"),
                new Cliente("C1", "Cliente 1", "2023-11-06T08:00:00")
            ));
           
            estadistica2.setClientesEgreso(Arrays.asList(
                new Cliente("C4", "Cliente 4", "2023-10-06T08:00:00")
            ));
           
    
            // Insertar datos en DynamoDB
            table.putItem(Item.fromJSON(convertToJson(estadistica1)));
            System.out.println("Registro insertado: " + convertToJson(estadistica1));
            
            table.putItem(Item.fromJSON(convertToJson(estadistica2)));
            System.out.println("Registro insertado: " + convertToJson(estadistica2));
    
            System.out.println("Registros insertados exitosamente.");
        } catch (Exception e) {
            System.err.println("No se pudo insertar el registro: " + e.getMessage());
        }
    }
    
    
    
    private String convertToJson(EstadisticaCambioCliente estadistica) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(estadistica);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir a JSON: " + e.getMessage());
        }
    }
    
    

         // private void insertTestData() {
    //     Table table = dynamoDB.getTable("EstadisticaCambioCliente");

    //     try {
    //         // Crear objetos de prueba
    //         EstadisticaCambioCliente estadistica1 = new EstadisticaCambioCliente("1", "Sofkiano 1");
    //         estadistica1.setClientesIngreso(Arrays.asList(
    //                 new Cliente("C1", "Cliente 1", "2023-09-01T10:00:00"),
    //                 new Cliente("C2", "Cliente 2", "2023-09-02T11:00:00")
    //         ));
    //         estadistica1.setClientesEgreso(Arrays.asList(
    //                 new Cliente("C3", "Cliente 3", "2023-09-03T12:00:00")
    //         ));

    //         EstadisticaCambioCliente estadistica2 = new EstadisticaCambioCliente("2", "Sofkiano 2");
    //         estadistica2.setClientesIngreso(Arrays.asList(
    //                 new Cliente("C4", "Cliente 4", "2023-09-05T09:00:00")
    //         ));
    //         estadistica2.setClientesEgreso(Arrays.asList(
    //                 new Cliente("C5", "Cliente 5", "2023-09-06T08:00:00")
    //         ));

    //         // Insertar datos
    //         table.putItem(Item.fromJSON(estadistica1.toJSON()));
    //         System.out.println("estadistica 1: "+ estadistica1.toJSON());
    //         table.putItem(Item.fromJSON(estadistica2.toJSON()));
    //         System.out.println("estadistica 2: "+ estadistica2.toJSON());

    //         System.out.println("Registros insertados exitosamente.");
    //     } catch (Exception e) {
    //         System.err.println("No se pudo insertar el registro: " + e.getMessage());
    //     }
    // }
}
