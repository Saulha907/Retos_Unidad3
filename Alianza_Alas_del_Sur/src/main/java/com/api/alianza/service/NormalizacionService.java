package com.api.alianza.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class NormalizacionService {
  private final ObjectMapper mapper;
  private static final Set<String> AEROLINEAS = Set.of("aeronorte", "surjet", "caribeair");
  public NormalizacionService(ObjectMapper mapper) { this.mapper = mapper; }

  public JsonNode cargarMuestra(String aerolinea) throws IOException {
    String clave = normalizarClave(aerolinea);
    try (InputStream stream = getClass().getResourceAsStream("/muestras/originales/" + clave + ".json")) {
      if (stream == null) throw new IllegalArgumentException("Muestra inexistente: " + clave);
      return mapper.readTree(stream);
    }
  }

  private String normalizarClave(String aerolinea) {
    String clave = aerolinea.toLowerCase(Locale.ROOT);
    if (!AEROLINEAS.contains(clave)) throw new IllegalArgumentException("Aerolínea no admitida. Usa aeronorte, surjet o caribeair.");
    return clave;
  }

  public ObjectNode transformar(String aerolinea, JsonNode entrada) {
    String clave = normalizarClave(aerolinea);
    if (entrada == null || !entrada.isObject()) throw new IllegalArgumentException("El cuerpo debe ser un objeto JSON.");
    return switch (clave) {
      case "aeronorte" -> transformarAeroNorte(entrada);
      case "surjet" -> transformarSurJet(entrada);
      default -> transformarCaribeAir(entrada);
    };
  }

  private ObjectNode base(String aerolinea, String localizador, String fecha, String hora, String zona) {
    // UUID versión 5 no está en la JDK; se usa identificador global determinista a partir del origen y localizador.
    String semilla = "ALAS-DEL-SUR:" + aerolinea + ":" + localizador;
    ObjectNode reserva = mapper.createObjectNode();
    reserva.put("idGlobal", UUID.nameUUIDFromBytes(semilla.getBytes(java.nio.charset.StandardCharsets.UTF_8)).toString());
    reserva.put("aerolineaOrigen", aerolinea);
    reserva.put("idOrigen", localizador);
    reserva.put("fechaVuelo", fecha);
    if (hora == null) reserva.putNull("horaLocal"); else reserva.put("horaLocal", hora);
    if (zona == null) reserva.putNull("zonaHoraria"); else reserva.put("zonaHoraria", zona);
    // No inventar vuelos ni números de vuelo que no aparezcan en las fuentes.
    reserva.set("vuelos", mapper.createArrayNode());
    return reserva;
  }

  private void tarifa(ObjectNode reserva, BigDecimal importe, String moneda) {
    ObjectNode tarifa = reserva.putObject("tarifa");
    tarifa.put("importe", importe);
    tarifa.put("moneda", moneda);
  }
  private void pasajero(ObjectNode reserva, String nombre, String apellido, String categoria, Integer edad) {
    ObjectNode p = reserva.putObject("pasajero");
    p.put("nombre", nombre);
    p.put("apellido", apellido);
    p.put("tipo", categoria);
    if (edad == null) p.putNull("edad"); else p.put("edad", edad);
  }
  private void equipaje(ObjectNode reserva, String tipoMedida, BigDecimal pesoKg, Integer piezas, BigDecimal maxPorPieza) {
    ArrayNode items = reserva.putArray("equipajes");
    ObjectNode b = items.addObject();
    b.put("tipoMedidaOrigen", tipoMedida);
    if (pesoKg == null) b.putNull("pesoTotalKg"); else b.put("pesoTotalKg", pesoKg);
    if (piezas == null) b.putNull("piezas"); else b.put("piezas", piezas);
    if (maxPorPieza == null) b.putNull("maxKgPorPieza"); else b.put("maxKgPorPieza", maxPorPieza);
  }

  private ObjectNode transformarAeroNorte(JsonNode o) {
    String id = requerido(o,"pnr");
    String fecha = LocalDate.parse(requerido(o,"fecha"),DateTimeFormatter.ofPattern("dd/MM/uuuu")).toString();
    String[] partes = requerido(o,"pasajero").split("/", -1);
    if (partes.length != 2 || partes[0].isBlank() || partes[1].isBlank()) throw new IllegalArgumentException("pasajero debe seguir APELLIDO/NOMBRE.");
    String menor = requerido(o,"menor");
    if (!menor.equals("S") && !menor.equals("N")) throw new IllegalArgumentException("menor solo acepta S o N.");
    ObjectNode r = base("AeroNorte",id,fecha,null,null);
    pasajero(r,capitalizar(partes[1]),capitalizar(partes[0]),menor.equals("S")?"MENOR":"ADULTO",null);
    tarifa(r,new BigDecimal(requerido(o,"tarifa")),"MXN");
    equipaje(r,"KG",new BigDecimal(requerido(o,"equipaje")),null,null);
    return r;
  }

  private ObjectNode transformarSurJet(JsonNode o) {
    String id=requerido(o,"booking");
    OffsetDateTime fecha=OffsetDateTime.parse(requerido(o,"flightDate"));
    ObjectNode r=base("SurJet",id,fecha.toLocalDate().toString(),fecha.toLocalTime().toString(),fecha.getOffset().getId());
    int edad=o.path("age").asInt(-1);
    if(edad < 0) throw new IllegalArgumentException("age debe ser entero no negativo.");
    pasajero(r,requerido(o,"firstName"),requerido(o,"lastName"),edad<2?"INFANTE":edad<12?"MENOR":"ADULTO",edad);
    JsonNode f=o.path("fare");
    tarifa(r,new BigDecimal(requerido(f,"amount")),requerido(f,"currency"));
    // 1 libra internacional = 0.45359237 kilogramos. El decimal es un peso real, no una pieza.
    BigDecimal kg=new BigDecimal(requerido(o,"bagsLb")).multiply(new BigDecimal("0.45359237"));
    equipaje(r,"LB",kg,null,null);
    return r;
  }

  private ObjectNode transformarCaribeAir(JsonNode o) {
    String id=requerido(o,"id");
    String fecha=LocalDate.parse(requerido(o,"fecha"),DateTimeFormatter.ofPattern("MM/dd/uuuu")).toString();
    String nombreCompleto=requerido(o,"nombre").trim();
    int pos=nombreCompleto.indexOf(' ');
    if(pos<1 || pos==nombreCompleto.length()-1) throw new IllegalArgumentException("nombre necesita nombre y apellido.");
    String tipo=switch(requerido(o,"tipoPasajero")){case "ADT"->"ADULTO";case "CHD"->"MENOR";case "INF"->"INFANTE";default->throw new IllegalArgumentException("Código de pasajero inválido.");};
    ObjectNode r=base("CaribeAir",id,fecha,null,null);
    pasajero(r,nombreCompleto.substring(0,pos),nombreCompleto.substring(pos+1),tipo,null);
    tarifa(r,new BigDecimal(requerido(o,"precio").replace(",","")),"MXN");
    String dato=requerido(o,"maleta").trim().toLowerCase(Locale.ROOT);
    java.util.regex.Matcher m=java.util.regex.Pattern.compile("^(\\d+) piezas?$").matcher(dato);
    if(!m.matches()) throw new IllegalArgumentException("maleta debe ser 'N pieza(s)'.");
    equipaje(r,"PIEZAS",null,Integer.parseInt(m.group(1)),new BigDecimal("23"));
    return r;
  }

  private String requerido(JsonNode o,String campo) {
    if(o==null || !o.hasNonNull(campo) || o.path(campo).asText().isBlank())throw new IllegalArgumentException("Falta el dato requerido: "+campo);
    return o.get(campo).asText();
  }
  private String capitalizar(String s) {
    String minus=s.toLowerCase(new Locale("es","MX"));
    return minus.substring(0,1).toUpperCase(new Locale("es","MX"))+minus.substring(1);
  }
}
