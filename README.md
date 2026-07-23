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

### Step 3 - Start the backend

```bash
cd backend
.\mvnw.cmd spring-boot:run
```

This will start the server for the backend.

### Step 4 - On a separate terminal and from the root directory, run the commands:

```bash
cd frontend
npm install
```

This will install the frontend dependencies.

### Step 5 - Start the frontend

```bash
npm run dev
```

This will start the development server for the frontend.

### Step 6 - Open a browser and visit [http://localhost:5173](http://localhost:5173) to open the frontend

This should connect to the backend automatically.
