# **PROJETO DE SISTEMAS EMBARCADOS**

Felipeh Arcanjo Gabriel 13678762
Daniel Umeda Kuhn Nº USP: 13676541

# **1. Introdução e Motivação**

Em algumas aplicações embarcadas, como iot (internet das coisas), é fundamental o monitoramento dos sensores para capturar as informações do ambiente com baixa latência e usá-las para análise posterior.

A motivação deste projeto é desenvolver uma imagem Linux mínima, baseada em Yocto Project, capaz de rodar em um Raspberry Pi 3B+ e operar um sensor ultrassônico HC-SR04, enviando continuamente as medições de distâncias para um servidor MQTT.

Isso permite usar a Raspberry Pi como um nó em uma rede IoT, por exemplo, para aplicações como: detecção de presença ou proximidade, monitoramento de distâncias em robôs móveis.


# **2. Problemática**

Um sistema embarcado tradicional utilizando distribuições Linux genéricas (como Raspberry Pi OS) tende a carregar serviços desnecessários para aplicações específicas, drivers não utilizados e inicialização mais lenta.

Isso aumenta o consumo de energia, tempo de boot e reduz a confiabilidade em aplicações embarcadas reais.

Além disso, o HC-SR04 exige controle preciso via GPIO, e bibliotecas como RPi.GPIO podem não estar disponíveis em configurações padrão do Yocto.

Assim para resolver esse problema, vamos criar uma distro Linux mínima, contendo apenas o essencial para nossa aplicação, integrar python, a biblioteca rpi.gpio para controle dos pinos do sensor HC-SR04 e path-mqtt, empacotar o aplicativo em uma receipe e criar um serviço systemd para inicializar automaticamente


# **3. Justificativa da Solução Adotada**

O Yocto Project foi escolhido pela sua capacidade de criar distribuições Linux para software embarcado e IoT que sejam independentes da arquitetura subjacente do hardware embarcado. Isso garante um sistema mais leve, boot mais rápido e menor consumo de energia.

A comunicação por MQTT é um padrão de mensagens leves para a Internet das Coisas (IoT) que usa um modelo de publicação/assinatura e funciona através de um broker. Dispositivos publishers enviam mensagens para um "tópico" e dispositivos subscribers "assinam" esse tópico para receber as mensagens. Isso permite uma comunicação eficiente, escalável e com baixo consumo de banda, ideal para dispositivos com poucos recursos. 

Como servidor intermediário (broker) utilizamos o mosquitto que recebe as mensagens de distancias da  Raspberry Pi.


## **4. Ferramentas, Bibliotecas e Dependências**


### 4.1  Ferramentas de Desenvolvimento



* Yocto Project (Kirkstone)
* Bitbake
* Github
* Python 3
* Raspberry Pi 3B+
* Broker MQTT (Mosquitto ou HiveMQ)
* Editor de texto (VSCode e nano)


### 4.2 Bibliotecas utilizadas no Python

* RPi.GPIO — acesso aos pinos GPIO
* paho-mqtt — comunicação MQTT


### 4.3 Dependências Yocto instaladas via receita



* python3-core
* python3-paho-mqtt
* python3-rpi-gpio
* systemd


# **5.Lista de materiais**


<table>
  <tr>
   <td><strong>Quantidade</strong>
   </td>
   <td><strong>Componente</strong>
   </td>
   <td><strong>Função</strong>
   </td>
  </tr>
  <tr>
   <td>1
   </td>
   <td>Raspberry Pi 3B+
   </td>
   <td>Computador embarcado
   </td>
  </tr>
  <tr>
   <td>1
   </td>
   <td>Cartão SD
   </td>
   <td>Armazenamento do Yocto
   </td>
  </tr>
  <tr>
   <td>1
   </td>
   <td>Fonte 5V 
   </td>
   <td>Alimentação
   </td>
  </tr>
  <tr>
   <td>1
   </td>
   <td>Sensor HC-SR04
   </td>
   <td>Medição de distância
   </td>
  </tr>
  <tr>
   <td>4
   </td>
   <td>Jumpers Macho-Fêmea
   </td>
   <td>Conexões de GPIO
   </td>
  </tr>
  <tr>
   <td>1
   </td>
   <td>Rede Wi-Fi ou Ethernet
   </td>
   <td>Comunicação MQTT
   </td>
  </tr>
</table>

# **6. Montagem**

<img src="https://github.com/user-attachments/assets/677c1a4f-d3f8-46ef-bb00-611eec997bc0" width="300" alt="Descrição da Imagem"/>


# **7. Caminhos Percorridos para a Implementação**

Primeiro, criamos a camada Yocto meta project para encapsular o projeto e colocar a receita Python e systemd. 

Depois, escrevemos a receita sensor-mqtt_01.bb, que define como se dará a build do aplicativo:  de onde vem os arquivos e sua empacotação no tempo de build, dependências de runtime, como instalar no rootfs,  integração com systemd, habilitação do serviço.

Mesmo com a receita pronta, ela não é incluída automaticamente na imagem, para isso criamos o arquivo layer.conf que informa o Yocto que a receipe deve ser  parte da imagem final e instalada na rootfs. Essa ação é essencial para garantir que o script e o serviço façam parte do sistema final gravado no cartão SD.

Com todos os componentes devidamente preparados, a imagem personalizada foi gerada usando o comando bitbake core-image-minimal, que construiu todo o ambiente Linux, instalou dependências, aplicou configurações, integrou o script, registrou o serviço e produziu a imagem final pronta para uso.

A imagem gerada foi gravada no cartão SD e o Raspberry Pi 3B+ foi iniciado com o novo sistema. No boot, o serviço sensor.service foi automaticamente ativado pelo systemd, iniciando o script de leitura do HC-SR04, que começou a realizar as medições e publicar os dados no broker MQTT. 
