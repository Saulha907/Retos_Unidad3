"""Validación académica; ejecutar con: pip install jsonschema lxml; python validar_ejemplos.py"""
from pathlib import Path
import json
from jsonschema import Draft202012Validator, FormatChecker
from lxml import etree
base=Path(__file__).resolve().parent
schema=json.loads((base/'esquemas/reserva.schema.json').read_text(encoding='utf-8'))
xsd=etree.XMLSchema(etree.parse(str(base/'esquemas/alianza.xsd')))
validador=Draft202012Validator(schema,format_checker=FormatChecker())
for nombre in ('aeronorte','surjet','caribeair'):
    datos=json.loads((base/'datos-transformados'/f'{nombre}.json').read_text(encoding='utf-8'))
    errores=list(validador.iter_errors(datos))
    xml_ok=xsd.validate(etree.parse(str(base/'datos-transformados'/f'{nombre}.xml')))
    print(f'{nombre}: JSON {"VÁLIDO" if not errores else "INVÁLIDO"} | XML {"VÁLIDO" if xml_ok else "INVÁLIDO"}')
    for error in errores: print('  ', error.message)
    if not xml_ok: print(xsd.error_log)
    if errores or not xml_ok: raise SystemExit(1)
print('Todas las transformaciones válidas.')
