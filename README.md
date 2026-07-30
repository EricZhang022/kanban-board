# kanban-board
A custom made Kanban board

## How to run locally

### Step 1 - Clone the Repository

First, clone the repository to your local machine using Git:

```bash
git clone https://github.com/EricZhang022/kanban-board.git
```

You may or may not need to ```cd``` into your repo where you cloned it.

### Step 2 - Configure JDK version

Run the following commands:

```bash
java -version
.\mvnw.cmd -version
```

Both outputs should show version 21 of the JDK. If they are not, install JDK 21 and reconfigure them in the PATH of your environment variables.

### Step 3 - Initialize PostgreSQL Database

Open a SQL Shell (like psql) and then enter in your password for user postgres.

Once logged in, type the following command:

```
CREATE DATABASE kanban_db;
```

This will create a local database on your machine named ```kanban_db```.

### Step 4 - Configure Spring Boot to connect to the database

Under the directory ```backend/src/main/resources```

Create a file named: ```application-local.properties```

Copy and paste the following code into it:

```
DB_URL=jdbc:postgresql://localhost:5432/kanban_db
DB_USERNAME=postgres
DB_PASSWORD=your_password
```

Replace ```your_password``` with your own password used to log in to PostgreSQL.

### Step 5 - Generate JWT Signing Keys

Backend signs login token using RS256 algorithm, which needs an RSA key pair. 
From the backend/ directory (same layer with src/) run these commands in terminal:

```bash
mkdir keys
openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out keys/jwt_private.pem
openssl rsa -pubout -in keys/jwt_private.pem -out keys/jwt_public.pem
```

### Step 6 - Start the backend

```bash
cd backend
.\mvnw.cmd spring-boot:run
```

This will start the server for the backend.

### Step 7 - On a separate terminal and from the root directory, run the commands:

```bash
cd frontend
npm install
```

This will install the frontend dependencies.

### Step 8 - Start the frontend

```bash
npm run dev
```

This will start the development server for the frontend.

### Step 9 - Open a browser and visit [http://localhost:5173](http://localhost:5173) to open the frontend

This should connect to the backend automatically.
