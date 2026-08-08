import { BrowserRouter, Routes, Route } from "react-router"
import HomePage from "./pages/HomePage"
import LoginPage from "./pages/LoginPage"
import SignupPage from "./pages/SignupPage"
import Dashboard from "./pages/Dashboard"
import Settings from "./pages/Settings"
import ProtectedRoute from "./pages/ProtectedRoute"

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/dashboard" element={
          <ProtectedRoute>
            <Dashboard />
          </ProtectedRoute>}/>
        <Route path="/settings" element={
          <ProtectedRoute>
            <Settings />
          </ProtectedRoute>}/>
      </Routes>
    </BrowserRouter>
  )
}

export default App
