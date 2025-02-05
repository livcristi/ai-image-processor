import React, { useState } from "react";
import InteractionList from "./InteractionList";
import InteractionPreview from "./InteractionPreview";
import ProcessImage from "./ProcessImage";

const InteractionsLogic = () => {
  const [selectedTab, setSelectedTab] = useState("interactions");
  const [selectedInteraction, setSelectedInteraction] = useState(null);

  return (
    <div className="flex flex-col items-center w-full h-screen bg-gray-100 relative">
      <div className="w-2xl  sticky top-0 z-10 flex justify-center py-4">
        <div className="w-full max-w-[85%] flex space-x-6 justify-center">
          <button
            className={`w-1/2 py-3 text-white font-semibold rounded-lg transition-all ${
              selectedTab === "process"
                ? "bg-orange-500 shadow-md scale-105"
                : "bg-gray-400 hover:bg-gray-500"
            }`}
            onClick={() => setSelectedTab("process")}
          >
            Process Image
          </button>
          <button
            className={`w-1/2 py-3 text-white font-semibold rounded-lg transition-all ${
              selectedTab === "interactions"
                ? "bg-orange-500 shadow-md scale-105"
                : "bg-gray-400 hover:bg-gray-500"
            }`}
            onClick={() => setSelectedTab("interactions")}
          >
            My Interactions
          </button>
        </div>
      </div>

      <div className="w-full max-w-[85%] bg-white shadow-lg p-8 rounded-lg h-3/4 flex justify-center items-center mt-6">
        {selectedTab === "interactions" ? (
          <div className="w-full h-full flex flex-col items-center space-y-6 overflow-auto">
            <InteractionList onSelect={setSelectedInteraction} />
            {selectedInteraction && (
              <InteractionPreview interactionId={selectedInteraction} />
            )}
          </div>
        ) : (
          <ProcessImage />
        )}
      </div>
    </div>
  );
};

export default InteractionsLogic;
