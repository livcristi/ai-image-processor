import React, { useState, useEffect } from "react";

const Login = ({ toggleView, currentView }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    if (currentView === "login") {
      setUsername("");
      setPassword("");
    }
  }, [currentView]);

  const handleLogin = async () => {
    try {
      const response = await fetch("http://74.248.77.96/handler/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });

      if (!response.ok) throw new Error("Invalid credentials");

      const data = await response.json();
      localStorage.setItem("token", data.token);
      localStorage.setItem("username", username);

      alert("Login Successful!");
      window.location.reload();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="bg-white shadow-lg rounded-lg p-6 w-96">
      <h2 className="text-2xl font-bold text-center text-gray-700 mb-4">
        Sign In
      </h2>
      <input
        type="text"
        name="loginEmail"
        value={username}
        placeholder="Username"
        className="w-full p-3 border rounded-md mb-4"
        onChange={(e) => setUsername(e.target.value)}
      />
      <input
        type="password"
        name="loginPassword"
        value={password}
        placeholder="Password"
        className="w-full p-3 border rounded-md mb-4"
        onChange={(e) => setPassword(e.target.value)}
      />
      <button
        className="w-full bg-blue-500 hover:bg-blue-600 text-white font-semibold py-3 rounded-md"
        onClick={handleLogin}
      >
        Sign In
      </button>
      {error && <p className="text-red-500 text-sm mt-2">{error}</p>}
      <p className="mt-4 text-center text-sm">
        Don't have an account?{" "}
        <button
          className="text-blue-500 hover:underline"
          onClick={() => toggleView("register")}
        >
          Sign Up
        </button>
      </p>
    </div>
  );
};

export default Login;
