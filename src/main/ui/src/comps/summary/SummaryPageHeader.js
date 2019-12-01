import React from "react";
import ButtonToolbar from 'react-bootstrap/ButtonToolbar';
import Button from 'react-bootstrap/Button';

const SummaryPageHeader = ({ selectSensor, summary, isLoading }) => {
  return (
    <div className={"summaryHeader headerButtonContainer"}>
      <h3 >Sensors</h3>
      {!isLoading && (
         
        <div>
          <ButtonToolbar>
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
          </ButtonToolbar>
        </div>
      )}
    </div>
  );
};

export default SummaryPageHeader;
