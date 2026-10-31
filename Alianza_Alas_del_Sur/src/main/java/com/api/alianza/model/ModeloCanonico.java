package com.api.alianza.model;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

// Modelo semántico de referencia. El adaptador JSON se encuentra en NormalizacionService.
public final class ModeloCanonico {
  private ModeloCanonico() {}
  public enum TipoPasajero { ADULTO, MENOR, INFANTE }
  public record Reserva(UUID idGlobal,String aerolineaOrigen,String idOrigen,LocalDate fechaVuelo,
                        String horaLocal,String zonaHoraria,Pasajero pasajero,Tarifa tarifa,
                        List<Vuelo> vuelos,List<Equipaje> equipajes) {}
  public record Pasajero(String nombre,String apellido,TipoPasajero tipo,Integer edad) {}
  public record Vuelo(String numeroVuelo,String origen,String destino) {}
  public record Tarifa(BigDecimal importe,String moneda) {}
  public record Equipaje(String tipoMedidaOrigen,BigDecimal pesoTotalKg,Integer piezas,BigDecimal maxKgPorPieza) {}
}
