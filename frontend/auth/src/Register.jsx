import React, { useState, useEffect } from "react";

const Register = ({ toggleView, currentView }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (currentView === "register") {
      setUsername("");
      setPassword("");
    }
  }, [currentView]);

  const handleRegister = async () => {
    try {
      const response = await fetch(
        "http://74.248.77.96/handler/auth/register",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ username, password }),
        }
      );

      if (!response.ok) throw new Error("Registration failed");

      alert("Registration Successful!");
      toggleView("login");
    } catch (err) {
      setMessage("Error: " + err.message);
    }
  };

  return (
    <div className="bg-white shadow-lg rounded-lg p-6 w-96">
      <h2 className="text-2xl font-bold text-center text-gray-700 mb-4">
        Sign Up
      </h2>
      <input
        type="text"
        name="registerEmail"
        value={username}
        placeholder="Email"
        className="w-full p-3 border rounded-md mb-4"
        onChange={(e) => setUsername(e.target.value)}
      />
      <input
        type="password"
        name="registerPassword"
        value={password}
        placeholder="Password"
        className="w-full p-3 border rounded-md mb-4"
        onChange={(e) => setPassword(e.target.value)}
      />
      <button
        className="w-full bg-blue-500 hover:bg-blue-600 text-white font-semibold py-3 rounded-md"
        onClick={handleRegister}
      >
        Sign Up
      </button>
      {message && <p className="text-red-500 text-sm mt-2">{message}</p>}
      <p className="mt-4 text-center text-sm">
        Already have an account?{" "}
        <button
          className="text-blue-500 hover:underline"
          onClick={() => toggleView("login")}
        >
          Sign In
        </button>
      </p>
    </div>
  );
};

export default Register;
