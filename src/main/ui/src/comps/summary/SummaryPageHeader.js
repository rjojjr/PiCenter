import React from "react";

const SummaryPageHeader = ({ selectSensor, summary, isLoading }) => {
  return (
    <div className={"summaryHeader headerButtonContainer"}>
      {!isLoading && (
        <div>
          <h3>Sensors</h3>
          {summary.map((sum, index) => (
            <button
              key={index}
              className={"summarySensorSelector"}
              onClick={() => {
                selectSensor(index);
              }}
            >
              {sum.roomName}
            </button>
          ))}
        </div>
      )}
    </div>
  );
};

export default SummaryPageHeader;
