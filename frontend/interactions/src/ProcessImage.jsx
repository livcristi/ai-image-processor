import React, { useState } from "react";
import axios from "axios";

const ProcessImage = () => {
  const [image, setImage] = useState(null);
  const [imagePreview, setImagePreview] = useState(null);
  const [operation, setOperation] = useState("OCR");
  const token = localStorage.getItem("token");

  const handleImageChange = (event) => {
    const file = event.target.files[0];
    setImage(file);
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => setImagePreview(reader.result);
      reader.readAsDataURL(file);
    }
  };

  const handleProcessImage = async () => {
    if (!image || !token) {
      alert("Please upload an image and login first.");
      return;
    }

    const userId = localStorage.getItem("username");
    if (!userId) {
      alert("Username not found. Please log in again.");
      return;
    }

    const formData = new FormData();
    formData.append("image", image);
    formData.append("operationType", operation);
    formData.append("userId", userId);

    try {
      const response = await axios.post(
        "http://74.248.77.96/handler/api/interactions",
        formData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "multipart/form-data",
          },
        }
      );

      alert("Processing successful! Check your interactions.");
    } catch (error) {
      alert(
        "Error processing image: " + error.response?.data?.message ||
          error.message
      );
    }
  };

  return (
    <div className="bg-white p-2 rounded-lg w-full max-w-[80%] h-full flex flex-col items-center">
      <h2 className="text-xl font-bold text-gray-700 mb-4">
        Upload & Process Image
      </h2>

      <div className="flex justify-center">
        {imagePreview ? (
          <img
            src={imagePreview}
            alt="Preview"
            className="w-40 h-40 object-contain border rounded-lg shadow-lg"
          />
        ) : (
          <div className="w-40 h-40 flex items-center justify-center bg-gray-200 text-gray-600 rounded-lg border">
            No Image Selected
          </div>
        )}
      </div>

      <input
        type="file"
        accept="image/*"
        id="fileUpload"
        onChange={handleImageChange}
        className="hidden"
      />

      <label
        htmlFor="fileUpload"
        className=" mt-3  cursor-pointer bg-orange-500 text-white font-semibold py-3 px-3 rounded-md shadow-md hover:bg-orange-600 transition"
      >
        Upload Image
      </label>

      <label className="block text-gray-700 font-semibold mt-2">
        Select Operation
      </label>

      <select
        className="w-72 p-2 border rounded-lg bg-gray-100"
        value={operation}
        onChange={(e) => setOperation(e.target.value)}
      >
        <option value="OCR">Read Text</option>
        <option value="BGR">Remove Background</option>
      </select>

      <button
        className="w-72 mt-4 bg-green-500 hover:bg-green-600 text-white font-semibold py-3 rounded-lg"
        onClick={handleProcessImage}
      >
        Process Image
      </button>
    </div>
  );
};

export default ProcessImage;
