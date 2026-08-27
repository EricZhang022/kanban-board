import { BrowserRouter, Routes, Route } from "react-router"
import HomePage from "./pages/HomePage"
import LoginPage from "./pages/LoginPage"
import SignupPage from "./pages/SignupPage"
import Dashboard from "./pages/Dashboard"
import Settings from "./pages/Settings"
import Layout from "./components/Layout"
import ProtectedRoute from "./components/ProtectedRoute"
import BoardPage from "./pages/BoardPage"
import CreateBoard from "./pages/CreateBoard"
import Notifications from "./pages/Notifications"

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />

        <Route element={
          <ProtectedRoute>
            <Layout />
          </ProtectedRoute>
        }>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/board/:id" element={<BoardPage />} />
          <Route path="/settings" element={<Settings />} />
          <Route path="/createboard" element={<CreateBoard />} />
          <Route path="/notifications" element={<Notifications />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
