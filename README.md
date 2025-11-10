# 📝 Sistema de Gerenciamento de Tarefas

Sistema web full-stack para gerenciamento de tarefas com interface dark mode minimalista e elegante.

🔗 **[Demo ao vivo](https://todolist-spring-boot-production.up.railway.app)** _(em breve)_

## 🚀 Tecnologias

**Backend:**
- Java 17
- Spring Boot 3.5.7
- Spring Data JPA
- H2 Database (desenvolvimento)
- Maven

**Frontend:**
- HTML5
- CSS3 (Design System dark mode personalizado)
- JavaScript ES6+ (Fetch API, Async/Await)

## ✨ Funcionalidades

- ✅ CRUD completo de tarefas
- 📊 Dashboard com estatísticas em tempo real
- 🔍 Filtros por status (Todas/Pendentes/Concluídas)
- 🎨 Interface dark mode minimalista
- 📱 Design responsivo
- ⚡ Single Page Application (SPA)
- 🔄 Atualizações em tempo real

## 🏗️ Arquitetura
```
src/
├── main/
│   ├── java/com/todolist/
│   │   ├── Tarefa.java           # Entidade JPA
│   │   ├── TarefaRepository.java # Data Access Layer
│   │   └── TarefaController.java # REST Controller
│   └── resources/
│       ├── static/
│       │   └── index.html        # Frontend SPA
│       └── application.properties
```

## 🛠️ Como executar localmente

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+

### Instalação

1. Clone o repositório
```bash
git clone https://github.com/Juhdev2022/todolist-spring-boot.git
cd todolist-spring-boot
```

2. Execute a aplicação
```bash
mvn spring-boot:run
```

3. Acesse no navegador
```
http://localhost:8080
```

## 🌐 API Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/tarefas` | Lista todas as tarefas |
| GET | `/api/tarefas/{id}` | Busca tarefa por ID |
| GET | `/api/tarefas/status/{concluida}` | Filtra tarefas por status |
| POST | `/api/tarefas` | Cria nova tarefa |
| PUT | `/api/tarefas/{id}` | Atualiza tarefa completa |
| PATCH | `/api/tarefas/{id}/concluir` | Alterna status concluída/pendente |
| DELETE | `/api/tarefas/{id}` | Deleta tarefa específica |
| DELETE | `/api/tarefas` | Deleta todas as tarefas |

### Exemplo de Request (POST)
```json
{
  "titulo": "Estudar Spring Boot",
  "descricao": "Aprender conceitos de JPA e REST API",
  "concluida": false
}
```

### Exemplo de Response
```json
{
  "id": 1,
  "titulo": "Estudar Spring Boot",
  "descricao": "Aprender conceitos de JPA e REST API",
  "concluida": false,
  "dataCriacao": "2025-11-10T14:30:00"
}
```

## 📚 Conceitos Aplicados

- **REST API** - Arquitetura RESTful com verbos HTTP adequados
- **JPA/Hibernate** - ORM para persistência de dados
- **Spring Data** - Repository pattern com JpaRepository
- **Injeção de Dependência** - @Autowired
- **Anotações Spring** - @Entity, @RestController, @RequestMapping
- **HTTP Status Codes** - ResponseEntity com códigos apropriados
- **CORS** - Configurado para aceitar requisições cross-origin

## 🎨 Design

Interface minimalista inspirada em design systems modernos:
- Paleta monocromática dark mode
- Tipografia clean (Inter/SF Pro style)
- Espaçamentos generosos
- Animações sutis e elegantes
- Feedback visual imediato

## 👩‍💻 Autora

**Julliana Leão**
- GitHub: [@Juhdev2022](https://github.com/Juhdev2022)
- LinkedIn: [www.linkedin.com/in/julliana-leao/]

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais e de portfólio.