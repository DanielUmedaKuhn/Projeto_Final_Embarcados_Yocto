#!/usr/bin/env python3
import time
import RPi.GPIO as GPIO
import paho.mqtt.client as mqtt

TRIG = 23
ECHO = 24
BROKER = "localhost"    # mosquitto e script rodam na rasp
TOPIC = "sensor/distancia"

GPIO.setmode(GPIO.BCM)
GPIO.setup(TRIG, GPIO.OUT)
GPIO.setup(ECHO, GPIO.IN)

def read_distance(): 
    GPIO.output(TRIG, False)
    time.sleep(0.0002)

    GPIO.output(TRIG, True)
    time.sleep(0.00001)
    GPIO.output(TRIG, False)

    start = time.time()
    timeout = start + 0.04

    while GPIO.input(ECHO) == 0 and time.time() < timeout:
        start = time.time()

    while GPIO.input(ECHO) == 1 and time.time() < timeout:
        stop = time.time()

    if time.time() >= timeout:
        return None

    interval = stop - start
    return (interval * 34300) / 2

client = mqtt.Client() # cria cliente MQTT

def mqtt_connect():
    """Tenta conectar."""
    while True:
        try:
            client.connect(BROKER, 1883, 60) # conecta ao broker
            client.loop_start()
            print("MQTT conectado.")
            break
        except:
            print("MQTT indisponível. Tentando novamente...")
            time.sleep(2)

mqtt_connect()

try:
    while True: # ciclo de leitura e publicação
        d = read_distance()
        if d is not None:
            print(f"Distância: {d:.2f} cm")
            try:
                client.publish(TOPIC, f"{d:.2f}") # envia a distancia para o tópico MQTT
            except:
                print("Erro ao publicar MQTT. Reconectando...")
                mqtt_connect()
        else:
            print("Timeout de leitura.")

        time.sleep(0.2)

except KeyboardInterrupt:
    GPIO.cleanup()
    client.loop_stop()
    client.disconnect()
