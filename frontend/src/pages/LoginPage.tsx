import { useState } from "react";
import { useNavigate } from "react-router";

function LoginPage() {
  const [identifier, setIdentifier] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (event: React.SubmitEvent<HTMLFormElement>) => {
    event.preventDefault();

    // console.log({
    //   identifier,
    //   password,
    // });

    try {
      const response = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({
          identifier,
          password,
        }),
      });

      const data = await response.json();
      if (response.ok) {
        navigate("/");
      }
      else {
        console.log(data.message);
      }

      console.log(data);
    } catch (error) {
      console.error("Login failed:", error);
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-100 px-4">
      <div className="w-full max-w-md rounded-2xl bg-white p-8 shadow-lg">
        <div className="mb-8 text-center">
          <h1 className="text-3xl font-bold text-gray-900">
            Welcome Back!
          </h1>
        </div>

        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label
              htmlFor="identifier"
              className="mb-2 block text-sm font-medium text-gray-700"
            >
              Username or Email
            </label>

            <input
              id="identifier"
              type="text"
              value={identifier}
              onChange={(event) => setIdentifier(event.target.value)}
              placeholder="Enter your username or email"
              className="w-full rounded-lg border border-gray-300 px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
              required
            />
          </div>

          <div>
            <label
              htmlFor="password"
              className="mb-2 block text-sm font-medium text-gray-700"
            >
              Password
            </label>

            <input
              id="password"
              type="password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              placeholder="Enter your password"
              className="w-full rounded-lg border border-gray-300 px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
              required
            />
          </div>

          <button
            type="submit"
            className="w-full rounded-lg bg-blue-600 py-3 font-semibold text-white transition hover:bg-blue-700"
          >
            Log In
          </button>
        </form>

        <p className="mt-6 text-center text-sm text-gray-600">
          Don't have an account?{" "}
          <button className="font-medium text-blue-600 hover:underline">
            Sign up
          </button>
        </p>
      </div>
    </div>
  );
}

export default LoginPage;