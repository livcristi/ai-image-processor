export const apiRequest = async (endpoint, options = {}) => {
  const token = localStorage.getItem("token");

  const headers = {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
  };

  const response = await fetch(`http://74.248.77.96/handler${endpoint}`, {
    ...options,
    headers,
  });

  if (!response.ok) {
    throw new Error(`Error: ${response.status} ${response.statusText}`);
  }

  return response.json();
};
