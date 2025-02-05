import React, { useState, useEffect } from "react";
import axios from "axios";
import InteractionPreview from "./InteractionPreview";
import { MdDone, MdDelete } from "react-icons/md";
import { AiOutlineLoading3Quarters } from "react-icons/ai";
 
const InteractionList = () => {
  const [interactions, setInteractions] = useState([]);
  const [expandedId, setExpandedId] = useState(null);
  const [previews, setPreviews] = useState({});
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(false);
  const pageSize = 3;
 
  const token = localStorage.getItem("token");
  const userId = localStorage.getItem("username");
 
  useEffect(() => {
    if (!token || !userId) {
      alert("Username or token not found. Please log in again.");
      return;
    }
    fetchInteractions();
  }, [page]);
 
  const fetchInteractions = async () => {
    setLoading(true);
    try {
      const response = await axios.get(
        `http://74.248.77.96/handler/api/interactions?userId=${userId}&page=${page}&size=${pageSize}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
 
      setInteractions(response.data.content || []);
      const receivedItems = response.data.content.length;
      setTotalPages(receivedItems < pageSize ? page + 1 : page + 2);
      fetchPreviews(response.data.content);
    } catch (error) {
      console.error("Error fetching interactions", error);
    }
    setLoading(false);
  };
 
  const fetchPreviews = async (interactionsData) => {
    const previewsData = {};
    await Promise.all(
      interactionsData.map(async (interaction) => {
        try {
          const response = await axios.get(
            `http://74.248.77.96/handler/api/interactions/${interaction.interactionId}/preview?userId=${userId}`,
            {
              headers: { Authorization: `Bearer ${token}` },
            }
          );
          previewsData[interaction.interactionId] = response.data.inputImage;
        } catch (error) {
          console.error(
            `Error fetching preview for ${interaction.interactionId}`,
            error
          );
        }
      })
    );
    setPreviews(previewsData);
  };
 
  const toggleExpand = (interactionId) => {
    setExpandedId(expandedId === interactionId ? null : interactionId);
  };
 
  const handleDelete = async (interactionId) => {
    try {
      await axios.delete(
        `http://74.248.77.96/handler/api/interactions/${interactionId}?userId=${userId}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setInteractions(
        interactions.filter((i) => i.interactionId !== interactionId)
      );
    } catch (error) {
      console.error("Error deleting interaction", error);
    }
  };
 
  return (
    <div className="p-4 rounded-lg w-full max-w-[90%] flex flex-col">
      <h2 className="text-xl font-bold text-center mb-4">My Interactions</h2>
 
      {loading ? (
        <p className="text-gray-600 text-center">Loading...</p>
      ) : interactions.length === 0 ? (
        <p className="text-gray-600 text-center">No interactions found</p>
      ) : (
        <ul className="space-y-3">
          {interactions.map((interaction) => (
            <li
              key={interaction.interactionId}
              className="p-4 bg-gray-100 border rounded-lg transition-all duration-300"
            >
              <div
                className="flex items-center justify-between cursor-pointer hover:bg-gray-200 transition rounded-lg p-2"
                onClick={() => toggleExpand(interaction.interactionId)}
              >
                <img
                  src={
                    previews[interaction.interactionId]
                      ? `data:image/png;base64,${
                          previews[interaction.interactionId]
                        }`
                      : "/default-placeholder.png"
                  }
                  alt="Thumbnail"
                  className="w-16 h-16 object-cover rounded-md border shadow-sm"
                />
 
                <div className="flex-grow px-4 text-left">
                  <p className="font-semibold text-gray-800">
                    {interaction.operationType === "OCR"
                      ? "Read Text"
                      : "Background Removal"}
                  </p>
                </div>
 
                <div>
                  {interaction.status !== "COMPLETED" ? (
                    <div className="flex flex-row items-center text-yellow-500 font-semibold">
                      <AiOutlineLoading3Quarters
                        className="animate-spin"
                        size={20}
                      />
                      <span className="ml-2">Processing...</span>
                    </div>
                  ) : (
                    <div className="flex flex-row items-center text-green-500 font-semibold">
                      <MdDone size={20} />
                      <span className="ml-2">Complete</span>
                    </div>
                  )}
                </div>
 
                {interaction.status === "COMPLETED" && (
                  <button
                    className="text-red-500 hover:text-red-700 font-semibold ml-2"
                    onClick={(e) => {
                      e.stopPropagation();
                      handleDelete(interaction.interactionId);
                    }}
                  >
                    <MdDelete size={20} />
                  </button>
                )}
              </div>
 
              {expandedId === interaction.interactionId &&
                interaction.status === "COMPLETED" && (
                  <InteractionPreview
                    interactionId={interaction.interactionId}
                  />
                )}
            </li>
          ))}
        </ul>
      )}
 
      <div className="flex justify-center mt-4 space-x-2">
        <button
          className="px-4 py-2 bg-gray-300 rounded-lg disabled:opacity-50"
          disabled={page === 0}
          onClick={() => setPage((prev) => prev - 1)}
        >
          Previous
        </button>
 
        <button
          className="px-4 py-2 bg-gray-300 rounded-lg disabled:opacity-50"
          disabled={interactions.length < pageSize}
          onClick={() => setPage((prev) => prev + 1)}
        >
          Next
        </button>
      </div>
    </div>
  );
};
 
export default InteractionList;
 