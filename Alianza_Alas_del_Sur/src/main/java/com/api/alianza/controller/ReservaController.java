package com.api.alianza.controller;
import com.api.alianza.service.NormalizacionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaController {
  private final NormalizacionService servicio;
  public ReservaController(NormalizacionService servicio){this.servicio=servicio;}

  @GetMapping("/muestras/{aerolinea}")
  public ObjectNode muestra(@PathVariable String aerolinea) throws IOException {
    return servicio.transformar(aerolinea,servicio.cargarMuestra(aerolinea));
  }
  @PostMapping("/normalizar/{aerolinea}")
  public ObjectNode normalizar(@PathVariable String aerolinea,@RequestBody JsonNode datos){
    return servicio.transformar(aerolinea,datos);
  }
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String,String>> error(IllegalArgumentException e){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error",e.getMessage()));
  }
}
