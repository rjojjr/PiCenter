import React from "react";

const SummaryPageHeader = ({ selectSensor, summary, isLoading }) => {
  return (
    <div className={"summaryHeader headerButtonContainer"}>
      <h3 >Sensors</h3>
      {!isLoading && (
         
        <div>
          
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
