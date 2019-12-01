import React from "react";
import Button from 'react-bootstrap/Button';

const SummaryPageHeader = ({ selectSensor, summary, isLoading }) => {
  return (
    <div className={"summaryHeader headerButtonContainer"}>
      <h3 >Sensors</h3>
      {!isLoading && (
         
        <div>
          
          {summary.map((sum, index) => (
            <Button
              key={index}
              variant={"primary"}
              className={"summarySensorSelector"}
              onClick={() => {
                selectSensor(index);
              }}
            >
              {sum.roomName}
            </Button>
          ))}
        </div>
      )}
    </div>
  );
};

export default SummaryPageHeader;
