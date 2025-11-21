# Configuração do Render para Spring Boot

## Problema: Render detectando como Node.js

Se o Render está detectando seu projeto como Node.js ao invés de Java, siga estas instruções:

## Solução 1: Configuração Manual no Dashboard (RECOMENDADO)

1. **Acesse o Dashboard do Render:**
   - Vá em: https://dashboard.render.com
   - Selecione seu serviço `todolist-spring-boot`

2. **Vá em Settings (Configurações)**

3. **Configure o Environment:**
   - **Environment:** Selecione `Java` (NÃO Node.js)
   - Se não aparecer Java, delete o serviço e crie um novo selecionando Java

4. **Configure Build & Deploy:**
   - **Build Command:** `mvn clean package -DskipTests`
   - **Start Command:** `java -jar target/todolist-0.0.1-SNAPSHOT.jar`
   - **Docker:** Deixe DESMARCADO (não use Dockerfile por enquanto)

5. **Configure Health Check:**
   - **Health Check Path:** `/api/tarefas`

6. **Variáveis de Ambiente:**
   - `PORT` - será definida automaticamente pelo Render
   - `SPRING_PROFILES_ACTIVE` = `prod` (opcional)

7. **Salve as configurações e faça um novo deploy**

## Solução 2: Usar Dockerfile

Se quiser usar Docker:

1. **No Settings do serviço:**
   - Marque a opção **"Use Dockerfile"**
   - Deixe Build Command e Start Command VAZIOS
   - O Render usará o Dockerfile automaticamente

## Solução 3: Recriar o Serviço

Se nada funcionar:

1. **Delete o serviço atual**
2. **Crie um novo Web Service:**
   - **Environment:** Selecione `Java` (importante!)
   - **Build Command:** `mvn clean package -DskipTests`
   - **Start Command:** `java -jar target/todolist-0.0.1-SNAPSHOT.jar`
   - Conecte ao seu repositório GitHub

## Verificação

Após configurar, os logs devem mostrar:
- `==> Detecting build system...`
- `==> Using Java/Maven`
- `==> Running build command 'mvn clean package -DskipTests'`

**NÃO deve aparecer:**
- `==> Using Node.js version...` ❌

## Troubleshooting

Se ainda der erro:
1. Verifique os logs completos do build
2. Certifique-se que o `pom.xml` está na raiz do projeto
3. Verifique se o Java 17 está disponível no Render

