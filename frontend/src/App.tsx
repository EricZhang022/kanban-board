import { BrowserRouter, Routes, Route } from "react-router"
import HomePage from "./pages/HomePage"
import LoginPage from "./pages/LoginPage"

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
