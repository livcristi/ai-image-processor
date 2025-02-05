import React, { useEffect, useState } from "react";
import axios from "axios";
import { FiDownload } from "react-icons/fi";

const InteractionPreview = ({ interactionId }) => {
  const [preview, setPreview] = useState(null);
  const [error, setError] = useState("");
  const token = localStorage.getItem("token");
  const userId = localStorage.getItem("username");

  useEffect(() => {
    if (!interactionId || !token || !userId) return;

    axios
      .get(
        `http://74.248.77.96/handler/api/interactions/${interactionId}/result?userId=${userId}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      )
      .then((res) => setPreview(res.data))
      .catch((err) => setError(err.message));
  }, [interactionId, token, userId]);

  if (error) return <p className="text-red-500">Error: {error}</p>;
  if (!preview) return <p className="text-gray-600">Loading preview...</p>;

  const inputImage = preview.inputImage
    ? `data:image/png;base64,${preview.inputImage}`
    : "/default-placeholder.png";

  const isOCR = preview.interactionMetadata.operationType === "OCR";

  const decodedText =
    isOCR && preview.resultData ? atob(preview.resultData) : "";

  const handleDownloadText = () => {
    if (!decodedText) return;

    const blob = new Blob([decodedText], { type: "text/plain" });
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = "extracted_text.txt";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  const handleDownloadImage = () => {
    if (!preview.resultData) return;

    const link = document.createElement("a");
    link.href = `data:image/png;base64,${preview.resultData}`;
    link.download = "processed_image.png";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  return (
    <div className="bg-gray-50 shadow-lg p-4 rounded-lg mt-2 max-h-80">
      <div className="flex flex-row justify-between items-center">
        <h2 className="text-lg font-bold">Interaction Details</h2>

        {preview.resultData && (
          <FiDownload
            size={20}
            onClick={isOCR ? handleDownloadText : handleDownloadImage}
            className="cursor-pointer text-blue-500 hover:text-blue-700"
          />
        )}
      </div>

      <div className="flex justify-between mt-4">
        <div className="flex flex-col items-center">
          <img
            src={inputImage}
            alt="Input"
            className="w-32 h-32 rounded-md border shadow-md"
          />
          <p className="text-gray-700 mt-1">Input</p>
        </div>

        <div className="flex flex-col items-center">
          {isOCR ? (
            <div className="bg-gray-200 p-4 rounded-md shadow-md max-w-xs max-h-40 overflow-auto ">
              <p className="text-gray-800 text-sm whitespace-pre-wrap">
                {decodedText || "No text extracted"}
              </p>
            </div>
          ) : (
            <img
              src={`data:image/png;base64,${preview.resultData}`}
              alt="Result"
              className="w-32 h-32 rounded-md border shadow-md"
            />
          )}
          {!isOCR && <p className="text-gray-700 mt-1">"Result"</p>}
        </div>
      </div>
    </div>
  );
};

export default InteractionPreview;
