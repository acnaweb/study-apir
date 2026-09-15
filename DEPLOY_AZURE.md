# Study API — Azure CLI

Este documento reúne os principais comandos utilizados para configurar, validar e diagnosticar a infraestrutura da **Study API** no Microsoft Azure.

## Sumário

1. [Pré-requisitos](#1-pré-requisitos)
2. [Conta, Subscription e Tenant](#2-conta-subscription-e-tenant)
3. [Service Principal para CI/CD](#3-service-principal-para-cicd)
4. [Azure SQL Server](#4-azure-sql-server)
5. [Azure SQL Database](#5-azure-sql-database)
6. [Teste de conectividade](#6-teste-de-conectividade)
7. [Azure Container Apps](#7-azure-container-apps)
8. [Diagnóstico e Logs](#8-diagnóstico-e-logs)
9. [Fluxo recomendado](#9-fluxo-recomendado)
10. [Resumo da arquitetura](#10-resumo-da-arquitetura)
11. [Pontos de atenção](#11-pontos-de-atenção)
12. [Checklist](#12-checklist)

---

# 1. Pré-requisitos

Antes de executar os comandos, certifique-se de possuir:

- Azure CLI instalado
- Login realizado no Azure
- Permissões suficientes na Subscription/Resource Group
- Acesso ao Azure SQL
- PowerShell, caso queira executar os testes de conectividade local

## Login

```bash
az login
```

## Verificar conta atual

```bash
az account show
```

---

# 2. Conta, Subscription e Tenant

## Consultar informações da Subscription

```bash
az account show \
  --query "{subscriptionId:id, tenantId:tenantId, subscriptionName:name}" \
  -o json
```

### Informações retornadas

| Campo | Descrição |
|---|---|
| `subscriptionId` | ID da Subscription Azure |
| `tenantId` | ID do Microsoft Entra ID |
| `subscriptionName` | Nome da Subscription |

Exemplo de retorno:

```json
{
  "subscriptionId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
  "tenantId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
  "subscriptionName": "Minha Subscription"
}
```

---

# 3. Service Principal para CI/CD

## Criar Service Principal

```bash
az ad sp create-for-rbac \
  --name "github-study-api-deploy" \
  --role contributor \
  --scopes "/subscriptions/1be73773-c052-4393-86f6-5b763efdc08e/resourceGroups/rs-study-apir" \
  --json-auth
```

### Objetivo

Criar uma identidade que poderá ser utilizada pelo pipeline de CI/CD, por exemplo:

```text
GitHub Actions
      |
      v
Service Principal
      |
      v
Resource Group
      |
      v
Azure Resources
```

### Escopo

O Service Principal recebe a role `Contributor` somente no Resource Group:

```text
rs-study-apir
```

Isso é preferível a conceder acesso à Subscription inteira quando a aplicação precisa acessar apenas os recursos desse projeto.

### Segurança

O comando retorna informações sensíveis.

**Não faça commit das credenciais no Git.**

Armazene-as em:

- GitHub Secrets
- Azure Key Vault
- Outro mecanismo seguro de gerenciamento de secrets

---

# 4. Azure SQL Server

## Informações utilizadas

```text
Resource Group:
rs-study-apir

SQL Server:
acnaweb-study-apir-prd
```

---

## Consultar SQL Server

```bash
az sql server show \
  --resource-group rs-study-apir \
  --name acnaweb-study-apir-prd \
  --query "{publicNetworkAccess:publicNetworkAccess,fullyQualifiedDomainName:fullyQualifiedDomainName}" \
  -o table
```

### Objetivo

Verificar:

- acesso público;
- FQDN do SQL Server.

Exemplo:

```text
PublicNetworkAccess    FullyQualifiedDomainName
--------------------   --------------------------------------------
Enabled                acnaweb-study-apir-prd.database.windows.net
```

---

## Criar regra de Firewall

```bash
az sql server firewall-rule create \
  --resource-group rs-study-apir \
  --server acnaweb-study-apir-prd \
  --name AllowAllIPs \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 255.255.255.255
```

### ⚠️ Atenção

Essa configuração permite acesso de qualquer endereço IPv4:

```text
0.0.0.0 → 255.255.255.255
```

**Não é recomendado para produção.**

Para ambientes produtivos, prefira:

- IPs específicos;
- Private Endpoint;
- Virtual Network;
- regras de firewall restritivas;
- Managed Identity quando aplicável.

---

## Liberar somente um IP

Exemplo:

```bash
az sql server firewall-rule create \
  --resource-group rs-study-apir \
  --server acnaweb-study-apir-prd \
  --name AllowMyIP \
  --start-ip-address SEU_IP \
  --end-ip-address SEU_IP
```

---

# 5. Azure SQL Database

## Criar Database

```bash
az sql db create \
  --resource-group rs-study-apir \
  --server "acnaweb-study-apir-prd" \
  --name "api" \
  --service-objective "Basic"
```

### Informações

```text
Server:
acnaweb-study-apir-prd

Database:
api

SKU:
Basic
```

Estrutura:

```text
Resource Group
└── rs-study-apir
    └── SQL Server
        └── acnaweb-study-apir-prd
            └── Database
                └── api
```

---

## Verificar Database

```bash
az sql db show \
  --resource-group rs-study-apir \
  --server "acnaweb-study-apir-prd" \
  --name "api" \
  --query "{name:name,status:status,sku:currentSku.name}" \
  -o table
```

Exemplo:

```text
Name    Status    Sku
------  --------  ------
api     Online    Basic
```

---

## Obter FQDN do SQL Server

```bash
az sql server show \
  --resource-group rs-study-apir \
  --name "acnaweb-study-apir-prd" \
  --query "fullyQualifiedDomainName" \
  -o tsv
```

Exemplo:

```text
acnaweb-study-apir-prd.database.windows.net
```

O parâmetro:

```text
-o tsv
```

faz com que o Azure CLI retorne somente o valor.

Isso é útil para scripts e pipelines.

---

# 6. Teste de conectividade

O teste abaixo deve ser executado **na máquina local** utilizando PowerShell.

```powershell
Test-NetConnection acnaweb-study-apir-prd.database.windows.net -Port 1433
```

## Resultado esperado

Procure por:

```text
TcpTestSucceeded : True
```

Se retornar:

```text
TcpTestSucceeded : False
```

verifique:

1. Firewall do Azure SQL
2. Firewall da máquina local
3. DNS
4. VPN
5. Rede corporativa
6. Acesso público do SQL Server
7. Disponibilidade do SQL Server

### Importante

Esse teste valida apenas conectividade TCP.

```text
TcpTestSucceeded : True
```

não significa que a autenticação no banco esteja funcionando.

---

# 7. Azure Container Apps

## Informações utilizadas

```text
Resource Group:
rs-study-apir

Container App:
study-apir-prd

Target Port:
8080
```

Arquitetura:

```text
Internet
    |
    v
Azure Container Apps
    |
    v
study-apir-prd
    |
    v
Container
    |
    v
Port 8080
```

---

# 7.1 Habilitar Ingress externo

```bash
az containerapp ingress enable \
  --name study-apir-prd \
  --resource-group rs-study-apir \
  --type external \
  --target-port 8080 \
  --transport auto
```

### Parâmetros

| Parâmetro | Descrição |
|---|---|
| `--name` | Nome do Container App |
| `--resource-group` | Resource Group |
| `--type external` | Permite acesso externo |
| `--target-port 8080` | Porta utilizada pela aplicação |
| `--transport auto` | Define automaticamente o transporte |

---

# 7.2 Obter URL pública

```bash
az containerapp show \
  --name study-apir-prd \
  --resource-group rs-study-apir \
  --query "properties.configuration.ingress.fqdn" \
  --output tsv
```

Exemplo:

```text
study-apir-prd.xxxxxx.azurecontainerapps.io
```

A aplicação poderá ser acessada através de:

```text
https://study-apir-prd.xxxxxx.azurecontainerapps.io
```

---

# 7.3 Verificar status

```bash
az containerapp show \
  --name study-apir-prd \
  --resource-group rs-study-apir \
  --query "{name:name, provisioningState:properties.provisioningState, runningStatus:properties.runningStatus}" \
  --output table
```

### Informações retornadas

| Campo | Descrição |
|---|---|
| `name` | Nome do Container App |
| `provisioningState` | Estado do provisionamento |
| `runningStatus` | Estado da aplicação |

---

# 7.4 Exibir configuração completa

```bash
az containerapp show \
  --name study-apir-prd \
  --resource-group rs-study-apir \
  --output json
```

Esse comando pode ser utilizado para investigar:

- ingress;
- containers;
- imagem;
- environment variables;
- revisions;
- escala;
- CPU;
- memória;
- rede;
- identidade;
- secrets;
- estado do recurso.

Para consultas específicas, prefira utilizar `--query`.

Exemplo:

```bash
az containerapp show \
  --name study-apir-prd \
  --resource-group rs-study-apir \
  --query "properties.configuration.ingress.fqdn" \
  -o tsv
```

---

# 8. Diagnóstico e Logs

## Exibir logs em tempo real

```bash
az containerapp logs show \
  --name study-apir-prd \
  --resource-group rs-study-apir \
  --follow
```

O parâmetro:

```text
--follow
```

mantém o terminal acompanhando os novos logs.

É semelhante ao:

```bash
docker logs -f
```

---

## Utilizar logs para investigar

Os logs são especialmente úteis para identificar:

- erro de inicialização;
- erro de conexão com SQL;
- exceptions;
- porta incorreta;
- variáveis de ambiente ausentes;
- problemas de autenticação;
- falhas de dependências;
- erros durante o deploy.

Para interromper:

```text
Ctrl + C
```

---

# 9. Fluxo recomendado

## 1. Confirmar Subscription

```bash
az account show \
  --query "{subscriptionId:id, tenantId:tenantId, subscriptionName:name}" \
  -o json
```

---

## 2. Criar Service Principal

```bash
az ad sp create-for-rbac \
  --name "github-study-api-deploy" \
  --role contributor \
  --scopes "/subscriptions/1be73773-c052-4393-86f6-5b763efdc08e/resourceGroups/rs-study-apir" \
  --json-auth
```

---

## 3. Validar SQL Server

```bash
az sql server show \
  --resource-group rs-study-apir \
  --name acnaweb-study-apir-prd \
  --query "{publicNetworkAccess:publicNetworkAccess,fullyQualifiedDomainName:fullyQualifiedDomainName}" \
  -o table
```

---

## 4. Configurar Firewall

Para desenvolvimento/testes:

```bash
az sql server firewall-rule create \
  --resource-group rs-study-apir \
  --server acnaweb-study-apir-prd \
  --name AllowAllIPs \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 255.255.255.255
```

---

## 5. Criar Database

```bash
az sql db create \
  --resource-group rs-study-apir \
  --server "acnaweb-study-apir-prd" \
  --name "api" \
  --service-objective "Basic"
```

---

## 6. Validar Database

```bash
az sql db show \
  --resource-group rs-study-apir \
  --server "acnaweb-study-apir-prd" \
  --name "api" \
  --query "{name:name,status:status,sku:currentSku.name}" \
  -o table
```

---

## 7. Testar conexão local

```powershell
Test-NetConnection acnaweb-study-apir-prd.database.windows.net -Port 1433
```

Resultado esperado:

```text
TcpTestSucceeded : True
```

---

## 8. Configurar Container App

```bash
az containerapp ingress enable \
  --name study-apir-prd \
  --resource-group rs-study-apir \
  --type external \
  --target-port 8080 \
  --transport auto
```

---

## 9. Obter URL da aplicação

```bash
az containerapp show \
  --name study-apir-prd \
  --resource-group rs-study-apir \
  --query "properties.configuration.ingress.fqdn" \
  --output tsv
```

---

## 10. Validar status

```bash
az containerapp show \
  --name study-apir-prd \
  --resource-group rs-study-apir \
  --query "{name:name, provisioningState:properties.provisioningState, runningStatus:properties.runningStatus}" \
  --output table
```

---

## 11. Verificar logs

```bash
az containerapp logs show \
  --name study-apir-prd \
  --resource-group rs-study-apir \
  --follow
```

---

# 10. Resumo da arquitetura

```text
                         GitHub
                           |
                           | CI/CD
                           v
                 +---------------------+
                 |  Service Principal  |
                 | github-study-api-   |
                 | deploy              |
                 +----------+----------+
                            |
                            v
                 +---------------------+
                 |   Resource Group    |
                 |    rs-study-apir    |
                 +----------+----------+
                            |
              +-------------+-------------+
              |                           |
              v                           v
    +------------------+       +------------------+
    | Container Apps   |       |   Azure SQL      |
    |                  |       |                  |
    | study-apir-prd   |------>| Server           |
    | Port 8080        |       | acnaweb-study-   |
    | External Ingress |       | apir-prd         |
    +------------------+       +--------+---------+
                                        |
                                        v
                                +---------------+
                                | Database      |
                                | api           |
                                +---------------+
```

---

# 11. Pontos de atenção

## Firewall

Evite em produção:

```text
0.0.0.0 → 255.255.255.255
```

Prefira regras restritivas.

---

## Service Principal

Nunca coloque credenciais diretamente no código ou no README.

Não faça commit de:

```text
clientSecret
password
credentials
tokens
```

Utilize secrets do GitHub ou outro mecanismo seguro.

---

## Container App

Confirme que a aplicação está escutando na porta:

```text
8080
```

O `target-port` do ingress precisa estar alinhado com a porta utilizada pelo container.

---

## SQL

O teste:

```powershell
Test-NetConnection ... -Port 1433
```

valida somente conectividade.

Não valida usuário, senha ou permissões no banco.

---

# 12. Checklist

- [ ] Azure CLI instalado
- [ ] `az login` executado
- [ ] Subscription correta selecionada
- [ ] Resource Group `rs-study-apir` disponível
- [ ] Service Principal criado
- [ ] Credenciais armazenadas com segurança
- [ ] SQL Server disponível
- [ ] Firewall configurado
- [ ] Database `api` criado
- [ ] Conectividade TCP na porta `1433` validada
- [ ] Container App `study-apir-prd` disponível
- [ ] Ingress externo configurado
- [ ] Target port `8080` configurada
- [ ] FQDN da aplicação obtido
- [ ] Status do Container App validado
- [ ] Logs verificados
