package com.api.alianza.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class NormalizacionServiceTest {
  private final NormalizacionService service = new NormalizacionService(new ObjectMapper());
  @Test void muestrasNormalizadas() throws Exception {
    for(String aero: new String[]{"aeronorte","surjet","caribeair"}) {
      var resultado = service.transformar(aero,service.cargarMuestra(aero));
      assertEquals("2026-04-03",resultado.get("fechaVuelo").asText());
      assertNotNull(resultado.get("idGlobal"));
    }
  }
  @Test void sePreservaMoneda() throws Exception {
    var resultado = service.transformar("surjet",service.cargarMuestra("surjet"));
    assertEquals("USD",resultado.path("tarifa").path("moneda").asText());
  }
  @Test void desconocidaEsError() {
    assertThrows(IllegalArgumentException.class,()->service.transformar("inexistente",new ObjectMapper().createObjectNode()));
  }
}
