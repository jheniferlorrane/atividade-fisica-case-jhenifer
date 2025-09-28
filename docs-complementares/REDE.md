# 🌐 Configuração de Rede e Acesso Remoto

## 🎯 Visão Geral

Por padrão, a aplicação roda em `localhost:8080`, mas você pode configurá-la para aceitar conexões de outros dispositivos na rede.

---

## 🔧 Configuração de IP e Acesso Externo

### **Descobrir IP da Máquina**

#### **Windows:**
```cmd
ipconfig
# Procure por "Endereço IPv4" na interface ativa
# Exemplo: 192.168.1.100
```

#### **macOS/Linux:**
```bash
# Opção 1: Comando específico
hostname -I | awk '{print $1}'

# Opção 2: Interface específica
ip addr show | grep 'inet ' | grep -v '127.0.0.1'

# Opção 3: macOS
ifconfig | grep 'inet ' | grep -v '127.0.0.1'
```

### **Configurar Aplicação para Aceitar Conexões Externas**

#### **Via Environment Variables (Recomendado):**

Crie arquivo `.env` na raiz do projeto:
```bash
# .env
HOST_IP=192.168.1.100    # Substitua pelo seu IP real
APP_PORT=8080
DB_PORT=3307
```

Atualize `docker-compose.yml`:
```yaml
app:
  environment:
    - SERVER_ADDRESS=0.0.0.0  # Aceita conexões de qualquer IP
    - HOST_IP=${HOST_IP}
  ports:
    - "${HOST_IP}:${APP_PORT}:8080"  # Bind específico no IP
```

#### **Via Application Properties:**
```properties
# application.properties
server.address=0.0.0.0
server.port=8080
```

---

## 🛡️ Configuração de Firewall

### **Windows (Windows Defender)**

#### **Via Interface Gráfica:**
1. **Painel de Controle** → **Sistema e Segurança** → **Firewall do Windows Defender**
2. **"Configurações avançadas"** → **"Regras de Entrada"**
3. **"Nova Regra"** → **"Porta"** → **TCP** → **8080**
4. **"Permitir conexão"** → **Nomear** "Spring Boot API"

#### **Via PowerShell (como Administrador):**
```powershell
# Criar regra para porta 8080
New-NetFirewallRule -DisplayName "API Atividades Fisicas" -Direction Inbound -Port 8080 -Protocol TCP -Action Allow

# Verificar regra criada
Get-NetFirewallRule -DisplayName "API Atividades Fisicas"
```

### **Linux (UFW - Ubuntu/Debian)**

```bash
# Permitir porta 8080
sudo ufw allow 8080

# Verificar status
sudo ufw status

# Regra mais específica (opcional)
sudo ufw allow from 192.168.1.0/24 to any port 8080
```

### **Linux (iptables)**

```bash
# Adicionar regra para aceitar conexões TCP na porta 8080
sudo iptables -A INPUT -p tcp --dport 8080 -j ACCEPT

# Salvar regras (Ubuntu/Debian)
sudo iptables-save > /etc/iptables/rules.v4

# Verificar regras
sudo iptables -L | grep 8080
```

### **macOS**

```bash
# macOS normalmente não bloqueia por padrão
# Se necessário, configure via System Preferences > Security & Privacy > Firewall
```

---

## 📱 Acesso via Dispositivos Móveis

### **Configuração**

1. **Conecte o dispositivo na mesma rede Wi-Fi**
2. **Use o IP da máquina host:**
   - Exemplo: `http://192.168.1.100:8080/atividades`
3. **Teste no navegador do dispositivo**

### **Apps Recomendados para Testes**

- **iOS/Android:**
  - **Insomnia** - App oficial para testes de API
  - **Postman** - Alternative popular
  - **HTTP Request** (Android) - Cliente HTTP simples
  - **Navegador** do dispositivo para GET requests

### **Exemplo de Teste Mobile**

```bash
# No dispositivo móvel, acessar:
http://192.168.1.100:8080/atividades

# Ou usar app como Insomnia:
POST http://192.168.1.100:8080/atividades
Content-Type: application/json

{
  "funcional": "MOBILE001",
  "dataHora": "2025-09-28T15:00:00",
  "codigoAtividade": "TesteMobile",
  "descricaoAtividade": "Teste via dispositivo móvel"
}
```

---

## 🏢 Ambientes Corporativos

### **Considerações Especiais**

#### **Firewall Corporativo:**
- ✅ Solicitar liberação da **porta 8080** ao TI
- ✅ Verificar se há **proxy corporativo**
- ✅ Confirmar **políticas de rede**

#### **VPN:**
- ✅ Conectar à **VPN corporativa** se necessário
- ✅ Verificar se VPN permite **acesso local**

#### **Rede Segmentada:**
- ✅ Confirmar se dispositivos estão na **mesma VLAN**
- ✅ Verificar **regras de roteamento** entre redes

### **Troubleshooting Corporativo**

```bash
# Testar conectividade básica
ping 192.168.1.100

# Testar porta específica
telnet 192.168.1.100 8080
# ou
nc -zv 192.168.1.100 8080

# Verificar rota de rede
traceroute 192.168.1.100    # Linux/macOS
tracert 192.168.1.100       # Windows
```

---

## 🔍 Teste de Conectividade

### **Verificar se API está Acessível Externamente**

```bash
# Da própria máquina (deve funcionar)
curl http://localhost:8080/atividades
curl http://192.168.1.100:8080/atividades

# De outro dispositivo na rede
curl http://192.168.1.100:8080/atividades

# Teste com verbose para debug
curl -v http://192.168.1.100:8080/atividades
```

### **Diagnóstico de Problemas**

#### **API não responde via IP externo:**

```bash
# 1. Verificar se aplicação está ouvindo em todas as interfaces
netstat -tlnp | grep 8080
# Deve mostrar: 0.0.0.0:8080 (não 127.0.0.1:8080)

# 2. Testar firewall local
# Windows:
Test-NetConnection -ComputerName 192.168.1.100 -Port 8080

# Linux:
sudo ufw status | grep 8080
```

#### **Timeout ou Connection Refused:**

```bash
# Verificar se containers estão rodando
docker-compose ps

# Verificar logs da aplicação
docker-compose logs app | grep "Tomcat started"

# Verificar configuração de rede Docker
docker network inspect $(docker-compose ps -q app | xargs docker inspect --format='{{range $net,$conf := .NetworkSettings.Networks}}{{$net}}{{end}}')
```

### **Script de Teste Automático**

```bash
#!/bin/bash
echo "🔍 Testando conectividade da API..."

# Descobrir IP local
LOCAL_IP=$(hostname -I | awk '{print $1}')
echo "📍 IP Local: $LOCAL_IP"

# Testar localhost
echo "🏠 Testando localhost..."
if curl -s http://localhost:8080/atividades > /dev/null; then
    echo "✅ Localhost OK"
else
    echo "❌ Localhost FALHOU"
fi

# Testar IP externo
echo "🌐 Testando IP externo..."
if curl -s http://$LOCAL_IP:8080/atividades > /dev/null; then
    echo "✅ IP externo OK"
    echo "🎯 API acessível em: http://$LOCAL_IP:8080"
else
    echo "❌ IP externo FALHOU"
    echo "🔧 Verificar firewall e configuração de rede"
fi

# Testar porta
echo "🔌 Testando porta 8080..."
if nc -z $LOCAL_IP 8080; then
    echo "✅ Porta 8080 aberta"
else
    echo "❌ Porta 8080 fechada"
fi
```

---

## ⚙️ Configurações Avançadas

### **Múltiplas Interfaces de Rede**

Se sua máquina tem múltiplas interfaces (Wi-Fi + Ethernet):

```bash
# Listar todas as interfaces
ip addr show    # Linux
ifconfig        # macOS

# Bind em interface específica
# docker-compose.yml
ports:
  - "192.168.1.100:8080:8080"  # Wi-Fi
  - "192.168.0.50:8080:8080"   # Ethernet
```

### **DNS Local (Opcional)**

Para usar nomes em vez de IPs:

```bash
# /etc/hosts (Linux/macOS) ou C:\Windows\System32\drivers\etc\hosts (Windows)
192.168.1.100    api-atividades.local

# Acessar via:
http://api-atividades.local:8080/atividades
```

### **Proxy Reverso (Produção)**

Para produção, considere usar Nginx:

```nginx
# nginx.conf
server {
    listen 80;
    server_name api-atividades.empresa.com;
    
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

---

## 🎯 Checklist de Configuração de Rede

### ✅ **Configuração Básica**
- [ ] IP da máquina identificado
- [ ] Aplicação configurada para aceitar conexões externas
- [ ] Firewall configurado para permitir porta 8080
- [ ] Docker binding correto para o IP

### ✅ **Testes de Conectividade**
- [ ] API responde via localhost
- [ ] API responde via IP externo
- [ ] Porta 8080 acessível externamente
- [ ] Teste de outro dispositivo na rede

### ✅ **Dispositivos Móveis**
- [ ] Mesmo Wi-Fi configurado
- [ ] App de teste instalado
- [ ] Requisições funcionando
- [ ] Filtros e CRUD testados

### ✅ **Ambiente Corporativo (se aplicável)**
- [ ] Liberação de porta solicitada ao TI
- [ ] VPN configurada se necessário
- [ ] Proxy corporativo considerado
- [ ] Políticas de segurança respeitadas

---

**🌐 Configuração de rede concluída! Sua API está acessível remotamente!**

**Para testar rapidamente:**
```bash
# Descobrir seu IP
hostname -I | awk '{print $1}'

# Testar de outro dispositivo
curl http://SEU_IP:8080/atividades
```