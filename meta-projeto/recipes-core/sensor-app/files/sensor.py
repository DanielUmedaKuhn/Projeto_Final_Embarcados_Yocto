#!/usr/bin/env python3
import time
# Descomente quando estiver no Pi:
# import RPi.GPIO as GPIO
# import paho.mqtt.client as mqtt

print("Iniciando script do sensor - v0.1")

# Exemplo de loop principal
while True:
    # Seu código para ler o sensor ultrassônico e enviar via MQTT vai aqui
    print("Lendo sensor... (simulado)")
    # Exemplo: client.publish("rpi/distancia", "10cm")
    time.sleep(10)
